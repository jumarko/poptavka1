/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.address.Address;
import com.eprovement.poptavka.shared.domain.AddressDetail;

public class AddressConverter extends AbstractConverter<Address, AddressDetail> {
    @Override
    public AddressDetail convertToTarget(Address address) {
        AddressDetail detail = new AddressDetail();
        if (address.getCity() != null) {
            detail.setCity(address.getCity().getName());
        }
        if (address.getStreet() != null) {
            StringBuilder fullStreet = new StringBuilder(address.getStreet());
            fullStreet.append(" ");
            if (address.getFlatNum() != null && !address.getFlatNum().equals("")) {
                fullStreet.append(address.getFlatNum());
            }
            if (address.getHouseNum() != null && !address.getHouseNum().equals("")) {
                fullStreet.append(address.getHouseNum());
            }
        }
        detail.setStreet(address.getStreet());
        detail.setZipCode(address.getZipCode());
        return detail;

    }

    @Override
    public Address converToSource(AddressDetail addressDetail) {
        throw new UnsupportedOperationException("Conversion from AddressDetail to domain object Address "
                + "is not implemented yet!");
    }
}
