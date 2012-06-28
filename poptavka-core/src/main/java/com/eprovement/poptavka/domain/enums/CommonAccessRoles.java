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

    public static final String CLIENT_ACCESS_ROLE_CODE = "client";
    public static final String SUPPLIER_ACCESS_ROLE_CODE = "supplier";

    @Override
    public String toString() {
        return "CommonAcessRoles:" + Arrays.toString(new String[] {
            CLIENT_ACCESS_ROLE_CODE,
            SUPPLIER_ACCESS_ROLE_CODE
        });
    }
}
