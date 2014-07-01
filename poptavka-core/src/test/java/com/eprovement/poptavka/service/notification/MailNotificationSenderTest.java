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

import java.util.Collections;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.mockito.Mockito;

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
        try {
            final ArgumentCaptor<MimeMessage> messageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
            verify(mailServiceMock).sendAsync(messageCaptor.capture());
            final MimeMessage message = messageCaptor.getValue();
            assertNotNull(message);
            assertThat(message.getFrom()[0].toString(), is(NOTIFICATION_FROM));
            assertThat(message.getAllRecipients()[0].toString(), is(notifiedUser.getEmail()));
            assertThat("Incorrect message subject", message.getSubject(), is("New message"));
            // TODO LATER ivlcek - check the body content of MimeMessage
            // assertThat("Incorrect message body", message.getText(), is(expectedMessageBody));
        } catch (MessagingException ex) {
            Logger.getLogger(MailNotificationSenderTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected NotificationSender getNotificationSender() {
        mailServiceMock = mock(MailService.class, Mockito.RETURNS_SMART_NULLS);
        Mockito.when(mailServiceMock.createMimeMessage()).thenReturn(
                new MimeMessage(Session.getInstance(new Properties())));

        return new MailNotificationSender(mailServiceMock, NOTIFICATION_FROM);
    }
}
