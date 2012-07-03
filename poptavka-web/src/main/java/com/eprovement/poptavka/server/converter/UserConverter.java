/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.address.Address;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.BusinessUserRole;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Partner;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.domain.user.rights.AccessRole;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.adminModule.AccessRoleDetail;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.Validate;

public final class UserConverter extends AbstractConverter<BusinessUser, BusinessUserDetail> {

    private final Converter<Address, AddressDetail> addressConverter;
    private final Converter<AccessRole, AccessRoleDetail> accessRoleConverter;

    private UserConverter(Converter<Address, AddressDetail> addressConverter,
            Converter<AccessRole, AccessRoleDetail> accessRoleConverter) {
        // Spring instantiates converters - see converters.xml
        Validate.notNull(addressConverter);
        Validate.notNull(accessRoleConverter);
        this.addressConverter = addressConverter;
        this.accessRoleConverter = accessRoleConverter;
    }

    @Override
    public BusinessUserDetail convertToTarget(BusinessUser source) {
        BusinessUserDetail detail = new BusinessUserDetail();
        //User
        detail.setUserId(source.getId());
        detail.setEmail(source.getEmail());
        detail.setPassword(source.getPassword());
        detail.setAccessRoles(accessRoleConverter.convertToTargetList(source.getAccessRoles()));
        //BusinessUser
        List<AddressDetail> addresses = new ArrayList<AddressDetail>();
        for (Address addr : source.getAddresses()) {
            addresses.add(addressConverter.convertToTarget(addr));
        }
        detail.setAddresses(addresses);
        //Roles - CLIENT, SUPPLIER, PARTNER, OPERATOR, ADMINISTRATOR, ...
        for (BusinessUserRole role : source.getBusinessUserRoles()) {
            if (role instanceof Client) {
                detail.addRole(BusinessUserDetail.BusinessRole.CLIENT);
            }
            if (role instanceof Supplier) {
                detail.addRole(BusinessUserDetail.BusinessRole.SUPPLIER);
            }
            if (role instanceof Partner) {
                detail.addRole(BusinessUserDetail.BusinessRole.PARTNER);
            }
        }
        if (source.getBusinessUserData() != null) {
            detail.setCompanyName(source.getBusinessUserData().getCompanyName());
            detail.setDescription(source.getBusinessUserData().getDescription());
            detail.setFirstName(source.getBusinessUserData().getPersonFirstName());
            detail.setLastName(source.getBusinessUserData().getPersonLastName());
            detail.setIdentificationNumber(source.getBusinessUserData().getIdentificationNumber());
            detail.setPhone(source.getBusinessUserData().getPhone());
        }

        return detail;
    }

    @Override
    public BusinessUser converToSource(BusinessUserDetail source) {
        throw new UnsupportedOperationException();
    }
}
