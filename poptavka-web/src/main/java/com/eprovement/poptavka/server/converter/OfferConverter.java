/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.offer.Offer;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;

public final class OfferConverter extends AbstractConverter<Offer, OfferDetail> {

    private OfferConverter() {
        // Spring instantiates converters - see converters.xml
    }

    @Override
    public OfferDetail convertToTarget(Offer source) {
        OfferDetail detail = new OfferDetail();
        //offer info
        detail.setId(source.getId());
        detail.setPrice(source.getPrice());
        //detail.setState(source.getState().getType());
        detail.setCreatedDate(convertDate(source.getCreated()));
        detail.setFinishDate(convertDate(source.getFinishDate()));
        //demand info
        detail.setDemandId(source.getDemand().getId());
        detail.setDemandTitle(source.getDemand().getTitle());
        //supplier info
        detail.setSupplierId(source.getSupplier().getId());
        detail.setDisplayName(source.getSupplier().getBusinessUser().getBusinessUserData().getDisplayName());
        return detail;
    }

    @Override
    public Offer convertToSource(OfferDetail source) {
        throw new UnsupportedOperationException();
    }
}
