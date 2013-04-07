package com.eprovement.poptavka.service.notification;

import com.eprovement.poptavka.domain.settings.Notification;
import com.eprovement.poptavka.domain.user.User;

import java.util.Map;

/**
 * Notification sender which is able to send notification message to the user.
 * Different implementations can use different transform channels, such as email, sms and so on.
 */
public interface NotificationSender {

    /**
     * Send new notification to the given {@code user}.
     * This method should compose appropriate message according to passed {@code notification} type.
     *
     * @param user user to which the notification will be sent.
     * @param notification notification which should be sent. this is a value from register
     * @param messageVariables optional variables together with values that should be used
     *                         for expanding message template associated with given notification, can be null
     * @see com.eprovement.poptavka.domain.register.Registers.Notification
     * @see com.eprovement.poptavka.domain.settings.Notification
     * @see Notification#messageTemplate
     */
    void sendNotification(User user, Notification notification, Map<String, String> messageVariables);
}
