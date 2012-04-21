/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.exception;

public class ExpiredActivationLinkException extends RuntimeException {
    public ExpiredActivationLinkException(String message) {
        super(message);
    }

    public ExpiredActivationLinkException(String message, Throwable cause) {
        super(message, cause);
    }
}
