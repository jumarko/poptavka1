/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.service.user;

public class ActivationLink {

    private String userEmail;
    private long validity;

    private ActivationLink() {
        // ONLY FOR JACKSON deserialization mechanism
    }

    public ActivationLink(String userEmail, long validity) {
        this.userEmail = userEmail;
        this.validity = validity;
    }


    public long getValidity() {
        return validity;
    }

    public void setValidity(long validity) {
        this.validity = validity;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

}
