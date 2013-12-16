/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.address.Address;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Converts Address to AddressDetail.
 * @author Juraj Martinka
 */
public final class AddressConverter extends AbstractConverter<Address, AddressDetail> {

    /**************************************************************************/
    /* RPC services                                                           */
    /**************************************************************************/
    private LocalityService localityService;

    @Autowired
    public void setLocalityService(LocalityService localityService) {
        this.localityService = localityService;
    }

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates AddressConverter.
     */
    private AddressConverter() {
        // Spring instantiates converters - see converters.xml
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public AddressDetail convertToTarget(Address address) {
        //get STATE ->> CITY --> DISTRICT --> STATE --> COUNTRY
        AddressDetail detail = new AddressDetail();
        if (address.getCity() != null) {
            detail.setCity(address.getCity().getName());
            detail.setCityId(address.getCity().getId());
            if (address.getCity().getParent() != null) {
                detail.setDistrict(address.getCity().getParent().getName());
                if (address.getCity().getParent().getParent() != null) {
                    detail.setRegion(address.getCity().getParent().getParent().getName());
                    if (address.getCity().getParent().getParent().getParent() != null) {
                        detail.setCountry(address.getCity().getParent().getParent().getParent().getName());
                    }
                }
            }
        }
        detail.setStreet(address.getStreet());
        detail.setZipCode(address.getZipCode());
        return detail;

    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Address convertToSource(AddressDetail addressDetail) {
        Address address = new Address();
        address.setCity(localityService.getLocality(addressDetail.getCityId()));
        address.setStreet(addressDetail.getStreet());
        address.setZipCode(addressDetail.getZipCode());
        return address;
    }
}
