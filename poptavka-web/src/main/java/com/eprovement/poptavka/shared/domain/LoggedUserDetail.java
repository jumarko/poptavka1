/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.shared.domain;

import com.eprovement.poptavka.domain.user.rights.AccessRole;
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
    /** Logged user must always has non-null id set! */
    private long userId;
    private String email;
    private ArrayList<AccessRole> accessRoles;


    /** Required for GWT */
    public LoggedUserDetail() { }

    public LoggedUserDetail(long userId, String email, List<AccessRole> accessRoles) {
        this.userId = userId;
        this.email = email;
        this.accessRoles = new ArrayList<AccessRole>(accessRoles);
    }

    public long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<AccessRole> getAccessRoles() {
        return accessRoles;
    }
}
