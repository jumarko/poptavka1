package com.eprovement.poptavka.service.jobs.notification;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.eprovement.poptavka.base.integration.DBUnitIntegrationTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.enums.Verification;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.BusinessUserRole;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.domain.user.UserNotification;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.demand.PotentialDemandService;
import java.io.Serializable;
import java.util.Collection;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;
import org.junit.Assert;

@DataSet(path = {
        "classpath:com/eprovement/poptavka/domain/address/LocalityDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/CategoryDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/register/RegisterDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/ExternalUsersDataSet.xml" },
    dtd = "classpath:test.dtd")
public class ExternalUserCheckerIntegrationTest extends DBUnitIntegrationTest {

    @Autowired
    private GeneralService generalService;

    @Autowired
    private ExternalUserChecker externalUserChecker;

    @Autowired
    private PotentialDemandService potentialDemandService;


    /**
     * <p>Test several scenarios for external users and their one-time notifications.</p>
     * <p>
     * Checks <code>UserNotification</code>-s before <code>ExternalUserChecker</code> job.
     * Checks <code>UserNotification</code>-s after <code>ExternalUserChecker</code> job has been executed, see
     * {@link com.eprovement.poptavka.service.jobs.notification.ExternalUserChecker#execute() }.
     * Checks <code>UserNotification</code>-s after new potential demands was sent to external suppliers, see
     * {@link com.eprovement.poptavka.service.demand.PotentialDemandService#sendDemandToPotentialSuppliers(Demand) }.
     * </p>
     * @throws Exception
     */
    @Test
    public void testUserWithPendingNotificationDisabled() throws Exception {
        // Unverified user is user that received notification some time ago and hasn't logged in until now.
        final BusinessUser unverifiedUserWithOldNotification = loadUser(111111119L);
        assertTrue("Before execution of ExternalUserChecker user should be enabled",
                unverifiedUserWithOldNotification.isEnabled());

        // Verified user is user that received notification some time ago and logged in the system at least once.
        final BusinessUser verifiedUserWithOldNotification = loadUser(111111199L);
        assertTrue("Before execution of ExternalUserChecker user should be enabled",
                verifiedUserWithOldNotification.isEnabled());

        // Current status of UserNotifications1 loaded from ExternalUsersDataSet.xml.
        List<UserNotification> userNotifications1 = generalService.findAll(UserNotification.class);
        Assert.assertEquals(2, userNotifications1.size());
        checkUserNotificationExists(111111119L, userNotifications1);
        checkUserNotificationExists(111111199L, userNotifications1);

        // Simulate that BusinessUser has been verified. User is becoming verified as soon as he logs in with provided
        // credentials sent by ExternalUserNotificator.
        verifiedUserWithOldNotification.setVerification(Verification.VERIFIED);

        externalUserChecker.execute();

        // typically, the disabled user should not be returned at all (null) but since the transaction has not been
        // committed yet, we just have to check the enabled field value
        final BusinessUser disabledUnverifiedUser = loadUser(111111119L);
        assertFalse("Unverified user with pending notification should be disabled", disabledUnverifiedUser.isEnabled());
        final BusinessUser enabledVerifiedUser = loadUser(111111199L);
        assertTrue("Verified user with pending notification should be enabled", enabledVerifiedUser.isEnabled());

        checkBusinessUserRolesAreDisabled(disabledUnverifiedUser);
        checkBusinessUserRolesAreEnabled(enabledVerifiedUser);

        // Current status of UserNotifications2 after one UserNotification was disabled by externalUserChecker.
        List<UserNotification> userNotifications2 = generalService.findAll(UserNotification.class);
        Assert.assertEquals(1, userNotifications2.size());
        checkUserNotificationExists(111111199L, userNotifications2);

        // Potential demand mathing both external suppliers above, however no-one from external suppliers above
        // should receive it since one has been disabled and the other has been verified.
        final Demand demand = generalService.find(Demand.class, 99L);
        potentialDemandService.sendDemandToPotentialSuppliers(demand);

        // Current status of UserNotifications3 after new potential demand was sent to all matching suppliers and after
        // ExternalUserNotificator sent notifications to proper suppliers.
        List<UserNotification> userNotifications3 = generalService.findAll(UserNotification.class);
        Assert.assertEquals(1, userNotifications3.size());
        checkUserNotificationExists(111111199L, userNotifications3);
    }

