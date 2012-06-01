/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.shared.domain.converter;

import cz.poptavka.sample.domain.user.rights.Permission;
import cz.poptavka.sample.shared.domain.adminModule.PermissionDetail;

public class PermissionConverter extends AbstractConverter<Permission, PermissionDetail> {

    @Override
    public PermissionDetail convertToTarget(Permission source) {
        PermissionDetail detail = new PermissionDetail();

        detail.setId(source.getId());
        detail.setName(source.getName());
        detail.setDescription(source.getDescription());
        detail.setCode(source.getCode());

        return detail;
    }

    @Override
    public Permission converToSource(PermissionDetail source) {
        throw new UnsupportedOperationException();
    }
}
