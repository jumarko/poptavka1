package com.eprovement.poptavka.service.notification;

import static org.apache.commons.lang.Validate.isTrue;
import static org.apache.commons.lang.Validate.notNull;
import static org.slf4j.LoggerFactory.getLogger;

import com.eprovement.poptavka.domain.settings.Notification;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.service.mail.MailService;
import com.eprovement.poptavka.validation.EmailValidator;
import org.slf4j.Logger;

import java.util.Map;
import javax.mail.MessagingException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * Implementation of {@link NotificationSender} which sends notifications via email.
 */
public class MailNotificationSender implements NotificationSender {

    private static final Logger LOGGER = getLogger(MailNotificationSender.class);

    private final MailService mailService;
    private final String notificationFromAddress;

    /**
     * Creates new notification service uses given {@code mailService} for sending emails
     * and {@code notificationFromAddress} as a From address
     * @param mailService mail service for sending emails.
     * @param notificationFromAddress email address from which all notification emails are sent
     */
    public MailNotificationSender(MailService mailService, String notificationFromAddress) {
        notNull(mailService, "mailService cannot be null!");
        isTrue(EmailValidator.getInstance().isValid(notificationFromAddress),
                "Valid 'From' address for notifications must be specified!");

        this.mailService = mailService;
        this.notificationFromAddress = notificationFromAddress;
    }

    /**
     * This implementation sends notification via email in an asynchronous way to a <strong>verified</strong> user.
     * User can specify optional message variables which will be used for expanding message template associated
     * with given notification.
     */
    @Override
    public void sendNotification(User user, Notification notification, Map<String, String> messageVariables) {
        checkParams(user, notification);

        if (! user.isVerified()) {
            LOGGER.info("action=notification_email status=skip user={} is not verified. "
                    + "Notification email won't be sent!", user.getEmail());
            return;
        }

        doSendNotification(user, notification, messageVariables);
    }

    /**
     * Sends {@code notification} to the {@code user} without checking if user is verified or not.
     * That means this method should be used with caution and appropriate checks should be made outside of this method.
     *
     * @param user user to be notified
     * @param notification notification to be sent
     * @param messageVariables variables which should be replaced in
     *
     * @see NotificationSender#sendNotification(User, Notification, Map)
     */
    public void doSendNotification(User user, Notification notification, Map<String, String> messageVariables) {
        checkParams(user, notification);

        LOGGER.info("action=notification_email_async status=start user={} notification={}", user, notification);
        MimeMessageHelper message = null;
        try {
            message = new MimeMessageHelper(mailService.createMimeMessage(), true, "UTF-8");
            message.setFrom(notificationFromAddress);
            message.setTo(user.getEmail());
            message.setSubject(NotificationSenderUtils.expandMessageSubject(notification, messageVariables));
            message.setText(NotificationSenderUtils.expandMessageBody(notification, messageVariables), true);
            message.addInline("logo", new ClassPathResource("images/email_logo.png"));
            message.addInline("background", new ClassPathResource("images/email_bg.jpg"));
        } catch (MessagingException ex) {
            LOGGER.error("action=notification_email_async status=error messages=" + ex.getLocalizedMessage(), ex);
        }

        mailService.sendAsync(message.getMimeMessage());
        LOGGER.info("action=notification_email_async status=finish user={} notification={}", user, notification);
    }


    //--------------------------------------------------- HELPER METHODS -----------------------------------------------

    private void checkParams(User user, Notification notification) {
        notNull(user, "User for notification cannot be null!");
        isTrue(EmailValidator.getInstance().isValid(user.getEmail()),
                "User email address must be valid! user=" + user);
        notNull(notification, "notification cannot be null!");
    }

}
