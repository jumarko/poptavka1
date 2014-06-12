package com.eprovement.poptavka.service.user;

import static org.apache.commons.collections.CollectionUtils.isEmpty;
import static org.apache.commons.collections.MapUtils.isNotEmpty;
import static org.apache.commons.lang.Validate.notEmpty;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Responsible for sending one-time notifications for users imported from external systems.
 * @see #send(BusinessUser, Registers.Notification) for more details.
 */
public class ExternalUserNotificator {

    static final String DEPLOYMENT_URL_PARAM = "wantsomething.url";
    static final String UNSUBSCRIBE_LINK_PARAM = "unsubscribe.link";
    static final String PASSWORD_PARAM = "password";

    private static final Logger LOGGER = getLogger(ExternalUserNotificator.class);

    private final MailNotificationSender mailNotificationSender;
    private final GeneralService generalService;
    private final RegisterService registerService;
    private final UserVerificationService userVerificationService;

    private final UriTemplate unsubscribeUriTemplate;
    private final String deploymentUrl;


    public ExternalUserNotificator(GeneralService generalService,
                                   RegisterService registerService,
                                   UserVerificationService userVerificationService,
                                   MailNotificationSender mailNotificationSender,
                                   @Value("deployment.url") String deploymentUrl) {
        notNull(generalService, "generalService cannot be null!");
        notNull(registerService, "registerService cannot be null!");
        notNull(userVerificationService, "userVerificationService cannot be null!");
        notNull(mailNotificationSender, "mailNotificationSender cannot be null!");
        notEmpty(deploymentUrl, "unsubscribeUriTemplate cannot be empty!");

        this.generalService = generalService;
        this.registerService = registerService;
        this.userVerificationService = userVerificationService;
        this.mailNotificationSender = mailNotificationSender;
        this.deploymentUrl = deploymentUrl;
        this.unsubscribeUriTemplate = new UriTemplate(deploymentUrl + "#unsubscribe?id={passwordHash}");
    }

    public void send(BusinessUser user, Registers.Notification notification) {
        send(user, notification, Collections.<String, String>emptyMap());
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
     * @param notificationType notification which determines the message being sent to the user
     * @param notificationParams custom notification params that will be expanded in notification template
     * @see com.eprovement.poptavka.service.notification.MailNotificationSender#doSendNotification
     */
    @Transactional
    public void send(
        BusinessUser user, Registers.Notification notificationType, Map<String, String> notificationParams) {

        notNull(user, "user cannot be null!");
        notNull(notificationType, "notification cannot be null!");

        if (user.isVerified() || ! user.isUserFromExternalSystem()) {
            LOGGER.info("external_notification status=skip user={} is already verified or not external", user);
            return;
        }

        final List<UserNotification> userNotifications = getUserNotifications(user);
        if (isEmpty(userNotifications)) {
            // we have to reset password and provide unsubscribe link
            final String newPassword = userVerificationService.resetPassword(user);

            final HashMap<String, String> notificationParameters = new HashMap<>();
            // custom parameters for external users' notifications
            if (isNotEmpty(notificationParams)) {
                notificationParameters.putAll(notificationParams);
            }
            // common parameters for external users' notifications
            notificationParameters.putAll(ImmutableMap.of(
                    // want-something web url,
                    DEPLOYMENT_URL_PARAM, deploymentUrl,
                    // plain-text password
                    PASSWORD_PARAM, newPassword,
                    // unsubscribe link is based on user's hashed password stored in DB
                    UNSUBSCRIBE_LINK_PARAM, unsubscribeUriTemplate.expand(user.getPassword()).toString()));
            final Notification notification = registerService.getValue(notificationType.getCode(), Notification.class);
            mailNotificationSender.doSendNotification(user, notification, notificationParameters);

            //TODO RELEASE JURAJ - please verify
            UserNotification userNotification = new UserNotification();
            userNotification.setNotification(notification);
            userNotification.setUser(user);
            generalService.persist(userNotification);
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
