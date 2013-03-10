package com.eprovement.poptavka.service.notification;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.eprovement.poptavka.domain.register.Registers;
import com.eprovement.poptavka.domain.settings.Notification;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.service.mail.MailService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;

import java.util.HashMap;
import java.util.Map;

public class MailNotificationSenderTest {

    private static final String NOTIFICATION_FROM = "noreply@want-something.com";
    private static final String NOTIFICATION_TO = "user@gmail.com";
    private static final String MESSAGE_TEMPLATE = "New message:\n    ${header}:\n    ${body}";
    private Notification newMessageNotification = new Notification();

    private MailNotificationSender mailNotificationSender;
    private MailService mailServiceMock;

    @Before
    public void setUp() throws Exception {
        mailServiceMock = mock(MailService.class);
        mailNotificationSender = new MailNotificationSender(mailServiceMock, NOTIFICATION_FROM);
        newMessageNotification.setName("New message");
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


    @Test(expected = IllegalArgumentException.class)
    public void testSendNotificationForInvalidUserEmailAddress() throws Exception {
        mailNotificationSender.sendNotification(new User(), newMessageNotification, null);
    }


    //--------------------------------------------------- HELPER METHODS -----------------------------------------------

    private void checkNotificationMessage(Map<String, String> messageVariables, String expectedMessageBody) {
        final User notifiedUser = new User(NOTIFICATION_TO, "myPassword");
        mailNotificationSender.sendNotification(notifiedUser, newMessageNotification, messageVariables);

        final ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailServiceMock).sendAsync(messageCaptor.capture());
        final SimpleMailMessage message = messageCaptor.getValue();
        assertNotNull(message);
        assertThat(message.getFrom(), is(NOTIFICATION_FROM));
        assertThat(message.getTo()[0], is(NOTIFICATION_TO));
        assertThat("Incorrect message subject", message.getSubject(), is("New message"));
        assertThat("Incorrect message body", message.getText(), is(expectedMessageBody));
    }

}
