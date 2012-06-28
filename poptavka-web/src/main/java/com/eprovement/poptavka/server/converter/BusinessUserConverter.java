/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.BusinessUserRole;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.shared.domain.UserDetail;

public class BusinessUserConverter extends AbstractConverter<BusinessUser, UserDetail> {

    private final AddressConverter addressConverter = new AddressConverter();
    @Override
    public UserDetail convertToTarget(BusinessUser businessUser) {
        final UserDetail detail = new UserDetail();
        detail.setUserId(businessUser.getId());
        detail.setAddresses(addressConverter.convertToTargetList(businessUser.getAddresses()));
        for (BusinessUserRole role : businessUser.getBusinessUserRoles()) {
            if (role instanceof Client) {
                detail.setClientId(role.getId());
                detail.getRoleList().add(UserDetail.Role.CLIENT);
            }
            if (role instanceof Supplier) {
                detail.setSupplierId(role.getId());
                detail.getRoleList().add(UserDetail.Role.SUPPLIER);
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
    public BusinessUser converToSource(UserDetail userDetail) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    // COnsider implementation of following methods
//    public static BusinessUser updateBusinessUser(BusinessUser domain, UserDetail detail) {
//        if (!domain.getEmail().equals(detail.getEmail())) {
//            domain.setEmail(detail.getEmail());
//        }
//        if (!domain.getPassword().equals(detail.getPassword())) {
//            domain.setPassword(detail.getPassword());
//        }
//        if (!domain.getBusinessUserData().getPersonFirstName().equals(detail.getFirstName())) {
//            domain.getBusinessUserData().setPersonFirstName(detail.getFirstName());
//        }
//        if (!domain.getBusinessUserData().getPersonLastName().equals(detail.getLastName())) {
//            domain.getBusinessUserData().setPersonLastName(detail.getLastName());
//        }
//        if (!domain.getBusinessUserData().getPhone().equals(detail.getPhone())) {
//            domain.getBusinessUserData().setPhone(detail.getPhone());
//        }
//        if (!domain.getBusinessUserData().getIdentificationNumber().equals(detail.getIdentificationNumber())) {
//            domain.getBusinessUserData().setIdentificationNumber(detail.getIdentificationNumber());
//        }
//        if (!domain.getBusinessUserData().getCompanyName().equals(detail.getCompanyName())) {
//            domain.getBusinessUserData().setCompanyName(detail.getCompanyName());
//        }
//        if (!domain.getBusinessUserData().getDescription().equals(detail.getDescription())) {
//            domain.getBusinessUserData().setDescription(detail.getDescription());
//        }
//        if (!domain.getBusinessUserData().getTaxId().equals(detail.getTaxId())) {
//            domain.getBusinessUserData().setTaxId(detail.getTaxId());
//        }
//        if (!domain.getBusinessUserData().getWebsite().equals(detail.getWebsite())) {
//            domain.getBusinessUserData().setWebsite(detail.getWebsite());
//        }
//        //TODO Martin - how to update following?
//        //    private SupplierDetail supplier = null;
//        //    private ArrayList<Role> roleList = new ArrayList<Role>();
//        //    private Long userId;
//        //    private Long clientId = -1L;
//        //    private Long supplierId = -1L;
//        //    private List<AddressDetail> addresses;
//        //    private ArrayList<String> demandsId = new ArrayList<String>();
//        //    private boolean verified = false;
//        return domain;
//    }

}
