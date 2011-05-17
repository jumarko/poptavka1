package cz.poptavka.sample.service.demand;

import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.demand.DemandOrigin;
import cz.poptavka.sample.domain.demand.DemandStatus;
import cz.poptavka.sample.domain.demand.DemandType;
import cz.poptavka.sample.domain.settings.Settings;
import cz.poptavka.sample.domain.user.BusinessUserData;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.service.address.LocalityService;
import cz.poptavka.sample.service.user.ClientService;
import cz.poptavka.sample.util.date.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Juraj Martinka
 *         Date: 4.2.11
 */
@DataSet(path = {
        "classpath:cz/poptavka/sample/domain/address/LocalityDataSet.xml",
        "classpath:cz/poptavka/sample/domain/demand/CategoryDataSet.xml",
        "classpath:cz/poptavka/sample/domain/demand/DemandDataSet.xml",
        "classpath:cz/poptavka/sample/domain/user/UsersDataSet.xml" },
        dtd = "classpath:test.dtd")
public class DemandServiceIntegrationTest extends DBUnitBaseTest {

    @Autowired
    private DemandService demandService;

    @Autowired
    private LocalityService localityService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ClientService clientService;


    @Test
    public void testGetDemandTypes() {
        final List<DemandType> demandTypes = this.demandService.getDemandTypes();
        Assert.assertEquals(3, demandTypes.size());
        checkDemandTypeExists("normal", demandTypes);
        checkDemandTypeExists("attractive", demandTypes);
        checkDemandTypeExists("all", demandTypes);
    }

    @Test
    public void testGetDemandType() {
        checkGetDemandTypeByCode("normal");
        checkGetDemandTypeByCode("attractive");
        checkGetDemandTypeByCode("all");
    }

