package cz.poptavka.sample.service.message;

import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import cz.poptavka.sample.dao.message.MessageFilter;
import cz.poptavka.sample.domain.message.Message;
import cz.poptavka.sample.domain.message.MessageUserRoleType;
import cz.poptavka.sample.domain.user.User;
import cz.poptavka.sample.service.GeneralService;
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
    public void testGetListOfClientDemandMessages() {
        final Message threadRoot1 = this.messageService.getById(1L);
        final Message threadRoot200 = this.messageService.getById(200L);
        final Message threadRoot300 = this.messageService.getById(300L);
        final User client = this.generalService.find(User.class, 111111112L);
        final Map<Message, Long> listOfClientDemandMessages =
                this.messageService.getListOfClientDemandMessages(client);
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
}

