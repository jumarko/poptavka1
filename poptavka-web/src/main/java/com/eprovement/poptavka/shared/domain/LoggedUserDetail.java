/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.shared.domain;

import com.eprovement.poptavka.shared.domain.adminModule.AccessRoleDetail;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Detail object encapsulating all information provided when user is logged on.
 *
 * @see com.eprovement.poptavka.client.service.demand.UserRPCService
 * @see com.eprovement.poptavka.service.user.LoginService
 */
public class LoggedUserDetail implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -120882367332196457L;
    /**
     * Logged user must always has non-null id set!
     */
    private long userId;
    private String email;
    private ArrayList<AccessRoleDetail> accessRoles;

    /**
     * Required for GWT
     */
    public LoggedUserDetail() {
        super();
    }

    public LoggedUserDetail(long userId, String email, List<AccessRoleDetail> accessRoles) {
        this.userId = userId;
        this.email = email;
        this.accessRoles = new ArrayList<AccessRoleDetail>(accessRoles);
    }

    public long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<AccessRoleDetail> getAccessRoles() {
        return accessRoles;
    }

    public void setAccessRoles(ArrayList<AccessRoleDetail> accessRoles) {
        this.accessRoles = accessRoles;
    }
}
