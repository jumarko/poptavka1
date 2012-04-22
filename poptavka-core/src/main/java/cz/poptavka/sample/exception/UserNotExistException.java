/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Represents more specific failure of login error.
 * This can be used for application purposes, but end user should never find out if it gives invalid email or password!
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Specified user has not been found.")
public class UserNotExistException extends RuntimeException {


    public UserNotExistException(String message) {
        super(message);
    }

    public UserNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
