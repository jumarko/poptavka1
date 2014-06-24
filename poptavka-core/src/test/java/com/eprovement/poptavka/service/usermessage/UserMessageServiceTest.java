package com.eprovement.poptavka.service.usermessage;

import com.eprovement.poptavka.base.integration.DBUnitIntegrationTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.message.ClientConversation;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.message.MessageService;
import com.googlecode.genericdao.search.Search;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Vojtech Hubr
 *         Date: 29.3.12
 */
@DataSet(path = {
        "classpath:com/eprovement/poptavka/domain/address/LocalityDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/RatingDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/UsersDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/DemandDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/message/MessageDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/offer/OfferDataSet.xml" },
        dtd = "classpath:test.dtd",
        disableForeignKeyChecks = true)
public class UserMessageServiceTest extends DBUnitIntegrationTest {

    @Autowired
    private MessageService messageService;
    @Autowired
    private GeneralService generalService;
    @Autowired
    private UserMessageService userMessageService;

    private User user;
    private BusinessUser businessUser;
    private User user2;
    private BusinessUser businessUser2;
    private BusinessUser businessUser4;
    private BusinessUser businessUserClient;
    private Demand demand2;
    private User user5;
    private BusinessUser businessUser5;


    @Before
    public void setUp() {
        this.user = new User();
        user.setId(111111111L);
        this.businessUser = new BusinessUser();
        businessUser.setId(111111111L);
        // business/user2 has a supplier role to test getPotentialDemands
        this.user2 = new User();
        user2.setId(111111114L);
        this.businessUser2 = new BusinessUser();
        businessUser2.setId(111111114L);
        this.businessUser4 = new BusinessUser();
        businessUser4.setId(111111114L);
        this.businessUserClient = new BusinessUser();
        businessUserClient.setId(111111112L);
        this.demand2 = new Demand();
        demand2.setId(2L);
        // initialize user5 who has roles Supplier (id=1111111115) and Client(id=1111111115)
        this.user5 = new User();
        user5.setId(111111115L);
        this.businessUser5 = new BusinessUser();
        businessUser5.setId(111111115L);
    }


    @Test
    public void testGetInbox() {

        final List<UserMessage> inbox = this.userMessageService.getInbox(
                this.user);

        Assert.assertEquals(12, inbox.size());
        checkUserMessageExists(2L, inbox);
        checkUserMessageExists(4L, inbox);
        checkUserMessageExists(8L, inbox);
        checkUserMessageExists(202L, inbox);
        checkUserMessageExists(302L, inbox);
        checkUserMessageExists(402L, inbox);
        checkUserMessageExists(407L, inbox);
        checkUserMessageExists(501L, inbox);
        checkUserMessageExists(601L, inbox);
        checkUserMessageExists(702L, inbox);
        checkUserMessageExists(705L, inbox);
        checkUserMessageExists(709L, inbox);

    }

    @Test
    public void testGetSentItems() {
        final List<UserMessage> inbox = this.userMessageService.getInbox(
                this.user);

        Assert.assertEquals(12, inbox.size());
        checkUserMessageExists(2L, inbox);
        checkUserMessageExists(4L, inbox);
        checkUserMessageExists(8L, inbox);
        checkUserMessageExists(202L, inbox);
        checkUserMessageExists(302L, inbox);
        checkUserMessageExists(402L, inbox);
        checkUserMessageExists(407L, inbox);
        checkUserMessageDoesntExists(500L, inbox);
        checkUserMessageExists(501L, inbox);
        checkUserMessageDoesntExists(502L, inbox);
        checkUserMessageExists(601L, inbox);
        checkUserMessageDoesntExists(602L, inbox);
        checkUserMessageDoesntExists(603L, inbox);
        checkUserMessageDoesntExists(604L, inbox);
        checkUserMessageExists(702L, inbox);
        checkUserMessageExists(705L, inbox);
        checkUserMessageExists(709L, inbox);
    }

