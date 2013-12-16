/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.user.rights.Permission;
import com.eprovement.poptavka.shared.domain.adminModule.PermissionDetail;

/**
 * Converts Permission to PermissionDetail.
 * @author Juraj Martinka
 */
public final class PermissionConverter extends AbstractConverter<Permission, PermissionDetail> {

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates PermissionConverter.
     */
    private PermissionConverter() {
        // Spring instantiates converters - see converters.xml
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public PermissionDetail convertToTarget(Permission source) {
        PermissionDetail detail = new PermissionDetail();

        detail.setId(source.getId());
        detail.setName(source.getName());
        detail.setDescription(source.getDescription());
        detail.setCode(source.getCode());

        return detail;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Permission convertToSource(PermissionDetail source) {
        throw new UnsupportedOperationException();
    }
}
