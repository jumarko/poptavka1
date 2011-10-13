package cz.poptavka.sample.service.message;

import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import cz.poptavka.sample.dao.message.MessageFilter;
import cz.poptavka.sample.domain.message.Message;
import cz.poptavka.sample.domain.message.MessageState;
import cz.poptavka.sample.domain.message.MessageUserRole;
import cz.poptavka.sample.domain.message.MessageUserRoleType;
import cz.poptavka.sample.domain.message.UserMessage;
import cz.poptavka.sample.domain.user.User;
import cz.poptavka.sample.exception.MessageException;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.service.usermessage.UserMessageService;
import java.util.ArrayList;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Juraj Martinka
 *         Date: 11.5.11
 */
@DataSet(path = {
        "classpath:cz/poptavka/sample/domain/user/UsersDataSet.xml",
        "classpath:cz/poptavka/sample/domain/demand/DemandDataSet.xml",
        "classpath:cz/poptavka/sample/domain/message/MessageDataSet.xml" },
        dtd = "classpath:test.dtd",
        disableForeignKeyChecks = true)
public class MessageServiceIntegrationTest extends DBUnitBaseTest {

    @Autowired
    private MessageService messageService;

    @Autowired
    private GeneralService generalService;

    @Autowired
    private UserMessageService userMessageService;

    private User user;


    @Before
    public void setUp() {
        this.user = new User();
        user.setId(111111111L);
    }


    @Test
    public void testGetUserMessageThreads() {

        final List<Message> messageThreads = this.messageService.getMessageThreads(this.user,
                MessageFilter.EMPTY_FILTER);

        // three thread roots for tested user
        Assert.assertEquals(3, messageThreads.size());
        checkUserMessageExists(1L, messageThreads);
        checkUserMessageExists(200L, messageThreads);
        checkUserMessageExists(300L, messageThreads);

        // one reply to the thread root message
        Assert.assertEquals(1, messageThreads.get(0).getChildren().size());
        checkUserMessageExists(3L, messageThreads.get(0).getChildren());

        // two replies to the reply to the thread root message
        Assert.assertEquals(2, messageThreads.get(0).getChildren().get(0).getChildren().size());
        checkUserMessageExists(2L, messageThreads.get(0).getChildren().get(0).getChildren());
        checkUserMessageExists(4L, messageThreads.get(0).getChildren().get(0).getChildren());
    }


    @Test
    public void testGetAllUserMessages() {
        final List<Message> allUserMessages = this.messageService.getAllMessages(this.user, MessageFilter.EMPTY_FILTER);
        Assert.assertEquals(7, allUserMessages.size());
        checkUserMessageExists(1L, allUserMessages);
        checkUserMessageExists(2L, allUserMessages);
        checkUserMessageExists(3L, allUserMessages);
        checkUserMessageExists(4L, allUserMessages);
        checkUserMessageExists(200L, allUserMessages);
        checkUserMessageExists(300L, allUserMessages);
        checkUserMessageExists(301L, allUserMessages);
    }

    @Test
    public void testGetUserReceivedMessages() {
        final List<Message> allUserReceivedMessages = this.messageService.getAllMessages(
                this.user,
                MessageFilter.MessageFilterBuilder.messageFilter()
                        .withMessageUserRoleType(MessageUserRoleType.TO).build());
        Assert.assertEquals(5, allUserReceivedMessages.size());
        checkUserMessageExists(1L, allUserReceivedMessages);
        checkUserMessageExists(2L, allUserReceivedMessages);
        checkUserMessageExists(4L, allUserReceivedMessages);
        checkUserMessageExists(200L, allUserReceivedMessages);
        checkUserMessageExists(300L, allUserReceivedMessages);
    }

