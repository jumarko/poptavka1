/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.domain.user.rights.AccessRole;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.eprovement.poptavka.shared.domain.adminModule.AccessRoleDetail;
import org.apache.commons.lang.Validate;

public final class UserConverter extends AbstractConverter<User, UserDetail> {

    private final Converter<AccessRole, AccessRoleDetail> accessRoleConverter;

    private UserConverter(
            Converter<AccessRole, AccessRoleDetail> accessRoleConverter) {
        // Spring instantiates converters - see converters.xml
        Validate.notNull(accessRoleConverter);
        this.accessRoleConverter = accessRoleConverter;
    }

    @Override
    public UserDetail convertToTarget(User source) {
        UserDetail detail = new UserDetail();
        //User
        detail.setUserId(source.getId());
        detail.setEmail(source.getEmail());
        detail.setPassword(source.getPassword());
        detail.setAccessRoles(accessRoleConverter.convertToTargetList(source.getAccessRoles()));

        return detail;
    }

    @Override
    public User convertToSource(UserDetail source) {
        throw new UnsupportedOperationException();
    }
}