    @Test
    public void testUserWithPendingNotificationYoungerThanMaxIsNotDisabled() throws Exception {
        final BusinessUser userWithNotification = loadUser(111111119L);
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
                loadUser(111111119L).isEnabled());
    }

    //--------------------------------------------------- HELPER METHODS -----------------------------------------------
    private BusinessUser loadUser(Serializable id) {
        final BusinessUser user = generalService.find(BusinessUser.class, id);
        assertNotNull("user shoult not be null", user);
        return user;
    }

    /**
     * Checks if <code>UserNotification</code> with given id <code>userNotificationId</code> exists in collection
     * <code>allUserNotifications</code>.
     *
     * @param userNotificationId the existence of which to be tested
     * @param allUserNotifications collection with all <code>UserNotification</code>-s
     */
    private void checkUserNotificationExists(final Long userNotificationId,
            Collection<UserNotification> allUserNotifications) {
        Assert.assertTrue(
                "UserNotification [id=" + userNotificationId + "] expected to be in"
                + " collection [" + allUserNotifications + "] is not there.",
                CollectionUtils.exists(allUserNotifications, new Predicate() {
                    @Override
                    public boolean evaluate(Object object) {
                        return userNotificationId.equals(((UserNotification) object).getId());
                    }
                }));
    }

    /**
     * Check whether all <code>BusinessUserRole</code>-s for corresponding disabled <code>BusinessUser</code>
     * are also disabled.
     *
     * @param disabledUnverifiedUser disabled and unverified <code>BusinessUser</code>
     */
    private void checkBusinessUserRolesAreDisabled(BusinessUser disabledUnverifiedUser) {
        for (BusinessUserRole bur : disabledUnverifiedUser.getBusinessUserRoles()) {
            if (bur instanceof Supplier) {
                assertFalse("Supplier's BusinessUserRole BUR.id=" + bur.getId()
                        + " of unverified BusinessUser, busnessUser.id=" + bur.getBusinessUser().getId()
                        + " should be disabled", ((Supplier) bur).isEnabled());
            }
            if (bur instanceof Client) {
                assertFalse("Client's BusinessUserRole, BUR.id=" + bur.getId()
                        + " of unverified BusinessUser, busnessUser.id=" + bur.getBusinessUser().getId()
                        + " should be disabled", ((Client) bur).isEnabled());
            }
        }
    }

    /**
     * Check whether all <code>BusinessUserRole</code>-s for corresponding enabled and verified
     * <code>BusinessUser</code> are also enabled.
     *
     * @param enabledVerifiedUser enabled and verified <code>BusinessUser</code>
     */
    private void checkBusinessUserRolesAreEnabled(BusinessUser enabledVerifiedUser) {
        for (BusinessUserRole bur : enabledVerifiedUser.getBusinessUserRoles()) {
            if (bur instanceof Supplier) {
                assertTrue("Supplier's BusinessUserRole BUR.id=" + bur.getId()
                        + " of enabled and verified BusinessUser, busnessUser.id=" + bur.getBusinessUser().getId()
                        + " should be enabled", ((Supplier) bur).isEnabled());
            }
            if (bur instanceof Client) {
                assertTrue("Client's BusinessUserRole, BUR.id=" + bur.getId()
                        + " of enabled and verified BusinessUser, busnessUser.id=" + bur.getBusinessUser().getId()
                        + " should be enabled", ((Client) bur).isEnabled());
            }
        }
    }
}
