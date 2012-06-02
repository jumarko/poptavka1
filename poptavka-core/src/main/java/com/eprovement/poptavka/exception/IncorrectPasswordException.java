/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.exception;

/**
 * Represents more specific failure of login error.
 * This can be used for application purposes, but end user should never find out if it gives invalid email or password!
 */
public class IncorrectPasswordException extends LoginException {
    private static final String COMMON_MESSAGE = "Given password does not match!";

    public IncorrectPasswordException() {
        super(COMMON_MESSAGE);
    }

    public IncorrectPasswordException(String message) {
        super(COMMON_MESSAGE + message);
    }

    public IncorrectPasswordException(String message, Throwable cause) {
        super(COMMON_MESSAGE + message, cause);
    }
}
