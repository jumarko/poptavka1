/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.shared.domain;

import com.eprovement.poptavka.client.common.validation.Email;
import com.eprovement.poptavka.shared.domain.adminModule.AccessRoleDetail;
import java.io.Serializable;
import java.util.ArrayList;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Detail object encapsulating all information provided when user is logged on.
 *
 * @see com.eprovement.poptavka.client.service.demand.UserRPCService
 * @see com.eprovement.poptavka.service.user.LoginService
 */
public class UserDetail implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -120882367332196457L;
    /**
     * Logged user must always has non-null id set!
     */
    private long userId;
    @NotBlank(message = "{supplierNotBlankEmail}")
    @Email(message = "{supplierEmail}")
    private String email;
    //TODO RELEASE - can be password here insead of BusinessUserDetail?
    @NotBlank(message = "{supplierNotBlankPassword}")
    private String password;
    private ArrayList<AccessRoleDetail> accessRoles;

    /**
     * Required for GWT
     */
    public UserDetail() {
        //for serialization
    }

    public UserDetail(long userId, String email, ArrayList<AccessRoleDetail> accessRoles) {
        this.userId = userId;
        this.email = email;
        this.accessRoles = accessRoles;
    }


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<AccessRoleDetail> getAccessRoles() {
        return accessRoles;
    }

    public void setAccessRoles(ArrayList<AccessRoleDetail> accessRoles) {
        this.accessRoles = accessRoles;
    }
}