    @Test
    public void testGetUserSentMessages() {
        final List<Message> allUserReceivedMessages = this.messageService.getAllMessages(
                this.user,
                MessageFilter.MessageFilterBuilder.messageFilter()
                        .withMessageUserRoleType(MessageUserRoleType.SENDER).build());
        Assert.assertEquals(2, allUserReceivedMessages.size());
        checkUserMessageExists(3L, allUserReceivedMessages);
        checkUserMessageExists(301L, allUserReceivedMessages);
    }


    @Test
    public void testGetPotentialDemandConversation() {
        final Message threadRoot = this.messageService.getById(1L);
        final User supplier = this.generalService.find(User.class, 111111112L);
        final List<Message> potentialDemandConversation =
                this.messageService.getPotentialDemandConversation(threadRoot, supplier);

        Assert.assertEquals(4, potentialDemandConversation.size());
        // check if all expected messages are in conversation
        checkUserMessageExists(1L, potentialDemandConversation);
        checkUserMessageExists(2L, potentialDemandConversation);
        checkUserMessageExists(3L, potentialDemandConversation);
        checkUserMessageExists(4L, potentialDemandConversation);
    }

    @Test
    public void testGetListOfClientDemandMessagesAll() {
        final Message threadRoot1 = this.messageService.getById(1L);
        final Message threadRoot200 = this.messageService.getById(200L);
        final Message threadRoot300 = this.messageService.getById(300L);
        final User client = this.generalService.find(User.class, 111111112L);
        final Map<Message, Long> listOfClientDemandMessages =
                this.messageService.getListOfClientDemandMessagesAll(client);
        Assert.assertEquals("Inacurrate number of threadRoot messages selected",
                3, listOfClientDemandMessages.size());

        checkUserMessageExists(threadRoot1.getId(), listOfClientDemandMessages.keySet());
        Assert.assertEquals("Inacurrate number of subMessages selected",
                (Object) 4L, (Object) listOfClientDemandMessages.get(threadRoot1));
        checkUserMessageExists(threadRoot200.getId(), listOfClientDemandMessages.keySet());
        Assert.assertEquals("Inacurrate number of subMessages selected",
                (Object) 1L, (Object) listOfClientDemandMessages.get(threadRoot200));
        checkUserMessageExists(threadRoot300.getId(), listOfClientDemandMessages.keySet());
        Assert.assertEquals("Inacurrate number of subMessages selected",
                (Object) 2L, (Object) listOfClientDemandMessages.get(threadRoot300));
    }

    @Test
    public void testGetListOfClientDemandMessagesUnread() {
        final Message threadRoot1 = this.messageService.getById(1L);
        final Message threadRoot200 = this.messageService.getById(200L);
        final Message threadRoot300 = this.messageService.getById(300L);
        final User client = this.generalService.find(User.class, 111111112L);
        final Map<Message, Long> listOfClientDemandMessages =
                this.messageService.getListOfClientDemandMessagesUnread(client);
        Assert.assertEquals("Inacurrate number of threadRoot messages selected",
                3, listOfClientDemandMessages.size());

        checkUserMessageExists(threadRoot1.getId(), listOfClientDemandMessages.keySet());
        Assert.assertEquals("Inacurrate number of unread subMessages selected",
                (Object) 1L, (Object) listOfClientDemandMessages.get(threadRoot1));
        checkUserMessageExists(threadRoot200.getId(), listOfClientDemandMessages.keySet());
        Assert.assertEquals("Inacurrate number of unread subMessages selected",
                (Object) 1L, (Object) listOfClientDemandMessages.get(threadRoot200));
        checkUserMessageExists(threadRoot300.getId(), listOfClientDemandMessages.keySet());
        Assert.assertEquals("Inacurrate number of unread subMessages selected",
                (Object) 0L, (Object) listOfClientDemandMessages.get(threadRoot300));
    }


