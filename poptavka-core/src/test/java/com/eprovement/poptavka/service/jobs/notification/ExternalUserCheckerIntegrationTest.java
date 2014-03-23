package com.eprovement.poptavka.service.jobs.notification;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.eprovement.poptavka.base.integration.DBUnitIntegrationTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.UserNotification;
import com.eprovement.poptavka.service.GeneralService;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DataSet(path = {
        "classpath:com/eprovement/poptavka/domain/address/LocalityDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/register/RegisterDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/UsersDataSet.xml" },
    dtd = "classpath:test.dtd")
public class ExternalUserCheckerIntegrationTest extends DBUnitIntegrationTest {

    @Autowired
    private GeneralService generalService;

    @Autowired
    private ExternalUserChecker externalUserChecker;


    @Test
    public void testUserWithPendingNotificationDisabled() throws Exception {
        final BusinessUser userWithOldNotification = loadUser();
        assertTrue("Before execution of ExternalUserChecker user should be enabled",
                userWithOldNotification.isEnabled());

        externalUserChecker.execute();

        // typically, the disabled user should not be returned at all (null) but since the transaction has not been
        // committed yet, we just have to check the enabled field value
        final BusinessUser disabledUser = loadUser();
        assertFalse("User with pending notification should be disabled", disabledUser.isEnabled());
    }

    @Test
    public void testUserWithPendingNotificationYoungerThanMaxIsNotDisabled() throws Exception {
        final BusinessUser userWithNotification = loadUser();
        final List<UserNotification> userNotifications = generalService.findAll(UserNotification.class);
        for (UserNotification notification : userNotifications) {
            if (userWithNotification.equals(notification.getUser())) {
                notification.setSent(new DateTime().minusWeeks(1).toDate());
                generalService.save(notification);
            }
        }

        externalUserChecker.execute();

        // typically, the disabled user should not be returned at all (null) but since the transaction has not been
        // committed yet, we just have to check the enabled field value
        assertTrue("User with pending notification younger than 30 days should NOT be disabled",
                loadUser().isEnabled());
    }

    //--------------------------------------------------- HELPER METHODS -----------------------------------------------
    private BusinessUser loadUser() {
        final BusinessUser user = generalService.find(BusinessUser.class, 111111119L);
        assertNotNull("user shoult not be null", user);
        return user;
    }
}
