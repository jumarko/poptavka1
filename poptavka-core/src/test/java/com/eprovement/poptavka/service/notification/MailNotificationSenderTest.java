package com.eprovement.poptavka.service.notification;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.service.mail.MailService;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;

import java.util.Collections;

public class MailNotificationSenderTest extends NotificationSenderTest {

    private static final String NOTIFICATION_FROM = "noreply@want-something.com";
    private MailService mailServiceMock;


    @Test(expected = IllegalArgumentException.class)
    public void testSendNotificationForInvalidUserEmailAddress() throws Exception {
        getNotificationSender().sendNotification(new User(), newMessageNotification, null);
    }

    /**
     * Tests that email notification is not sent to unverified user.
     * Only verified users can receive notifications.
     * @throws Exception
     */
    @Test
    public void testNotificationNotSentToUnverifiedUser() throws Exception {
        final User notifiedUser = new User("user@gmail.com", "myPassword");
        getNotificationSender().sendNotification(notifiedUser, newMessageNotification,
                Collections.<String, String>emptyMap());

        verifyZeroInteractions(mailServiceMock);
    }



    @Override
    protected void verifyNotificationSent(User notifiedUser, String expectedMessageSubject,
                                          String expectedMessageBody) {
        final ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailServiceMock).sendAsync(messageCaptor.capture());
        final SimpleMailMessage message = messageCaptor.getValue();
        assertNotNull(message);
        assertThat(message.getFrom(), is(NOTIFICATION_FROM));
        assertThat(message.getTo()[0], is(notifiedUser.getEmail()));
        assertThat("Incorrect message subject", message.getSubject(), is(expectedMessageSubject));
        assertThat("Incorrect message body", message.getText(), is(expectedMessageBody));
    }

    @Override
    protected NotificationSender getNotificationSender() {
        mailServiceMock = mock(MailService.class);
        return new MailNotificationSender(mailServiceMock, NOTIFICATION_FROM);
    }
}
