package com.eprovement.poptavka.util.search;

import com.eprovement.poptavka.base.integration.DBUnitIntegrationTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.service.usermessage.UserMessageService;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.map.LinkedMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Vojtech Hubr
 */
@DataSet(path = {
        "classpath:com/eprovement/poptavka/domain/address/LocalityDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/UsersDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/DemandDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/message/MessageDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/offer/OfferDataSet.xml" },
        dtd = "classpath:test.dtd",
        disableForeignKeyChecks = true)
public class SearcherTest extends DBUnitIntegrationTest {

    @Autowired
    private UserMessageService userMessageService;

    private User user;
    private Demand demand;
    private Demand demand10;

    @Before
    public void setUp() {
        this.user = new User();
        this.demand = new Demand();
        this.demand10 = new Demand();
        user.setId(111111111L);
        demand.setId(2L);
        demand10.setId(10L);
    }

    @Test
    public void testSearchCollection() throws SearcherException {

        final List<UserMessage> inbox = this.userMessageService.getInbox(
                this.user);

        Search search = new Search(UserMessage.class);
        search.addFilter(new Filter("isRead", false));
        List<UserMessage> inboxFiltered = Searcher.searchCollection(
                inbox, search);
        Assert.assertEquals(3, inboxFiltered.size());
        checkUserMessageExists(4L, inboxFiltered);
        checkUserMessageExists(8L, inboxFiltered);
        checkUserMessageExists(202L, inboxFiltered);

        search = new Search(UserMessage.class);
        search.addFilter(new Filter("message.demand", demand));
        search.addSort("message.body", false);
        search.addSort("message.created", true);
        search.addSort("message.threadRoot.id", true);
        inboxFiltered = Searcher.searchCollection(
                inbox, search);
        Assert.assertEquals(5, inboxFiltered.size());
        checkUserMessageExists(2L, inboxFiltered);
        checkUserMessageExists(4L, inboxFiltered);
        checkUserMessageExists(8L, inboxFiltered);
        checkUserMessageExists(202L, inboxFiltered);
        checkUserMessageExists(302L, inboxFiltered);

        int i = 0;
        checkUserMessageExists(302L, i++, inboxFiltered);
        checkUserMessageExists(202L, i++, inboxFiltered);
        checkUserMessageExists(2L, i++, inboxFiltered);
        checkUserMessageExists(8L, i++, inboxFiltered);
        checkUserMessageExists(4L, i++, inboxFiltered);

        // same as above, but with pagination
        search.setPage(1);
        search.setMaxResults(2);
        inboxFiltered = Searcher.searchCollection(inbox, search);

        i = 0;
        checkUserMessageExists(2L, i++, inboxFiltered);
        checkUserMessageExists(8L, i++, inboxFiltered);

        search.setPage(2);
        search.setMaxResults(2);
        inboxFiltered = Searcher.searchCollection(inbox, search);

        i = 0;
        checkUserMessageExists(4L, i++, inboxFiltered);

        // same as above, but with constraints
        search.setFirstResult(1);
        search.setMaxResults(2);
        inboxFiltered = Searcher.searchCollection(inbox, search);

        i = 0;
        checkUserMessageExists(202L, i++, inboxFiltered);
        checkUserMessageExists(2L, i++, inboxFiltered);

        // the two above combined
        search = new Search(UserMessage.class);
        search.addFilter(new Filter("message.demand", demand));
        Filter filter = new Filter("isRead", false);
        filter.setOperator(Filter.OP_NOT_EQUAL);
        search.addFilter(filter);
        inboxFiltered = Searcher.searchCollection(
                inbox, search);
        Assert.assertEquals(2, inboxFiltered.size());
        checkUserMessageExists(2L, inboxFiltered);
        checkUserMessageExists(302L, inboxFiltered);

        search = new Search(UserMessage.class);
        List<Demand> demands = new ArrayList();
        demands.add(demand);
        demands.add(demand10);
        search.addFilter(new Filter("message.demand", demands, Filter.OP_IN));
        inboxFiltered = Searcher.searchCollection(
                inbox, search);
        Assert.assertEquals(6, inboxFiltered.size());
        checkUserMessageExists(2L, inboxFiltered);
        checkUserMessageExists(4L, inboxFiltered);
        checkUserMessageExists(8L, inboxFiltered);
        checkUserMessageExists(202L, inboxFiltered);
        checkUserMessageExists(302L, inboxFiltered);
        checkUserMessageExists(503L, inboxFiltered);
    }

    @Test
    public void testSearchMapByKeys() throws SearcherException {
        Map<Message, Integer> messageCounts = new HashMap(); 
        
        Message message = new Message();
        message.setSubject("z");
        message.setId(1L);
        messageCounts.put(message, 1);

        Message message2 = new Message();
        message2.setSubject("c");
        message2.setId(2L);
        messageCounts.put(message2, 5);

        Message message3 = new Message();
        message3.setSubject("w");
        message3.setId(3L);
        messageCounts.put(message3, 77);
        
        Search search = new Search(Message.class);
        search.addSortDesc("subject");
        
        LinkedHashMap<Message, Integer> sorted = Searcher.searchMapByKeys(messageCounts, search);
        
        List<Message> sortedKeys = new ArrayList(sorted.keySet());
        checkUserMessageSubjectAndCount(sortedKeys, 0, "z", sorted, 1);
        checkUserMessageSubjectAndCount(sortedKeys, 1, "w", sorted, 77);
        checkUserMessageSubjectAndCount(sortedKeys, 2, "c", sorted, 5);
        
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearchCollectionFailsForNullSearch() throws Exception {
        Searcher.searchCollection(Arrays.asList("1"), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearchCollectionFailsForNullSearchClass() throws Exception {
        Searcher.searchCollection(Arrays.asList("1"), new Search());
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
    /**
     * Checks if message with given id <code>messageId</code> is in collection <code>allUserMessages</code>
     * at a given <code>index</code>.
     *
     * @param messageId
     * @param index
     * @param allUserMessages
     */
    private void checkUserMessageExists(Long userMessageId, int index, List<UserMessage> allUserMessages) {
        Assert.assertTrue(
            "UserMessage [id=" + userMessageId + "] expected to be in"
            + " the list [" + allUserMessages + "] at index ["
            + index + "] is not there.",
            allUserMessages.get(index).getId().equals(userMessageId)
        );
    }
    
    private void checkUserMessageSubjectAndCount(List<Message> messageList, int index, String expectedSubject,
            Map<Message, Integer> messageMap, Integer expectedCount) {
        Assert.assertTrue(
            "Mesage with subject=" + expectedSubject + " expected to be in"
            + " the list [" + messageList + "] at index ["
            + index + "] is not there.",
            messageList.get(index).getSubject().equals(expectedSubject)
        );
        Assert.assertEquals(
            "Mesage with subject=" + expectedSubject + " should have a corresponding"
            + " count of " + expectedCount + " but instead it's "
            + index + "] is not there.",
            expectedCount, messageMap.get(messageList.get(index))
        );
    }


}
