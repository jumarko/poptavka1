package com.eprovement.poptavka.service.notification;

import static java.util.Arrays.asList;

import com.eprovement.poptavka.domain.enums.MessageUserRoleType;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.message.MessageUserRole;
import com.eprovement.poptavka.domain.settings.Notification;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.service.message.MessageService;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Implementation of {@link NotificationSender} which sends notifications via internal (application) messages.
 * That means not external communication channel is used, user gets message which can be seen in poptavka
 * application itself.
 * @see
 */
public class InternalMessageNotificationSender implements NotificationSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(InternalMessageNotificationSender.class);
    private final MessageService messageService;

    /**
     * Creates new notification service uses given {@code mailService} for sending emails
     * and {@code notificationFromAddress} as a From address
     * @param messageService message service for sending internal messages.
     */
    public InternalMessageNotificationSender(MessageService messageService) {
        Validate.notNull(messageService, "messageService cannot be null!");
        this.messageService = messageService;
    }

    /**
     * This implementation sends notification via email in an asynchronous way.
     * User can specify optional message variables which will be used for expanding message template associated
     * with given notification.
     */
    @Override
    public void sendNotification(User user, Notification notification, Map<String, String> messageVariables) {
        Validate.notNull(user, "User for notification cannot be null!");
        Validate.notNull(notification, "notification cannot be null!");

        LOGGER.info("action=notification_message status=startuser={} notification={}", user, notification);
        // TODO: create message and set its parameters
        final Message notificationMessage = messageService.newThreadRoot(user).getMessage();
        addNotifiedUserRole(user, notificationMessage);
        notificationMessage.setSubject(notification.getName());
        notificationMessage.setBody(NotificationSenderUtils.expandMessageBody(notification, messageVariables));
        messageService.send(notificationMessage);
        LOGGER.info("action=notification_message status=finish user={} notification={}", user, notification);
    }

    private void addNotifiedUserRole(User user, Message notificationMessage) {
        final MessageUserRole notifiedUserToRole = new MessageUserRole();
        notifiedUserToRole.setType(MessageUserRoleType.TO);
        notifiedUserToRole.setUser(user);
        notifiedUserToRole.setMessage(notificationMessage);
        notificationMessage.setRoles(asList(notifiedUserToRole));
    }

}
