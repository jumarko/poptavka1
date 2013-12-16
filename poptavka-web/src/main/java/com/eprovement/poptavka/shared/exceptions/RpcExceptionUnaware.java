/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.shared.exceptions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation for public methods in RPC service classes that are not considered to the regular
 * RPC service methods (i.e. business methods used on client).
 * Please, consider twice, before using this annotation. If you do not add  "throws RPCException" clause
 *  to the regular RPC method then correct exception handling on client is virtually impossible.
 *
 * @see RPCException
 * @see com.eprovement.poptavka.client.common.security.SecuredAsyncCallback
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD })
public @interface RpcExceptionUnaware {
}
