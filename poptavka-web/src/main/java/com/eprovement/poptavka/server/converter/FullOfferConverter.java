/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.offer.Offer;
import com.eprovement.poptavka.service.offer.OfferService;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.offer.FullOfferDetail;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public final class FullOfferConverter extends AbstractConverter<Message, FullOfferDetail> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FullOfferConverter.class);
    /**************************************************************************/
    /* RPC Services                                                           */
    /**************************************************************************/
    private OfferService offerService;

    @Autowired
    public void setOfferService(OfferService offerService) {
        this.offerService = offerService;
    }
    /**************************************************************************/
    /* Other converters                                                       */
    /**************************************************************************/
    private final Converter<Offer, OfferDetail> offerConverter;
    private final Converter<Message, MessageDetail> messageConverter;
    private final Converter<Demand, FullDemandDetail> demandConverter;

    private FullOfferConverter(Converter<Offer, OfferDetail> offerConverter,
            Converter<Message, MessageDetail> messageConverter,
            Converter<Demand, FullDemandDetail> demandConverter) {
        // Spring instantiates converters - see converters.xml
        Validate.notNull(offerConverter);
        Validate.notNull(messageConverter);
        this.offerConverter = offerConverter;
        this.messageConverter = messageConverter;
        this.demandConverter = demandConverter;
    }

    @Override
    public FullOfferDetail convertToTarget(Message message) {
        final FullOfferDetail detail = new FullOfferDetail();
        if (message == null) {
            return detail;
        }
        detail.setMessageDetail(messageConverter.convertToTarget(message));
        detail.setOfferDetail(offerConverter.convertToTarget(message.getOffer()));
        detail.setDemandDetail(demandConverter.convertToTarget(message.getDemand()));

        detail.setRead(true);

        LOGGER.info("OFFER ID: " + message.getId() + ", OFFER DETAIL ID: " + message.getOffer().getId());
        return detail;

    }

    @Override
    public Message convertToSource(FullOfferDetail fullOfferDetail) {
        //TODO Juraj - ako to implementovat?
//        offerService.getById(fullOfferDetail.getOfferDetail().getId());
        throw new UnsupportedOperationException("Conversion from FullOfferDetail to domain object Message "
                + "is not implemented yet!");

    }
}
