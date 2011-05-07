package cz.poptavka.sample.messaging.sample;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Juraj Martinka
 *         Date: 5.5.11
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:messaging-config-test.xml")
// Messaging is not completely ready
@Ignore
public class MessageSenderTest {

    private static final int NUMBER_OF_SENT_MESSAGES = 9;
    private static final int ONE_SECOND = 1000;
    private static final int HUNDRED_MS = 100;
    private static final int TIMEOUT = 50;

    @Autowired
    private MessageSender sender;
    @Autowired
    private MessageHandler listener;



    @Test
//    @Ignore
    public void testSend() throws Exception {

        final int sleepInterval = HUNDRED_MS;

        for (int i = 0; i < NUMBER_OF_SENT_MESSAGES; i++) {
            sender.send("Hi, Guy! This is " + (i + 1) + ". message");
            Thread.sleep(sleepInterval);
        }

        int timeout = 0;
        while (listener.getTotalNumberOfReceivedMessage() < NUMBER_OF_SENT_MESSAGES) {
            Thread.sleep(sleepInterval);
            timeout++;
            if (timeout > TIMEOUT) {
                Assert.fail("Insufficient number of received messages!");
            }
        }

        Assert.assertEquals(NUMBER_OF_SENT_MESSAGES, listener.getTotalNumberOfReceivedMessage());
    }
}
