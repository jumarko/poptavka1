package cz.poptavka.sample.messaging.crawler;

import com.google.common.base.Preconditions;
import cz.poptavka.crawldemands.demand.Demand;
import cz.poptavka.sample.service.demand.ProcessCrawledDemandsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.util.Arrays;

/**
 * Message listener which process incoming crawled demands.
 * Those demands are being crawled from external systems and sent to the poptavka for storing.
 *
 * See separate project CrawlDemands.
 *
 * @author Juraj Martinka
 *         Date: 16.5.11
 */
public class ProcessCrawledDemandsHandler implements MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessCrawledDemandsHandler.class);

    private AmqpTemplate messagingTemplate;
    private ProcessCrawledDemandsService processCrawledDemandsService;


    public ProcessCrawledDemandsHandler(AmqpTemplate messagingTemplate,
                                        ProcessCrawledDemandsService processCrawledDemandsService) {
        Preconditions.checkNotNull(messagingTemplate);
        this.messagingTemplate = messagingTemplate;
        this.processCrawledDemandsService = processCrawledDemandsService;
    }


    /**
     * TODO: avoid running this message listener by tests with exception {@link ProcessCrawledDemandsHandlerTest}.
     * @param message
     */
    @Override
    public void onMessage(Message message) {
        final Demand[] newCrawledDemands = (Demand[]) this.messagingTemplate.receiveAndConvert();
        LOGGER.info("\n========================================================================");
        LOGGER.info("New crawled demand arrived: " + Arrays.toString(newCrawledDemands));

        processCrawledDemandsService.processCrawledDemands(newCrawledDemands);
    }

}
