/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.service.demand;

import static org.apache.commons.lang.Validate.isTrue;
import static org.apache.commons.lang.Validate.notNull;

import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.demand.PotentialSupplier;
import com.eprovement.poptavka.domain.enums.MessageContext;
import com.eprovement.poptavka.domain.enums.MessageState;
import com.eprovement.poptavka.domain.enums.MessageUserRoleType;
import com.eprovement.poptavka.domain.enums.Period;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.message.MessageUserRole;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.register.Registers;
import com.eprovement.poptavka.exception.MessageException;
import com.eprovement.poptavka.service.message.MessageService;
import com.eprovement.poptavka.service.notification.NotificationService;
import com.eprovement.poptavka.service.user.ExternalUserNotificator;
import com.eprovement.poptavka.service.usermessage.UserMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of {@link PotentialDemandService} which uses {@link com.eprovement.poptavka.domain.message.Message}-s
 * objects for sending notifications about new potential demands to supplier.
 */
public class MessageBasedPotentialDemandService implements PotentialDemandService {

    /** Default number of max count of suppliers to which the demand is sent. */
    private static final Integer DEFAULT_MAX_SUPPLIERS = 50;
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageBasedPotentialDemandService.class);
    private static final String DEMAND_TITLE_NOTIFICATION_PARAM = "demand.title";
    private static final String DEMAND_DESC_NOTIFICATION_PARAM = "demand.desc";
    private static final String SUPPLIER_NOTIFICATION_PARAM = "supplier";

    private final MessageService messageService;
    private final UserMessageService userMessageService;
    private final SuppliersSelection suppliersSelection;
    private final NotificationService notificationService;
    private final ExternalUserNotificator externalUserNotificator;


    public MessageBasedPotentialDemandService(MessageService messageService, SuppliersSelection suppliersSelection,
                                              UserMessageService userMessageService,
                                              NotificationService notificationService,
                                              ExternalUserNotificator externalUserNotificator) {
        notNull(messageService, "messageService cannot be null");
        notNull(suppliersSelection, "suppliersSelection algorithm cannot be null!");
        notNull(userMessageService, "userMessageService cannot be null!");
        notNull(notificationService, "notificationService cannot be null!");
        notNull(externalUserNotificator, "externalUserNotificator cannot be null!");

        this.messageService = messageService;
        this.suppliersSelection = suppliersSelection;
        this.userMessageService = userMessageService;
        this.notificationService = notificationService;
        this.externalUserNotificator = externalUserNotificator;
    }


    @Override
    @Transactional
    public void sendDemandToPotentialSuppliers(Demand demand) throws MessageException {
        LOGGER.info("Action=demand_send_to_suppliers status=start demand=" + demand);
        notNull(demand, "demand cannot be null!");

        // update thread root message before sending to potential suppliers
        final Set<PotentialSupplier> potentialSuppliers = this.suppliersSelection.getPotentialSuppliers(demand);
        // set notification params
        Map<String, String> notificationParams = new HashMap<>();
        notificationParams.put(DEMAND_TITLE_NOTIFICATION_PARAM, demand.getTitle());
        notificationParams.put(DEMAND_DESC_NOTIFICATION_PARAM, demand.getDescription());

        notifyExternalSuppliers(potentialSuppliers, notificationParams);
        final Message threadRootMessage = updateDemandThreadRootMessage(demand, potentialSuppliers);
        messageService.send(threadRootMessage);

        LOGGER.info("Action=demand_send_to_suppliers status=finish demand=" + demand);
    }


    @Override
    @Transactional
    public void sendDemandToPotentialSupplier(Demand demand, PotentialSupplier potentialSupplier)
        throws MessageException {
        LOGGER.debug("Action=demand_send_to_supplier status=start demand=" + demand);
        notNull(demand, "demand cannot be null");
        notNull(potentialSupplier, "potential supplier cannot be null");

        final Message threadRootMessage = updateDemandThreadRootMessage(demand, Arrays.asList(potentialSupplier));
        final UserMessage potentialDemandUserMessage = userMessageService.createUserMessage(
                threadRootMessage, potentialSupplier.getSupplier().getBusinessUser());
        notificationService.notifyUserNewMessage(Period.INSTANTLY, potentialDemandUserMessage);

        LOGGER.debug("Action=demand_send_to_supplier status=finish demand=" + demand);
    }


    //--------------------------------------------------- HELPER METHODS -----------------------------------------------

    /**
     * Sends notification to all external suppliers from given set of potential suppliers if applicable.
     *
     * @param potentialSuppliers potential suppliers, some of them can still be the external ones
     * @see com.eprovement.poptavka.service.user.ExternalUserNotificator
     */
    private void notifyExternalSuppliers(
        Set<PotentialSupplier> potentialSuppliers, Map<String, String> notificationParams) {

        for (PotentialSupplier potentialSupplier : potentialSuppliers) {
            notificationParams.put(SUPPLIER_NOTIFICATION_PARAM,
                potentialSupplier.getSupplier().getBusinessUser().getDisplayName());

            externalUserNotificator.send(potentialSupplier.getSupplier().getBusinessUser(),
                    Registers.Notification.EXTERNAL_SUPPLIER, notificationParams);
        }
    }

    /**
     * Updates thread root message of given {@code demand} adding all {@code potentialSuppliers}.
     * @param demand demand for which the thread root message will be updated
     * @param potentialSuppliers all potential suppliers that should be added to the roles of {@code demand}'s
     *                           thread root message
     * @return updated thread root message containing all potential suppliers
     */
    private Message updateDemandThreadRootMessage(Demand demand, Collection<PotentialSupplier> potentialSuppliers) {
        notNull(demand, "demand cannot be null");

        fillDefaultValues(demand);
        Message threadRootMessage = messageService.getThreadRootMessage(demand);
        isTrue(threadRootMessage != null, "threadRootMessage cannot be null");
        isTrue(threadRootMessage.getRoles() != null, "threadRootMessage.roles cannot be null");

        final List<MessageUserRole> messageUserRoles = new ArrayList<>();
        for (PotentialSupplier potentialSupplier : potentialSuppliers) {
            final MessageUserRole messageUserRole = new MessageUserRole();
            messageUserRole.setMessage(threadRootMessage);
            messageUserRole.setUser(potentialSupplier.getSupplier().getBusinessUser());
            messageUserRole.setType(MessageUserRoleType.BCC);
            messageUserRole.setMessageContext(MessageContext.POTENTIAL_SUPPLIERS_DEMAND);
            messageUserRoles.add(messageUserRole);
        }

        threadRootMessage.setRoles(messageUserRoles);
        threadRootMessage.setMessageState(MessageState.COMPOSED);
        threadRootMessage = messageService.update(threadRootMessage);
        return threadRootMessage;
    }

    private void fillDefaultValues(Demand demand) {
        if (demand.getMaxSuppliers() == null) {
            demand.setMaxSuppliers(DEFAULT_MAX_SUPPLIERS);
        }
    }

}
