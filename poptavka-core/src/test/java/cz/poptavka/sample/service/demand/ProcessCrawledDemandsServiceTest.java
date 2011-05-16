package cz.poptavka.sample.service.demand;

import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import cz.poptavka.sample.util.messaging.demand.TestingDemand;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test of processing of incoming crawled demands.
 * <p>
 *     Must extend {@link DBUnitBaseTest} because it required some basic data to be presented when storing crawled
 *     demands - e.g. required {@link cz.poptavka.sample.domain.demand.DemandType}.
 * @author Juraj Martinka
 *         Date: 16.5.11
 */
@DataSet(path = "classpath:cz/poptavka/sample/domain/demand/DemandDataSet.xml", dtd = "classpath:test.dtd")
public class ProcessCrawledDemandsServiceTest extends DBUnitBaseTest {

    @Autowired
    private ProcessCrawledDemandsService processCrawledDemandsService;


    @Test
    public void testProcessCrawledDemands() throws Exception {
        this.processCrawledDemandsService.processCrawledDemands(TestingDemand.generateDemands());
    }
}
