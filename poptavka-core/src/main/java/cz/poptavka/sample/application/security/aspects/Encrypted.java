/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.application.security.aspects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used for marking properties that should be encrypted.
 * Useful e.g. for password encryption.
 * This annotation is processed by aspect and appropriate encryption is then automatically applied to the given property
 * when its value is being set.
 * <p>
 *     Example:
 *     Consider a task of hashing all user Passwords. Password is stored in User#password field.
 *     Field has an setter {@code setPassword}.
 *     <ol>
 *         <li>Mark password field with {@link Encrypted}  annotation - default setting {@code oneWay = true} is fine
 *             for password hashing (we do not want to be possible - for anyone - to decrypt the password</li>
 *         <li>when {@code User#setPassword} is called then input argument is automatically encrypted and this
 *             encrypted value is store to the password field afterwards.</li>
 *     </ol>
 * </p>
 *
 * @see {@link cz.poptavka.sample.domain.user.User#setPassword(String)}
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface Encrypted {

    /**
     * Indicates whether one-way hash function should be used for encrypting or (if not) encryptor with master password
     * which means that anyone with knowledge of master password is able to decrypt property value and see its plain
     * text value.
     * Password should always be encrypted with {@code oneWay = true}
     */
    boolean oneWay() default true;
}
