/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandDetail;

public final class ClientDemandUserMessageConverter extends AbstractConverter<UserMessage, ClientDemandDetail> {

    private ClientDemandUserMessageConverter() {
        // Spring instantiates converters - see converters.xml
    }

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
            detail.setValidToDate(userMessage.getMessage().getDemand().getValidTo());
        }
        detail.setIsStarred(userMessage.isStarred());
        return detail;

    }

    @Override
    public UserMessage convertToSource(ClientDemandDetail clientDemandDetail) {
        throw new UnsupportedOperationException("Conversion from ClientDemandDetail to domain object UserMessage "
                + "is not implemented yet!");
    }
}
