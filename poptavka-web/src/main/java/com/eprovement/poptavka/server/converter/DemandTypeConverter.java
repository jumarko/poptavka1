/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.demand.DemandType;
import com.eprovement.poptavka.shared.domain.demand.DemandTypeDetail;

/**
 * Converts DemandType to DemandTypeDetail and vice versa.
 * @author Juraj Martinka
 */
public final class DemandTypeConverter extends AbstractConverter<DemandType, DemandTypeDetail> {

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates DemandTypeConverter.
     */
    private DemandTypeConverter() {
        // Spring instantiates converters - see converters.xml
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public DemandTypeDetail convertToTarget(DemandType demandType) {
        DemandTypeDetail detail = new DemandTypeDetail();

        detail.setId(demandType.getId());
        detail.setValue(demandType.getType().getValue());
        detail.setDescription(demandType.getDescription());

        return detail;

    }

    /**
     * @{inheritDoc}
     */
    @Override
    public DemandType convertToSource(DemandTypeDetail demandTypeDetail) {
        throw new UnsupportedOperationException("Conversion from DemandTypeDetail to domain object DemandType "
                + "is not implemented yet!");
    }
}
