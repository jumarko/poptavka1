/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.shared.domain.offer.FullOfferDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FullOfferConverter extends AbstractConverter<Message, FullOfferDetail> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FullOfferConverter.class);


    @Override
    public FullOfferDetail convertToTarget(Message message) {
        final FullOfferDetail detail = new FullOfferDetail();
        if (message == null) {
            return detail;
        }
        detail.setMessageDetail(new MessageConverter().convertToTarget(message));
        detail.setOfferDetail(new OfferConverter().convertToTarget(message.getOffer()));

        detail.setIsRead(true);

        LOGGER.info("OFFER ID: " + message.getId() + ", OFFER DETAIL ID: " + message.getOffer().getId());
        return detail;

    }

    @Override
    public Message converToSource(FullOfferDetail fullOfferDetail) {
        throw new UnsupportedOperationException("Conversion from FullOfferDetail to domain object Message "
                + "is not implemented yet!");
    }
}
