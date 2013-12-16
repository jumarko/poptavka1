/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.product.Service;
import com.eprovement.poptavka.domain.product.UserService;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import java.util.ArrayList;
import java.util.List;
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
    private final Converter<Service, ServiceDetail> serviceConverter;

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates SupplierConverter.
     */
    private SupplierConverter(
            Converter<Category, ICatLocDetail> categoryConverter,
            Converter<Locality, ICatLocDetail> localityConverter,
            Converter<BusinessUser, BusinessUserDetail> businessUserConverter,
            Converter<Service, ServiceDetail> serviceConverter) {
        // Spring instantiates converters - see converters.xml
        Validate.notNull(categoryConverter);
        Validate.notNull(localityConverter);
        Validate.notNull(businessUserConverter);
        this.categoryConverter = categoryConverter;
        this.localityConverter = localityConverter;
        this.businessUserConverter = businessUserConverter;
        this.serviceConverter = serviceConverter;
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
        detail.setOveralRating(source.getOveralRating());
        if (source.isCertified() != null) {
            detail.setCertified(source.isCertified());
        }
        detail.setCategories(categoryConverter.convertToTargetList(source.getCategories()));
        detail.setLocalities(localityConverter.convertToTargetList(source.getLocalities()));
        //TODO LATER ivlcek: services
        if (source.getBusinessUser() != null) {
            detail.setUserData(businessUserConverter.convertToTarget(source.getBusinessUser()));
        }
        detail.setServices(convertServices(source.getBusinessUser().getUserServices()));
        return detail;

    }

    /**
     * Converts services from UserServices to ServiceDetails.
     * @param services - list of UserServices
     * @return list of ServiceDetails
     */
    private List<ServiceDetail> convertServices(List<UserService> services) {
        List<ServiceDetail> servicesDetails = new ArrayList<ServiceDetail>();
        for (UserService userService : services) {
            servicesDetails.add(serviceConverter.convertToTarget(userService.getService()));
        }
        return servicesDetails;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Supplier convertToSource(FullSupplierDetail source) {
        throw new UnsupportedOperationException();
    }
}
