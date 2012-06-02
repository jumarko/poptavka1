/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.application.security.aspects;

import com.eprovement.poptavka.application.security.Encryptor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * An aspect that applies encryption to all properties marked with annotation {@link Encrypted}.
 * <p>
 *     This implementation uses setter injection instead of constructor injection due the limitation of AspectJ
 *     load time weaving. when using CGLIB. See <a href="http://stackoverflow.com/questions/4490022/
 *     error-creating-bean-with-name-org-springframework-web-servlet-mvc-annotation-de">Error creating bean</a>.
 * </p>
 */
@Aspect
public class EncryptorAspect {

    private Encryptor encryptor;

    @Pointcut(" execution (@com.eprovement.poptavka.application.security.aspects.Encrypted * *(..))")
    private void encryptedProperties() { }

    @Around("encryptedProperties()")
    public void encryptProperty(ProceedingJoinPoint joinPoint) throws Throwable {
        if (encryptor == null) {
            throw new IllegalStateException("Encryptor must be defined, cannot encrypt any property wihout it.");
        }

        Object[] args = joinPoint.getArgs();
        final Signature signature = joinPoint.getSignature();
        if (signature instanceof MethodSignature) {
            boolean encryptParameters = false;
            // check if method has @Encrypted annotation
            final Encrypted encryptedAnnotation =
                    ((MethodSignature) signature).getMethod().getAnnotation(Encrypted.class);
            if (encryptedAnnotation == null) {
                // this should not happen - aspect applies only to the methods that ARE ANNOTATED with @Encrypted
                throw new IllegalStateException("Unexpected behavior of EncryptorAspect - it should not have been "
                        + "applied to the method that is not annoted with @Encrypted annotation.");
            }
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof String) {
                    args[i] = this.encryptor.encrypt((String) args[i]);
                }
            }
        }

        joinPoint.proceed(args);
    }

    public void setEncryptor(Encryptor encryptor) {
        this.encryptor = encryptor;
    }
}
