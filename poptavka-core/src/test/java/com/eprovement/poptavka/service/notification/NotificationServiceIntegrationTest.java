package com.eprovement.poptavka.service.notification;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.eprovement.poptavka.base.integration.DBUnitIntegrationTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.enums.MessageState;
import com.eprovement.poptavka.domain.enums.Period;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.mail.MailService;
import com.eprovement.poptavka.service.register.RegisterService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;

import java.util.ArrayList;

@DataSet(path = {
        "classpath:com/eprovement/poptavka/domain/register/RegisterDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/address/LocalityDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/UsersDataSet.xml"
        }, dtd = "classpath:test.dtd")
public class NotificationServiceIntegrationTest extends DBUnitIntegrationTest {

    private static final String NOTIFICATION_FROM = "noreply@want-something.com";

    @Autowired
    private RegisterService registerService;
    @Autowired
    private GeneralService generalService;

    private MailNotificationSender mailNotificationSender;
    private MailService mailServiceMock;
    private NotificationService notificationService;

    @Before
    public void setUp() throws Exception {
        mailServiceMock = mock(MailService.class);
        mailNotificationSender = spy(new MailNotificationSender(mailServiceMock, NOTIFICATION_FROM));
        this.notificationService = new NotificationServiceImpl(registerService, mailNotificationSender);
    }


    @Test
    public void testSendNewMessageNotification() throws Exception {
        final String expectedMessageBody = "New message(s) summary:\n"
                + "    First new message subject\n"
                + "    Second new message subject\n"
                + "    Last new message subject\n";
        checkNotificationSent(expectedMessageBody,
                createMessageWithSubject("First new message subject"),
                createMessageWithSubject("Second new message subject"),
                createMessageWithSubject("Last new message subject"));

    }

    @Test
    public void testNewMessageNotificationIsNotSentForUserWithoutNotificationSetting() throws Exception {
        checkNotificationNotSent(111111112L);
    }


    @Test
    public void testNewMessageNotificationIsNotSentForUserWithDailyPeriod() throws Exception {
        checkNotificationNotSent(111111113L);
    }


    @Test
    public void testSendNotificationForInvalidUser() throws Exception {
        final UserMessage userMessage = createUserMessage(new Message(), new User());
        notificationService.notifyUserNewMessage(Period.INSTANTLY, userMessage);
        verifyZeroInteractions(mailNotificationSender);
    }


    //--------------------------------------------------- HELPER METHODS -----------------------------------------------

    private Message createMessageWithSubject(String messageSubject) {
        final Message originalMessage = new Message();
        originalMessage.setMessageState(MessageState.COMPOSED);
        originalMessage.setSubject(messageSubject);
        return originalMessage;
    }

    private void checkNotificationSent(String expectedMessageBody, Message... originalMessages) {
        final User notifiedUser = generalService.find(User.class, 111111111L);
        final ArrayList<UserMessage> newUserMessages = new ArrayList<>();
        for (Message message : originalMessages) {
            newUserMessages.add(createUserMessage(message, notifiedUser));
        }
        notificationService.notifyUserNewMessage(Period.INSTANTLY,
                newUserMessages.toArray(new UserMessage[newUserMessages.size()]));

        final ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailServiceMock).sendAsync(messageCaptor.capture());
        final SimpleMailMessage message = messageCaptor.getValue();
        assertNotNull(message);
        assertThat(message.getFrom(), is(NOTIFICATION_FROM));
        assertThat(message.getTo()[0], is(notifiedUser.getEmail()));
        assertThat("Incorrect message subject", message.getSubject(), is("New message"));
        assertThat("Incorrect message body", message.getText(), is(expectedMessageBody));
    }

    private UserMessage createUserMessage(Message originalMessage, User notifiedUser) {
        final UserMessage userMessage = new UserMessage();
        userMessage.setUser(notifiedUser);
        userMessage.setMessage(originalMessage);
        return userMessage;
    }

    private void checkNotificationNotSent(Long userId) {
        final Message originalMessage = new Message();
        originalMessage.setMessageState(MessageState.COMPOSED);
        final User notifiedUser = generalService.find(User.class, userId);
        notificationService.notifyUserNewMessage(Period.INSTANTLY, createUserMessage(originalMessage, notifiedUser));
        verifyZeroInteractions(mailNotificationSender);
    }
}