    @Test
    public void testGetDescendants() {
        Message threadRoot = this.messageService.getById(1L);
        List<Message> descendants =
                this.messageService.getAllDescendants(threadRoot);
        Assert.assertEquals("Inacurrate number of descendants selected",
                3, descendants.size());

        checkUserMessageExists(2L, descendants);
        checkUserMessageExists(3L, descendants);
        checkUserMessageExists(4L, descendants);

        threadRoot = this.messageService.getById(5L);
        descendants =
                this.messageService.getAllDescendants(threadRoot);
        Assert.assertEquals("Inacurrate number of descendants selected",
                1, descendants.size());

        checkUserMessageExists(6L, descendants);
    }

    @Test
    public void testMessageLifeCycle() throws MessageException {
        final User user = this.generalService.find(User.class, 111111111L);
        final User user2 = this.generalService.find(User.class, 111111112L);
        long messageId = messageService.newThreadRoot(user).getId();
        Message message = messageService.getById(messageId);
        Assert.assertNotNull("Test message wasn't created.", message);

        Assert.assertEquals("MessageState of a newly creted message"
                + " should be COMPOSED, but was" + message.getMessageState()
                + ".", MessageState.COMPOSED, message.getMessageState());

        Assert.assertEquals("Incorrect sender set to newly created message.",
                user, message.getSender());

        testUserMessagePresent(message, user);
        testUserMessageNull(message, user2);
        message.setSubject("Hello");
        message.setBody("Chtěl bych zadat poptávku na sexuální služby,"
                + " ale nevím jak se to dělá.");
        MessageUserRole messageUserRole = new MessageUserRole();
        messageUserRole.setMessage(message);
        messageUserRole.setUser(user2);
        messageUserRole.setType(MessageUserRoleType.TO);
        List<MessageUserRole> messageUserRoles = new ArrayList();
        messageUserRoles.add(messageUserRole);
        message.setRoles(messageUserRoles);
        messageService.send(message);
        Assert.assertEquals("MessageState of a message after sending"
                + " should be SENT, but was" + message.getMessageState()
                + ".", MessageState.SENT, message.getMessageState());
        testUserMessagePresent(message, user2);

        Message reply = messageService.newReply(message, user2);

        testUserMessagePresent(reply, user2);
        testUserMessageNull(reply, user);

        Assert.assertEquals("MessageState of a newly creted reply"
                + " should be COMPOSED, but was" + reply.getMessageState()
                + ".", MessageState.COMPOSED, reply.getMessageState());

        Assert.assertEquals("Reply subject incorrect.",
                "Re: Hello", reply.getSubject());

        Assert.assertEquals("Incorrect sender set to reply.",
                user2, reply.getSender());

        messageService.send(reply);

        Assert.assertEquals("MessageState of a reply after sending"
                + " should be SENT, but was" + reply.getMessageState()
                + ".", MessageState.SENT, reply.getMessageState());

        testUserMessagePresent(reply, user);

    }

    //---------------------------------------------- HELPER METHODS ---------------------------------------------------
    /**
     * Checks if message with given id <code>messageId</code> exists in collection <code>allUserMessages</code>.
     *
     * @param messageId
     * @param allUserMessages
     */
    private void checkUserMessageExists(final Long messageId, Collection<Message> allUserMessages) {
        Assert.assertTrue(
                "Message [id=" + messageId + "] expected to be in collection [" + allUserMessages + "] is not there.",
                CollectionUtils.exists(allUserMessages, new Predicate() {
                    @Override
                    public boolean evaluate(Object object) {
                        return messageId.equals(((Message) object).getId());
                    }
                }));
    }

    private void testUserMessagePresent(Message message, User user) {
        UserMessage userMessage = userMessageService.getUserMessage(message,
                user);
        Assert.assertNotNull("A proper UserMessage wasn't created.",
                userMessage);

        Assert.assertEquals("User of the UserMessage is improperly set.",
                user, userMessage.getUser());
        Assert.assertEquals("Message of the UserMessage is improperly set.",
                message, userMessage.getMessage());

    }

    private void testUserMessageNull(Message message, User user) {
        UserMessage userMessage = userMessageService.getUserMessage(message,
                user);
        Assert.assertNull("The UserMessage shouldn't have been created.",
                userMessage);
    }

}

