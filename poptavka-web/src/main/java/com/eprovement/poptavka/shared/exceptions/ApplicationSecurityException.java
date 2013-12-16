/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.shared.exceptions;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Exception representing unauthorized access to the resource, e.g.:
 * <ol>
 *     <li>anonymous user</li>
 *     <li>user without required permissions</li>
 * </ol>
 *
 * @see com.eprovement.poptavka.application.security.RpcExceptionAspect
 */
public class ApplicationSecurityException extends RuntimeException implements IsSerializable {

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
