/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.application.security;

import cz.poptavka.sample.exception.IncorrectActivationLinkException;
import org.apache.commons.lang.Validate;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.util.text.StrongTextEncryptor;
import org.jasypt.util.text.TextEncryptor;

public class SymmetricKeyEncryptor implements Encryptor {

    protected static final int MIN_PASSWORD_LENGTH = 8;
    private final TextEncryptor textEncryptor;

    public SymmetricKeyEncryptor(String encryptionPassword) {
        Validate.notEmpty("Password for encryption cannot be empty");
        Validate.isTrue(encryptionPassword.length() >= MIN_PASSWORD_LENGTH, "Password must have at least "
                + MIN_PASSWORD_LENGTH + " characters!");

        this.textEncryptor = createTextEncryptor(encryptionPassword);
    }


    @Override
    public String encrypt(String plainText) {
        return this.textEncryptor.encrypt(plainText);
    }

    @Override
    public String decrypt(String encryptedText) {
        try {
            return textEncryptor.decrypt(encryptedText);
        } catch (EncryptionOperationNotPossibleException eonpe) {
            throw new IncorrectActivationLinkException("Passed activation link is in illegal format. Probably, "
                    + "it has been encrypted incorrectly.");
        }

    }

    @Override
    public boolean matches(String plainText, String digest) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("");
    }


    //--------------------------------------------------- HELPER METHODS -----------------------------------------------
    private static TextEncryptor createTextEncryptor(String encryptionPassword) {
        final StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
        textEncryptor.setPassword(encryptionPassword);
        return textEncryptor;
    }

}
