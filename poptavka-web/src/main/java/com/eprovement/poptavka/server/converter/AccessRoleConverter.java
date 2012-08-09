/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.user.rights.AccessRole;
import com.eprovement.poptavka.domain.user.rights.Permission;
import com.eprovement.poptavka.shared.domain.adminModule.AccessRoleDetail;
import com.eprovement.poptavka.shared.domain.adminModule.PermissionDetail;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.lang.Validate;

public final class AccessRoleConverter extends AbstractConverter<AccessRole, AccessRoleDetail> {

    private final Converter<Permission, PermissionDetail> permissionConverter;

    private AccessRoleConverter(Converter<Permission, PermissionDetail> permissionConverter) {
        // Spring instantiates converters - see converters.xml
        Validate.notNull(permissionConverter);
        this.permissionConverter = permissionConverter;
    }

    @Override
    public AccessRoleDetail convertToTarget(AccessRole source) {
        AccessRoleDetail detail = new AccessRoleDetail();

        detail.setId(source.getId());
        detail.setName(source.getName());
        detail.setDescription(source.getDescription());
        detail.setCode(source.getCode());
        ArrayList<PermissionDetail> permissions = new ArrayList<PermissionDetail>();
        for (Iterator<Permission> it = source.getPermissions().iterator(); it.hasNext();) {
            permissions.add(permissionConverter.convertToTarget(it.next()));
        }
        detail.setPermissions(permissions);
        return detail;
    }

    @Override
    public AccessRole convertToSource(AccessRoleDetail source) {
        throw new UnsupportedOperationException();
    }
}
