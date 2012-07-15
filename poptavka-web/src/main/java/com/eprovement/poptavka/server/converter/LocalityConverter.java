/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.shared.domain.LocalityDetail;

public final class LocalityConverter extends AbstractConverter<Locality, LocalityDetail> {

    private LocalityConverter() {
        // Spring instantiates converters - see converters.xml
    }

    @Override
    public LocalityDetail convertToTarget(Locality locality) {
        LocalityDetail detail = new LocalityDetail();
        detail.setId(locality.getId());
        detail.setCode(locality.getCode());
        detail.setName(locality.getName());
        return detail;

    }

    @Override
    public Locality converToSource(LocalityDetail localityDetail) {
        throw new UnsupportedOperationException("Conversion from AddressDetail to domain object Address "
                + "is not implemented yet!");
    }
}
