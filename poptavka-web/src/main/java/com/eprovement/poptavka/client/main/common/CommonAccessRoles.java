/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.main.common;

import com.eprovement.poptavka.shared.domain.adminModule.AccessRoleDetail;

/**
 *
 * @author Martin
 */
public final class CommonAccessRoles {

    private CommonAccessRoles() {
    }

    public static final AccessRoleDetail ADMIN = new AccessRoleDetail("admin");
    public static final AccessRoleDetail CLIENT = new AccessRoleDetail("client");
    public static final AccessRoleDetail SUPPLIER = new AccessRoleDetail("supplier");
}
