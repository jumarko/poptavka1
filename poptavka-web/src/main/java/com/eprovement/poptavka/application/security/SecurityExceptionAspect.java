/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.application.security;

import com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.AuthenticationException;

/**
 * Aspect that translates spring security {@link org.springframework.security.core.AuthenticationException}
 * to the our custom {@link com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException} which
 * can be used on a client side.
 *
 * <p>
 *     Security handling is inspired by
 *     <a href="http://blog.maxmatveev.com/2011/06/gwt-and-spring-security-integration-as.html">
 *         GWT and spring security integration</a>
 * </p>
 * @see ApplicationSecurityException
 */
@Aspect
public class SecurityExceptionAspect {

    @Pointcut("execution(* *(..)) && @annotation(GwtMethod)")
    private void executionOfGwtMethod()  { }

    @Around("executionOfGwtMethod()")
    public Object translateSecurityException(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            return proceedingJoinPoint.proceed();
        } catch (AuthenticationException e) {
            throw new ApplicationSecurityException("Unauthorized access to method="
                    + proceedingJoinPoint.getClass() + ":" + proceedingJoinPoint.getSignature(), e);
        }
    }
}
