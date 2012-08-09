/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.demand.DemandType;
import com.eprovement.poptavka.shared.domain.demand.DemandTypeDetail;

public final class DemandTypeConverter extends AbstractConverter<DemandType, DemandTypeDetail> {

    private DemandTypeConverter() {
        // Spring instantiates converters - see converters.xml
    }

    @Override
    public DemandTypeDetail convertToTarget(DemandType demandType) {
        DemandTypeDetail detail = new DemandTypeDetail();

        detail.setId(demandType.getId());
        detail.setValue(demandType.getType().getValue());
        detail.setDescription(demandType.getDescription());

        return detail;

    }

    @Override
    public DemandType convertToSource(DemandTypeDetail demandTypeDetail) {
        throw new UnsupportedOperationException("Conversion from DemandTypeDetail to domain object DemandType "
                + "is not implemented yet!");
    }
}
