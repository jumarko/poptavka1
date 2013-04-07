package com.eprovement.poptavka.service.notification;

import com.eprovement.poptavka.domain.enums.Period;
import com.eprovement.poptavka.domain.message.UserMessage;

/**
 * Notification service providing methods for working with notifications.
 */
public interface NotificationService {

    /**
     * Notifies user associated with given user messages if he has notification settings period
     * equal to {@code expectedPeriod}.
     * All given user messages should have the same user and are supposed to be sent together
     * in one notification message.
     * <p>
     * Notification should sent almost immediately (but asynchronously).
     * </p>
     * @param expectedPeriod expectedPeriod which user has to have for "new.message" notification
     * @param array of all new user messages that will be sent in one notification.
     * @see com.eprovement.poptavka.domain.settings.NotificationItem
     */
    void notifyUserNewMessage(Period expectedPeriod, UserMessage... newMessages);
}
