/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.service.user;

import com.eprovement.poptavka.base.integration.DBUnitIntegrationTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.exception.IncorrectActivationCodeException;
import com.eprovement.poptavka.service.GeneralService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


@DataSet(path = {
        "classpath:com/eprovement/poptavka/domain/address/LocalityDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/UsersDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/register/RegisterDataSet.xml" },
    dtd = "classpath:test.dtd")
public class BusinessUserVerificationServiceIntegrationTest extends DBUnitIntegrationTest {

    @Autowired
    private UserVerificationService userVerificationService;
    @Autowired
    private GeneralService generalService;

    private String activationCode;

    private BusinessUser user;

    @Before
    public void generateNewActivationCode() {
        final BusinessUser userToBeActivated = generalService.find(BusinessUser.class, 111111111L);
        activationCode = userVerificationService.generateActivationCode(userToBeActivated);
        this.user = userToBeActivated;
    }

    @Test
    public void generateActivationCode() {
        Assert.assertFalse(activationCode.isEmpty());
        Assert.assertTrue("Suspicious activation code of unexpected length. Expected 6 alphanumeric characters but "
                + " actual length=" + activationCode.length(), activationCode.length() == 6);
    }


    @Test
    public void verifyActivationCode() {
        userVerificationService.verifyActivationCode(user, activationCode);
        // no exception - everything ok
    }

    @Test(expected = IncorrectActivationCodeException.class)
    public void verifyActivationLinkForIncorrectLinkInPlaintext() {
        userVerificationService.verifyActivationCode(
                user, "abc");
    }


}
