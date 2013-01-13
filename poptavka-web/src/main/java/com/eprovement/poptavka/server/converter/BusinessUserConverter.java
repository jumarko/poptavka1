/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.address.Address;
import com.eprovement.poptavka.domain.enums.Verification;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.BusinessUserRole;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import org.apache.commons.lang.Validate;

public final class BusinessUserConverter extends AbstractConverter<BusinessUser, BusinessUserDetail> {

    private final Converter<Address, AddressDetail> addressConverter;

    private BusinessUserConverter(Converter<Address, AddressDetail> addressConverter) {
        // Spring instantiates converters - see converters.xml
        Validate.notNull(addressConverter);
        this.addressConverter = addressConverter;
    }


    @Override
    public BusinessUserDetail convertToTarget(BusinessUser businessUser) {
        if (businessUser == null) {
            return null;
        }
        final BusinessUserDetail detail = new BusinessUserDetail();
        detail.setUserId(businessUser.getId());
        detail.setAddresses(addressConverter.convertToTargetList(businessUser.getAddresses()));
        detail.setVerified(true);
        for (BusinessUserRole role : businessUser.getBusinessUserRoles()) {
            if (role.getVerification() == Verification.UNVERIFIED) {
                detail.setVerified(false);
            }
            if (role instanceof Client) {
                detail.setClientId(role.getId());
                detail.getBusinessRoles().add(BusinessUserDetail.BusinessRole.CLIENT);
            }
            if (role instanceof Supplier) {
                detail.setSupplierId(role.getId());
                detail.getBusinessRoles().add(BusinessUserDetail.BusinessRole.SUPPLIER);
            }
        }
        if (businessUser.getBusinessUserData() != null) {
            detail.setCompanyName(businessUser.getBusinessUserData().getCompanyName());
            detail.setDescription(businessUser.getBusinessUserData().getDescription());
            detail.setFirstName(businessUser.getBusinessUserData().getPersonFirstName());
            detail.setLastName(businessUser.getBusinessUserData().getPersonLastName());
            detail.setIdentificationNumber(businessUser.getBusinessUserData().getIdentificationNumber());
            detail.setPhone(businessUser.getBusinessUserData().getPhone());
        }
        detail.setPassword(businessUser.getPassword());
        detail.setEmail(businessUser.getEmail());

        return detail;

    }

    @Override
    public BusinessUser convertToSource(BusinessUserDetail userDetail) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
