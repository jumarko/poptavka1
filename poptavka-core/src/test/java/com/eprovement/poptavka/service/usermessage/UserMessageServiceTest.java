package com.eprovement.poptavka.service.usermessage;

import com.eprovement.poptavka.base.integration.DBUnitBaseTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.message.MessageService;
import com.eprovement.poptavka.service.user.SupplierService;
import java.util.Collection;
import java.util.List;
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
        "classpath:com/eprovement/poptavka/domain/demand/RatingDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/UsersDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/DemandDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/message/MessageDataSet.xml" },
        dtd = "classpath:test.dtd",
        disableForeignKeyChecks = true)
public class UserMessageServiceTest extends DBUnitBaseTest {

    @Autowired
    private MessageService messageService;
    @Autowired
    private GeneralService generalService;
    @Autowired
    private UserMessageService userMessageService;
    @Autowired
    private SupplierService supplierService;

    private User user;
    private BusinessUser businessUser;


    @Before
    public void setUp() {
        this.user = new User();
        user.setId(111111111L);
        this.businessUser = new BusinessUser();
        businessUser.setId(111111111L);
    }


    @Test
    public void testGetInbox() {

        final List<UserMessage> inbox = this.userMessageService.getInbox(
                this.user);

        Assert.assertEquals(7, inbox.size());
        checkUserMessageExists(2L, inbox);
        checkUserMessageExists(4L, inbox);
        checkUserMessageExists(8L, inbox);
        checkUserMessageExists(202L, inbox);
        checkUserMessageExists(302L, inbox);
        checkUserMessageExists(402L, inbox);
        checkUserMessageExists(407L, inbox);
    }

    @Test
    public void testGetSentItems() {
        final List<UserMessage> inbox = this.userMessageService.getInbox(
                this.user);

        Assert.assertEquals(7, inbox.size());
        checkUserMessageExists(2L, inbox);
        checkUserMessageExists(4L, inbox);
        checkUserMessageExists(8L, inbox);
        checkUserMessageExists(202L, inbox);
        checkUserMessageExists(302L, inbox);
        checkUserMessageExists(402L, inbox);
        checkUserMessageExists(407L, inbox);
    }

    @Test
    public void testGetPotentialDemands() {

        final List<UserMessage> sentItems = this.userMessageService
                .getSentItems(this.user);

        Assert.assertEquals(3, sentItems.size());
        checkUserMessageExists(6L, sentItems);
        checkUserMessageExists(304L, sentItems);
        checkUserMessageExists(404L, sentItems);
    }

    @Test
    public void testSetMessageReadStatus() {
        final UserMessage unreadUserMessage = this.generalService.find(UserMessage.class, 4L);
        unreadUserMessage.setRead(true);
        this.generalService.save(unreadUserMessage);
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

}

