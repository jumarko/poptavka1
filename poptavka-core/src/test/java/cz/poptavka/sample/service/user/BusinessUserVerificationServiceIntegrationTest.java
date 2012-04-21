/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.service.user;

import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import cz.poptavka.sample.domain.user.BusinessUser;
import cz.poptavka.sample.domain.user.User;
import cz.poptavka.sample.exception.IncorrectActivationLinkException;
import cz.poptavka.sample.exception.UserNotExistException;
import cz.poptavka.sample.service.GeneralService;
import static org.hamcrest.core.Is.is;
import org.junit.Assert;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


@DataSet(path = {
        "classpath:cz/poptavka/sample/domain/user/UsersDataSet.xml",
        "classpath:cz/poptavka/sample/domain/register/RegisterDataSet.xml" },
    dtd = "classpath:test.dtd")
public class BusinessUserVerificationServiceIntegrationTest extends DBUnitBaseTest {

    @Autowired
    private BusinessUserVerificationService userVerificationService;
    @Autowired
    private GeneralService generalService;

    private String activationLink;

    @Before
    public void generateNewLink() {
        final BusinessUser userToBeActivated = generalService.find(BusinessUser.class, 111111111L);
        activationLink = userVerificationService.generateActivationLink(userToBeActivated);
    }

    @Test
    public void generateActivationLink() {
        Assert.assertFalse(activationLink.isEmpty());
        Assert.assertTrue(activationLink.startsWith("https://46.137.95.172:8443/poptavka/"));
    }

    @Test(expected = UserNotExistException.class)
    public void generateActivationLinkForNonexistentUser() {
        final BusinessUser user = new BusinessUser();
        user.setId(1234L);
        userVerificationService.generateActivationLink(user);
    }

    @Test
    public void verifyActivationLink() {
        final User activatedUser = userVerificationService.verifyActivationLink(activationLink);
        Assert.assertNotNull(activatedUser);
        assertThat(activatedUser.getId(), is(111111111L));
    }

    @Test(expected = IncorrectActivationLinkException.class)
    public void verifyActivationLinkForIncorrectLinkInPlaintext() {
        userVerificationService.verifyActivationLink("{\"userEmail\":\"moj@supplier.cz\", \"validity\":100000000");
    }

}
