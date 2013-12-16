/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.common;

import com.eprovement.poptavka.shared.domain.adminModule.AccessRoleDetail;

/**
 * Defines access roles for easier role usage and comparison.
 * @author Juraj Martinka
 */
public final class CommonAccessRoles {

    /**
     * Creates ComonAccressRoles.
     */
    private CommonAccessRoles() {
    }

    /**************************************************************************/
    /* Define access roles                                                    */
    /**************************************************************************/
    public static final AccessRoleDetail ADMIN = new AccessRoleDetail(
            com.eprovement.poptavka.domain.enums.CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE);
    public static final AccessRoleDetail CLIENT = new AccessRoleDetail(
            com.eprovement.poptavka.domain.enums.CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE);
    public static final AccessRoleDetail SUPPLIER = new AccessRoleDetail(
            com.eprovement.poptavka.domain.enums.CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE);
}
