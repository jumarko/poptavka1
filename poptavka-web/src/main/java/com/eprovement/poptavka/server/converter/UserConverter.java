/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.domain.user.rights.AccessRole;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.eprovement.poptavka.shared.domain.adminModule.AccessRoleDetail;
import org.apache.commons.lang.Validate;

/**
 * Converts to User to UserDetail
 * @author Juraj Martinka
 */
public final class UserConverter extends AbstractConverter<User, UserDetail> {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private final Converter<AccessRole, AccessRoleDetail> accessRoleConverter;

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates UserConverter.
     */
    private UserConverter(
            Converter<AccessRole, AccessRoleDetail> accessRoleConverter) {
        // Spring instantiates converters - see converters.xml
        Validate.notNull(accessRoleConverter);
        this.accessRoleConverter = accessRoleConverter;
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
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

    /**
     * @{inheritDoc}
     */
    @Override
    public User convertToSource(UserDetail source) {
        throw new UnsupportedOperationException();
    }
}
