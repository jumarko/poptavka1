/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.enums.LocalityType;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.shared.domain.LocalitySuggestionDetail;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;

public final class LocalitySuggestionConverter extends AbstractConverter<Locality, LocalitySuggestionDetail> {

    /**************************************************************************/
    /* RPC Services                                                           */
    /**************************************************************************/
    private LocalityService localityService;

    @Autowired
    public void setLocalityService(LocalityService localityService) {
        this.localityService = localityService;
    }

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    private LocalitySuggestionConverter() {
        // Spring instantiates converters - see converters.xml
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    @Override
    public LocalitySuggestionDetail convertToTarget(Locality localityCity) {
        Preconditions.checkArgument(
                localityCity.getType() == LocalityType.CITY,
                "Converting locality suggestion but LocalityType expected to be a CITY, but found %s",
                localityCity.getType());
        Preconditions.checkNotNull(
                localityCity.getParent(),
                "District attribute NULL while converting locality suggestion.",
                localityCity.getType());
        Preconditions.checkNotNull(
                localityCity.getParent(),
                "State attribute NULL while converting locality suggestion.",
                localityCity.getType());
        LocalitySuggestionDetail detail = new LocalitySuggestionDetail();
        //get STATE ->> CITY --> DISTRICT --> STATE --> COUNTRY
        if (localityCity.getParent() != null && localityCity.getParent().getParent() != null) {
            detail.setStateId(localityCity.getParent().getParent().getId());
            detail.setStateName(localityCity.getParent().getParent().getName());
        }

        detail.setCityId(localityCity.getId());
        detail.setCityName(localityCity.getName());

        return detail;
    }

    @Override
    public Locality convertToSource(LocalitySuggestionDetail localityDetailSuggestion) {
        return localityService.getLocality(localityDetailSuggestion.getCityId());
    }
}
