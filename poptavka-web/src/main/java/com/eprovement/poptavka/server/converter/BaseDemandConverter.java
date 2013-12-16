/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.shared.domain.demand.BaseDemandDetail;

/**
 * Converts Demand to BaseDemandDetail and vice versa.
 * @author Juraj Martinka
 */
public final class BaseDemandConverter extends AbstractConverter<Demand, BaseDemandDetail> {

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates BaseDemandConverter.
     */
    private BaseDemandConverter() {
        // Spring instantiates converters - see converters.xml
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public BaseDemandDetail convertToTarget(Demand demand) {
        final BaseDemandDetail baseDemandDetail = new BaseDemandDetail();
        baseDemandDetail.setDemandId(demand.getId());
        baseDemandDetail.setTitle(demand.getTitle());
        baseDemandDetail.setDescription(demand.getDescription());
        baseDemandDetail.setPrice(demand.getPrice());
        baseDemandDetail.setEndDate(demand.getEndDate());
        baseDemandDetail.setValidToDate(demand.getValidTo());
        return baseDemandDetail;

    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Demand convertToSource(BaseDemandDetail baseDemandDetail) {
        throw new UnsupportedOperationException("Conversion from BaseDemandDetail to domain object Demand "
                + "is not implemented yet!");
    }
}
