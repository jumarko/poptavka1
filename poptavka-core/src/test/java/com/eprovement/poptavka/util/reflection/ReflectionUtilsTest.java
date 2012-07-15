package com.eprovement.poptavka.util.reflection;

import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.message.UserMessage;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Juraj Martinka
 *         Date: 1.5.11
 */
public class ReflectionUtilsTest {

    @Test
    public void testGetValue() throws Exception {
        final Demand demand = new Demand();
        final String demandTitle = "Test demand";
        demand.setTitle(demandTitle);

        Assert.assertEquals(demandTitle, ReflectionUtils.getValue(demand, "title"));

        final Message message = new Message();
        final Message threadRoot = new Message();
        message.setThreadRoot(threadRoot);

        Assert.assertEquals(threadRoot, ReflectionUtils.getValue(message, "threadRoot"));

        final UserMessage userMessage = new UserMessage();
        userMessage.setStarred(true);

        Assert.assertEquals(true, ReflectionUtils.getValue(userMessage, "starred"));

        userMessage.setStarred(false);

        Assert.assertEquals(false, ReflectionUtils.getValue(userMessage, "starred"));

    }
}
