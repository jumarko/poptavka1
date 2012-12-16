/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Provided activation link is broken. It is possible "
        + "that it was rewritten by newer activation link or it is just a fake.")
public class IncorrectActivationCodeException extends RuntimeException {
    public IncorrectActivationCodeException(String message) {
        super(message);
    }

    public IncorrectActivationCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
