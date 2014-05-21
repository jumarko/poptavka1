package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.common.Origin;
import com.eprovement.poptavka.shared.domain.demand.OriginDetail;

/**
 * Converts Origin to OriginDetail.
 * @author Juraj Martinka
 */
public final class OriginConverter extends AbstractConverter<Origin, OriginDetail> {

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates OriginConverter.
     */
    private OriginConverter() {
        // Spring instantiates converters - see converters.xml
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public OriginDetail convertToTarget(Origin source) {
        if (source == null) {
            return null;
        }

        OriginDetail detail = new OriginDetail();
        detail.setId(source.getId());
        detail.setName(source.getName());
        detail.setDescription(source.getDescription());
        detail.setUrl(source.getUrl());
        return detail;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Origin convertToSource(OriginDetail originDetail) {
        throw new UnsupportedOperationException("Conversion from OriginDetail to domain object Origin"
                + "is not implemented yet!");
    }
}
