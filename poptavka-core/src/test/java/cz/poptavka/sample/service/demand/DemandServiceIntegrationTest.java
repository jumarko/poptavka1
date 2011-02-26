package cz.poptavka.sample.service.demand;

import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.demand.DemandType;
import cz.poptavka.sample.service.address.LocalityService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 4.2.11
 */
@DataSet(path = "classpath:cz/poptavka/sample/base/BaseDataSet.xml", dtd = "classpath:test.dtd")
public class DemandServiceIntegrationTest extends DBUnitBaseTest {

    @Autowired
    private DemandService demandService;

    @Autowired
    private LocalityService localityService;


    @Test
    public void testGetDemands() {
        checkDemands(3, "loc2");
        checkDemands(7, "loc1");
        checkDemands(4, "loc11");
        checkDemands(0, "loc0111");
        checkDemands(2, "loc121");

        // check combination of localities - the result is NOT always the sum of demands related to the localities
        // because duplicating of demands in result is not allowed!
        checkDemands(10, "loc1", "loc2");
        checkDemands(10, "loc1", "loc2", "loc11");
        checkDemands(5, "loc121", "loc2");
    }



    @Test
    public void testGetDemandsCount() {
        checkDemandCount("loc1", 7);
        checkDemandCount("loc2", 3);
        checkDemandCount("loc11", 4);
        checkDemandCount("loc121", 2);

        checkDemandCount("loc12", 2);
        checkDemandCount("loc111", 0);
        checkDemandCount("loc1211", 1);
    }


    @Test
    public void testDemandTypes() {
        checkDemandType(1L, DemandType.Type.NORMAL);
        checkDemandType(2L, DemandType.Type.ATTRACTIVE);
        checkDemandType(3L, DemandType.Type.ALL);
    }


    //------------------------------ HELPER METHODS --------------------------------------------------------------------

    private void checkDemands(int expectedDemandsNumber, String... localityCodes) {
        if (localityCodes.length == 0) {
            throw new IllegalArgumentException("No localities for testing!");
        }

        List<Locality> localities = new ArrayList<Locality>();
        for (String localityCode : localityCodes) {
            localities.add(this.localityService.getLocality(localityCode));
        }
        final Collection<Demand> demandsForLocalities = this.demandService.getDemands(
                localities.toArray(new Locality[localities.size()]));
        final String message = "Locality codes [" + localityCodes + "]";
        Assert.assertNotNull(message, demandsForLocalities);
        Assert.assertEquals(message, expectedDemandsNumber, demandsForLocalities.size());
    }

    private void checkDemandCount(String localityCode, int expectedCount) {
        final String message = "Locality code [" + localityCode + "]";
        Assert.assertEquals(message,
                expectedCount,
                this.demandService.getDemandsCount(this.localityService.getLocality(localityCode)));
    }


    private void checkDemandType(long demandId, DemandType.Type expectedType) {
        final Demand demand = this.demandService.getById(demandId);
        Assert.assertEquals(expectedType, demand.getType().getType());
    }


}
