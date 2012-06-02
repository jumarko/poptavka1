/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.shared.domain.converter;

import com.eprovement.poptavka.domain.user.rights.AccessRole;
import com.eprovement.poptavka.domain.user.rights.Permission;
import com.eprovement.poptavka.shared.domain.adminModule.AccessRoleDetail;
import com.eprovement.poptavka.shared.domain.adminModule.PermissionDetail;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AccessRoleConverter extends AbstractConverter<AccessRole, AccessRoleDetail> {

    private final Converter<Permission, PermissionDetail> permissionConverter = new PermissionConverter();

    @Override
    public AccessRoleDetail convertToTarget(AccessRole source) {
        AccessRoleDetail detail = new AccessRoleDetail();

        detail.setId(source.getId());
        detail.setName(source.getName());
        detail.setDescription(source.getDescription());
        detail.setCode(source.getCode());
        List<PermissionDetail> permissions = new ArrayList<PermissionDetail>();
        for (Iterator<Permission> it = source.getPermissions().iterator(); it.hasNext();) {
            permissions.add(permissionConverter.convertToTarget(it.next()));
        }
        detail.setPermissions(permissions);
        return detail;
    }

    @Override
    public AccessRole converToSource(AccessRoleDetail source) {
        throw new UnsupportedOperationException();
    }
}
