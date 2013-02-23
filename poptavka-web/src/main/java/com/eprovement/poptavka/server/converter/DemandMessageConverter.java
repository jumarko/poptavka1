/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.demand.DemandType;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.shared.domain.demand.DemandTypeDetail;
import com.eprovement.poptavka.shared.domain.message.DemandMessageDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import org.apache.commons.lang.Validate;

public final class DemandMessageConverter extends AbstractConverter<UserMessage, DemandMessageDetail> {

    private final Converter<DemandType, DemandTypeDetail> demandTypeConverter;

    private final Converter<UserMessage, MessageDetail> messageConverter;

    private DemandMessageConverter(Converter<DemandType, DemandTypeDetail> demandTypeConverter,
            Converter<UserMessage, MessageDetail> messageConverter) {
        // Spring instantiates converters - see converters.xml

        Validate.notNull(demandTypeConverter);
        Validate.notNull(messageConverter);

        this.demandTypeConverter = demandTypeConverter;
        this.messageConverter = messageConverter;
    }

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
    public UserMessage convertToSource(DemandMessageDetail demandMessageDetail) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
