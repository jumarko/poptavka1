/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.demand.DemandOrigin;
import com.eprovement.poptavka.shared.domain.demand.DemandOriginDetail;

public class DemandOriginConverter extends AbstractConverter<DemandOrigin, DemandOriginDetail> {
    @Override
    public DemandOriginDetail convertToTarget(DemandOrigin demandOrigin) {
        final DemandOriginDetail detail = new DemandOriginDetail();

        detail.setId(demandOrigin.getId());
        detail.setName(demandOrigin.getName());
        detail.setDescription(demandOrigin.getDescription());
        detail.setUrl(demandOrigin.getUrl());

        return detail;
    }

    @Override
    public DemandOrigin converToSource(DemandOriginDetail demandOriginDetail) {
        throw new UnsupportedOperationException("Conversion from DemandOriginDetail to domain object DemandOrigin"
                + "is not implemented yet!");
    }
}