    @Test
    public void testGetPotentialDemands() {

        // sentItems test
        final List<UserMessage> sentItems = this.userMessageService
                .getSentItems(this.user);

        Assert.assertEquals(5, sentItems.size());
        checkUserMessageExists(6L, sentItems);
        checkUserMessageExists(304L, sentItems);
        checkUserMessageExists(404L, sentItems);
        checkUserMessageExists(603L, sentItems);
        checkUserMessageExists(707L, sentItems);

        // test for SUPPLIER - BUSINESSS_USER="111111111"
        final List<UserMessage> potentialDemands = this.userMessageService.getPotentialDemands(businessUser);
        Assert.assertEquals(3, potentialDemands.size());

        // check THREADROOT_ID="1" with DEMAND_ID="2"
        checkUserMessageDoesntExists(1L, potentialDemands);
        checkUserMessageDoesntExists(2L, potentialDemands);
        checkUserMessageDoesntExists(3L, potentialDemands);
        checkUserMessageDoesntExists(4L, potentialDemands);
        checkUserMessageDoesntExists(5L, potentialDemands);
        checkUserMessageDoesntExists(6L, potentialDemands);
        checkUserMessageDoesntExists(7L, potentialDemands);
        checkUserMessageExists(8L, potentialDemands);

        // check THREADROOT_ID="200" with DEMAND_ID="21"
        checkUserMessageDoesntExists(201L, potentialDemands);
        checkUserMessageExists(202L, potentialDemands);

        // check THREADROOT_ID="300" with DEMAND_ID="22"
        checkUserMessageDoesntExists(301L, potentialDemands);
        checkUserMessageDoesntExists(302L, potentialDemands);
        checkUserMessageDoesntExists(303L, potentialDemands);
        checkUserMessageDoesntExists(304L, potentialDemands);

        // check THREADROOT_ID="400" with DEMAND_ID="[NULL]"
        checkUserMessageDoesntExists(400L, potentialDemands);
        checkUserMessageDoesntExists(407L, potentialDemands);
        checkUserMessageDoesntExists(408L, potentialDemands);

        // check THREADROOT_ID="500" with DEMAND_ID="10"
        checkUserMessageDoesntExists(500L, potentialDemands);
        checkUserMessageDoesntExists(502L, potentialDemands);
        checkUserMessageExists(501L, potentialDemands);

        // check THREADROOT_ID="550" with DEMAND_ID="9"
        checkUserMessageDoesntExists(550L, potentialDemands);
        checkUserMessageDoesntExists(551L, potentialDemands);

        // check THREADROOT_ID="601" with DEMAND_ID="23"
        checkUserMessageDoesntExists(601L, potentialDemands);
        checkUserMessageDoesntExists(602L, potentialDemands);
        checkUserMessageDoesntExists(603L, potentialDemands);
        checkUserMessageDoesntExists(604L, potentialDemands);

        // check THREADROOT_ID="701" with DEMAND_ID="70"
        checkUserMessageDoesntExists(701L, potentialDemands);
        checkUserMessageDoesntExists(702L, potentialDemands);
        checkUserMessageDoesntExists(703L, potentialDemands);
        checkUserMessageDoesntExists(704L, potentialDemands);
        checkUserMessageDoesntExists(705L, potentialDemands);
        checkUserMessageDoesntExists(706L, potentialDemands);
        checkUserMessageDoesntExists(707L, potentialDemands);
        checkUserMessageDoesntExists(708L, potentialDemands);
        checkUserMessageDoesntExists(709L, potentialDemands);
        checkUserMessageDoesntExists(710L, potentialDemands);
        checkUserMessageDoesntExists(711L, potentialDemands);
        checkUserMessageDoesntExists(712L, potentialDemands);


        // test for SUPPLIER - BUSINESSS_USER="111111114"
        final List<UserMessage> potentialDemands2 = this.userMessageService.getPotentialDemands(businessUser4);
        Assert.assertEquals(2, potentialDemands2.size());

        // check THREADROOT_ID="1" with DEMAND_ID="2"
        checkUserMessageDoesntExists(7L, potentialDemands2);
        checkUserMessageDoesntExists(8L, potentialDemands2);

        // check THREADROOT_ID="200" with DEMAND_ID="21"
        checkUserMessageDoesntExists(201L, potentialDemands2);
        checkUserMessageDoesntExists(202L, potentialDemands2);

        // check THREADROOT_ID="300" with DEMAND_ID="22"
        checkUserMessageDoesntExists(303L, potentialDemands2);
        checkUserMessageDoesntExists(304L, potentialDemands2);

        // check THREADROOT_ID="400" with DEMAND_ID="[NULL]"
        checkUserMessageDoesntExists(407L, potentialDemands2);
        checkUserMessageDoesntExists(408L, potentialDemands2);

        // check THREADROOT_ID="500" with DEMAND_ID="10"
        checkUserMessageDoesntExists(500L, potentialDemands2);
        checkUserMessageDoesntExists(501L, potentialDemands2);
        checkUserMessageExists(502L, potentialDemands2);

        // check THREADROOT_ID="550" with DEMAND_ID="9"
        checkUserMessageDoesntExists(550L, potentialDemands2);
        checkUserMessageExists(551L, potentialDemands2);

        // check THREADROOT_ID="601" with DEMAND_ID="23"
        checkUserMessageDoesntExists(604L, potentialDemands2);

        // check THREADROOT_ID="701" with DEMAND_ID="70"
        checkUserMessageDoesntExists(701L, potentialDemands2);
        checkUserMessageDoesntExists(702L, potentialDemands2);
        checkUserMessageDoesntExists(703L, potentialDemands2);
        checkUserMessageDoesntExists(704L, potentialDemands2);
        checkUserMessageDoesntExists(705L, potentialDemands2);
        checkUserMessageDoesntExists(706L, potentialDemands2);
        checkUserMessageDoesntExists(707L, potentialDemands2);
        checkUserMessageDoesntExists(708L, potentialDemands2);
        checkUserMessageDoesntExists(709L, potentialDemands2);
        checkUserMessageDoesntExists(710L, potentialDemands2);
        checkUserMessageDoesntExists(711L, potentialDemands2);
        checkUserMessageDoesntExists(712L, potentialDemands2);


        // test for SUPPLIER - BUSINESSS_USER="1111111115"
        final List<UserMessage> potentialDemands5 = this.userMessageService.getPotentialDemands(businessUser5);
        final int potentialDemands5count = this.userMessageService.getPotentialDemandsCount(businessUser5);
//        Assert.assertEquals(1, potentialDemands5count);

        // check THREADROOT_ID="701" with DEMAND_ID="70"
        checkUserMessageDoesntExists(701L, potentialDemands5);
        checkUserMessageDoesntExists(702L, potentialDemands5);
        checkUserMessageDoesntExists(703L, potentialDemands5);
        checkUserMessageDoesntExists(704L, potentialDemands5);
        checkUserMessageDoesntExists(705L, potentialDemands5);
        checkUserMessageDoesntExists(706L, potentialDemands5);
        checkUserMessageDoesntExists(707L, potentialDemands5);
        checkUserMessageDoesntExists(708L, potentialDemands5);
        checkUserMessageDoesntExists(709L, potentialDemands5);
        checkUserMessageDoesntExists(710L, potentialDemands5);
        checkUserMessageDoesntExists(711L, potentialDemands5);
        // TODO RELEASE: this test should be working but it is not. The 712 UserMessage should be inside of collection
        checkUserMessageExists(712L, potentialDemands5);

    }

