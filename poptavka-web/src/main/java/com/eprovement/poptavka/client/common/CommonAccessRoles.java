/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.common;

import com.eprovement.poptavka.shared.domain.adminModule.AccessRoleDetail;

/**
 *
 * @author Martin
 */
public final class CommonAccessRoles {

    private CommonAccessRoles() {
    }

    public static final AccessRoleDetail ADMIN = new AccessRoleDetail(
            com.eprovement.poptavka.domain.enums.CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE);
    public static final AccessRoleDetail CLIENT = new AccessRoleDetail(
            com.eprovement.poptavka.domain.enums.CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE);
    public static final AccessRoleDetail SUPPLIER = new AccessRoleDetail(
            com.eprovement.poptavka.domain.enums.CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE);
}
