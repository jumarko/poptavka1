package com.eprovement.poptavka.service.demand;

import com.eprovement.poptavka.base.integration.DBUnitIntegrationTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.address.Address;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.demand.DemandOrigin;
import com.eprovement.poptavka.domain.demand.DemandType;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.enums.DemandTypeType;
import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.domain.settings.Settings;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.BusinessUserData;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.domain.user.rights.AccessRole;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.service.user.ClientService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Juraj Martinka
 *         Date: 4.2.11
 */
@DataSet(path = {
        "classpath:com/eprovement/poptavka/domain/address/LocalityDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/CategoryDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/RatingDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/register/RegisterDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/UsersDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/DemandDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/offer/OfferDataSet.xml" },
        dtd = "classpath:test.dtd",
        disableForeignKeyChecks = false)
public class DemandServiceIntegrationTest extends DBUnitIntegrationTest {

    @Autowired
    private DemandService demandService;

    @Autowired
    private LocalityService localityService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private GeneralService generalService;


    private User user;
    private BusinessUser businessUser;

    @Test
    public void testGetDemandTypes() {
        final List<DemandType> demandTypes = this.demandService.getDemandTypes();
        Assert.assertEquals(3, demandTypes.size());
        checkDemandTypeExists("normal", demandTypes);
        checkDemandTypeExists("attractive", demandTypes);
//        checkDemandTypeExists("all", demandTypes);
    }

    @Test
    public void testGetDemandType() {
        checkGetDemandTypeByCode("normal");
        checkGetDemandTypeByCode("attractive");
//        checkGetDemandTypeByCode("all");
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
        checkDemandsByLocality(4, 2L);
        checkDemandsByLocality(7, 1L);
        checkDemandsByLocality(4, 11L);
        checkDemandsByLocality(0, 0111L);
        checkDemandsByLocality(2, 121L);

        // check combination of localities - the result is NOT always the sum of demands related to the localities
        // because duplication of demands in result is not allowed!
        checkDemandsByLocality(11, 1L, 2L);
        checkDemandsByLocality(11, 1L, 2L, 11L);
        checkDemandsByLocality(6, 121L, 2L);
    }


    //----------------------------------  Methods for testing additional criteria methods ------------------------------
    @Test
    public void testGetDemandsByLocalityWithNoRestrictions() {
        checkDemandsByLocalityAdditionalCriteria(4, ResultCriteria.EMPTY_CRITERIA, 2L);
        checkDemandsByLocalityAdditionalCriteria(7, ResultCriteria.EMPTY_CRITERIA, 1L);
        checkDemandsByLocalityAdditionalCriteria(4, ResultCriteria.EMPTY_CRITERIA, 11L);
        checkDemandsByLocalityAdditionalCriteria(0, ResultCriteria.EMPTY_CRITERIA, 0111L);
        checkDemandsByLocalityAdditionalCriteria(2, ResultCriteria.EMPTY_CRITERIA, 121L);

        // check combination of localities - the result is NOT always the sum of demands related to the localities
        // because duplication of demands in result is not allowed!
        checkDemandsByLocalityAdditionalCriteria(11, ResultCriteria.EMPTY_CRITERIA, 1L, 2L);
        checkDemandsByLocalityAdditionalCriteria(11, ResultCriteria.EMPTY_CRITERIA, 1L, 2L, 11L);
        checkDemandsByLocalityAdditionalCriteria(6, ResultCriteria.EMPTY_CRITERIA, 121L, 2L);
    }


    @Test
    public void testGetDemandsByLocalityWithMaxResults() {
        final ResultCriteria criteria = new ResultCriteria.Builder()
                .maxResults(4)
                .build();
        checkDemandsByLocalityAdditionalCriteria(4, criteria, 2L);
        checkDemandsByLocalityAdditionalCriteria(4, criteria, 1L);
        checkDemandsByLocalityAdditionalCriteria(4, criteria, 11L);
        checkDemandsByLocalityAdditionalCriteria(0, criteria, 0111L);
        checkDemandsByLocalityAdditionalCriteria(2, criteria, 121L);

        // check combination of localities - the result is NOT always the sum of demands related to the localities
        // because duplication of demands in result is not allowed!
        checkDemandsByLocalityAdditionalCriteria(4, criteria, 1L, 2L);
        checkDemandsByLocalityAdditionalCriteria(4, criteria, 1L, 2L, 11L);
        checkDemandsByLocalityAdditionalCriteria(4, criteria, 121L, 2L);
    }

