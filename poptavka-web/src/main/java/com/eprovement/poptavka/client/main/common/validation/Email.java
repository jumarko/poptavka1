/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.main.common.validation;

//$Id: Email.java 19327 2010-04-30 08:11:15Z hardy.ferentschik $
/*
 * JBoss, Home of Professional Open Source Copyright 2009, Red Hat, Inc. and/or
 * its affiliates, and individual contributors by the @authors tag. See the
 * copyright.txt in the distribution for a full listing of individual
 * contributors.
 * 
* Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
 * applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 *
 */
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
@Constraint(validatedBy = com.eprovement.poptavka.client.main.common.validation.EmailValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
public @interface Email {

    String message() default "Invalid email input";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Defines several {@code @Email} annotations on the same element.
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RUNTIME)
    @Documented
    public @interface List {

        com.eprovement.poptavka.client.main.common.validation.Email[] value();
    }
}
