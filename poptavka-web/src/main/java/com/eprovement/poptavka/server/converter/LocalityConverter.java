/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import org.springframework.beans.factory.annotation.Autowired;

public final class LocalityConverter extends AbstractConverter<Locality, LocalityDetail> {

    private LocalityService localityService;

    @Autowired
    public void setLocalityService(LocalityService localityService) {
        this.localityService = localityService;
    }

    private LocalityConverter() {
        // Spring instantiates converters - see converters.xml
    }

    @Override
    public LocalityDetail convertToTarget(Locality locality) {
        LocalityDetail detail = new LocalityDetail();
        detail.setId(locality.getId());
        detail.setCode(locality.getCode());
        detail.setName(locality.getName());
        detail.setLocalityType(locality.getType());
        return detail;

    }

    @Override
    public Locality converToSource(LocalityDetail localityDetail) {
        return localityService.getLocality(localityDetail.getCode());
    }
}
