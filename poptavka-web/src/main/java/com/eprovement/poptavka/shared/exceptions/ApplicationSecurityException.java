/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.shared.exceptions;

import java.io.Serializable;

/**
 * Exception representing unauthorized access to the resource, e.g.:
 * <ol>
 *     <li>anonymous user</li>
 *     <li>user without required permissions</li>
 * </ol>
 *
 * @see com.eprovement.poptavka.application.security.SecurityExceptionAspect
 */
public class ApplicationSecurityException extends RuntimeException implements Serializable {

    public ApplicationSecurityException() {
        // for GWT serialization
    }

    public ApplicationSecurityException(String message) {
        super(message);
    }

    public ApplicationSecurityException(String message, Throwable cause) {
        super(message, cause);
    }
}
