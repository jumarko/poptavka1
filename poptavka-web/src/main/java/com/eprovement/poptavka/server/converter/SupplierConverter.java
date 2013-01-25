/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import org.apache.commons.lang.Validate;

public final class SupplierConverter extends AbstractConverter<Supplier, FullSupplierDetail> {

    private final Converter<Category, CategoryDetail> categoryConverter;
    private final Converter<Locality, LocalityDetail> localityConverter;
    private final Converter<BusinessUser, BusinessUserDetail> businessUserConverter;

    private SupplierConverter(
            Converter<Category, CategoryDetail> categoryConverter,
            Converter<Locality, LocalityDetail> localityConverter,
            Converter<BusinessUser, BusinessUserDetail> businessUserConverter) {
        // Spring instantiates converters - see converters.xml
        Validate.notNull(categoryConverter);
        Validate.notNull(localityConverter);
        Validate.notNull(businessUserConverter);
        this.categoryConverter = categoryConverter;
        this.localityConverter = localityConverter;
        this.businessUserConverter = businessUserConverter;
    }

    @Override
    public FullSupplierDetail convertToTarget(Supplier source) {
        FullSupplierDetail detail = new FullSupplierDetail();
        detail.setSupplierId(source.getId());
        if (source.isCertified() != null) {
            detail.setCertified(source.isCertified());
        }
        detail.setCategories(categoryConverter.convertToTargetList(source.getCategories()));
        detail.setLocalities(localityConverter.convertToTargetList(source.getLocalities()));
        //TODO services
        if (source.getBusinessUser() != null) {
            detail.setUserData(businessUserConverter.convertToTarget(source.getBusinessUser()));
            detail.getUserData().setOverallRating(source.getOveralRating());
        }
        return detail;

    }

    @Override
    public Supplier convertToSource(FullSupplierDetail source) {
        throw new UnsupportedOperationException();
    }
}
