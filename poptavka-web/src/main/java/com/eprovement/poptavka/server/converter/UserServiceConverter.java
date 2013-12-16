/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.product.Service;
import com.eprovement.poptavka.domain.product.UserService;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.eprovement.poptavka.shared.domain.UserServiceDetail;
import org.apache.commons.lang.Validate;

/**
 * Converts UserService to UserServiceDetail.
 * @author Juraj Martinka
 */
public final class UserServiceConverter extends AbstractConverter<UserService, UserServiceDetail> {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private final Converter<Service, ServiceDetail> serviceConverter;
    private final Converter<BusinessUser, BusinessUserDetail> businessUserConverter;

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates UserServiceConverter.
     */
    private UserServiceConverter(Converter<Service, ServiceDetail> serviceConverter,
            Converter<BusinessUser, BusinessUserDetail> businessUserConverter) {
        // Spring instantiates converters - see converters.xml
        Validate.notNull(serviceConverter);
        Validate.notNull(businessUserConverter);
        this.serviceConverter = serviceConverter;
        this.businessUserConverter = businessUserConverter;
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public UserServiceDetail convertToTarget(UserService userService) {
        final UserServiceDetail detail = new UserServiceDetail();

        detail.setService(serviceConverter.convertToTarget(userService.getService()));
        detail.setStatus(userService.getStatus().name());
        detail.setUser(businessUserConverter.convertToTarget(userService.getUser()));

        return detail;

    }

    /**
     * @{inheritDoc}
     */
    @Override
    public UserService convertToSource(UserServiceDetail userServiceDetail) {
        throw new UnsupportedOperationException("Conversion from UserServiceDetail to domain object UserService "
               + "is not implemented yet!");
    }
}
