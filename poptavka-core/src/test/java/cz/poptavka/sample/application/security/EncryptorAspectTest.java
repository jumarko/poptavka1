/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.application.security;

import cz.poptavka.sample.base.integration.BasicIntegrationTest;
import static org.hamcrest.core.Is.is;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class EncryptorAspectTest extends BasicIntegrationTest {

    @Autowired
    @Qualifier("passwordEncryptor")
    private Encryptor encryptor;


    @Test
    public void testEncryptUserPasswordApplied() throws Exception {
        final EncryptedPassword encryptedPassword = new EncryptedPassword();
        final String plainText = "myPassword";
        encryptedPassword.setPassword(plainText);
        // checks that password has been encrypted
        Assert.assertFalse("Only a very very bad digest function can mapped password to itself!",
                plainText.equals(encryptedPassword.getPassword()));
        Assert.assertTrue(this.encryptor.matches(plainText, encryptedPassword.getPassword()));
    }


    /**
     * Test whether {@link cz.poptavka.sample.application.security.aspects.EncryptorAspect} has NOT been applied
     * to the method that is NOT annotated with {@link cz.poptavka.sample.application.security.aspects.Encrypted}.
     * @throws Exception
     */
    @Test
    public void testEncryptUserPasswordNotApplied() throws Exception {
        final PlaintextPassword plaintextPassword = new PlaintextPassword();
        final String plainText = "myPassword";
        plaintextPassword.setPassword(plainText);
        // checks that password has been encrypted
        Assert.assertThat("Plaintext password should not be encrypted, it should be the same as original value",
                plaintextPassword.getPassword(), is(plainText));
    }

}
