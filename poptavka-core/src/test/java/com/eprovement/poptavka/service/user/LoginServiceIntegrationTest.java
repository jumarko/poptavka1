/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.service.user;

import com.eprovement.poptavka.application.security.Encryptor;
import com.eprovement.poptavka.base.integration.DBUnitIntegrationTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.exception.IncorrectPasswordException;
import com.eprovement.poptavka.exception.LoginUserNotExistException;
import com.eprovement.poptavka.service.GeneralService;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@DataSet(path = {
        "classpath:com/eprovement/poptavka/domain/address/LocalityDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/UsersDataSet.xml" },
        dtd = "classpath:test.dtd")
public class LoginServiceIntegrationTest extends DBUnitIntegrationTest {

    private static final String TEST_USER_EMAIL = "elvira@email.com";
    private static final String TEST_USER_PASSWORD = "ahoj1";
    @Autowired
    private LoginService loginService;
    @Autowired
    @Qualifier("passwordEncryptor")
    private Encryptor encryptor;
    @Autowired
    private GeneralService generalService;

    @Before
    public void hashPlainTextPasswordsInTestDatabase() {
        System.out.println("ahoj1:" + encryptor.encrypt("ahoj1"));
        System.out.println("ahoj2:" + encryptor.encrypt("ahoj2"));
        System.out.println("3ahoj:" + encryptor.encrypt("3ahoj"));
        System.out.println("4ahoj:" + encryptor.encrypt("4ahoj"));
    }



    @Test
    public void loginUser() {
        final User user = loginService.loginUser(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        assertNotNull(user);
        assertThat(user.getEmail(), is(TEST_USER_EMAIL));
        // since password should be hashed, we cannot get its plaintext form from User object
        assertFalse(user.getPassword().equals(TEST_USER_PASSWORD));
    }

    @Test(expected = LoginUserNotExistException.class)
    public void loginUserForNonexistentEmail() {
        loginService.loginUser("super-dummy-email-unknown@poptavka-fake.com", "SUPER_SECRET");
    }

    @Test(expected = IncorrectPasswordException.class)
    public void loginUserForIncorrectPassword() {
        loginService.loginUser("elvira@email.com", "ahoj11");
    }

    @Test(expected = IncorrectPasswordException.class)
    public void loginUserForIncorrectPasswordLength() {
        loginService.loginUser("elvira.plaintext.password@email.com", "ahoj1");
    }
}
