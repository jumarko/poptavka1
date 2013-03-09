/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.service.demand;

import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.demand.PotentialSupplier;
import com.eprovement.poptavka.domain.enums.MessageContext;
import com.eprovement.poptavka.domain.enums.MessageState;
import com.eprovement.poptavka.domain.enums.MessageUserRoleType;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.message.MessageUserRole;
import com.eprovement.poptavka.exception.MessageException;
import com.eprovement.poptavka.service.message.MessageService;
import com.eprovement.poptavka.service.usermessage.UserMessageService;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Implementation of {@link PotentialDemandService} which uses {@link com.eprovement.poptavka.domain.message.Message}-s
 * objects for sending notifications about new potential demands to supplier.
 */
public class MessageBasedPotentialDemandService implements PotentialDemandService {

    /** Default number of max count of suppliers to which the demand is sent. */
    private static final Integer DEFAULT_MAX_SUPPLIERS = 50;
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageBasedPotentialDemandService.class);

    private final MessageService messageService;
    private final UserMessageService userMessageService;
    private final SuppliersSelection suppliersSelection;


    public MessageBasedPotentialDemandService(MessageService messageService, SuppliersSelection suppliersSelection,
                UserMessageService userMessageService) {
        Validate.notNull(messageService, "messageService cannot be null");
        Validate.notNull(suppliersSelection, "suppliersSelection algorithm cannot be null!");
        this.messageService = messageService;
        this.suppliersSelection = suppliersSelection;
        this.userMessageService = userMessageService;
    }


    @Override
    @Transactional
    public void sendDemandToPotentialSuppliers(Demand demand) throws MessageException {
        LOGGER.info("Action=demand_send_to_suppliers status=start demand=" + demand);
        Validate.notNull(demand, "demand cannot be null!");

        // update thread root message before sending to potential suppliers
        final Message threadRootMessage = updateDemandThreadRootMessage(demand,
                this.suppliersSelection.getPotentialSuppliers(demand));
        messageService.send(threadRootMessage);

        LOGGER.info("Action=demand_send_to_suppliers status=finish demand=" + demand);
    }


    @Override
    @Transactional
    public void sendDemandToPotentialSupplier(Demand demand, PotentialSupplier potentialSupplier)
        throws MessageException {
        LOGGER.debug("Action=demand_send_to_supplier status=start demand=" + demand);
        Validate.notNull(demand, "demand cannot be null");
        Validate.notNull(potentialSupplier, "potential supplier cannot be null");

        final Message threadRootMessage = updateDemandThreadRootMessage(demand, Arrays.asList(potentialSupplier));
        userMessageService.createUserMessage(threadRootMessage, potentialSupplier.getSupplier().getBusinessUser());

        LOGGER.debug("Action=demand_send_to_supplier status=finish demand=" + demand);
    }


    //--------------------------------------------------- HELPER METHODS -----------------------------------------------

    /**
     * Updates thread root message of given {@code demand} adding all {@code potentialSuppliers}.
     * @param demand demand for which the thread root message will be updated
     * @param potentialSuppliers all potential suppliers that should be added to the roles of {@code demand}'s
     *                           thread root message
     * @return updated thread root message containing all potential suppliers
     */
    private Message updateDemandThreadRootMessage(Demand demand, Collection<PotentialSupplier> potentialSuppliers) {
        Validate.notNull(demand, "demand cannot be null");

        fillDefaultValues(demand);
        Message threadRootMessage = messageService.getThreadRootMessage(demand);
        Validate.isTrue(threadRootMessage != null, "threadRootMessage cannot be null");
        Validate.isTrue(threadRootMessage.getRoles() != null, "threadRootMessage.roles cannot be null");

        final List<MessageUserRole> messageUserRoles = new ArrayList<>();
        for (PotentialSupplier potentialSupplier : potentialSuppliers) {
            final MessageUserRole messageUserRole = new MessageUserRole();
            messageUserRole.setMessage(threadRootMessage);
            messageUserRole.setUser(potentialSupplier.getSupplier().getBusinessUser());
            messageUserRole.setType(MessageUserRoleType.BCC);
            messageUserRole.setMessageContext(MessageContext.POTENTIAL_SUPPLIERS_DEMAND);
            messageUserRoles.add(messageUserRole);
        }

        threadRootMessage.getRoles().addAll(messageUserRoles);
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
