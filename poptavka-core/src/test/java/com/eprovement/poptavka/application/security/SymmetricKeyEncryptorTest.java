/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.application.security;

import com.eprovement.poptavka.base.integration.BasicIntegrationTest;
import org.apache.commons.lang.StringUtils;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SymmetricKeyEncryptorTest extends BasicIntegrationTest {

    private static final String MESSAGE = "Super secret message to be encrypted.";

    @Autowired
    private SymmetricKeyEncryptor symmetricKeyEncryptor;

    @Test
    public void encrypt() {
        final String encryptedMessage = symmetricKeyEncryptor.encrypt(MESSAGE);
        assertTrue(StringUtils.isNotEmpty(encryptedMessage));
        assertFalse(encryptedMessage.equalsIgnoreCase(MESSAGE));
        assertFalse(encryptedMessage.length() == MESSAGE.length());
        System.out.println(encryptedMessage);
    }

    @Test
    public void decrypt() {
        final String encryptedMessage = symmetricKeyEncryptor.encrypt(MESSAGE);
        assertFalse(encryptedMessage.equalsIgnoreCase(MESSAGE));
        assertThat(symmetricKeyEncryptor.decrypt(encryptedMessage), is(MESSAGE));
    }
}
