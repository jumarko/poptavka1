/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.application.security;

import cz.poptavka.sample.application.security.aspects.Encrypted;

class EncryptedPassword {

    private String password;

    public String getPassword() {
        return password;
    }

    @Encrypted
    public void setPassword(String password) {
        this.password = password;
    }
}
