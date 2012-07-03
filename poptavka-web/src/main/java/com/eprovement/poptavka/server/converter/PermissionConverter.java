/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.user.rights.Permission;
import com.eprovement.poptavka.shared.domain.adminModule.PermissionDetail;

public final class PermissionConverter extends AbstractConverter<Permission, PermissionDetail> {

    private PermissionConverter() {
        // Spring instantiates converters - see converters.xml
    }

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
