/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.application.security;

public interface Encryptor {
    /**
     * Encrypts given {@code plainText} and returned its encrypted form.
     *
     * @param plainText plain form of text which will be encrypted
     * @return encrypted {@code plainText} or null if input {@code plainText} is also null
     */
    String encrypt(String plainText);

    /**
     * Decrypts given encrypted text to the plain text.
     * This method assumes that {@code encryptedText} has been encrypted via this encryptor
     * {@link #encrypt(String)}  method, otherwise it is very improbable, that decryption
     * will be successful.
     *
     * @param encryptedText
     * @return plain text
     * @throws UnsupportedOperationException if encryptor does not support this operation,
     *        e.g. {@link PasswordEncryptor}
     */
    String decrypt(String encryptedText) throws UnsupportedOperationException;


    /**
     * Checks whether given {@code plainText} matches with given {@code digest} (hash).
     * <strong>This is optinal operation!</strong>.
     * @param plainText plain text form of some string
     * @param digest hashed form of the string
     * @return true if digest has been constructed by hashing {@code plainText} using this encryptor
     * @throws UnsupportedOperationException if encryptor does not support this operation
     */
    boolean matches(String plainText, String digest) throws UnsupportedOperationException;

}
