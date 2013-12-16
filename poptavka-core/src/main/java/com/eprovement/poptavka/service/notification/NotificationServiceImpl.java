package com.eprovement.poptavka.service.notification;

import com.eprovement.poptavka.domain.enums.Period;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.register.Registers;
import com.eprovement.poptavka.domain.settings.Notification;
import com.eprovement.poptavka.domain.settings.NotificationItem;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.service.register.RegisterService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

public class NotificationServiceImpl implements NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private static final int MAX_LENGTH_OF_MESSAGE = 60;

    private final NotificationSender notificationSender;
    private final RegisterService registerService;

    public NotificationServiceImpl(RegisterService registerService, NotificationSender notificationSender) {
        Validate.notNull(registerService, "registerService cannot be null!");
        Validate.notNull(notificationSender, "notificationSender cannot be null");
        this.notificationSender = notificationSender;
        this.registerService = registerService;
    }

    @Override
    @Transactional(readOnly = true)
    public void notifyUserNewMessage(Period expectedPeriod, final UserMessage... newMessages) {
        if (ArrayUtils.isEmpty(newMessages)) {
            LOGGER.info("action=notification_new_message status=no_messages_for_notification");
            return;
        }
        final User userToBeNotified = newMessages[0].getUser();
        Validate.notNull(userToBeNotified, "user to be notified cannot be null!");

        if (hasUserExpectedNotificationSetting(userToBeNotified, expectedPeriod, Registers.Notification.NEW_MESSAGE)) {
            final Notification notificationEntity =
                    registerService.getValue(Registers.Notification.NEW_MESSAGE.getCode(), Notification.class);
            final String composedMessageBody = composeNotificationMessage(userToBeNotified, newMessages);
            if (emptyNotificationMessage(composedMessageBody)) {
                LOGGER.info("action=notification_new_message status=no_unread_messages_for_notification");
                return;
            }
            LOGGER.debug("action=notification_new_message status=notification_message_composed user={}",
                    userToBeNotified);
            notificationSender.sendNotification(userToBeNotified, notificationEntity, new HashMap<String, String>() {
                {
                    put("header", "You have " + newMessages.length + " new message(s)");
                    put("body", composedMessageBody);
                }
            });
            LOGGER.debug("action=notification_new_message status=notification_sent user={}", userToBeNotified);
        }
    }

    private boolean emptyNotificationMessage(String composedMessageBody) {
        return composedMessageBody.length() == 0;
    }

    //--------------------------------------------------- HELPER METHODS -----------------------------------------------

    /**
     * Checks if given {@code user} has a setting for given notification and period is {@code expectedPeriod}.
     *
     * @param user for which notifications will be checked
     * @param expectedPeriod period which has to be set in user's notifications settings ({@link NotificationItem})
     *@param notification type of notification  @return true if user has instant notification setting, false otherwise
     */
    private boolean hasUserExpectedNotificationSetting(User user, Period expectedPeriod,
                                                       Registers.Notification notification) {
        if (user.getSettings() == null) {
            // user has no settings -> this means that it has not notification settings as well
            return false;
        }
        final List<NotificationItem> userNotificationSettings = user.getSettings().getNotificationItems();
        if (CollectionUtils.isNotEmpty(userNotificationSettings)) {
            for (NotificationItem notificationSetting : userNotificationSettings) {
                if (notification.getCode().equals(notificationSetting.getNotification().getCode())
                        && expectedPeriod == notificationSetting.getPeriod()) {
                    return true;
                }
            }
        }
        return false;
    }

    private String composeNotificationMessage(User userToBeNotified, UserMessage[] newMessages) {
        final StringBuilder composedMessageBody = new StringBuilder();
        for (UserMessage newUserMessage : newMessages) {
            if (newUserMessage.isRead()) {
                // do not notify about messages that have already been read !?
                continue;
            }
            if (! userToBeNotified.equals(newUserMessage.getUser())) {
                throw new IllegalArgumentException("User set in UserMessage=" + newUserMessage + " is different"
                        + " from user in very first UserMessage=" + newMessages[0]
                        + ". Make sure that all passed UserMessage-s have the same User!");
            }
            Validate.notNull(newUserMessage.getMessage(), "userMessage.message cannot be null!");
            Validate.notNull(newUserMessage.getMessage().getSubject(), "userMessage.message.subject cannot be null!");
            Validate.notNull(newUserMessage.getMessage().getBody(), "userMessage.message.body cannot be null!");
            composedMessageBody.append("    ");
            composedMessageBody.append(newUserMessage.getMessage().getSubject());
            composedMessageBody.append(" - ");
            composedMessageBody.append(newUserMessage.getMessage().getBody().substring(
                    0, Math.min(newUserMessage.getMessage().getBody().length(), MAX_LENGTH_OF_MESSAGE)));
            composedMessageBody.append("...\n");
        }
        return composedMessageBody.toString();
    }
}
