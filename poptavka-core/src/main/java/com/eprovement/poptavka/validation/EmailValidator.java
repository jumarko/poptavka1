/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.validation;

import org.apache.commons.lang.StringUtils;

/**
 * Common validator for emails.
 * Should be used whenever possible (i.e. whole codebase except gwt client classes).
 */
public class EmailValidator {

    private static final EmailValidator INSTANCE = new EmailValidator();

    private final org.hibernate.validator.constraints.impl.EmailValidator emailValidator =
            new org.hibernate.validator.constraints.impl.EmailValidator();


    /**
     * Returns single instance of EmailValidator. This is used instead of spring injection since it is needed
     * in various places such as backend service and RPC service (in RPC service validator cannot be injected via
     * setter injection because it is needed for validation in another setter).
     *
     * @return single instance of {@link EmailValidator}
     */
    public static EmailValidator getInstance() {
        return INSTANCE;
    }

    /**
     * Checks if given {@code email} is a valid one.
     *
     * @param email email to be checked
     * @return true if email is valid, false otherwise
     */
    public boolean isValid(String email) {
        return StringUtils.isNotEmpty(email) && emailValidator.isValid(email, null);
    }
}
