package cz.poptavka.sample.service.message;

import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import cz.poptavka.sample.dao.message.MessageFilter;
import cz.poptavka.sample.domain.message.Message;
import cz.poptavka.sample.domain.message.MessageState;
import cz.poptavka.sample.domain.message.UserMessage;
import cz.poptavka.sample.domain.message.UserMessageRoleType;
import cz.poptavka.sample.domain.user.User;
import cz.poptavka.sample.exception.MessageException;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.service.user.SupplierService;
import cz.poptavka.sample.service.usermessage.UserMessageService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import static org.hamcrest.core.Is.is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Juraj Martinka
 *         Date: 11.5.11
 */
@DataSet(path = {
        "classpath:cz/poptavka/sample/domain/demand/RatingDataSet.xml",
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
    @Autowired
    private SupplierService supplierService;

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

        Assert.assertEquals(5, messageThreads.size());
        checkMessageExists(1L, messageThreads);
        checkMessageExists(200L, messageThreads);
        checkMessageExists(300L, messageThreads);
        checkMessageExists(400L, messageThreads);
        checkMessageExists(501L, messageThreads);

        // one reply to the thread root message
        Assert.assertEquals(1, messageThreads.get(0).getChildren().size());
        checkMessageExists(3L, messageThreads.get(0).getChildren());

        // two replies to the reply to the thread root message
        Assert.assertEquals(2, messageThreads.get(0).getChildren().get(0).getChildren().size());
        checkMessageExists(2L, messageThreads.get(0).getChildren().get(0).getChildren());
        checkMessageExists(4L, messageThreads.get(0).getChildren().get(0).getChildren());
    }


    @Test
    public void testGetAllUserMessages() {
        final List<Message> allUserMessages = this.messageService.getAllMessages(this.user, MessageFilter.EMPTY_FILTER);
        Assert.assertEquals(11, allUserMessages.size());
        checkMessageExists(1L, allUserMessages);
        checkMessageExists(2L, allUserMessages);
        checkMessageExists(3L, allUserMessages);
        checkMessageExists(4L, allUserMessages);
        checkMessageExists(200L, allUserMessages);
        checkMessageExists(300L, allUserMessages);
        checkMessageExists(301L, allUserMessages);
        checkMessageExists(400L, allUserMessages);
        checkMessageExists(401L, allUserMessages);
        checkMessageExists(402L, allUserMessages);
        checkMessageExists(501L, allUserMessages);
    }

    @Test
    public void testGetUserReceivedMessages() {
        final List<Message> allUserReceivedMessages = this.messageService.getAllMessages(
                this.user,
                MessageFilter.MessageFilterBuilder.messageFilter()
                        .withUserMessageRoleType(UserMessageRoleType.TO).build());
        Assert.assertEquals(9, allUserReceivedMessages.size());
        checkMessageExists(1L, allUserReceivedMessages);
        checkMessageExists(2L, allUserReceivedMessages);
        checkMessageExists(3L, allUserReceivedMessages);
        checkMessageExists(4L, allUserReceivedMessages);
        checkMessageExists(200L, allUserReceivedMessages);
        checkMessageExists(300L, allUserReceivedMessages);
        checkMessageExists(301L, allUserReceivedMessages);
        checkMessageExists(400L, allUserReceivedMessages);
        checkMessageExists(501L, allUserReceivedMessages);
    }

    @Test
    public void testGetUserSentMessages() {
        final List<Message> allUserReceivedMessages = this.messageService.getAllMessages(
                this.user,
                MessageFilter.MessageFilterBuilder.messageFilter()
                        .withUserMessageRoleType(UserMessageRoleType.SENDER).build());
        Assert.assertEquals(3, allUserReceivedMessages.size());
        checkMessageExists(3L, allUserReceivedMessages);
        checkMessageExists(301L, allUserReceivedMessages);
        checkMessageExists(401L, allUserReceivedMessages);
    }


    @Test
    public void testGetPotentialDemandConversation() {
        final Message threadRoot = this.messageService.getById(1L);
        final User supplier = this.generalService.find(User.class, 111111112L);
        final List<Message> potentialDemandConversation =
                this.messageService.getPotentialDemandConversation(threadRoot, supplier);

        Assert.assertEquals(4, potentialDemandConversation.size());
        // check if all expected messages are in conversation
        checkMessageExists(1L, potentialDemandConversation);
        checkMessageExists(2L, potentialDemandConversation);
        checkMessageExists(3L, potentialDemandConversation);
        checkMessageExists(4L, potentialDemandConversation);
    }

    @Test
    public void testGetListOfClientDemandMessagesAll() {
        final Message threadRoot1 = this.messageService.getById(1L);
        final Message threadRoot200 = this.messageService.getById(200L);
        final Message threadRoot300 = this.messageService.getById(300L);
        final User client = this.generalService.find(User.class, 111111112L);
        final Map<Message, Integer> listOfClientDemandMessages =
                this.messageService.getListOfClientDemandMessagesAll(client);
        Assert.assertEquals("Inacurrate number of threadRoot messages selected",
                3, listOfClientDemandMessages.size());

        checkMessageExists(threadRoot1.getId(), listOfClientDemandMessages.keySet());
        Assert.assertEquals("Inacurrate number of subMessages selected",
                (Object) 4, (Object) listOfClientDemandMessages.get(threadRoot1));
        checkMessageExists(threadRoot200.getId(), listOfClientDemandMessages.keySet());
        Assert.assertEquals("Inacurrate number of subMessages selected",
                (Object) 1, (Object) listOfClientDemandMessages.get(threadRoot200));
        checkMessageExists(threadRoot300.getId(), listOfClientDemandMessages.keySet());
        Assert.assertEquals("Inacurrate number of subMessages selected",
                (Object) 2, (Object) listOfClientDemandMessages.get(threadRoot300));
    }

    @Test
    public void testGetListOfClientDemandMessagesUnread() {
        final Message threadRoot1 = this.messageService.getById(1L);
        final Message threadRoot200 = this.messageService.getById(200L);
        final Message threadRoot300 = this.messageService.getById(300L);
        final User client = this.generalService.find(User.class, 111111112L);
        final Map<Message, Integer> listOfClientDemandMessages =
                this.messageService.getListOfClientDemandMessagesUnread(client);
        Assert.assertEquals("Inacurrate number of threadRoot messages selected",
                3, listOfClientDemandMessages.size());

        checkMessageExists(threadRoot1.getId(), listOfClientDemandMessages.keySet());
        Assert.assertEquals("Inacurrate number of unread subMessages selected",
                (Object) 1, (Object) listOfClientDemandMessages.get(threadRoot1));
        checkMessageExists(threadRoot200.getId(), listOfClientDemandMessages.keySet());
        Assert.assertEquals("Inacurrate number of unread subMessages selected",
                (Object) 1, (Object) listOfClientDemandMessages.get(threadRoot200));
        checkMessageExists(threadRoot300.getId(), listOfClientDemandMessages.keySet());
        Assert.assertEquals("Inacurrate number of unread subMessages selected",
                (Object) 0, (Object) listOfClientDemandMessages.get(threadRoot300));
    }


    @Test
    public void testGetDescendants() {
        Message threadRoot = this.messageService.getById(1L);
        List<Message> descendants =
                this.messageService.getAllDescendants(threadRoot);
        Assert.assertEquals("Inacurrate number of descendants selected",
                3, descendants.size());

        checkMessageExists(2L, descendants);
        checkMessageExists(3L, descendants);
        checkMessageExists(4L, descendants);

        threadRoot = this.messageService.getById(5L);
        descendants =
                this.messageService.getAllDescendants(threadRoot);
        Assert.assertEquals("Inacurrate number of descendants selected",
                1, descendants.size());

        checkMessageExists(6L, descendants);
    }

    @Test
    public void testGetDescendantsCount() {
        List<Message> threadRoot = new ArrayList();
        threadRoot.add(this.messageService.getById(1L));
        int descendantsCount =
                this.messageService.getAllDescendantsCount(threadRoot);
        Assert.assertEquals("Inacurrate number of descendants selected",
                3, descendantsCount);

        threadRoot.clear();
        threadRoot.add(this.messageService.getById(5L));

        descendantsCount =
                this.messageService.getAllDescendantsCount(threadRoot);
        Assert.assertEquals("Inacurrate number of descendants selected",
                1, descendantsCount);
    }

    @Test
    public void testGetDescendantsForUserCount() {
        final User user = this.generalService.find(User.class, 111111111L);
        final User user2 = this.generalService.find(User.class, 111111112L);
        final User user3 = this.generalService.find(User.class, 111111113L);
        List<Message> threadRoot = new ArrayList();
        threadRoot.add(this.messageService.getById(1L));
        int descendantsCount =
                this.messageService.getAllDescendantsCount(threadRoot, user);
        Assert.assertEquals("Incorrect number of descendants selected",
                3, descendantsCount);
        descendantsCount =
                this.messageService.getAllDescendantsCount(threadRoot, user2);
        Assert.assertEquals("Incorrect number of descendants selected",
                3, descendantsCount);
        descendantsCount =
                this.messageService.getAllDescendantsCount(threadRoot, user3);
        Assert.assertEquals("Incorrect number of descendants selected",
                0, descendantsCount);

        threadRoot.clear();
        threadRoot.add(this.messageService.getById(400L));
        descendantsCount =
                this.messageService.getAllDescendantsCount(threadRoot, user);
        Assert.assertEquals("Incorrect number of descendants selected",
                2, descendantsCount);
        descendantsCount =
                this.messageService.getAllDescendantsCount(threadRoot, user2);
        Assert.assertEquals("Incorrect number of descendants selected",
                2, descendantsCount);
        descendantsCount =
                this.messageService.getAllDescendantsCount(threadRoot, user3);
        Assert.assertEquals("Incorrect number of descendants selected",
                1, descendantsCount);

    }

    @Test
    public void testGetUnreadDescendantsCount() {
        final User user = this.generalService.find(User.class, 111111111L);
        final User user2 = this.generalService.find(User.class, 111111112L);
        final User user3 = this.generalService.find(User.class, 111111113L);
        List<Message> threadRoot = new ArrayList();
        threadRoot.add(this.messageService.getById(1L));
        int descendantsCount =
                this.messageService.getUnreadDescendantsCount(threadRoot, user);
        Assert.assertEquals("Incorrect number of descendants selected",
                2, descendantsCount);
        descendantsCount =
                this.messageService.getUnreadDescendantsCount(threadRoot, user2);
        Assert.assertEquals("Incorrect number of descendants selected",
                1, descendantsCount);
        descendantsCount =
                this.messageService.getUnreadDescendantsCount(threadRoot, user3);
        Assert.assertEquals("Incorrect number of descendants selected",
                0, descendantsCount);

        threadRoot.clear();
        threadRoot.add(this.messageService.getById(400L));
        descendantsCount =
                this.messageService.getUnreadDescendantsCount(threadRoot, user);
        Assert.assertEquals("Incorrect number of descendants selected",
                0, descendantsCount);
        descendantsCount =
                this.messageService.getUnreadDescendantsCount(threadRoot, user2);
        Assert.assertEquals("Incorrect number of descendants selected",
                0, descendantsCount);
        descendantsCount =
                this.messageService.getUnreadDescendantsCount(threadRoot, user3);
        Assert.assertEquals("Incorrect number of descendants selected",
                1, descendantsCount);

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

        testUserMessagePresent(message, user, UserMessageRoleType.SENDER);
        testUserMessageNull(message, user2);
        message.setSubject("Hello");
        message.setBody("Chtěl bych zadat poptávku na sexuální služby,"
                + " ale nevím jak se to dělá.");
        final UserMessage userMessage = new UserMessage();
        userMessage.setMessage(message);
        userMessage.setUser(user2);
        userMessage.setRoleType(UserMessageRoleType.TO);
        final List<UserMessage> userMessages = new ArrayList<UserMessage>();
        userMessages.add(userMessage);
        message.setUserMessages(userMessages);
        messageService.send(message);
        Assert.assertEquals("MessageState of a message after sending"
                + " should be SENT, but was" + message.getMessageState()
                + ".", MessageState.SENT, message.getMessageState());
        testUserMessagePresent(message, user2, UserMessageRoleType.TO);

        Message reply = messageService.newReply(message, user2);

        testUserMessagePresent(reply, user2, UserMessageRoleType.SENDER);
        testUserMessagePresent(reply, user, UserMessageRoleType.TO);

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
    }

    //---------------------------------------------- HELPER METHODS ---------------------------------------------------
    /**
     * Checks if message with given id <code>messageId</code> exists in collection <code>allUserMessages</code>.
     *
     * @param messageId
     * @param allMessages
     */
    private void checkMessageExists(final Long messageId, Collection<Message> allMessages) {
        Assert.assertTrue(
                "Message [id=" + messageId + "] expected to be in collection [" + allMessages + "] is not there.",
                CollectionUtils.exists(allMessages, new Predicate() {
                    @Override
                    public boolean evaluate(Object object) {
                        return messageId.equals(((Message) object).getId());
                    }
                }));
    }

    private void testUserMessagePresent(Message message, User user, UserMessageRoleType roleType) {
        UserMessage userMessage = userMessageService.getUserMessage(message,
                user);
        Assert.assertNotNull("A proper UserMessage wasn't created.",
                userMessage);

        Assert.assertEquals("User of the UserMessage is improperly set.",
                user, userMessage.getUser());
        Assert.assertThat("Message of the UserMessage is improperly set.",
                message, is(userMessage.getMessage()));
        Assert.assertThat("Incorrect roleType", userMessage.getRoleType(), is(roleType));

    }

    private void testUserMessageNull(Message message, User user) {
        UserMessage userMessage = userMessageService.getUserMessage(message,
                user);
        Assert.assertNull("The UserMessage shouldn't have been created.",
                userMessage);
    }

}

