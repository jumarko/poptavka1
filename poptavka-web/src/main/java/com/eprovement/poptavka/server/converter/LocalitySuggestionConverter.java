/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.shared.domain.LocalityDetailSuggestion;
import org.springframework.beans.factory.annotation.Autowired;

public final class LocalitySuggestionConverter extends AbstractConverter<Locality, LocalityDetailSuggestion> {

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
    public LocalityDetailSuggestion convertToTarget(Locality localityCity) {
        LocalityDetailSuggestion detail = new LocalityDetailSuggestion();
        //get STATE ->> CITY->DISTRICT->STATE
        detail.setStateId(localityCity.getParent().getParent().getId());
        detail.setStateName(localityCity.getParent().getParent().getCode());
        detail.setStateName(localityCity.getParent().getParent().getName());

        detail.setCityId(localityCity.getId());
        detail.setCityName(localityCity.getCode());
        detail.setCityName(localityCity.getName());

        return detail;
    }

    @Override
    public Locality convertToSource(LocalityDetailSuggestion localityDetailSuggestion) {
        return localityService.getLocality(localityDetailSuggestion.getCityCode());
    }
}
