/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.product.UserService;
import com.eprovement.poptavka.shared.domain.UserServiceDetail;

public class UserServiceConverter extends AbstractConverter<UserService, UserServiceDetail> {

    private final ServiceConverter serviceConverter = new ServiceConverter();
    private final BusinessUserConverter businessUserConverter = new BusinessUserConverter();

    @Override
    public UserServiceDetail convertToTarget(UserService userService) {
        final UserServiceDetail detail = new UserServiceDetail();

        detail.setService(serviceConverter.convertToTarget(userService.getService()));
        detail.setStatus(userService.getStatus().name());
        detail.setUser(businessUserConverter.convertToTarget(userService.getUser()));

        return detail;

    }

    @Override
    public UserService converToSource(UserServiceDetail userServiceDetail) {
        throw new UnsupportedOperationException("Conversion from UserServiceDetail to domain object UserService "
               + "is not implemented yet!");
    }
}
