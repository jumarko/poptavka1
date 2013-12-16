/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.common.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The string has to be a well-formed email address. Martin: Can use build-in
 * email annotation because that one uses EmailValidator, which uses
 * java.util.rexep and those are not supported in javascript. Therefore, when
 * code is complied to javascript build-in annotaion is not working. Causion:
 * this is only for client side, server side cau use buil-in email annotation.
 *
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 * @author Martin Slavkovsky
 */
@Documented
@Constraint(validatedBy = com.eprovement.poptavka.client.common.validation.EmailValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
public @interface Email {

    String message() default "Invalid email input";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    /**
     * Defines several {@code @Email} annotations on the same element.
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    public @interface List {

        com.eprovement.poptavka.client.common.validation.Email[] value();
    }
}
