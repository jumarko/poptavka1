/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.application.security;

import com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclarePrecedence;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

/**
 * Aspect that translates all exceptions in RPC service methods to the serializable exceptions
 * that can be used on client side.
 * Spring security exceptions {@link org.springframework.security.core.AuthenticationException}
 * and {@link AccessDeniedException} are handled specially --> transformed
 * to the our custom {@link com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException}.
 *
 * <p>
 *     Security handling is inspired by
 *     <a href="http://blog.maxmatveev.com/2011/06/gwt-and-spring-security-integration-as.html">
 *         GWT and spring security integration</a>
 * </p>
 * @see ApplicationSecurityException
 * @see RPCException
 */
@Aspect
@DeclarePrecedence("RpcExceptionAspect, *")
public class RpcExceptionAspect {

    @Pointcut("execution(public * com.eprovement.poptavka..*RPCServiceImpl.*(..))")
    private void executionOfRpcMethod()  { }

    @Around("executionOfRpcMethod()")
    public Object translateSecurityException(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            return proceedingJoinPoint.proceed();
        } catch (AuthenticationException ae) {
            throw new ApplicationSecurityException("Unauthorized access to method="
                    + proceedingJoinPoint.getClass() + ":" + proceedingJoinPoint.getSignature(), ae);
        } catch (AccessDeniedException ade) {
            throw new ApplicationSecurityException("Access denied to method="
                    + proceedingJoinPoint.getClass() + ":" + proceedingJoinPoint.getSignature(), ade);
        } catch (Exception e) {
            throw new RPCException("Unknown error", e);
        }
    }
}