    @Test
    public void testGetDemandsByLocalityWithFirstResultMaxResults() {
        final ResultCriteria criteria = new ResultCriteria.Builder()
                .firstResult(2)
                .maxResults(4)
                .build();
        checkDemandsByLocalityAdditionalCriteria(2, criteria, 2L);
        checkDemandsByLocalityAdditionalCriteria(4, criteria, 1L);
        checkDemandsByLocalityAdditionalCriteria(2, criteria, 11L);
        checkDemandsByLocalityAdditionalCriteria(0, criteria, 0111L);
        checkDemandsByLocalityAdditionalCriteria(0, criteria, 121L);

        // check combination of localities - the result is NOT always the sum of demands related to the localities
        // because duplication of demands in result is not allowed!
        checkDemandsByLocalityAdditionalCriteria(4, criteria, 1L, 2L);
        checkDemandsByLocalityAdditionalCriteria(4, criteria, 1L, 2L, 11L);
        checkDemandsByLocalityAdditionalCriteria(4, criteria, 121L, 2L);
    }


    @Test
    public void testGetDemandsByLocalityWithFirstResultOrderBy() {
        final ResultCriteria criteria = new ResultCriteria.Builder()
                .firstResult(2)
                .orderByColumns(Arrays.asList("title"))
                .build();

        final Collection<Demand> loc2Demands = checkDemandsByLocalityAdditionalCriteria(2, criteria, 2L);
        checkDemandExists(loc2Demands, 9L);
        checkDemandExists(loc2Demands, 10L);

        final Collection<Demand> loc1Demands = checkDemandsByLocalityAdditionalCriteria(5, criteria, 1L);
        checkDemandExists(loc1Demands, 5L);
        checkDemandExists(loc1Demands, 6L);
        checkDemandExists(loc1Demands, 7L);
        checkDemandExists(loc1Demands, 8L);
        checkDemandExists(loc1Demands, 18L);

        final Collection<Demand> loc11Demands = checkDemandsByLocalityAdditionalCriteria(2, criteria, 11L);
        checkDemandExists(loc11Demands, 6L);
        checkDemandExists(loc11Demands, 7L);

        checkDemandsByLocalityAdditionalCriteria(0, criteria, 0111L);

        checkDemandsByLocalityAdditionalCriteria(0, criteria, 121L);

        // check combination of localities - the result is NOT always the sum of demands related to the localities
        // because duplication of demands in result is not allowed!
        final Collection<Demand> loc1loc2demands =
                checkDemandsByLocalityAdditionalCriteria(9, criteria, 1L, 2L);
        checkDemandExists(loc1loc2demands, 5L);
        checkDemandExists(loc1loc2demands, 6L);
        checkDemandExists(loc1loc2demands, 7L);
        checkDemandExists(loc1loc2demands, 8L);

        final Collection<Demand> loc1loc2loc11Demands =
                checkDemandsByLocalityAdditionalCriteria(9, criteria, 1L, 2L, 11L);
        checkDemandExists(loc1loc2loc11Demands, 3L);
        checkDemandExists(loc1loc2loc11Demands, 4L);
        checkDemandExists(loc1loc2loc11Demands, 5L);
        checkDemandExists(loc1loc2loc11Demands, 6L);
        checkDemandExists(loc1loc2loc11Demands, 7L);
        checkDemandExists(loc1loc2loc11Demands, 8L);
        checkDemandExists(loc1loc2loc11Demands, 9L);
        checkDemandExists(loc1loc2loc11Demands, 10L);

        final Collection<Demand> loc121Loc2Demands =
                checkDemandsByLocalityAdditionalCriteria(4, criteria, 121L, 2L);
        checkDemandExists(loc121Loc2Demands, 8L);
        checkDemandExists(loc121Loc2Demands, 9L);
        checkDemandExists(loc121Loc2Demands, 10L);
        checkDemandExists(loc121Loc2Demands, 18L);
    }

    //----------------------------------  End of methods for testing additional criteria methods -----------------------


