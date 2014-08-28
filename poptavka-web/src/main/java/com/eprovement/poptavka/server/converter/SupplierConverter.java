/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import org.apache.commons.lang.Validate;

/**
 * Converts Supplier to FullSupplierDetail.
 * @author Juraj Martinka
 */
public final class SupplierConverter extends AbstractConverter<Supplier, FullSupplierDetail> {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private final Converter<Category, ICatLocDetail> categoryConverter;
    private final Converter<Locality, ICatLocDetail> localityConverter;
    private final Converter<BusinessUser, BusinessUserDetail> businessUserConverter;

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates SupplierConverter.
     */
    private SupplierConverter(
            Converter<Category, ICatLocDetail> categoryConverter,
            Converter<Locality, ICatLocDetail> localityConverter,
            Converter<BusinessUser, BusinessUserDetail> businessUserConverter) {
        // Spring instantiates converters - see converters.xml
        Validate.notNull(categoryConverter);
        Validate.notNull(localityConverter);
        Validate.notNull(businessUserConverter);
        this.categoryConverter = categoryConverter;
        this.localityConverter = localityConverter;
        this.businessUserConverter = businessUserConverter;
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public FullSupplierDetail convertToTarget(Supplier source) {
        FullSupplierDetail detail = new FullSupplierDetail();
        detail.setSupplierId(source.getId());
        detail.setOveralRating(source.getOveralRating() == null ? 0 : source.getOveralRating());
        if (source.isCertified() != null) {
            detail.setCertified(source.isCertified());
        }
        detail.setCategories(categoryConverter.convertToTargetList(source.getCategories()));
        detail.setLocalities(localityConverter.convertToTargetList(source.getLocalities()));
        //TODO LATER ivlcek: services
        if (source.getBusinessUser() != null) {
            detail.setUserData(businessUserConverter.convertToTarget(source.getBusinessUser()));
        }
        return detail;

    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Supplier convertToSource(FullSupplierDetail source) {
        throw new UnsupportedOperationException();
    }
}
