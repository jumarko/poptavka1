/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.application.security;

import com.eprovement.poptavka.base.integration.BasicIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Tests for password encryptor.
 * Since encryptor settings are in spring xml configuration file, this test
 * must extends {@link BasicIntegrationTest}.
 */
public class PasswordEncryptorTest extends BasicIntegrationTest {

    @Autowired
    @Qualifier("passwordEncryptor")
    private Encryptor passwordEncryptor;

    @Test
    public void testEncrypt() throws Exception {
        final String password = "myPassword";
        final String encryptedPassword = this.passwordEncryptor.encrypt(password);
        Assert.assertNotNull(encryptedPassword);
        Assert.assertFalse(password.equals(encryptedPassword));
        Assert.assertTrue("Password hash length must be a multiple of 8!", encryptedPassword.length() % 8 == 0);
        System.out.println(encryptedPassword);
    }
}
