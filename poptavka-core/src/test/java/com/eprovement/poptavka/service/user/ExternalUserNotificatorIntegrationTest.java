package com.eprovement.poptavka.service.user;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.eprovement.poptavka.base.integration.DBUnitIntegrationTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.register.Registers;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.mail.MailServiceMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;

import java.util.List;

@DataSet(path = {
        "classpath:com/eprovement/poptavka/domain/address/LocalityDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/register/RegisterDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/UsersDataSet.xml" },
    dtd = "classpath:test.dtd")
public class ExternalUserNotificatorIntegrationTest extends DBUnitIntegrationTest {

    /** Hash of original externalUser's password (ahoj4) stored in DB. */
    private static final String USER_PASSWORD_HASH =
            "ktWVVhGwWiMsAVgfbIZmX96r5WXBwIzMcGzwUBVAXcFvtiJutGRbKOk58TfQ4Wr2";

    @Autowired
    private ExternalUserNotificator notificator;

    @Autowired
    private GeneralService generalService;

    @Autowired
    private MailServiceMock mailServiceMock;

    private BusinessUser externalUser;

    @Before
    public void setUp() throws Exception {
        mailServiceMock.cleanAll();

        // see UsersDataSet.xml
        externalUser = generalService.find(BusinessUser.class, 111111116L);
    }

    @Test
    public void notifyExternalUserForTheFirstTime() throws Exception {

        notificator.send(externalUser, Registers.Notification.EXTERNAL_SUPPLIER);

        assertTrue("user which is already verified should not be disabled", externalUser.isEnabled());

        assertThat("Password should be reset for user", externalUser.getPassword(), not(USER_PASSWORD_HASH));

        final List<SimpleMailMessage> notificationMails = mailServiceMock.getSentSimpleMailMessages();
        assertThat("Exactly one notification email should be sent!", notificationMails, hasSize(1));
        final String notificationMail = notificationMails.get(0).getText();

        assertThat("Generated password expected in mail body!",
                notificationMail, containsString("password: "));
        assertThat("Unsubscribe link expected in mail body!",
                notificationMail,
                containsString("https://devel.want-something.com/#unsubscribe?id=" + externalUser.getPassword()));
    }

    @Test
    public void alreadyNotifiedUserNotNotified() throws Exception {
        final BusinessUser userAlreadyNotified = generalService.find(BusinessUser.class, 111111117L);
        checkNotNotified(userAlreadyNotified);
    }

    @Test
    public void verifiedUserNotNotified() throws Exception {
        final BusinessUser verifiedUser = generalService.find(BusinessUser.class, 111111118L);
        checkNotNotified(verifiedUser);
    }

    @Test
    public void internalUserNotNotified() throws Exception {
        final BusinessUser internalUser = generalService.find(BusinessUser.class, 111111111L);
        checkNotNotified(internalUser);
    }


    //--------------------------------------------------- HELPER METHODS ---------------------------------------------

    private void checkNotNotified(BusinessUser user) {
        notificator.send(user, Registers.Notification.EXTERNAL_SUPPLIER);

        // check that user has not been disabled - this should be done via regular job
        assertTrue("user which is has already been notified should not be disabled", externalUser.isEnabled());

        assertThat("Password should NOT be reset for user who has already been notified",
                externalUser.getPassword(), is(USER_PASSWORD_HASH));

        assertThat("No notification emails should be sent!", mailServiceMock.getSentSimpleMailMessages(), empty());
    }


}