    @Test
    public void testGetDemandTypeForWrongCode() {
        String demandTypeCode = "virtual";
        DemandType demandType = this.demandService.getDemandType(demandTypeCode);
        Assert.assertNull(demandType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDemandTypeForNullCode() {
        String demandTypeCode = null;
        DemandType demandType = this.demandService.getDemandType(demandTypeCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDemandTypeForBlankCode() {
        String demandTypeCode = null;
        DemandType demandType = this.demandService.getDemandType(demandTypeCode);
    }


    @Test
    public void testGetDemandOrigins() {
        final List<DemandOrigin> demandOrigins = this.demandService.getDemandOrigins();
        Assert.assertEquals(3, demandOrigins.size());
        checkDemandOriginExists("epoptavka.cz", demandOrigins);
        checkDemandOriginExists("poptavam.com", demandOrigins);
        checkDemandOriginExists("isvzus.cz", demandOrigins);
    }

    @Test
    public void testGetDemandOrigin() {
        checkGetDemandOriginByCode("epoptavka.cz");
        checkGetDemandOriginByCode("poptavam.com");
        checkGetDemandOriginByCode("isvzus.cz");

        // check once again - this time the cached value should be returned
        checkGetDemandOriginByCode("epoptavka.cz");
    }


    @Test
    public void testGetDemandOriginForWrongCode() {
        final String nonexistingDemandOriginCode = "normal";
        final DemandOrigin nonexistingDemandOrigin = this.demandService.getDemandOrigin(nonexistingDemandOriginCode);
        Assert.assertNull("DemandOrigin with code [" + nonexistingDemandOriginCode + "] should not exist.",
                nonexistingDemandOrigin);
    }


    @Test
    public void testGetDemandsByLocality() {
        checkDemandsByLocality(4, "loc2");
        checkDemandsByLocality(6, "loc1");
        checkDemandsByLocality(4, "loc11");
        checkDemandsByLocality(0, "loc0111");
        checkDemandsByLocality(1, "loc121");

        // check combination of localities - the result is NOT always the sum of demands related to the localities
        // because duplication of demands in result is not allowed!
        checkDemandsByLocality(10, "loc1", "loc2");
        checkDemandsByLocality(10, "loc1", "loc2", "loc11");
        checkDemandsByLocality(5, "loc121", "loc2");
    }


    //----------------------------------  Methods for testing additional criteria methods ------------------------------
    @Test
    public void testGetDemandsByLocalityWithNoRestrictions() {
        checkDemandsByLocalityAdditionalCriteria(4, ResultCriteria.EMPTY_CRITERIA, "loc2");
        checkDemandsByLocalityAdditionalCriteria(6, ResultCriteria.EMPTY_CRITERIA, "loc1");
        checkDemandsByLocalityAdditionalCriteria(4, ResultCriteria.EMPTY_CRITERIA, "loc11");
        checkDemandsByLocalityAdditionalCriteria(0, ResultCriteria.EMPTY_CRITERIA, "loc0111");
        checkDemandsByLocalityAdditionalCriteria(1, ResultCriteria.EMPTY_CRITERIA, "loc121");

        // check combination of localities - the result is NOT always the sum of demands related to the localities
        // because duplication of demands in result is not allowed!
        checkDemandsByLocalityAdditionalCriteria(10, ResultCriteria.EMPTY_CRITERIA, "loc1", "loc2");
        checkDemandsByLocalityAdditionalCriteria(10, ResultCriteria.EMPTY_CRITERIA, "loc1", "loc2", "loc11");
        checkDemandsByLocalityAdditionalCriteria(5, ResultCriteria.EMPTY_CRITERIA, "loc121", "loc2");
    }


    @Test
    public void testGetDemandsByLocalityWithMaxResults() {
        final ResultCriteria criteria = new ResultCriteria.Builder()
                .maxResults(4)
                .build();
        checkDemandsByLocalityAdditionalCriteria(4, criteria, "loc2");
        checkDemandsByLocalityAdditionalCriteria(4, criteria, "loc1");
        checkDemandsByLocalityAdditionalCriteria(4, criteria, "loc11");
        checkDemandsByLocalityAdditionalCriteria(0, criteria, "loc0111");
        checkDemandsByLocalityAdditionalCriteria(1, criteria, "loc121");

        // check combination of localities - the result is NOT always the sum of demands related to the localities
        // because duplication of demands in result is not allowed!
        checkDemandsByLocalityAdditionalCriteria(4, criteria, "loc1", "loc2");
        checkDemandsByLocalityAdditionalCriteria(4, criteria, "loc1", "loc2", "loc11");
        checkDemandsByLocalityAdditionalCriteria(4, criteria, "loc121", "loc2");
    }

    @Test
    public void testGetDemandsByLocalityWithFirstResultMaxResults() {
        final ResultCriteria criteria = new ResultCriteria.Builder()
                .firstResult(2)
                .maxResults(4)
                .build();
        checkDemandsByLocalityAdditionalCriteria(2, criteria, "loc2");
        checkDemandsByLocalityAdditionalCriteria(4, criteria, "loc1");
        checkDemandsByLocalityAdditionalCriteria(2, criteria, "loc11");
        checkDemandsByLocalityAdditionalCriteria(0, criteria, "loc0111");
        checkDemandsByLocalityAdditionalCriteria(0, criteria, "loc121");

        // check combination of localities - the result is NOT always the sum of demands related to the localities
        // because duplication of demands in result is not allowed!
        checkDemandsByLocalityAdditionalCriteria(4, criteria, "loc1", "loc2");
        checkDemandsByLocalityAdditionalCriteria(4, criteria, "loc1", "loc2", "loc11");
        checkDemandsByLocalityAdditionalCriteria(3, criteria, "loc121", "loc2");
    }


    @Test
    public void testGetDemandsByLocalityWithFirstResultOrderBy() {
        final ResultCriteria criteria = new ResultCriteria.Builder()
                .firstResult(2)
                .orderByColumns(Arrays.asList("title"))
                .build();

        final Collection<Demand> loc2Demands = checkDemandsByLocalityAdditionalCriteria(2, criteria, "loc2");
        checkDemandExists(loc2Demands, 9L);
        checkDemandExists(loc2Demands, 10L);

        final Collection<Demand> loc1Demands = checkDemandsByLocalityAdditionalCriteria(4, criteria, "loc1");
        checkDemandExists(loc1Demands, 5L);
        checkDemandExists(loc1Demands, 6L);
        checkDemandExists(loc1Demands, 7L);
        checkDemandExists(loc1Demands, 8L);

        final Collection<Demand> loc11Demands = checkDemandsByLocalityAdditionalCriteria(2, criteria, "loc11");
        checkDemandExists(loc11Demands, 6L);
        checkDemandExists(loc11Demands, 7L);

        checkDemandsByLocalityAdditionalCriteria(0, criteria, "loc0111");

        checkDemandsByLocalityAdditionalCriteria(0, criteria, "loc121");

        // check combination of localities - the result is NOT always the sum of demands related to the localities
        // because duplication of demands in result is not allowed!
        final Collection<Demand> loc1loc2demands =
                checkDemandsByLocalityAdditionalCriteria(8, criteria, "loc1", "loc2");
        checkDemandExists(loc1loc2demands, 5L);
        checkDemandExists(loc1loc2demands, 6L);
        checkDemandExists(loc1loc2demands, 7L);
        checkDemandExists(loc1loc2demands, 8L);

        final Collection<Demand> loc1loc2loc11Demands =
                checkDemandsByLocalityAdditionalCriteria(8, criteria, "loc1", "loc2", "loc11");
        checkDemandExists(loc1loc2loc11Demands, 3L);
        checkDemandExists(loc1loc2loc11Demands, 4L);
        checkDemandExists(loc1loc2loc11Demands, 5L);
        checkDemandExists(loc1loc2loc11Demands, 6L);
        checkDemandExists(loc1loc2loc11Demands, 7L);
        checkDemandExists(loc1loc2loc11Demands, 8L);
        checkDemandExists(loc1loc2loc11Demands, 9L);
        checkDemandExists(loc1loc2loc11Demands, 10L);

        final Collection<Demand> loc121Loc2Demands =
                checkDemandsByLocalityAdditionalCriteria(3, criteria, "loc121", "loc2");
        checkDemandExists(loc121Loc2Demands, 8L);
        checkDemandExists(loc121Loc2Demands, 9L);
        checkDemandExists(loc121Loc2Demands, 10L);
    }

    //----------------------------------  End of methods for testing additional criteria methods -----------------------


    @Test
    public void testGetDemandsCountForAllLocalities() {
        final Map<Locality, Long> demandsCountForAllLocalities = this.demandService.getDemandsCountForAllLocalities();
        Assert.assertEquals(12, demandsCountForAllLocalities.size());

        checkDemandCountForLocality("loc1", 6L, demandsCountForAllLocalities);
        checkDemandCountForLocality("loc2", 4L, demandsCountForAllLocalities);
        checkDemandCountForLocality("loc11", 4L, demandsCountForAllLocalities);
        checkDemandCountForLocality("loc121", 1L, demandsCountForAllLocalities);

        checkDemandCountForLocality("loc12", 1L, demandsCountForAllLocalities);
        checkDemandCountForLocality("loc111", 0L, demandsCountForAllLocalities);
    }


    @Test
    public void testGetDemandsCountForAllCategories() {
        final Map<Category, Long> demandsCountForAllCategories = this.demandService.getDemandsCountForAllCategories();
        Assert.assertEquals(17, demandsCountForAllCategories.size());

        checkDemandsForCategory("cat0", 10L, demandsCountForAllCategories);
        checkDemandsForCategory("cat1", 5L, demandsCountForAllCategories);
        checkDemandsForCategory("cat2", 1L, demandsCountForAllCategories);
        checkDemandsForCategory("cat3", 3L, demandsCountForAllCategories);
        checkDemandsForCategory("cat11", 4L, demandsCountForAllCategories);
        checkDemandsForCategory("cat31", 2L, demandsCountForAllCategories);
        checkDemandsForCategory("cat312", 1L, demandsCountForAllCategories);
        checkDemandsForCategory("cat1132", 1L, demandsCountForAllCategories);
    }

    @Test
    public void testGetDemandsCountByLocality() {
        checkDemandCountByLocality("loc1", 6);
        checkDemandCountByLocality("loc2", 4);
        checkDemandCountByLocality("loc11", 4);
        checkDemandCountByLocality("loc121", 1);

        checkDemandCountByLocality("loc12", 1);
        checkDemandCountByLocality("loc111", 0);
        checkDemandCountByLocality("loc21", 1);
    }

    @Test
    public void getDemandsCountWithoutChildrenByLocality() {
        checkDemandCountWithoutChildrenByLocality("loc1", 1);
        checkDemandCountWithoutChildrenByLocality("loc2", 3);
        checkDemandCountWithoutChildrenByLocality("loc21", 1);
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
        checkDemandType(3L, DemandType.Type.NORMAL);
    }


    @Test
    public void testGetAllDemands() {
        Assert.assertEquals(10, this.demandService.getAll().size());
    }


    @Test
    public void testCreateDemand() {

        final Demand demand = new Demand();
        demand.setTitle("Title poptavka");
        demand.setType(this.demandService.getDemandType(DemandType.Type.NORMAL.getValue()));
        final BigDecimal price = BigDecimal.valueOf(10000);
        demand.setPrice(price);
        demand.setMaxSuppliers(20);
        demand.setMinRating(99);
        demand.setStatus(DemandStatus.NEW);
        final Date endDate = DateUtils.parseDate("2011-05-01");
        demand.setEndDate(endDate);
        final Date validTo = DateUtils.parseDate("2011-06-01");
        demand.setValidTo(validTo);


        final Client newClient = new Client();
        newClient.getBusinessUser().setEmail("test@poptavam.com");
        final String clientSurname = "Client";
        newClient.getBusinessUser().setBusinessUserData(
                new BusinessUserData.Builder().personFirstName("Test").personLastName(clientSurname).build());
        newClient.getBusinessUser().setSettings(new Settings());
        this.clientService.create(newClient);

        demand.setClient(clientService.getById(newClient.getId()));
        demandService.create(demand);

        final Demand createdDemand = this.demandService.getById(demand.getId());
        Assert.assertNotNull(createdDemand);
        Assert.assertEquals(price, createdDemand.getPrice());
        Assert.assertEquals(DemandStatus.NEW, createdDemand.getStatus());
        Assert.assertEquals(validTo, createdDemand.getValidTo());
        Assert.assertEquals(clientSurname,
                createdDemand.getClient().getBusinessUser().getBusinessUserData().getPersonLastName());

    }


    //------------------------------ HELPER METHODS --------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------
    private void checkDemandTypeExists(final String demandTypeCode, final List<DemandType> demandTypes) {
        Assert.assertTrue(CollectionUtils.exists(demandTypes, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                return demandTypeCode.equals(((DemandType) object).getCode());
            }
        }));
    }

    private void checkDemandOriginExists(final String demandOriginCode, final List<DemandOrigin> demandOrigins) {
        Assert.assertTrue(CollectionUtils.exists(demandOrigins, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                return demandOriginCode.equals(((DemandOrigin) object).getCode());
            }
        }));
    }

