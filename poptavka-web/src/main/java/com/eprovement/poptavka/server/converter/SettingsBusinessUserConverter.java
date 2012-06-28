/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.address.Address;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.settings.SettingsDetail;
import java.util.ArrayList;
import java.util.List;

public class SettingsBusinessUserConverter extends AbstractConverter<BusinessUser, SettingsDetail> {

    private final AddressConverter addressConverter = new AddressConverter();

    @Override
    public SettingsDetail convertToTarget(BusinessUser businessUser) {
        final SettingsDetail detail = new SettingsDetail();
        List<AddressDetail> addresses = new ArrayList<AddressDetail>();
        for (Address addr : businessUser.getAddresses()) {
            addresses.add(addressConverter.convertToTarget(addr));
        }
        detail.setAddresses(addresses);
        if (businessUser.getBusinessUserData() != null) {
            detail.setCompanyName(businessUser.getBusinessUserData().getCompanyName());
            detail.setDescription(businessUser.getBusinessUserData().getDescription());
            detail.setFirstName(businessUser.getBusinessUserData().getPersonFirstName());
            detail.setLastName(businessUser.getBusinessUserData().getPersonLastName());
            detail.setIdentificationNumber(businessUser.getBusinessUserData().getIdentificationNumber());
            detail.setPhone(businessUser.getBusinessUserData().getPhone());
        }
        detail.setEmail(businessUser.getEmail());

        return detail;

    }

    @Override
    public BusinessUser converToSource(SettingsDetail settingsDetail) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
