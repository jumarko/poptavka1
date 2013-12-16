package com.eprovement.poptavka.service.notification;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.service.mail.MailService;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;

public class MailNotificationSenderTest extends NotificationSenderTest {

    private static final String NOTIFICATION_FROM = "noreply@want-something.com";
    private MailService mailServiceMock;


    @Test(expected = IllegalArgumentException.class)
    public void testSendNotificationForInvalidUserEmailAddress() throws Exception {
        getNotificationSender().sendNotification(new User(), newMessageNotification, null);
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
