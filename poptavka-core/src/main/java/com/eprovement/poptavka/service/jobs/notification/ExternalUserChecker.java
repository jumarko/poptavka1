package com.eprovement.poptavka.service.jobs.notification;

import static org.apache.commons.lang.Validate.notNull;
import static org.slf4j.LoggerFactory.getLogger;

import com.eprovement.poptavka.domain.common.DomainObject;
import com.eprovement.poptavka.domain.enums.Verification;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.BusinessUserRole;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Supplier;
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

        for (UserNotification notification : pendingNotifications) {
            final User user = notification.getUser();
            if (user == null) {
                LOGGER.warn("action=external_user_check status=skip no user set in user notification={}", notification);
                continue;
            }

            // Disable corresponding BusinessUser and all its BusinessUserRoles.
            BusinessUser businessUser = (BusinessUser) generalService.find(BusinessUser.class, user.getId());
            for (BusinessUserRole businessUserRole : businessUser.getBusinessUserRoles()) {
                if (businessUserRole instanceof Client) {
                    disable((Client) businessUserRole);
                }
                if (businessUserRole instanceof Supplier) {
                    disable((Supplier) businessUserRole);
                }
            }

            disable(businessUser);
            disable(notification);
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
