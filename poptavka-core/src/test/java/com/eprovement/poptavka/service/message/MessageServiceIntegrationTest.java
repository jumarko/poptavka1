package com.eprovement.poptavka.service.message;

import com.eprovement.poptavka.base.integration.DBUnitIntegrationTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.dao.message.MessageFilter;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.enums.MessageState;
import com.eprovement.poptavka.domain.enums.MessageUserRoleType;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.message.MessageUserRole;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.exception.MessageException;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.offer.OfferService;
import com.eprovement.poptavka.service.usermessage.UserMessageService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collection;

import com.googlecode.genericdao.search.Search;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hamcrest.core.AnyOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Juraj Martinka
 *         Date: 11.5.11
 */
@DataSet(path = {
        "classpath:com/eprovement/poptavka/domain/register/RegisterDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/address/LocalityDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/RatingDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/UsersDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/DemandDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/message/MessageDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/offer/OfferDataSet.xml" },
        dtd = "classpath:test.dtd",
        disableForeignKeyChecks = true)
public class MessageServiceIntegrationTest extends DBUnitIntegrationTest {

    @Autowired
    private MessageService messageService;
    @Autowired
    private GeneralService generalService;
    @Autowired
    private UserMessageService userMessageService;
    @Autowired
    private OfferService offerService;

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

