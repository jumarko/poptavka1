package com.eprovement.poptavka.service.notification;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eprovement.poptavka.domain.enums.MessageState;
import com.eprovement.poptavka.domain.enums.MessageUserRoleType;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.service.message.MessageService;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class InternalMessageNotificationSenderTest extends NotificationSenderTest {

    private MessageService messageServiceMock;


    @Override
    protected void verifyNotificationSent(User notifiedUser, String expectedMessageSubject,
                                          String expectedMessageBody) {
        final ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(messageServiceMock).send(messageCaptor.capture());
        final Message message = messageCaptor.getValue();
        assertNotNull("Notification message should not be null", message);
        assertNotNull("Message sender should be set to some suitable system user", message.getSender());
        assertThat("Incorrect message subject", message.getSubject(), is(expectedMessageSubject));
        assertThat("Incorrect message body", message.getBody(), is(expectedMessageBody));
        // check recipient
        assertNotNull("message's roles have to be set", message.getRoles());
        assertThat("exactly one notification message roles expected", message.getRoles().size(), is(1));
        assertThat("notification message has to have a role of type 'TO'",
                message.getRoles().get(0).getType(), is(MessageUserRoleType.TO));
        assertThat("notification message role has incorrect recipient",
                message.getRoles().get(0).getUser(), is(notifiedUser));
        assertThat("notification message role has to have reference to proper message",
                message.getRoles().get(0).getMessage(), is(message));

        // verify that message has been constructed and updated properly:
        // -- created via newThreadRoot method which ensures that all required attributes of message are set
        verify(messageServiceMock).newThreadRoot(message.getSender());
        // -- updated properly: update need to be called after newThreadRoot to set subject and body
        verify(messageServiceMock).send(message);

    }

    @Override
    protected NotificationSender getNotificationSender() {
        messageServiceMock = mock(MessageService.class);
        when(messageServiceMock.newThreadRoot(any(User.class))).thenAnswer(new Answer<UserMessage>() {
            @Override
            public UserMessage answer(InvocationOnMock invocation) throws Throwable {
                final User user = (User) invocation.getArguments()[0];
                final Message notificationMessage = new Message();
                notificationMessage.setMessageState(MessageState.COMPOSED);
                notificationMessage.setSender(user);
                final UserMessage notificationUserMessage = new UserMessage();
                notificationUserMessage.setMessage(notificationMessage);
                notificationUserMessage.setUser(user);
                return notificationUserMessage;
            }
        });
        return new InternalMessageNotificationSender(messageServiceMock);
    }

}
