/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.demand.DemandType;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.shared.domain.demand.DemandTypeDetail;
import com.eprovement.poptavka.shared.domain.message.DemandMessageDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import org.apache.commons.lang.Validate;

/**
 * Converts UserMessage to DemandMessageDetail and vice versa.
 * @author Juraj Martinka
 */
public final class DemandMessageConverter extends AbstractConverter<UserMessage, DemandMessageDetail> {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private final Converter<DemandType, DemandTypeDetail> demandTypeConverter;
    private final Converter<UserMessage, MessageDetail> messageConverter;

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates DemandMessageConverter.
     */
    private DemandMessageConverter(Converter<DemandType, DemandTypeDetail> demandTypeConverter,
            Converter<UserMessage, MessageDetail> messageConverter) {
        // Spring instantiates converters - see converters.xml

        Validate.notNull(demandTypeConverter);
        Validate.notNull(messageConverter);

        this.demandTypeConverter = demandTypeConverter;
        this.messageConverter = messageConverter;
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public DemandMessageDetail convertToTarget(UserMessage userMessage) {
        final DemandMessageDetail demandMessageDetail = new DemandMessageDetail();

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

    /**
     * @{inheritDoc}
     */
    @Override
    public UserMessage convertToSource(DemandMessageDetail demandMessageDetail) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
