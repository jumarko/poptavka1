package com.eprovement.poptavka.service.notification.welcome;

import com.eprovement.poptavka.domain.enums.NotificationType;
import com.eprovement.poptavka.domain.settings.Notification;
import com.eprovement.poptavka.domain.settings.NotificationItem;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.service.notification.NotificationSender;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Sender responsible for sending welcome message to the new <i>activated</i> users.
 * This class aggregates (potentially) many types of {@link NotificationSender}-s.
 * This way we can send welcome message via multiple channels, for example:
 * <ul>
 *     <li>internal message visible only in application</li>
 *     <li>email</li>
 *     <li>sms</li>
 *     <li>chat</li>
 * </ul>
 * For current types of notification senders used, check argument passed to the constructor of this class
 */
public class WelcomeMessageSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(WelcomeMessageSender.class);

    private final Collection<NotificationSender> senders;

    /**
     * Creates new welcome sender which sends welcome messages via passed {@code welcomeMessageSenders}
     * @param welcomeMessageSenders senders which will be used for sending welcome message, can be for example
     *                              mail sender, internal message sender, and so on.
     * @see NotificationType#WELCOME
     */
    public WelcomeMessageSender(Collection<NotificationSender> welcomeMessageSenders) {
        Validate.notEmpty(welcomeMessageSenders, "at least one sender for welcome message should be provided!");
        this.senders = welcomeMessageSenders;
    }

    /**
     * Sends a welcome message to the {@code user}.
     * @param user user to which the message will be sent, cannot be null
     * @throws IllegalArgumentException if {@code user} is null
     */
    public void sendWelcomeMessage(User user) {
        Validate.notNull(user, "user cannot be null!");
        Validate.notNull(user.getSettings(), "each user should have valid settings!");
        final Collection<Notification> welcomeNotifications = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(user.getSettings().getNotificationItems())) {
            for (NotificationItem userNotification : user.getSettings().getNotificationItems()) {
                if (userNotification.getNotification() != null
                        && (NotificationType.WELCOME == userNotification.getNotification().getNotificationType())) {
                    // each user should have at most one welcome notification so we can take the first one
                    welcomeNotifications.add(userNotification.getNotification());
                }
            }
        }
        if (CollectionUtils.isEmpty(welcomeNotifications)) {
            LOGGER.warn("action=send_welcome_message status=no_welcome_notification user={}", user);
            return;
        }
        for (NotificationSender sender : senders) {
            // client has only one welcome notification, but supplier does have two
            for (Notification notification : welcomeNotifications) {
                sender.sendNotification(user, notification, null);
            }
        }
    }
}
