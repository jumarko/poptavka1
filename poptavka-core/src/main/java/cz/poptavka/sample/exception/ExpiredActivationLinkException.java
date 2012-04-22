/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Validity of provided activation link expired."
        + " Send a request for generation new activation link.")
public class ExpiredActivationLinkException extends RuntimeException {
    public ExpiredActivationLinkException(String message) {
        super(message);
    }

    public ExpiredActivationLinkException(String message, Throwable cause) {
        super(message, cause);
    }
}
