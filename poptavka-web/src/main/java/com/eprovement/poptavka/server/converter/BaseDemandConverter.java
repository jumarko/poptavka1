/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.shared.domain.demand.BaseDemandDetail;

public final class BaseDemandConverter extends AbstractConverter<Demand, BaseDemandDetail> {

    private BaseDemandConverter() {
        // Spring instantiates converters - see converters.xml
    }

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

    @Override
    public Demand converToSource(BaseDemandDetail baseDemandDetail) {
        throw new UnsupportedOperationException("Conversion from BaseDemandDetail to domain object Demand "
                + "is not implemented yet!");
    }
}
