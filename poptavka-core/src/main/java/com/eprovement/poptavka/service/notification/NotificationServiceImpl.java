/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.service.notification;

import com.eprovement.poptavka.domain.enums.Period;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.register.Registers;
import com.eprovement.poptavka.domain.settings.Notification;
import com.eprovement.poptavka.domain.settings.NotificationItem;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.service.register.RegisterService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;

import java.util.HashMap;
import java.util.List;

public class NotificationServiceImpl implements NotificationService {

    private final NotificationSender notificationSender;
    private final RegisterService registerService;

    public NotificationServiceImpl(RegisterService registerService, NotificationSender notificationSender) {
        Validate.notNull(registerService, "registerService cannot be null!");
        Validate.notNull(notificationSender, "notificationSender cannot be null");
        this.notificationSender = notificationSender;
        this.registerService = registerService;
    }

    @Override
    public void notifyUserNewMessage(final UserMessage... newMessages) {
        Validate.notEmpty(newMessages, "newMessage cannot be empty!");
        final User userToBeNotified = newMessages[0].getUser();
        Validate.notNull(userToBeNotified, "user to be notified cannot be null!");

        if (hasUserInstantNotificationSetting(userToBeNotified, Registers.Notification.NEW_MESSAGE)) {
            final Notification notificationEntity =
                    registerService.getValue(Registers.Notification.NEW_MESSAGE.getCode(), Notification.class);
            final StringBuilder composedMessageBody = new StringBuilder();
            for (UserMessage newUserMessage : newMessages) {
                if (! userToBeNotified.equals(newUserMessage.getUser())) {
                    throw new IllegalArgumentException("User set in UserMessage=" + newUserMessage + " is different"
                            + " from user in very first UserMessage=" + newMessages[0]
                            + ". Make sure that all passed UserMessage-s have the same User!");
                }
                composedMessageBody.append("    ");
                composedMessageBody.append(newUserMessage.getMessage().getSubject());
                composedMessageBody.append("\n");
            }
            notificationSender.sendNotification(userToBeNotified, notificationEntity, new HashMap<String, String>() {
                {
                    put("header", "You have " + newMessages.length + " new message(s)");
                    put("body", composedMessageBody.toString());
                }
            });
        }
    }

    //--------------------------------------------------- HELPER METHODS -----------------------------------------------

    /**
     * Checks if given {@code user} has a setting for given notification and period is {@link Period#INSTANTLY}.
     *
     * @param user         for which notifications will be checked
     * @param notification type of notification
     * @return true if user has instant notification setting, false otherwise
     */
    private boolean hasUserInstantNotificationSetting(User user, Registers.Notification notification) {
        if (user.getSettings() == null) {
            // user has no settings -> this means that it has not notification settings as well
            return false;
        }
        final List<NotificationItem> userNotificationSettings = user.getSettings().getNotificationItems();
        if (CollectionUtils.isNotEmpty(userNotificationSettings)) {
            for (NotificationItem notificationSetting : userNotificationSettings) {
                if (notification.getCode().equals(notificationSetting.getNotification().getCode())
                        && Period.INSTANTLY == notificationSetting.getPeriod()) {
                    return true;
                }
            }
        }
        return false;
    }
}
