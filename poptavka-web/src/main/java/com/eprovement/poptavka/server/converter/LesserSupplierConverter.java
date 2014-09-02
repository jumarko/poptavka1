/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.shared.domain.LesserBusinessUserDetail;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.domain.supplier.LesserSupplierDetail;
import org.apache.commons.lang.Validate;

/**
 * Converts Supplier to LesserSupplierDetail.
 * @author Ivan Vlcek
 */
public final class LesserSupplierConverter extends AbstractConverter<Supplier, LesserSupplierDetail> {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private final Converter<Category, ICatLocDetail> categoryConverter;
    private final Converter<BusinessUser, LesserBusinessUserDetail> lesserBusinessUserConverter;

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates SupplierConverter.
     */
    private LesserSupplierConverter(
            Converter<Category, ICatLocDetail> categoryConverter,
            Converter<BusinessUser, LesserBusinessUserDetail> lesserBusinessUserConverter) {
        // Spring instantiates converters - see converters.xml
        Validate.notNull(categoryConverter);
        Validate.notNull(lesserBusinessUserConverter);
        this.categoryConverter = categoryConverter;
        this.lesserBusinessUserConverter = lesserBusinessUserConverter;
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public LesserSupplierDetail convertToTarget(Supplier source) {
        LesserSupplierDetail detail = new LesserSupplierDetail();
        detail.setSupplierId(source.getId());
        detail.setOveralRating(source.getOveralRating() == null ? 0 : source.getOveralRating());
        if (source.isCertified() != null) {
            detail.setCertified(source.isCertified());
        }
        detail.setCategories(categoryConverter.convertToTargetList(source.getCategories()));
        if (source.getBusinessUser() != null) {
            detail.setUserData(lesserBusinessUserConverter.convertToTarget(source.getBusinessUser()));
        }
        return detail;

    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Supplier convertToSource(LesserSupplierDetail source) {
        throw new UnsupportedOperationException();
    }
}
