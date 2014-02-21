package com.eprovement.poptavka.service.notification;

import com.eprovement.poptavka.domain.enums.Verification;
import com.eprovement.poptavka.domain.register.Registers;
import com.eprovement.poptavka.domain.settings.Notification;
import com.eprovement.poptavka.domain.user.User;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public abstract class NotificationSenderTest {
    private static final String MESSAGE_TEMPLATE = "New message:\n    ${header}:\n    ${body}";
    private static final String MESSAGE_SUBJECT = "New message";

    protected final Notification newMessageNotification = new Notification();

    protected NotificationSenderTest() {
        newMessageNotification.setName(MESSAGE_SUBJECT);
        newMessageNotification.setCode(Registers.Notification.NEW_MESSAGE.getCode());
        newMessageNotification.setMessageTemplate(MESSAGE_TEMPLATE);
    }

    @Test
    public void testSendNotificationMessageWithoutVariables() throws Exception {
        // no variables has been passed so raw message template should be used
        checkNotificationMessage(null, MESSAGE_TEMPLATE);
    }


    @Test
    public void testSendNotificationMessageWithVariables() throws Exception {
        final Map<String, String> messageVariables = new HashMap<String, String>() { {
                put("header", "1000");
                put("body", "My dear user. I'm sending you some notification\nabout your new messages\n "
                    + "in our great system.");
            }
        };
        final String expectedMessageBody = "New message:\n    1000:\n    My dear user. "
                + "I'm sending you some notification\n"
                + "about your new messages\n"
                + " in our great system.";
        checkNotificationMessage(messageVariables, expectedMessageBody);
    }


    protected abstract void verifyNotificationSent(User notifiedUser, String expectedMessageSubject,
                                                   String expectedMessageBody);

    /**
     * @return concrete implementation of {@link NotificationSender} which you want to be tested.
     */
    protected abstract NotificationSender getNotificationSender();

    //--------------------------------------------------- HELPER METHODS -----------------------------------------------

    private void checkNotificationMessage(Map<String, String> messageVariables, String expectedMessageBody) {
        final User notifiedUser = new User("user@gmail.com", "myPassword");
        notifiedUser.setVerification(Verification.VERIFIED);
        getNotificationSender().sendNotification(notifiedUser, newMessageNotification, messageVariables);

        verifyNotificationSent(notifiedUser, MESSAGE_SUBJECT, expectedMessageBody);
    }

}
