/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.shared.domain.message.DemandMessageDetail;

public class DemandMessageConverter extends AbstractConverter<UserMessage, DemandMessageDetail> {

    private final DemandTypeConverter demandTypeConverter = new DemandTypeConverter();

    private final MessageDetailFromUserMessageConverter messageDetailConverter =
            new MessageDetailFromUserMessageConverter();

    @Override
    public DemandMessageDetail convertToTarget(UserMessage userMessage) {
        final DemandMessageDetail demandMessageDetail = new DemandMessageDetail();

        // TODO:
//        MessageDetail.fillMessageDetail(demandMessageDetail, userMessage);
        demandMessageDetail.setDemandId(userMessage.getMessage().getDemand().getId());
        demandMessageDetail.setDemandTitle(userMessage.getMessage().getDemand().getTitle());
        demandMessageDetail.setPrice(userMessage.getMessage().getDemand().getPrice());
        demandMessageDetail.setEndDate(userMessage.getMessage().getDemand().getEndDate());
        demandMessageDetail.setValidToDate(userMessage.getMessage().getDemand().getValidTo());
        demandMessageDetail.setDemandType(demandTypeConverter.convertToTarget(
                userMessage.getMessage().getDemand().getType()));
        demandMessageDetail.setDemandStatus(userMessage.getMessage().getDemand().getStatus());
        return demandMessageDetail;


    }

    @Override
    public UserMessage converToSource(DemandMessageDetail demandMessageDetail) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
