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
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.message.MessageService;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Implementation of {@link PotentialDemandService} which uses {@link com.eprovement.poptavka.domain.message.Message}-s
 * objects for sending notifications about new potential demands to supplier.
 */
public class MessageBasedPotentialDemandService implements PotentialDemandService {

    /** Default number of max count of suppliers to which the demand is sent. */
    private static final Integer DEFAULT_MAX_SUPPLIERS = Integer.valueOf(50);
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageBasedPotentialDemandService.class);

    private final GeneralService generalService;
    private final MessageService messageService;
    private final SuppliersSelection suppliersSelection;


    public MessageBasedPotentialDemandService(GeneralService generalService, MessageService messageService,
                                              SuppliersSelection suppliersSelection) {
        Validate.notNull(generalService, "generalService cannot be null");
        Validate.notNull(messageService, "messageService cannot be null");
        Validate.notNull(suppliersSelection, "suppliersSelection algorithm cannot be null!");
        this.generalService = generalService;
        this.messageService = messageService;
        this.suppliersSelection = suppliersSelection;
    }


    @Override
    @Transactional
    public void sendDemandToPotentialSuppliers(Demand demand) throws MessageException {
        LOGGER.info("Action=demand_send_to_suppliers status=start demand=" + demand);
        fillDefaultValues(demand);

        final Set<PotentialSupplier> potentialSuppliers = this.suppliersSelection.getPotentialSuppliers(demand);
        for (PotentialSupplier potentialSupplier : potentialSuppliers) {
            sendDemandToPotentialSupplier(demand, potentialSupplier);
        }
        LOGGER.info("Action=demand_send_to_suppliers status=finish demand=" + demand);
    }

    @Override
    @Transactional
    public void sendDemandToPotentialSupplier(Demand demand, PotentialSupplier potentialSupplier)
        throws MessageException {
        LOGGER.debug("Action=demand_send_to_supplier status=start demand=" + demand);

        Message message = composePotentialDemandMessage(demand, potentialSupplier,
                messageService.getThreadRootMessage(demand));

        message = messageService.update(message);

        messageService.send(message);

        LOGGER.debug("Action=demand_send_to_supplier status=finish demand=" + demand);
    }


    //--------------------------------------------------- HELPER METHODS -----------------------------------------------

    private Message composePotentialDemandMessage(Demand demand, PotentialSupplier potentialSupplier,
            Message message)
        throws MessageException {
        // TODO Vojto there should be some intro message for the user
        message.setMessageState(MessageState.COMPOSED);
        message.setBody(demand.getDescription());
        message.setSubject(demand.getTitle());

        final List<MessageUserRole> messageUserRoles = new ArrayList<MessageUserRole>();

        final MessageUserRole messageUserRole = new MessageUserRole();
        messageUserRole.setMessage(message);
        messageUserRole.setUser(potentialSupplier.getSupplier().getBusinessUser());
        messageUserRole.setType(MessageUserRoleType.TO);
        messageUserRole.setMessageContext(MessageContext.POTENTIAL_SUPPLIERS_DEMAND);
        messageUserRoles.add(messageUserRole);

        message.setRoles(messageUserRoles);

        message.setMessageState(MessageState.COMPOSED);
        return message;
    }


    private void fillDefaultValues(Demand demand) {
        if (demand.getMaxSuppliers() == null) {
            demand.setMaxSuppliers(DEFAULT_MAX_SUPPLIERS);
        }
    }

}
