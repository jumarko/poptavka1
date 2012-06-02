package com.eprovement.poptavka.application.security;

import org.jasypt.digest.StringDigester;

/**
 * Responsible for hashing passwords using one-way hashing function (more details on algorithm and other settings in
 * spring configuration file).
 * A typical usage of this class is as an aspect applied to the setter of field which has to be encrypted.
 * <p>
 *     Please, DO NOT ADD method for decryption, for securing user password, this is not recommended!
 * </p>
 * @see <a href="http://stackoverflow.com/questions/1075128/
 * password-encryption-with-spring-hibernate-jasypt-or-something-else">
 * Password encryption with Spring/Hibernate - Jasypt or something else?
 *     </a>
 */
public class PasswordEncryptor implements Encryptor {

    private StringDigester stringDigester;

    public String encrypt(String passwordPlainText) {
        return stringDigester.digest(passwordPlainText);
    }

    public boolean matches(String plainText, String digest) {
        return stringDigester.matches(plainText, digest);
    }

    public void setStringDigester(StringDigester stringDigester) {
        this.stringDigester = stringDigester;
    }

    @Override
    public String decrypt(String encryptedText) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Password encryptor MUST be a one-way only."
                + " No decryption of users' password will ever be supported!");
    }
}