    @Test
    public void testGetDemandsCountForAllLocalities() {
        final Map<Locality, Long> demandsCountForAllLocalities = this.demandService.getDemandsCountForAllLocalities();
        Assert.assertEquals(12, demandsCountForAllLocalities.size());

        checkDemandCountForLocality(1L, 7L, demandsCountForAllLocalities);
        checkDemandCountForLocality(2L, 4L, demandsCountForAllLocalities);
        checkDemandCountForLocality(11L, 4L, demandsCountForAllLocalities);
        checkDemandCountForLocality(121L, 2L, demandsCountForAllLocalities);

        checkDemandCountForLocality(12L, 2L, demandsCountForAllLocalities);
        checkDemandCountForLocality(111L, 0L, demandsCountForAllLocalities);
    }


    @Test
    public void testGetDemandsCountForAllCategories() {
        final Map<Category, Long> demandsCountForAllCategories = this.demandService.getDemandsCountForAllCategories();
        Assert.assertEquals(17, demandsCountForAllCategories.size());

        checkDemandsForCategory(0L, 11L, demandsCountForAllCategories);
        checkDemandsForCategory(1L, 6L, demandsCountForAllCategories);
        checkDemandsForCategory(2L, 1L, demandsCountForAllCategories);
        checkDemandsForCategory(3L, 3L, demandsCountForAllCategories);
        checkDemandsForCategory(11L, 5L, demandsCountForAllCategories);
        checkDemandsForCategory(31L, 2L, demandsCountForAllCategories);
        checkDemandsForCategory(312L, 1L, demandsCountForAllCategories);
        checkDemandsForCategory(1132L, 1L, demandsCountForAllCategories);
    }

    @Test
    public void testGetDemandsCountByLocality() {
        checkDemandCountByLocality(1L, 7);
        checkDemandCountByLocality(2L, 4);
        checkDemandCountByLocality(11L, 4);
        checkDemandCountByLocality(121L, 2);

        checkDemandCountByLocality(12L, 2);
        checkDemandCountByLocality(111L, 0);
        checkDemandCountByLocality(21L, 1);
    }

    @Test
    public void getDemandsCountWithoutChildrenByLocality() {
        checkDemandCountWithoutChildrenByLocality(1L, 1);
        checkDemandCountWithoutChildrenByLocality(2L, 3);
        checkDemandCountWithoutChildrenByLocality(21L, 1);
        checkDemandCountWithoutChildrenByLocality(211L, 0);
    }


    @Test
    public void testGetDemandsByCategory() {
        checkDemandsByCategory(11, 0L);
        checkDemandsByCategory(6, 1L);
        checkDemandsByCategory(1, 2L);
        checkDemandsByCategory(3, 3L);
        checkDemandsByCategory(5, 11L);
        checkDemandsByCategory(2, 31L);
        checkDemandsByCategory(1, 312L);
        checkDemandsByCategory(1, 1132L);

        // check if same demands are not presented twice
        checkDemandsByCategory(5, 11L, 1132L);
    }


    @Test
    public void testGetDemandsCountByCategory() {
        checkDemandCountByCategory(1L, 6);
        checkDemandCountByCategory(2L, 1);
        checkDemandCountByCategory(3L, 3);
        checkDemandCountByCategory(11L, 5);
        checkDemandCountByCategory(31L, 2);
        checkDemandCountByCategory(312L, 1);
        checkDemandCountByCategory(1132L, 1);
    }


    @Test
    public void getDemandsCountWithoutChildrenByCategory() {
        checkDemandCountWithoutChildrenByCategory(0L, 1);
        checkDemandCountWithoutChildrenByCategory(1L, 1);
        checkDemandCountWithoutChildrenByCategory(2L, 1);
        checkDemandCountWithoutChildrenByCategory(21L, 0);
        checkDemandCountWithoutChildrenByCategory(22L, 0);
        checkDemandCountWithoutChildrenByCategory(23L, 0);
        checkDemandCountWithoutChildrenByCategory(3L, 1);
        checkDemandCountWithoutChildrenByCategory(11L, 2);
        checkDemandCountWithoutChildrenByCategory(31L, 1);
        checkDemandCountWithoutChildrenByCategory(112L, 2);
        checkDemandCountWithoutChildrenByCategory(311L, 0);
        checkDemandCountWithoutChildrenByCategory(312L, 1);
        checkDemandCountWithoutChildrenByCategory(1132L, 1);
    }