        Assert.assertEquals(7, messageThreads.size());
        checkUserMessageExists(1L, messageThreads);
        checkUserMessageExists(200L, messageThreads);
        checkUserMessageExists(300L, messageThreads);
        checkUserMessageExists(400L, messageThreads);
        checkUserMessageExists(500L, messageThreads);
        checkUserMessageExists(601L, messageThreads);
        checkUserMessageExists(701L, messageThreads);

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
        Assert.assertEquals(17, allUserMessages.size());
        checkUserMessageExists(1L, allUserMessages);
        checkUserMessageExists(2L, allUserMessages);
        checkUserMessageExists(3L, allUserMessages);
        checkUserMessageExists(4L, allUserMessages);
        checkUserMessageExists(200L, allUserMessages);
        checkUserMessageExists(300L, allUserMessages);
        checkUserMessageExists(301L, allUserMessages);
        checkUserMessageExists(400L, allUserMessages);
        checkUserMessageExists(401L, allUserMessages);
        checkUserMessageExists(402L, allUserMessages);
    }

    @Test
    public void testGetUserReceivedMessages() {
        final List<Message> allUserReceivedMessages = this.messageService.getAllMessages(
                this.user,
                MessageFilter.MessageFilterBuilder.messageFilter()
                        .withMessageUserRoleType(MessageUserRoleType.TO).build());
        Assert.assertEquals(12, allUserReceivedMessages.size());
        checkUserMessageExists(1L, allUserReceivedMessages);
        checkUserMessageExists(2L, allUserReceivedMessages);
        checkUserMessageExists(4L, allUserReceivedMessages);
        checkUserMessageExists(200L, allUserReceivedMessages);
        checkUserMessageExists(300L, allUserReceivedMessages);
        checkUserMessageExists(400L, allUserReceivedMessages);
        checkUserMessageExists(402L, allUserReceivedMessages);
        checkUserMessageExists(500L, allUserReceivedMessages);
        checkUserMessageExists(601L, allUserReceivedMessages);
        checkUserMessageExists(701L, allUserReceivedMessages);
        checkUserMessageExists(704L, allUserReceivedMessages);
        checkUserMessageExists(702L, allUserReceivedMessages);
    }

    @Test
    public void testGetUserSentMessages() {
        final List<Message> allUserReceivedMessages = this.messageService.getAllMessages(
                this.user,
                MessageFilter.MessageFilterBuilder.messageFilter()
                        .withMessageUserRoleType(MessageUserRoleType.SENDER).build());
        Assert.assertEquals(5, allUserReceivedMessages.size());
        checkUserMessageExists(3L, allUserReceivedMessages);
        checkUserMessageExists(301L, allUserReceivedMessages);
        checkUserMessageExists(401L, allUserReceivedMessages);
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

        // the same test but returns UserMessage instead of Messages
        // -> search definition is empty since we don't want to apply any further search criteria
        final List<UserMessage> potentialDemandConversationUserMessages =
                this.messageService.getConversationUserMessages(threadRoot, supplier, null);

        Assert.assertEquals(4, potentialDemandConversationUserMessages.size());
        // check if all expected messages are in conversation
        checkUserMessageExistsUserMessage(1L, potentialDemandConversationUserMessages);
        checkUserMessageExistsUserMessage(3L, potentialDemandConversationUserMessages);
        checkUserMessageExistsUserMessage(5L, potentialDemandConversationUserMessages);
        checkUserMessageExistsUserMessage(7L, potentialDemandConversationUserMessages);
    }

    @Test
    public void testGetConversationUserMessageBySearchDefinition() throws Exception {
        final Message threadRoot = this.messageService.getById(1L);
        final User supplier = this.generalService.find(User.class, 111111112L);

        // sort from the most recent one
        final Search searchDefinition = new Search(UserMessage.class);
        searchDefinition.addSort("message.created", true);

        final List<UserMessage> potentialDemandConversationUserMessages =
                this.messageService.getConversationUserMessages(threadRoot, supplier, searchDefinition);

        Assert.assertEquals(4, potentialDemandConversationUserMessages.size());
        // check if all expected messages are in conversation
        // anyOf matcher is used because messages 2, 4 and 1, 3 have the same created date
        // and their order is not fully predictable
        assertThat("Unxpected UserMessage by order specified in Search definition",
                potentialDemandConversationUserMessages.get(0).getMessage().getId(), AnyOf.anyOf(is(2L), is(4L)));
        assertThat("Unxpected UserMessage by order specified in Search definition",
                potentialDemandConversationUserMessages.get(1).getMessage().getId(), AnyOf.anyOf(is(2L), is(4L)));
        assertThat("Unxpected UserMessage by order specified in Search definition",
                potentialDemandConversationUserMessages.get(2).getMessage().getId(), AnyOf.anyOf(is(1L), is(3L)));
        assertThat("Unxpected UserMessage by order specified in Search definition",
                potentialDemandConversationUserMessages.get(3).getMessage().getId(), AnyOf.anyOf(is(1L), is(3L)));
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
        long messageId = messageService.newThreadRoot(user).getMessage().getId();
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

        Message reply = messageService.newReply(message, user2).getMessage();

        testUserMessagePresent(reply, user2);
        testUserMessageNull(reply, user);

        Assert.assertEquals("MessageState of a newly creted reply"
                + " should be COMPOSED, but was" + reply.getMessageState()
                + ".", MessageState.COMPOSED, reply.getMessageState());

        Assert.assertEquals("Reply subject incorrect.",
                "Re: Hello", reply.getSubject());

        Assert.assertEquals("Incorrect sender set to reply.",
                user2, reply.getSender());

        reply.setBody("Message body");
        messageService.send(reply);

        Assert.assertEquals("MessageState of a reply after sending"
                + " should be SENT, but was" + reply.getMessageState()
                + ".", MessageState.SENT, reply.getMessageState());

        testUserMessagePresent(reply, user);

    }


    @Test
    public void testGetThreadRootMessage() throws MessageException {

        final Demand demand1 = this.generalService.find(Demand.class, 1L);
        Message message1 = messageService.getThreadRootMessage(demand1);
        Assert.assertNull(message1);

        final Demand demand2 = this.generalService.find(Demand.class, 9L);
        Message message2 = messageService.getThreadRootMessage(demand2);
        Assert.assertNotNull(message2);
        Assert.assertEquals("Expected thread root message for demnand [id=" + demand2.getId() + "]"
                + message2.getId().longValue(), 550L, message2.getId().longValue());
    }

    @Test
    public void testGetOffersCountForSupplier() {
        long supplierId = 111111112L;
        long count = offerService.getPendingOffersCountForSupplier(supplierId);
        Assert.assertEquals("Expected count of pending offers [count=" + count
                + "]for supplier was different", 2L, count);

        long supplierId2 = 111111142L;
        long count2 = offerService.getPendingOffersCountForSupplier(supplierId2);
        Assert.assertEquals("Expected count of pending offers [count=" + count2
                + "]for supplier was different", 2L, count2);

        long count3 = offerService.getAcceptedOffersCountForSupplier(supplierId);
        Assert.assertEquals("Expected count of accepted offers [count=" + count3
                + "]for supplier was different", 0L, count3);

        long count4 = offerService.getAcceptedOffersCountForSupplier(supplierId2);
        Assert.assertEquals("Expected count of accepted offers [count=" + count4
                + "]for supplier was different", 0L, count4);

    }

    @Test
    public void testGetSupplierConversationsWithClosedDemands() {
        BusinessUser userWithClosedDemands = new BusinessUser();
        userWithClosedDemands.setId(111111111L);
        Map<UserMessage, Integer> closedDemandsMap = userMessageService.getSupplierConversationsWithClosedDemands(
                userWithClosedDemands);
        long count = closedDemandsMap.size();
        Assert.assertEquals("Expected count of different conversations with closed demands [count=" + count
                + "] for user was different", 1L, count);

        for (Map.Entry<UserMessage, Integer> entryKey : closedDemandsMap.entrySet()) {
            UserMessage latestUserMessage = entryKey.getKey();
            int countOfSubmessages = entryKey.getValue().intValue();
            Assert.assertEquals("Expected latestUserMessage id [id=" + latestUserMessage.getId() + "] "
                    + " for conversation with closed demands for user was different than expected",
                    603L, latestUserMessage.getId().longValue());
            Assert.assertEquals("Expected sumbessages count [count=" + countOfSubmessages
                + "] for latestUserMessage id [id=" + latestUserMessage.getId() + "] "
                    + " in conversation for closed demands for user was different than expected",
                    1L, countOfSubmessages);
        }
    }

    @Test
    public void testGetAdminDemandsByStatus() {

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

    /**
     * Checks if message with given id <code>messageId</code> exists in collection <code>allUserMessages</code>.
     *
     * @param messageId
     * @param allUserMessages
     */
    private void checkUserMessageExistsUserMessage(final Long userMessageId, Collection<UserMessage> allUserMessages) {
        Assert.assertTrue(
                "UserMessage [id=" + userMessageId + "] expected to be in collection ["
                    + allUserMessages + "] is not there.",
                CollectionUtils.exists(allUserMessages, new Predicate() {
                    @Override
                    public boolean evaluate(Object object) {
                        return userMessageId.equals(((UserMessage) object).getId());
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

