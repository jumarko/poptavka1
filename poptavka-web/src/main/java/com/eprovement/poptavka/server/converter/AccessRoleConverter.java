/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.user.rights.AccessRole;
import com.eprovement.poptavka.domain.user.rights.Permission;
import com.eprovement.poptavka.shared.domain.adminModule.AccessRoleDetail;
import com.eprovement.poptavka.shared.domain.adminModule.PermissionDetail;
import org.apache.commons.lang.Validate;

/**
 * Converts AccessRole to AccessRoleDetail.
 * @author Juraj Martinka
 */
public final class AccessRoleConverter extends AbstractConverter<AccessRole, AccessRoleDetail> {

    private final Converter<Permission, PermissionDetail> permissionConverter;

    /**
     * Creates AccessRoleConverter.
     * @param permissionConverter
     */
    private AccessRoleConverter(Converter<Permission, PermissionDetail> permissionConverter) {
        // Spring instantiates converters - see converters.xml
        Validate.notNull(permissionConverter);
        this.permissionConverter = permissionConverter;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public AccessRoleDetail convertToTarget(AccessRole source) {
        AccessRoleDetail detail = new AccessRoleDetail();

        detail.setId(source.getId());
        detail.setName(source.getName());
        detail.setDescription(source.getDescription());
        detail.setCode(source.getCode());
        if (source.getPermissions() != null) {
            detail.setPermissions(permissionConverter.convertToTargetList(source.getPermissions()));
        }
        return detail;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public AccessRole convertToSource(AccessRoleDetail source) {
        throw new UnsupportedOperationException();
    }
}
