/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.domain.enums;

import java.util.Arrays;

/**
 * List of string codes for common AccessRole-s.
 */
public final class CommonAccessRoles {

    private CommonAccessRoles() {
        // only wrapper for access role codes
    }

    public static final String ADMIN_ACCESS_ROLE_CODE = "ROLE_ADMIN";
    public static final String USER_ACCESS_ROLE_CODE = "ROLE_USER";
    public static final String CLIENT_ACCESS_ROLE_CODE = "ROLE_CLIENT";
    public static final String SUPPLIER_ACCESS_ROLE_CODE = "ROLE_SUPPLIER";

    @Override
    public String toString() {
        return "CommonAcessRoles:" + Arrays.toString(new String[] {
            USER_ACCESS_ROLE_CODE,
            CLIENT_ACCESS_ROLE_CODE,
            SUPPLIER_ACCESS_ROLE_CODE
        });
    }
}
