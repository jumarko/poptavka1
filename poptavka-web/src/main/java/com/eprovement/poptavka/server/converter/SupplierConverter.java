/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.address.Address;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.Validate;

public final class SupplierConverter extends AbstractConverter<Supplier, FullSupplierDetail> {

    private final Converter<Address, AddressDetail> addressConverter;

    private SupplierConverter(Converter<Address, AddressDetail> addressConverter) {
        // Spring instantiates converters - see converters.xml
        Validate.notNull(addressConverter);
        this.addressConverter = addressConverter;
    }

    @Override
    public FullSupplierDetail convertToTarget(Supplier source) {
        FullSupplierDetail detail = new FullSupplierDetail();
        detail.setSupplierId(source.getId());
        if (source.getOveralRating() != null) {
            detail.setOverallRating(source.getOveralRating());
        }
        if (source.isCertified() != null) {
            detail.setCertified(source.isCertified());
        }
        if (source.getVerification() != null) {
            detail.setVerification(source.getVerification().name());
        }
        //categories
        Map<Long, String> catMap = new HashMap<Long, String>();
        for (Category cat : source.getCategories()) {
            catMap.put(cat.getId(), cat.getName());
        }
        detail.setCategories(catMap);
        //localities
        Map<String, String> locMap = new HashMap<String, String>();
        for (Locality loc : source.getLocalities()) {
            locMap.put(loc.getCode(), loc.getName());
        }
        detail.setLocalities(locMap);
        if (source.getBusinessUser() != null) {
            for (Address address : source.getBusinessUser().getAddresses()) {
                detail.addAddress(addressConverter.convertToTarget(address));
            }
            detail.setEmail(source.getBusinessUser().getEmail());
            if (source.getBusinessUser().getBusinessUserData() != null) {
                detail.setDescription(source.getBusinessUser().getBusinessUserData().getDescription());
                //        services = supplier.getBusinessUser().getUserServices();
                if (source.getBusinessUser().getBusinessType() != null) {
                    detail.setBusinessType(source.getBusinessUser().getBusinessType().getValue());
                }
                detail.setCompanyName(source.getBusinessUser().getBusinessUserData().getCompanyName());
                detail.setTaxId(source.getBusinessUser().getBusinessUserData().getTaxId());
                detail.setWebsite(source.getBusinessUser().getBusinessUserData().getWebsite());
                if (source.getBusinessUser().getBusinessUserData().getIdentificationNumber() != null) {
                    detail.setIdentificationNumber(source.getBusinessUser()
                            .getBusinessUserData().getIdentificationNumber());
                }
                detail.setFirstName(source.getBusinessUser().getBusinessUserData().getPersonFirstName());
                detail.setLastName(source.getBusinessUser().getBusinessUserData().getPersonLastName());
                detail.setPhone(source.getBusinessUser().getBusinessUserData().getPhone());
            }
        }
        return detail;

    }

    @Override
    public Supplier converToSource(FullSupplierDetail source) {
        throw new UnsupportedOperationException();
    }
}
