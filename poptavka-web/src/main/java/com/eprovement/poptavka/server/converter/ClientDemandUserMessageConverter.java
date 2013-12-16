/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandDetail;

/**
 * Converts UserMessage to ClientDemandDetail and vice versa.
 * @author Juraj Martinka
 */
public final class ClientDemandUserMessageConverter extends AbstractConverter<UserMessage, ClientDemandDetail> {

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates ClientDemandUserMessageConverter.
     */
    private ClientDemandUserMessageConverter() {
        // Spring instantiates converters - see converters.xml
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public ClientDemandDetail convertToTarget(UserMessage userMessage) {
        final ClientDemandDetail detail = new ClientDemandDetail();
        detail.setUserMessageId(userMessage.getId());
        if (userMessage.getMessage() != null && userMessage.getMessage().getDemand() != null) {
            detail.setDemandId(userMessage.getMessage().getDemand().getId());
            detail.setThreadRootId(userMessage.getMessage().getThreadRoot().getId());
            detail.setDemandStatus(userMessage.getMessage().getDemand().getStatus());
            detail.setDemandTitle(userMessage.getMessage().getDemand().getTitle());
            detail.setPrice(userMessage.getMessage().getDemand().getPrice());
            detail.setEndDate(userMessage.getMessage().getDemand().getEndDate());
            detail.setValidTo(userMessage.getMessage().getDemand().getValidTo());
        }
        detail.setStarred(userMessage.isStarred());
        return detail;

    }

    /**
     * @{inheritDoc}
     */
    @Override
    public UserMessage convertToSource(ClientDemandDetail clientDemandDetail) {
        throw new UnsupportedOperationException("Conversion from ClientDemandDetail to domain object UserMessage "
                + "is not implemented yet!");
    }
}