    private void checkDemandExists(Collection<Demand> loc2Demands, final long demandId) {
        Assert.assertTrue(CollectionUtils.exists(loc2Demands, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                return demandId == ((Demand) object).getId();
            }
        }));
    }


    private void checkGetDemandTypeByCode(String demandTypeCode) {
        final DemandType demandType = this.demandService.getDemandType(demandTypeCode);
        Assert.assertEquals(demandTypeCode, demandType.getCode());
    }

    private void checkGetDemandOriginByCode(String demandOriginCode) {
        final DemandOrigin demandOrigin = this.demandService.getDemandOrigin(demandOriginCode);
        Assert.assertEquals(demandOriginCode, demandOrigin.getCode());
    }

    private void checkDemandsByLocality(int expectedDemandsNumber, String... localityCodes) {
        checkDemandsByLocalityAdditionalCriteria(expectedDemandsNumber, null, localityCodes);
    }


    /**
     * Check wheter given localities are related to exactly <code>expectedDemandsNumber</code> number of demands.
     *
     * @param expectedDemandsNumber
     * @param resultCriteria
     * @param localityCodes
     */
    private Collection<Demand> checkDemandsByLocalityAdditionalCriteria(int expectedDemandsNumber,
                                                                        ResultCriteria resultCriteria,
                                                                        String... localityCodes) {
        if (localityCodes.length == 0) {
            throw new IllegalArgumentException("No localities for testing!");
        }

        final List<Locality> localities = new ArrayList<Locality>();
        for (String localityCode : localityCodes) {
            localities.add(this.localityService.getLocality(localityCode));
        }

        final Collection<Demand> demandsForLocalities = this.demandService.getDemands(
                resultCriteria,
                localities.toArray(new Locality[localities.size()]));
        final String message = "Locality codes [" + Arrays.toString(localityCodes) + "]";
        Assert.assertNotNull(message, demandsForLocalities);
        Assert.assertEquals(message, expectedDemandsNumber, demandsForLocalities.size());

        return demandsForLocalities;
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

    private void checkDemandCountForLocality(String localityCode, Long expectedDemandsCount,
                                             Map<Locality, Long> demandsCountForAllLocalities) {
        final String message = "Locality code [" + localityCode + "]";
        Assert.assertEquals(message,
                expectedDemandsCount,
                demandsCountForAllLocalities.get(this.localityService.getLocality(localityCode)));
    }


    private void checkDemandType(long demandId, DemandType.Type expectedType) {
        final Demand demand = this.demandService.getById(demandId);
        Assert.assertEquals(expectedType, demand.getType().getType());
    }

    private void checkDemandsForCategory(String categoryCode, Long expectedDemandsCount,
                                         Map<Category, Long> demandsCountForAllCategories) {
        final String message = "Category code [" + categoryCode + "]";
        Assert.assertEquals(message,
                expectedDemandsCount,
                demandsCountForAllCategories.get(this.categoryService.getCategory(categoryCode)));
    }

}
