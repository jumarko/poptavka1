/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.exception;

/**
 * Represents a problem with user login, most common is invalid username or password.
 */
public class LoginException extends RuntimeException {

    protected static final String COMMON_MESSAGE = "Login failed: ";

    public LoginException() {
        super(COMMON_MESSAGE);
    }

    public LoginException(String message) {
        super(COMMON_MESSAGE + message);
    }

    public LoginException(String message, Throwable cause) {
        super(COMMON_MESSAGE + message, cause);
    }
}