    @Test
    public void testDemandTypes() {
        checkDemandType(1L, DemandTypeType.NORMAL);
        checkDemandType(2L, DemandTypeType.ATTRACTIVE);
        checkDemandType(3L, DemandTypeType.NORMAL);
    }


    @Test
    public void testGetAllDemands() {
        Assert.assertEquals(11, this.demandService.getAll().size());
    }

    @Test
    public void testGetAllDemandsByCriteria() {
        final ResultCriteria resultCriteria =
                new ResultCriteria.Builder()
                        .firstResult(0)
                        .maxResults(15)
                        .orderByColumns(new HashMap<String, OrderType>() { {  put("createdDate", OrderType.DESC);  } })
                        .build();

        Assert.assertEquals(11, this.demandService.getAll(resultCriteria).size());
    }


    @Test
    public void testCreateDemand() {

        final Demand demand = new Demand();
        demand.setTitle("Title poptavka");
        demand.setDescription("Test poptavka description");
        demand.setType(this.demandService.getDemandType(DemandTypeType.NORMAL.getValue()));
        final BigDecimal price = BigDecimal.valueOf(10000);
        demand.setPrice(price);
        demand.setMaxSuppliers(20);
        demand.setMinRating(99);
        demand.setStatus(DemandStatus.NEW);
        // one day to the future
        final Date endDate = new Date(System.currentTimeMillis() + 8640000);
        demand.setEndDate(endDate);
        // ten days to the future
        final Date validTo = new Date(System.currentTimeMillis() + 86400000);
        demand.setValidTo(validTo);


        final Client newClient = new Client();
        newClient.getBusinessUser().setEmail("test@poptavam.com");
        newClient.getBusinessUser().setPassword("myPassword");
        newClient.getBusinessUser().setAccessRoles(Arrays.asList(this.generalService.find(AccessRole.class, 1L)));
        final Address clientAddress = new Address();
        clientAddress.setCity(this.localityService.getLocality(211L));
        clientAddress.setStreet("Gotham city");
        clientAddress.setZipCode("12");
        newClient.getBusinessUser().setAddresses(Arrays.asList(clientAddress));
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




    @Test
    public void testGetDemandsByCategoryAndLocality() {
        final Locality locality11 = this.localityService.getById(11);
        final Category category11 = this.categoryService.getById(11);


        final Collection<Demand> demandsByCategoriesAndLocalities =
                this.demandService.getDemands(
                        ResultCriteria.EMPTY_CRITERIA,
                        Arrays.asList(category11),
                        Arrays.asList(locality11));

        Assert.assertThat(demandsByCategoriesAndLocalities.size(), Is.is(2));
        checkDemandExists(demandsByCategoriesAndLocalities, 5);
        checkDemandExists(demandsByCategoriesAndLocalities, 6);

    }

    @Test
    public void testGetDemandsCountByCategoryAndLocality() {
        final Locality locality11 = this.localityService.getById(11);
        final Category category11 = this.categoryService.getById(11);


        final long demandsCount =
                this.demandService.getDemandsCount(Arrays.asList(category11), Arrays.asList(locality11));

        Assert.assertThat(demandsCount, Is.is(2L));
    }

    @Test
    public void testGetClientDemandsWithOffer() {
        Client client = generalService.find(Client.class, 111111112L);
        Assert.assertEquals("Number of client demands [" + client.toString() + "]", 2, client.getDemands().size());
        Client client2 = generalService.find(Client.class, 111111113L);
        Assert.assertEquals("Number of client demands [" + client2.toString() + "]", 1, client2.getDemands().size());

        Assert.assertEquals("Number of client demands with offer [" + client.toString() + "]", 2L,
                demandService.getClientDemandsWithOfferCount(client));
        checkDemandExists(demandService.getClientDemandsWithOffer(client), 2L);

        Assert.assertEquals("Number of client demands with offer [" + client2.toString() + "]", 0L,
                demandService.getClientDemandsWithOfferCount(client2));
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

    private void checkDemandExists(Collection<Demand> demands, final long demandId) {
        Assert.assertTrue(CollectionUtils.exists(demands, new Predicate() {
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

    private void checkDemandsByLocality(int expectedDemandsNumber, Long... localityCodes) {
        checkDemandsByLocalityAdditionalCriteria(expectedDemandsNumber, null, localityCodes);
    }


    /**
     * Check wheter given localities are related to exactly <code>expectedDemandsNumber</code> number of demands.
     *
     * @param expectedDemandsNumber
     * @param resultCriteria
     * @param localityIds
     */
    private Collection<Demand> checkDemandsByLocalityAdditionalCriteria(int expectedDemandsNumber,
                                                                        ResultCriteria resultCriteria,
                                                                        Long... localityIds) {
        if (localityIds.length == 0) {
            throw new IllegalArgumentException("No localities for testing!");
        }

        final List<Locality> localities = new ArrayList<Locality>();
        for (Long localityCode : localityIds) {
            localities.add(this.localityService.getLocality(localityCode));
        }

        final Collection<Demand> demandsForLocalities = this.demandService.getDemands(
                resultCriteria,
                localities.toArray(new Locality[localities.size()]));
        final String message = "Locality codes [" + Arrays.toString(localityIds) + "]";
        Assert.assertNotNull(message, demandsForLocalities);
        Assert.assertEquals(message, expectedDemandsNumber, demandsForLocalities.size());

        return demandsForLocalities;
    }


    private void checkDemandCountByLocality(Long localityId, int expectedCount) {
        final String message = "Locality code [" + localityId + "]";
        Assert.assertEquals(message,
                expectedCount,
                this.demandService.getDemandsCount(this.localityService.getLocality(localityId)));
    }

    private void checkDemandCountWithoutChildrenByLocality(Long localityId, int expectedCount) {
        final String message = "Locality code [" + localityId + "]";
        Assert.assertEquals(message,
                expectedCount,
                this.demandService.getDemandsCountWithoutChildren(this.localityService.getLocality(localityId)));
    }


    private void checkDemandsByCategory(int expectedDemandsNumber, Long... categoryIds) {
        if (categoryIds.length == 0) {
            throw new IllegalArgumentException("No categories for testing!");
        }

        List<Category> categories = new ArrayList<Category>();
        for (Long categoryId : categoryIds) {
            categories.add(this.categoryService.getCategory(categoryId));
        }
        final Collection<Demand> demandsForCategories = this.demandService.getDemands(
                categories.toArray(new Category[categories.size()]));
        final String message = "Category ids [" + categoryIds + "]";
        Assert.assertNotNull(message, demandsForCategories);
        Assert.assertEquals(message, expectedDemandsNumber, demandsForCategories.size());
    }

    private void checkDemandCountByCategory(Long categoryId, int expectedCount) {
        final String message = "Category id [" + categoryId + "]";
        Assert.assertEquals(message,
                expectedCount,
                this.demandService.getDemandsCount(this.categoryService.getCategory(categoryId)));
    }


    private void checkDemandCountWithoutChildrenByCategory(Long categoryId, int expectedCount) {
        final String message = "Category id [" + categoryId + "]";
        Assert.assertEquals(message,
                expectedCount,
                this.demandService.getDemandsCountWithoutChildren(this.categoryService.getCategory(categoryId)));
    }

    private void checkDemandCountForLocality(Long loccalityId, Long expectedDemandsCount,
                                             Map<Locality, Long> demandsCountForAllLocalities) {
        final String message = "Locality code [" + loccalityId + "]";
        Assert.assertEquals(message,
                expectedDemandsCount,
                demandsCountForAllLocalities.get(this.localityService.getLocality(loccalityId)));
    }


    private void checkDemandType(long demandId, DemandTypeType expectedType) {
        final Demand demand = this.demandService.getById(demandId);
        Assert.assertEquals(expectedType, demand.getType().getType());
    }

    private void checkDemandsForCategory(Long categoryId, Long expectedDemandsCount,
                                         Map<Category, Long> demandsCountForAllCategories) {
        final String message = "Category id [" + categoryId + "]";
        Assert.assertEquals(message,
                expectedDemandsCount,
                demandsCountForAllCategories.get(this.categoryService.getCategory(categoryId)));
    }

}
