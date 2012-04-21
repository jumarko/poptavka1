/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.exception;

public class IncorrectActivationLinkException extends RuntimeException {
    public IncorrectActivationLinkException(String message) {
        super(message);
    }

    public IncorrectActivationLinkException(String message, Throwable cause) {
        super(message, cause);
    }
}