    @Test
    public void testGetPotentialDemandsCount() {

        final long potentialDemandsCount = this.userMessageService
                .getPotentialDemandsCount(this.businessUser);
        Assert.assertEquals(3L, potentialDemandsCount);
        // test for businessUser2
        final long potentialDemandsCount2 = this.userMessageService
                .getPotentialDemandsCount(this.businessUser4);
        Assert.assertEquals(2L, potentialDemandsCount2);
    }

    @Test
    public void testSetMessageReadStatus() {
        final UserMessage unreadUserMessage = this.generalService.find(UserMessage.class, 4L);
        unreadUserMessage.setRead(true);
        this.generalService.save(unreadUserMessage);
    }

    @Test
    public void testGetSupplierConversationsWithoutOffer() {
        final Map<UserMessage, Integer> supplierConversations = this.userMessageService
                .getSupplierConversationsWithoutOffer(this.businessUser);
        Assert.assertEquals(3, supplierConversations.size());
        checkUserMessageIdAndCount(8L, 4, supplierConversations);
        checkUserMessageIdAndCount(202L, 1, supplierConversations);
        checkUserMessageIdAndCount(501L, 1, supplierConversations);
    }

    @Test
    public void testGetSupplierConversationsWithOffer() {
        final Map<UserMessage, Integer> supplierConversations = this.userMessageService
                   .getSupplierConversationsWithOffer(this.businessUser);
        Assert.assertEquals(2, supplierConversations.size());
        checkUserMessageIdAndCount(304L, 1, supplierConversations);
        checkUserMessageIdAndCount(709L, 3, supplierConversations);
    }

