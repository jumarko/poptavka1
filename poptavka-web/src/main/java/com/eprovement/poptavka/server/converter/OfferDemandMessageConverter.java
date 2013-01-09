/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.shared.domain.message.OfferDemandMessage;

public final class OfferDemandMessageConverter extends AbstractConverter<UserMessage, OfferDemandMessage> {

    private OfferDemandMessageConverter() {
        // Spring instantiates converters - see converters.xml
    }

    @Override
    public OfferDemandMessage convertToTarget(UserMessage userMessage) {
        OfferDemandMessage detail = new OfferDemandMessage();
        detail.setMessageId(userMessage.getId());
        detail.setBody(userMessage.getMessage().getBody());
        detail.setCreated(convertDate(userMessage.getMessage().getCreated()));
//        m.setFirstBornId(serialVersionUID);
        detail.setMessageState(userMessage.getMessage().getMessageState().name());
//        m.setNexSiblingId(serialVersionUID);
        detail.setParentId(userMessage.getMessage().getParent() == null ? detail.getThreadRootId()
                : userMessage.getMessage().getParent().getId());
        detail.setSenderId(userMessage.getMessage().getSender().getId());
        detail.setSent(convertDate(userMessage.getMessage().getSent()));
        detail.setSubject(userMessage.getMessage().getSubject());
        detail.setThreadRootId(userMessage.getMessage().getThreadRoot().getId());
        detail.setUserMessageId(userMessage.getId());
        detail.setDemandId(userMessage.getMessage().getDemand().getId());
        detail.setPrice(userMessage.getMessage().getDemand().getPrice());
        detail.setStarred(userMessage.isStarred());
        detail.setEndDate(convertDate(userMessage.getMessage().getDemand().getEndDate()));
        detail.setValidToDate(convertDate(userMessage.getMessage().getDemand().getValidTo()));
        detail.setDemandTitle(userMessage.getMessage().getDemand().getTitle());
        detail.setOfferCount(userMessage.getMessage().getDemand().getOffers().size());
        detail.setMaxOfferCount(userMessage.getMessage().getDemand().getMaxSuppliers());
        return detail;

    }

    @Override
    public UserMessage convertToSource(OfferDemandMessage offerDemandMessage) {
        throw new UnsupportedOperationException("Conversion from OfferDemandMessage to domain object UserMessage "
                + "is not implemented yet!");
    }
}
