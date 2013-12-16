package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.common.Origin;
import com.eprovement.poptavka.shared.domain.demand.OriginDetail;

public final class OriginConverter extends AbstractConverter<Origin, OriginDetail> {

    private OriginConverter() {
        // Spring instantiates converters - see converters.xml
    }

    @Override
    public OriginDetail convertToTarget(Origin origin) {
        final OriginDetail detail = new OriginDetail();

        detail.setId(origin.getId());
        detail.setName(origin.getName());
        detail.setDescription(origin.getDescription());
        detail.setUrl(origin.getUrl());

        return detail;
    }

    @Override
    public Origin convertToSource(OriginDetail originDetail) {
        throw new UnsupportedOperationException("Conversion from OriginDetail to domain object Origin"
                + "is not implemented yet!");
    }
}