    @Test
    public void testGetSupplierConversationsWithoutOfferCount() {
        final int supplierConversationsCount = this.userMessageService
                .getSupplierConversationsWithoutOfferCount(this.businessUser);
        Assert.assertEquals(3, supplierConversationsCount);
    }

    @Test
    public void testGetSupplierConversationsWithOfferCount() {
        final int supplierConversationsCount = this.userMessageService
                .getSupplierConversationsWithOfferCount(this.businessUser);
        Assert.assertEquals(3, supplierConversationsCount);
    }

    @Test
    public void testGetClientConversationsWithoutOffer() {
        final Map<UserMessage, ClientConversation> clientConversations = this.userMessageService
                .getClientConversationsWithoutOffer(this.businessUserClient, demand2);
        Assert.assertEquals(2, clientConversations.size());
        checkUserMessageIdAndCountAndSupplierId(7L, 3, 111111111L, clientConversations);

        final Demand demand21 = generalService.find(Demand.class, 21L);
        final Map<UserMessage, ClientConversation> clientConversations2 = this.userMessageService
                .getClientConversationsWithoutOffer(this.businessUserClient, demand21);
        Assert.assertEquals(0, clientConversations2.size());
    }

    @Test
    public void testGetClientConversationsWithoutOfferCount() {
        final int clientConversationsCount = this.userMessageService
                .getClientConversationsWithoutOfferCount(this.businessUserClient, demand2);
        Assert.assertEquals("The count of client's [id="
                + this.businessUserClient.getId() + "] and demand's [id=" + demand2.getId()
                + "] conversations without offer is"
                + " incorrect.", 2, clientConversationsCount);
    }

    @Test
    public void testGetClientConversationsWithOfferCount() {
        Demand demand22 = this.generalService.find(Demand.class, 22L);
        final int clientConversationsCount = this.userMessageService
                .getClientConversationsWithOfferCount(this.businessUserClient, demand22);
        Assert.assertEquals("The count of client's (id="
                + this.businessUserClient.getId() + "conversations with offer is"
                + " incorrect.", 1, clientConversationsCount);
    }

    @Test
    public void testGetClientConversationsWithAcceptedOffer() {
        Search search = new Search(UserMessage.class);
        final Map<UserMessage, ClientConversation> clientConversations = this.userMessageService
                .getClientConversationsWithAcceptedOffer(this.businessUserClient, search);
        Assert.assertEquals(0, clientConversations.size());
    }

    @Test
    public void testGetClientConversationsWithClosedOffer() {
        Search search = new Search(UserMessage.class);
        final Map<UserMessage, ClientConversation> clientConversations = this.userMessageService
                .getClientConversationsWithClosedOffer(this.businessUserClient, search);
        Assert.assertEquals(1, clientConversations.size());
        checkUserMessageIdAndCountAndSupplierId(604L, 1, 111111111L, clientConversations);
    }

