/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.address.Address;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.BusinessUserRole;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Partner;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import org.apache.commons.lang.Validate;

/**
 * Converts BusinessUser to BusinessUserDetail and vice versa.
 * @author Juraj Martinka
 */
public final class BusinessUserConverter extends AbstractConverter<BusinessUser, BusinessUserDetail> {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private final Converter<Address, AddressDetail> addressConverter;

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates BusinessUserConverter.
     */
    private BusinessUserConverter(Converter<Address, AddressDetail> addressConverter) {
        // Spring instantiates converters - see converters.xml
        Validate.notNull(addressConverter);
        this.addressConverter = addressConverter;
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public BusinessUserDetail convertToTarget(BusinessUser source) {
        if (source == null) {
            return null;
        }
        final BusinessUserDetail detail = new BusinessUserDetail();
        detail.setUserId(source.getId());
        detail.setEmail(source.getEmail());
        detail.setPassword(source.getPassword());
        //preco pada na:
        //org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role:
        //com.eprovement.poptavka.domain.user.rights.AccessRole.permissions, no session or session was closed
//        detail.setAccessRoles(accessRoleConverter.convertToTargetList(user.getAccessRoles())_;
        detail.setAddresses(addressConverter.convertToTargetList(source.getAddresses()));
        detail.setBusinessType(source.getBusinessType());
        //BusinessRole
        for (BusinessUserRole role : source.getBusinessUserRoles()) {
            detail.setVerification(role.getVerification());
            if (role instanceof Client) {
                detail.setClientId(role.getId());
                detail.getBusinessRoles().add(BusinessUserDetail.BusinessRole.CLIENT);
            }
            if (role instanceof Supplier) {
                detail.setSupplierId(role.getId());
                detail.getBusinessRoles().add(BusinessUserDetail.BusinessRole.SUPPLIER);
            }
            if (role instanceof Partner) {
                detail.getBusinessRoles().add(BusinessUserDetail.BusinessRole.PARTNER);
            }
        }
        //BusinessUserData
        if (source.getBusinessUserData() != null) {
            detail.setCompanyName(source.getBusinessUserData().getCompanyName());
            detail.setDisplayName(source.getBusinessUserData().getDisplayName());
            detail.setIdentificationNumber(source.getBusinessUserData().getIdentificationNumber());
            detail.setPersonFirstName(source.getBusinessUserData().getPersonFirstName());
            detail.setPersonLastName(source.getBusinessUserData().getPersonLastName());
            detail.setPhone(source.getBusinessUserData().getPhone());
            detail.setWebsite(source.getBusinessUserData().getWebsite());
            detail.setDescription(source.getBusinessUserData().getDescription());
            detail.setTaxId(source.getBusinessUserData().getTaxId());
        }
        return detail;

    }

    /**
     * @{inheritDoc}
     */
    @Override
    public BusinessUser convertToSource(BusinessUserDetail userDetail) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
