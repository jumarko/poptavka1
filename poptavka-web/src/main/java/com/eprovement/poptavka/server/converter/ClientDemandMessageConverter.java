/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.shared.domain.message.ClientDemandMessageDetail;

public class ClientDemandMessageConverter extends AbstractConverter<UserMessage, ClientDemandMessageDetail> {
    @Override
    public ClientDemandMessageDetail convertToTarget(UserMessage userMessage) {
        ClientDemandMessageDetail detail = new ClientDemandMessageDetail();
        if (userMessage.getMessage() != null
                && userMessage.getMessage().getDemand() != null
                && userMessage.getMessage().getDemand().getClient() != null
                && userMessage.getMessage().getDemand().getClient().getBusinessUser() != null
                && userMessage.getMessage().getDemand().getClient().getBusinessUser().getBusinessUserData() != null) {
            detail.setClientName(userMessage.getMessage().getDemand()
                    .getClient().getBusinessUser().getBusinessUserData().getDisplayName());
        }

        return detail;

    }

    @Override
    public UserMessage converToSource(ClientDemandMessageDetail clientDemandMessageDetail) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
