package com.eprovement.poptavka.service.demand;

import com.googlecode.genericdao.search.Search;
import com.eprovement.poptavka.base.integration.DBUnitBaseTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.demand.DemandType;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.util.messaging.demand.TestingDemand;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test of processing of incoming crawled demands.
 * <p>
 *     Must extend {@link DBUnitBaseTest} because it required some basic data to be presented when storing crawled
 *     demands - e.g. required {@link com.eprovement.poptavka.domain.demand.DemandType}.
 * @author Juraj Martinka
 *         Date: 16.5.11
 */
@DataSet(path = {
        "classpath:com/eprovement/poptavka/domain/address/LocalityDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/CategoryDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/RatingDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/register/RegisterDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/UsersDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/DemandDataSet.xml" },
        dtd = "classpath:test.dtd")
public class ProcessCrawledDemandsServiceTest extends DBUnitBaseTest {

    @Autowired
    private ProcessCrawledDemandsService processCrawledDemandsService;

    @Autowired
    private GeneralService generalService;


    @Test
    public void testProcessCrawledDemands() throws Exception {
        this.processCrawledDemandsService.processCrawledDemands(TestingDemand.generateDemands());

        // find and check the processed demands
        final Search demandSearch = new Search(Demand.class).addFilterEqual("title", TestingDemand.TEST_DEMAND_1_TITLE);
        final Demand testDemand1 = (Demand) this.generalService.search(demandSearch).get(0);

        Assert.assertNotNull(testDemand1.getClient());
        Assert.assertEquals(TestingDemand.TEST_DEMAND_1_EMAIL, testDemand1.getClient().getBusinessUser().getEmail());
        Assert.assertEquals(DemandType.Type.NORMAL, testDemand1.getType().getType());
    }
}
