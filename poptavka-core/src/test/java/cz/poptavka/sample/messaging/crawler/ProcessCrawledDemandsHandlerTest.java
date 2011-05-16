package cz.poptavka.sample.messaging.crawler;

import cz.poptavka.sample.base.integration.BasicIntegrationTest;
import org.junit.Test;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Juraj Martinka
 *         Date: 16.5.11
 */
public class ProcessCrawledDemandsHandlerTest extends BasicIntegrationTest {

    @Autowired
    private MessageListener processCrawledDemandsHandler;

    @Test
    public void testOnMessage() throws Exception {
        // check if following method is called continuously until all messages are processed
        int timeoutMs = 20000;
        final int sleepPeriod = 1000;
        while (timeoutMs > 0) {
            try {
                Thread.sleep(sleepPeriod);
            } catch (InterruptedException ie) {
                System.err.println("Warning: " + ie.getLocalizedMessage());
            }
            timeoutMs = timeoutMs - sleepPeriod;
        }
//        processCrawledDemandsHandler.onMessage();
    }
}