    @Test
    public void testGetConversation() {
        Message rootMessage = this.generalService.find(Message.class, 1L);
        List<UserMessage> conversation = this.userMessageService
                .getConversation(this.businessUserClient, this.businessUser, rootMessage);
        Assert.assertEquals("Number of userMessages in the conversation between users of id="
                + this.businessUserClient.getId() + " and id=" + this.businessUser.getId()
                + " spanning from rootMessage id=" + rootMessage.getId() + " is "
                + " incorrect.", 4, conversation.size());
        checkUserMessageExists(1L, conversation);
        checkUserMessageDoesntExists(2L, conversation);
        checkUserMessageExists(3L, conversation);
        checkUserMessageDoesntExists(4L, conversation);
        checkUserMessageExists(5L, conversation);
        checkUserMessageDoesntExists(6L, conversation);
        checkUserMessageExists(7L, conversation);
        checkUserMessageDoesntExists(8L, conversation);
        conversation = this.userMessageService
                .getConversation(this.businessUserClient, this.businessUser4, rootMessage);
        Assert.assertEquals("Number of userMessages in the conversation between users of id="
                + this.businessUserClient.getId() + " and id=" + this.businessUser.getId()
                + " spanning from rootMessage id=" + rootMessage.getId() + " is "
                + " incorrect.", 0, conversation.size());
    }

    //---------------------------------------------- HELPER METHODS ---------------------------------------------------
    /**
     * Checks if message with given id <code>messageId</code> exists in collection <code>allUserMessages</code>.
     *
     * @param messageId
     * @param allUserMessages
     */
    private void checkMessageExists(final Long messageId, Collection<Message> allUserMessages) {
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
    private void checkUserMessageExists(final Long userMessageId, Collection<UserMessage> allUserMessages) {
        Assert.assertTrue(
                "UserMessage [id=" + userMessageId + "] expected to be in"
                + " collection [" + allUserMessages + "] is not there.",
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

    /**
     * Checks if message with given id <code>messageId</code> exists in collection <code>allUserMessages</code>.
     *
     * @param messageId
     * @param allUserMessages
     */
    private void checkUserMessageDoesntExists(final Long userMessageId, Collection<UserMessage> allUserMessages) {
        Assert.assertFalse(
                "UserMessage [id=" + userMessageId + "] not expected to be in"
                + " collection [" + allUserMessages + "] is there.",
                CollectionUtils.exists(allUserMessages, new Predicate() {
                    @Override
                    public boolean evaluate(Object object) {
                        return userMessageId.equals(((UserMessage) object).getId());
                    }
                }));
    }

    private void checkUserMessageIdAndCount(final long userMessageId,
            final int count, Map<UserMessage, Integer> allUserMessages) {
        UserMessage userMessage = new UserMessage();
        userMessage.setId(userMessageId);
        Assert.assertTrue(
                "UserMessuage [id=" + userMessageId + "] not expected to be in"
                + " collection [" + allUserMessages + "] is there.",
                allUserMessages.containsKey(userMessage));
        int actualCount =  allUserMessages.get(userMessage);
        Assert.assertTrue(
                "The count of [id=" + userMessageId + "]'s conversation"
                + " should be " + count + ", not " + actualCount + ".",
               actualCount == count);
    }

    private void checkUserMessageIdAndCountAndSupplierId(final long userMessageId,
            final int count, final long supplierId, Map<UserMessage, ClientConversation> allUserMessages) {
        UserMessage userMessageExpected = new UserMessage();
        userMessageExpected.setId(userMessageId);
        Assert.assertTrue(
                "UserMessuage [id=" + userMessageId + "] not expected to be in"
                + " collection [" + allUserMessages + "] is there.",
                allUserMessages.containsKey(userMessageExpected));
        Assert.assertEquals(
                "The count of [id=" + userMessageId + "]'s conversation"
                + " is incorrect.",
               (long) count, (long) allUserMessages.get(userMessageExpected).getMessageCount());
        Assert.assertEquals(
                "The supplier of [id=" + userMessageId + "]'s conversation"
                + " is incorrect.",
               (long) supplierId, (long) allUserMessages.get(userMessageExpected).getSupplier().getId());
    }
}

