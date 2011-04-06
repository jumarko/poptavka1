package cz.poptavka.sample.service.demand;

import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
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
@DataSet(path = {
        "classpath:cz/poptavka/sample/domain/address/LocalityDataSet.xml",
        "classpath:cz/poptavka/sample/domain/demand/CategoryDataSet.xml",
        "classpath:cz/poptavka/sample/domain/demand/DemandDataSet.xml" },
        dtd = "classpath:test.dtd")
public class DemandServiceIntegrationTest extends DBUnitBaseTest {

    @Autowired
    private DemandService demandService;

    @Autowired
    private LocalityService localityService;

    @Autowired
    private CategoryService categoryService;


    @Test
    public void testGetDemandsByLocality() {
        checkDemandsByLocality(3, "loc2");
        checkDemandsByLocality(7, "loc1");
        checkDemandsByLocality(4, "loc11");
        checkDemandsByLocality(0, "loc0111");
        checkDemandsByLocality(2, "loc121");

        // check combination of localities - the result is NOT always the sum of demands related to the localities
        // because duplication of demands in result is not allowed!
        checkDemandsByLocality(10, "loc1", "loc2");
        checkDemandsByLocality(10, "loc1", "loc2", "loc11");
        checkDemandsByLocality(5, "loc121", "loc2");
    }



    @Test
    public void testGetDemandsCountByLocality() {
        checkDemandCountByLocality("loc1", 7);
        checkDemandCountByLocality("loc2", 3);
        checkDemandCountByLocality("loc11", 4);
        checkDemandCountByLocality("loc121", 2);

        checkDemandCountByLocality("loc12", 2);
        checkDemandCountByLocality("loc111", 0);
        checkDemandCountByLocality("loc1211", 1);
    }

    @Test
    public void getDemandsCountWithoutChildrenByLocality() {
        checkDemandCountWithoutChildrenByLocality("loc1", 1);
        checkDemandCountWithoutChildrenByLocality("loc2", 3);
        checkDemandCountWithoutChildrenByLocality("loc1211", 1);
        checkDemandCountWithoutChildrenByLocality("loc211", 0);
    }


    @Test
    public void testGetDemandsByCategory() {
        checkDemandsByCategory(10, "cat0");
        checkDemandsByCategory(5, "cat1");
        checkDemandsByCategory(1, "cat2");
        checkDemandsByCategory(3, "cat3");
        checkDemandsByCategory(4, "cat11");
        checkDemandsByCategory(2, "cat31");
        checkDemandsByCategory(1, "cat312");
        checkDemandsByCategory(1, "cat1132");

        // check if same demands are not presented twice
        checkDemandsByCategory(4, "cat11", "cat1132");
    }



    @Test
    public void testGetDemandsCountByCategory() {
        checkDemandCountByCategory("cat0", 10);
        checkDemandCountByCategory("cat1", 5);
        checkDemandCountByCategory("cat2", 1);
        checkDemandCountByCategory("cat3", 3);
        checkDemandCountByCategory("cat11", 4);
        checkDemandCountByCategory("cat31", 2);
        checkDemandCountByCategory("cat312", 1);
        checkDemandCountByCategory("cat1132", 1);
    }


    @Test
    public void getDemandsCountWithoutChildrenByCategory() {
        checkDemandCountWithoutChildrenByCategory("cat0", 1);
        checkDemandCountWithoutChildrenByCategory("cat1", 1);
        checkDemandCountWithoutChildrenByCategory("cat2", 1);
        checkDemandCountWithoutChildrenByCategory("cat21", 0);
        checkDemandCountWithoutChildrenByCategory("cat22", 0);
        checkDemandCountWithoutChildrenByCategory("cat23", 0);
        checkDemandCountWithoutChildrenByCategory("cat3", 1);
        checkDemandCountWithoutChildrenByCategory("cat11", 2);
        checkDemandCountWithoutChildrenByCategory("cat31", 1);
        checkDemandCountWithoutChildrenByCategory("cat112", 1);
        checkDemandCountWithoutChildrenByCategory("cat311", 0);
        checkDemandCountWithoutChildrenByCategory("cat312", 1);
        checkDemandCountWithoutChildrenByCategory("cat1132", 1);
    }



    @Test
    public void testDemandTypes() {
        checkDemandType(1L, DemandType.Type.NORMAL);
        checkDemandType(2L, DemandType.Type.ATTRACTIVE);
        checkDemandType(3L, DemandType.Type.ALL);
    }


    @Test
    public void testGetAllDemands() {
        Assert.assertEquals(10, this.demandService.getAll().size());
    }


    //------------------------------ HELPER METHODS --------------------------------------------------------------------

    private void checkDemandsByLocality(int expectedDemandsNumber, String... localityCodes) {
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

    private void checkDemandCountByLocality(String localityCode, int expectedCount) {
        final String message = "Locality code [" + localityCode + "]";
        Assert.assertEquals(message,
                expectedCount,
                this.demandService.getDemandsCount(this.localityService.getLocality(localityCode)));
    }

    private void checkDemandCountWithoutChildrenByLocality(String localityCode, int expectedCount) {
        final String message = "Locality code [" + localityCode + "]";
        Assert.assertEquals(message,
                expectedCount,
                this.demandService.getDemandsCountWithoutChildren(this.localityService.getLocality(localityCode)));
    }


    private void checkDemandsByCategory(int expectedDemandsNumber, String... categoryCodes) {
        if (categoryCodes.length == 0) {
            throw new IllegalArgumentException("No categories for testing!");
        }

        List<Category> categories = new ArrayList<Category>();
        for (String categoryCode : categoryCodes) {
            categories.add(this.categoryService.getCategory(categoryCode));
        }
        final Collection<Demand> demandsForCategories = this.demandService.getDemands(
                categories.toArray(new Category[categories.size()]));
        final String message = "Category codes [" + categoryCodes + "]";
        Assert.assertNotNull(message, demandsForCategories);
        Assert.assertEquals(message, expectedDemandsNumber, demandsForCategories.size());
    }

    private void checkDemandCountByCategory(String categoryCode, int expectedCount) {
        final String message = "Category code [" + categoryCode + "]";
        Assert.assertEquals(message,
                expectedCount,
                this.demandService.getDemandsCount(this.categoryService.getCategory(categoryCode)));
    }


    private void checkDemandCountWithoutChildrenByCategory(String categoryCode, int expectedCount) {
        final String message = "Category code [" + categoryCode + "]";
        Assert.assertEquals(message,
                expectedCount,
                this.demandService.getDemandsCountWithoutChildren(this.categoryService.getCategory(categoryCode)));
    }



    private void checkDemandType(long demandId, DemandType.Type expectedType) {
        final Demand demand = this.demandService.getById(demandId);
        Assert.assertEquals(expectedType, demand.getType().getType());
    }


}
