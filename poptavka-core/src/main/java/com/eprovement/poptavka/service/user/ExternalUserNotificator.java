package com.eprovement.poptavka.service.user;

import static org.apache.commons.collections.CollectionUtils.isEmpty;
import static org.apache.commons.lang.Validate.notNull;
import static org.slf4j.LoggerFactory.getLogger;

import com.eprovement.poptavka.domain.register.Registers;
import com.eprovement.poptavka.domain.settings.Notification;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.UserNotification;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.notification.MailNotificationSender;
import com.eprovement.poptavka.service.register.RegisterService;
import com.google.common.collect.ImmutableMap;
import com.googlecode.genericdao.search.Search;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Responsible for sending one-time notifications for users imported from external systems.
 * @see #send(BusinessUser, Registers.Notification) for more details.
 */
public class ExternalUserNotificator {

    static final String UNSUBSCRIBE_LINK_PARAM = "unsubscribe.link";
    static final String PASSWORD_PARAM = "password";

    private static final Logger LOGGER = getLogger(ExternalUserNotificator.class);

    private final MailNotificationSender mailNotificationSender;
    private final GeneralService generalService;
    private final RegisterService registerService;
    private final UserVerificationService userVerificationService;


    public ExternalUserNotificator(GeneralService generalService,
                                   RegisterService registerService,
                                   UserVerificationService userVerificationService,
                                   MailNotificationSender mailNotificationSender) {
        notNull(generalService, "generalService cannot be null!");
        notNull(registerService, "registerService cannot be null!");
        notNull(userVerificationService, "userVerificationService cannot be null!");
        notNull(mailNotificationSender, "mailNotificationSender cannot be null!");
        this.generalService = generalService;
        this.registerService = registerService;
        this.userVerificationService = userVerificationService;
        this.mailNotificationSender = mailNotificationSender;
    }

    /**
     * Sends {@code notification} to the given {@code user} if following conditions are satisfied:
     * <ol>
     *      <li>User comes from external system</li>
     *      <li>User has NOT been verified yet (once user is verified he's considered to be an ordinary user)</li>
     *      <li>No previous notifications were sent</li>
     * </ol>
     *
     * Note:
     *     If external unverified user logs in to our system, then he's verified and no more notifications will be sent.
     *
     * @param user user to be notified if conditions are satisfied
     * @param notification notification which determines the message being sent to the user
     */
    @Transactional
    public void send(BusinessUser user, Registers.Notification notification) {
        notNull(user, "user cannot be null!");
        notNull(notification, "notification cannot be null!");

        if (user.isVerified() || ! user.isUserFromExternalSystem()) {
            LOGGER.info("external_notification status=skip user={} is already verified or not external", user);
            return;
        }

        final List<UserNotification> userNotifications = getUserNotifications(user);
        if (isEmpty(userNotifications)) {
            // we have to reset password and provide unsubscribe link
            final String newPassword = userVerificationService.resetPassword(user);

            final Notification userNotification = registerService.getValue(notification.getCode(), Notification.class);
            mailNotificationSender.doSendNotification(user, userNotification, ImmutableMap.of(
                    // plain-text password
                    PASSWORD_PARAM, newPassword,
                    // TODO: how to get proper url?
                    // unsubscribe link is based on user's hashed password stored in DB
                    UNSUBSCRIBE_LINK_PARAM, "https://want-something.com/unsubscribe?id=" + user.getPassword()));
        }
    }


    //--------------------------------------------------- HELPER METHODS -----------------------------------------------
    /**
     * Get list of all user's notifications.
     */
    private List<UserNotification> getUserNotifications(BusinessUser user) {
        final Search userNotificationSearch = new Search(UserNotification.class);
        userNotificationSearch.addFilterEqual("user", user);
        return generalService.search(userNotificationSearch);
    }
}
