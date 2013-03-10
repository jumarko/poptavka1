/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.service.notification;

import com.eprovement.poptavka.domain.message.UserMessage;

/**
 * Notification service providing methods for working with notifications.
 */
public interface NotificationService {

    /**
     * Notifies user associated with given user messages.
     * All given user messages should have the same user and are supposed to be sent together
     * in one notification message.
     * <p>
     * Notification is sent immediately only if user has notification
     * settings for {@link com.eprovement.poptavka.domain.register.Registers.Notification#NEW_MESSAGE}
     * set to {@link com.eprovement.poptavka.domain.enums.Period#INSTANTLY}.
     * Otherwise the Notification job should handle notifications.
     * </p>
     * @param array of all new user messages that will be sent in one notification.
     */
    void notifyUserNewMessage(UserMessage... newMessages);
}
