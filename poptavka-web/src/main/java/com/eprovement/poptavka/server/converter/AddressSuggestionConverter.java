/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.enums.LocalityType;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.shared.selectors.addressSelector.AddressSuggestionDetail;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;

public final class AddressSuggestionConverter extends AbstractConverter<Locality, AddressSuggestionDetail> {

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
    private AddressSuggestionConverter() {
        // Spring instantiates converters - see converters.xml
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    @Override
    public AddressSuggestionDetail convertToTarget(Locality localityCity) {
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
        AddressSuggestionDetail detail = new AddressSuggestionDetail();
        //get STATE ->> CITY --> DISTRICT --> STATE --> COUNTRY
        if (localityCity.getParent() != null && localityCity.getParent().getParent() != null) {
            detail.setStateId(localityCity.getParent().getParent().getId());
            detail.setStateName(localityCity.getParent().getParent().getName());
        }

        detail.setCityId(localityCity.getId());
        detail.setCityName(localityCity.getName());

        return detail;
    }

    public Locality convertToSource(AddressSuggestionDetail catLocDetailSuggestion) {
        return localityService.getLocality(catLocDetailSuggestion.getCityId());
    }
}
