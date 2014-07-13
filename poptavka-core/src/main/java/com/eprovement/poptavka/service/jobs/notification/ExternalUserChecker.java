package com.eprovement.poptavka.service.jobs.notification;

import static org.apache.commons.lang.Validate.notNull;
import static org.slf4j.LoggerFactory.getLogger;

import com.eprovement.poptavka.domain.common.DomainObject;
import com.eprovement.poptavka.domain.enums.Verification;
import com.eprovement.poptavka.domain.settings.NotificationItem;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.BusinessUserRole;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.domain.user.SupplierCategory;
import com.eprovement.poptavka.domain.user.SupplierLocality;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.domain.user.UserNotification;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.jobs.base.Job;
import com.googlecode.genericdao.search.Search;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Job that regularly checks the users imported from external systems and disables those that were inactive
 * for {@link #MAX_PENDING_NOTIFICATION_DAYS} days.
 */
public class ExternalUserChecker implements Job {

    private static final Logger LOGGER = getLogger(ExternalUserChecker.class);

    /**
     * Max number of days for which we wait for external user to log in our system.
     * After this period, the user is automatically disabled if new notification is sent to him.
     */
    public static final int MAX_PENDING_NOTIFICATION_DAYS = 30;
    private final GeneralService generalService;


    public ExternalUserChecker(GeneralService generalService) {
        notNull(generalService, "generalService cannot be null!");
        this.generalService = generalService;
    }

    /**
     * <p>Day-to-day job that disables all external users where following conditions are met.</p>
     *
     * <p>
     * <code>User</code> already received a one-time <code>UserNotification</code> several days ago, see
     * {@link com.eprovement.poptavka.service.jobs.notification.ExternalUserChecker#MAX_PENDING_NOTIFICATION_DAYS}
     * and haven't logged in to the system with provided credentials until now. Since <code>User</code> hasn't logged in
     * he is still unverified, see {@link com.eprovement.poptavka.domain.user.User#verification}.
     * </p>
     * <p>
     * We suppose that this external <code>User</code> is not interested in our service thus we disable corresponding
     * <code>BusinessUser</code> and all its <code>BusinessUserRole</code>-s. The external <code>User</code> will not
     * receive any other notifications, see {@link com.eprovement.poptavka.service.user.ExternalUserNotificator#send(
     * BusinessUser, Registers.Notification, Map)}, unless we enable the user again e.g. after one year since last
     * notification.
     * </p>
     */
    @Override
    @Scheduled(cron = EVERY_DAY)
    @Transactional
    public void execute() {
        final Search notificationsSearch = new Search(UserNotification.class);
        final DateTime maxDaysAgo = new DateTime().minusDays(MAX_PENDING_NOTIFICATION_DAYS);
        notificationsSearch.addFilterLessThan("sent", maxDaysAgo.toDate());
        notificationsSearch.addFilterNotEqual("user.verification", Verification.VERIFIED);
        final List<UserNotification> pendingNotifications = generalService.search(notificationsSearch);

        for (UserNotification userNotification : pendingNotifications) {
            final User user = userNotification.getUser();
            if (user == null) {
                LOGGER.warn("action=external_user_check status=skip no user set in user notification={}",
                        userNotification);
                continue;
            }
            // Disable corresponding BusinessUser and all its BusinessUserRoles.
            // Disabling of associated entities must be done in following order below.
            BusinessUser businessUser = (BusinessUser) generalService.find(BusinessUser.class, user.getId());
            for (BusinessUserRole businessUserRole : businessUser.getBusinessUserRoles()) {
                if (businessUserRole instanceof Client) {
                    disable((Client) businessUserRole);
                }
                if (businessUserRole instanceof Supplier) {
                    Supplier supplierToDisable = (Supplier) businessUserRole;
                    // Disable SupplierCategory-ies.
                    Search searchCategories = new Search(SupplierCategory.class);
                    searchCategories.addFilterEqual("supplier", supplierToDisable);
                    List<SupplierCategory> supplierCategories = generalService.search(searchCategories);
                    for (SupplierCategory supplierCategory : supplierCategories) {
                        disable(supplierCategory);
                    }
                    // Disable SupplierLocality-ies.
                    Search searchLocalities = new Search(SupplierLocality.class);
                    searchLocalities.addFilterEqual("supplier", supplierToDisable);
                    List<SupplierLocality> supplierLocalities = generalService.search(searchLocalities);
                    for (SupplierLocality supplierLocality : supplierLocalities) {
                        disable(supplierLocality);
                    }
                    // Disable Supplier.
                    disable(supplierToDisable);
                }
            }
            disable(businessUser.getBusinessUserData());
            // For external User-s there is no activation email when User is imported into our system.
            if (businessUser.getActivationEmail() != null) {
                disable(businessUser.getActivationEmail());
            }
            // Disable NotificationItem-s.
            for (NotificationItem notificationItem : businessUser.getSettings().getNotificationItems()) {
                disable(notificationItem);
            }
            // Disable User's settings.
            disable(businessUser.getSettings());
            disable(businessUser);
            // TODO RELEASE ivlcek - is it necessary to disable UserNotification?
            // Maybe it woule be better not to disable this item so that we know that we have already sent the one-time
            // notification to external user.
            disable(userNotification);
            // TODO LATER ivlcek - later consider disabling of associated UserService.
            LOGGER.debug("action=external_user_check status=user={} has been disabled",
                    userNotification);
        }
    }

    @Override
    public String description() {
        return "ExternalUserChecker is responsible for checking ";
    }

    //--------------------------------------------------- HELPER METHODS -----------------------------------------------

    private void disable(DomainObject domainObject) {
        domainObject.setEnabled(false);
        generalService.save(domainObject);
    }

}
