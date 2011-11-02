package cz.poptavka.sample.service.fulltext;

import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import cz.poptavka.sample.domain.demand.Demand;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 20.5.11
 */
@DataSet(path = {
        "classpath:cz/poptavka/sample/domain/address/LocalityDataSet.xml",
        "classpath:cz/poptavka/sample/domain/demand/CategoryDataSet.xml",
        "classpath:cz/poptavka/sample/domain/demand/RatingDataSet.xml",
        "classpath:cz/poptavka/sample/domain/user/UsersDataSet.xml",
        "classpath:cz/poptavka/sample/domain/demand/DemandDataSet.xml",
        "classpath:cz/poptavka/sample/domain/demand/DemandFulltextDataSet.xml" },
        dtd = "classpath:test.dtd")
// TODO Fulltext index is not created correctly, probably because transaction is not really commited
// in integration tests
@Ignore
public class HibernateFulltextSearchIntegrationTest extends DBUnitBaseTest {

    @Autowired
    private FulltextSearchService fulltextSearchService;

    private static volatile boolean fulltextIndexInitialized = false;


    /**
     * Creates initial fulltext index - only once per test class due the performance reasons.
     */
    @Before
    public void createFulltextIndex() {
        if (!fulltextIndexInitialized) {
            fulltextSearchService.createInitialFulltextIndex();
            fulltextIndexInitialized = true;
        }
    }


    @Test
    public void testSearchBasic() {
        final List<Demand> foundDemands =
                this.fulltextSearchService.search(Demand.class, Demand.DEMAND_FULLTEXT_FIELDS, "diskr\u00e9tnos\u009D");
        Assert.assertTrue(CollectionUtils.isNotEmpty(foundDemands));
    }

    @Test
    public void testSearchCaseInsensitive() {
        final List<Demand> foundDemands =
                this.fulltextSearchService.search(Demand.class, Demand.DEMAND_FULLTEXT_FIELDS, "maxim\u00e1ln\u00ed");
        Assert.assertTrue(CollectionUtils.isNotEmpty(foundDemands));
    }

    @Test
    public void testSearchAccentInsensitive() {
        final List<Demand> foundDemands =
                this.fulltextSearchService.search(Demand.class, Demand.DEMAND_FULLTEXT_FIELDS, "pozaduju");
        Assert.assertTrue(CollectionUtils.isNotEmpty(foundDemands));
        Assert.assertEquals(2, foundDemands.size());


    }

}
