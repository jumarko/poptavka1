/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.service.user;

import com.eprovement.poptavka.base.integration.DBUnitBaseTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.exception.IncorrectActivationLinkException;
import com.eprovement.poptavka.service.GeneralService;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import static org.hamcrest.core.Is.is;
import org.junit.Assert;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


@DataSet(path = {
        "classpath:com/eprovement/poptavka/domain/address/LocalityDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/UsersDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/register/RegisterDataSet.xml" },
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


    @Test
    public void verifyActivationLink() {
        activationLink = decode(activationLink);
        final User activatedUser = userVerificationService.verifyActivationLink(activationLink);
        Assert.assertNotNull(activatedUser);
        assertThat(activatedUser.getId(), is(111111111L));
    }

    @Test(expected = IncorrectActivationLinkException.class)
    public void verifyActivationLinkForIncorrectLinkInPlaintext() {
        userVerificationService.verifyActivationLink(
                "user/activation?link={\"userEmail\":\"moj@supplier.cz\", \"validity\":100000000");
    }


    //--------------------------------------------------- HELPER METHODS -----------------------------------------------
    private String decode(String urlEncoded) {
        try {
            return URLDecoder.decode(urlEncoded, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Never should occur - unsupported encoding", e);
        }
    }

}
