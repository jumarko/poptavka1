package com.eprovement.poptavka.service.usermessage;

import com.eprovement.poptavka.base.integration.DBUnitIntegrationTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.message.ClientConversation;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.offer.OfferState;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.message.MessageService;
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
    }


    @Test
    public void testGetInbox() {

        final List<UserMessage> inbox = this.userMessageService.getInbox(
                this.user);

        Assert.assertEquals(9, inbox.size());
        checkUserMessageExists(2L, inbox);
        checkUserMessageExists(4L, inbox);
        checkUserMessageExists(8L, inbox);
        checkUserMessageExists(202L, inbox);
        checkUserMessageExists(302L, inbox);
        checkUserMessageExists(402L, inbox);
        checkUserMessageExists(407L, inbox);
        checkUserMessageExists(503L, inbox);
    }

    @Test
    public void testGetSentItems() {
        final List<UserMessage> inbox = this.userMessageService.getInbox(
                this.user);

        Assert.assertEquals(9, inbox.size());
        checkUserMessageExists(2L, inbox);
        checkUserMessageExists(4L, inbox);
        checkUserMessageExists(8L, inbox);
        checkUserMessageExists(202L, inbox);
        checkUserMessageExists(302L, inbox);
        checkUserMessageExists(402L, inbox);
        checkUserMessageExists(407L, inbox);
        checkUserMessageExists(503L, inbox);
    }

    @Test
    public void testGetPotentialDemands() {

        // sentItems test
        final List<UserMessage> sentItems = this.userMessageService
                .getSentItems(this.user);

        Assert.assertEquals(4, sentItems.size());
        checkUserMessageExists(6L, sentItems);
        checkUserMessageExists(304L, sentItems);
        checkUserMessageExists(404L, sentItems);

        // test for businessUser
        final List<UserMessage> potentialDemands = this.userMessageService.getPotentialDemands(businessUser);
        Assert.assertEquals(3, potentialDemands.size());
        checkUserMessageExists(2L, potentialDemands);
        checkUserMessageExists(202L, potentialDemands);
        checkUserMessageExists(503L, potentialDemands);
        checkUserMessageDoesntExists(302L, potentialDemands);
        checkUserMessageDoesntExists(501L, potentialDemands);
        checkUserMessageDoesntExists(502L, potentialDemands);
        checkUserMessageDoesntExists(501L, potentialDemands);
        checkUserMessageDoesntExists(401L, potentialDemands);
        checkUserMessageDoesntExists(403L, potentialDemands);
        checkUserMessageDoesntExists(405L, potentialDemands);
        checkUserMessageDoesntExists(406L, potentialDemands);
        checkUserMessageDoesntExists(408L, potentialDemands);
        checkUserMessageDoesntExists(1L, potentialDemands);
        checkUserMessageDoesntExists(3L, potentialDemands);
        checkUserMessageDoesntExists(4L, potentialDemands);

        // test for businessUser2
        final List<UserMessage> potentialDemands2 = this.userMessageService.getPotentialDemands(businessUser4);
        Assert.assertEquals(2, potentialDemands2.size());
        checkUserMessageExists(501L, potentialDemands2);
        checkUserMessageExists(502L, potentialDemands2);
        checkUserMessageDoesntExists(401L, potentialDemands2);
        checkUserMessageDoesntExists(403L, potentialDemands2);
        checkUserMessageDoesntExists(405L, potentialDemands2);
        checkUserMessageDoesntExists(406L, potentialDemands2);
        checkUserMessageDoesntExists(408L, potentialDemands2);
        checkUserMessageDoesntExists(1L, potentialDemands2);
        checkUserMessageDoesntExists(2L, potentialDemands2);
        checkUserMessageDoesntExists(3L, potentialDemands2);
        checkUserMessageDoesntExists(4L, potentialDemands2);
        checkUserMessageDoesntExists(503L, potentialDemands2);
        checkUserMessageDoesntExists(202L, potentialDemands2);
        checkUserMessageDoesntExists(302L, potentialDemands2);

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
        checkUserMessageIdAndCount(503L, 1, supplierConversations);
    }

    @Test
    public void testGetSupplierConversationsWithOffer() {
        final OfferState pendingState = generalService.find(OfferState.class, 2L);
        final Map<UserMessage, Integer> supplierConversations = this.userMessageService
                .getSupplierConversationsWithOffer(this.businessUser, pendingState);
        Assert.assertEquals(1, supplierConversations.size());
        checkUserMessageIdAndCount(304L, 2, supplierConversations);
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
        Assert.assertEquals(2, supplierConversationsCount);
    }

    @Test
    public void testGetClientConversationsWithoutOffer() {
        final Map<UserMessage, ClientConversation> clientConversations = this.userMessageService
                .getClientConversationsWithoutOffer(this.businessUserClient, demand2);
        Assert.assertEquals(2, clientConversations.size());
        checkUserMessageIdAndCountAndSupplierId(7L, 4, 111111111L, clientConversations);

        final Demand demand21 = generalService.find(Demand.class, 21L);
        final Map<UserMessage, ClientConversation> clientConversations2 = this.userMessageService
                .getClientConversationsWithoutOffer(this.businessUserClient, demand21);
        Assert.assertEquals(1, clientConversations2.size());
        checkUserMessageIdAndCountAndSupplierId(201L, 1, 111111111L, clientConversations2);
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

