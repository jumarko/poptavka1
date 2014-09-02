/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.shared.domain;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Detail object encapsulating all information provided when user is logged on.
 *
 * @see com.eprovement.poptavka.client.service.demand.UserRPCService
 * @see com.eprovement.poptavka.service.user.LoginService
 */
public class LesserBusinessUserDetail implements IsSerializable {

    /**
     * Logged user must always has non-null id set!
     */
    private long userId;
    private String displayName;
    private ArrayList<AddressDetail> addresses = new ArrayList<AddressDetail>();

    /**
     * Required for GWT
     */
    public LesserBusinessUserDetail() {
        //for serialization
    }


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public ArrayList<AddressDetail> getAddresses() {
        return addresses;
    }

    public void setAddresses(Collection<AddressDetail> addresses) {
        this.addresses = new ArrayList<AddressDetail>(addresses);
    }

}
