package cz.poptavka.sample.service;

import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.demand.DemandStatus;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.domain.user.Person;
import cz.poptavka.sample.service.address.LocalityService;
import cz.poptavka.sample.service.demand.CategoryService;
import cz.poptavka.sample.service.demand.DemandService;
import cz.poptavka.sample.service.user.ClientService;
import cz.poptavka.sample.util.date.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.criterion.Example;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 24.4.11
 */
@DataSet(path = {
        "classpath:cz/poptavka/sample/domain/address/LocalityDataSet.xml",
        "classpath:cz/poptavka/sample/domain/demand/CategoryDataSet.xml",
        "classpath:cz/poptavka/sample/domain/demand/DemandDataSet.xml",
        "classpath:cz/poptavka/sample/domain/user/UsersDataSet.xml" },
        dtd = "classpath:test.dtd")
public class GenericServiceIntegrationTest extends DBUnitBaseTest {

    @Autowired
    private LocalityService localityService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private DemandService demandService;

    @Autowired
    private CategoryService categoryService;


    private Client exampleClient;


    @Before
    public void setUp() {
        this.exampleClient = createExampleClient();
    }


    //----------------------------------  METHODS FOR TESTING findByExample() method -----------------------------------
    @Test
    public void findLocalityByExample() {
        final List<Locality> localityByCode = this.localityService.findByExample(new Locality("loc111", null));
        Assert.assertEquals(1, localityByCode.size());
        Assert.assertEquals("locality111", localityByCode.get(0).getName());

        final List<Locality> localityByName = this.localityService.findByExample(new Locality(null, "locality111"));
        Assert.assertEquals(1, localityByName.size());
        Assert.assertEquals("loc111", localityByCode.get(0).getCode());

        // compare IDs
        Assert.assertEquals(localityByCode, localityByName);

        final Locality localityFilter = new Locality(null, null, LocalityType.DISTRICT);
        final List<Locality> allDistricts = this.localityService.findByExample(localityFilter);
        Assert.assertEquals(3, allDistricts.size());
        checkLocalityExistence("loc11", allDistricts);
        checkLocalityExistence("loc12", allDistricts);
        checkLocalityExistence("loc21", allDistricts);

        final List<Locality> allLocalities = this.localityService.findByExample(new Locality());
        Assert.assertEquals(12, allLocalities.size());
    }


    // TODO: find the way of using association types with Query By Example approach
    @Test
    public void findClientByExample() {
        final List<Client> clientsByNamesBroken = this.clientService.findByExample(this.exampleClient);
        // filtering by association type Person has no effect, check the documentation
        // for GenericService#findByExample() method
        Assert.assertEquals(4, clientsByNamesBroken.size());

    }

    @Test
    public void findDemandByExample() {
        checkDemandsByStatus(DemandStatus.NEW, 6);

        // TODO  skip for now because of strange error in DemandDataSet.xml - field ENDDATE
        // "Error casting value for table 'DEMAND' and column 'ENDDATE', "
//        checkDemandsByEndDate("2011-10-10", 3);
//        checkDemandsByEndDate("2011-04-04", 1);
//        checkDemandsByEndDate("2011-04-05", 0);
    }



    //----------------------------------  METHODS FOR TESTING findByExampleCustom() method -----------------------------
    @Test
    public void findLocalityByExampleCustom() {
        final Locality locality = new Locality("loc111", null);
        // without specification of level, the defaull level 0 is applied => no localities should be found
        final List<Locality> zeroLevelLocalitiesByCode = this.localityService.findByExampleCustom(Example.create(
                locality));
        Assert.assertEquals(0, zeroLevelLocalitiesByCode.size());

        // after explicit setting of level the unique locality have to be found
        final Locality localityWithExplicitLevel = new Locality("loc111", null);
        localityWithExplicitLevel.setLevel(4);
        final List<Locality> cityByCode = this.localityService.findByExampleCustom(
                Example.create(localityWithExplicitLevel));
        Assert.assertEquals(1, cityByCode.size());
        Assert.assertEquals("loc111", cityByCode.get(0).getCode());

        // check method result with no explicit restrictions -> empty list should be returned because no localities
        // with level zero exist
        final List<Locality> allLocalities = this.localityService.findByExampleCustom(Example.create(new Locality()));
        Assert.assertEquals(0, allLocalities.size());
    }



    //--------------------------------  METHODS FOR TESTING filtering based on <code>ResultCriteria</code> -------------
    @Test
    public void testGetAllWithNoCriteria() {
        // no restrictions
        final List<Demand> allDemands = this.demandService.getAll(ResultCriteria.EMPTY_CRITERIA);
        Assert.assertEquals(10, allDemands.size());
    }


    @Test
    public void testGetAllWithMaxResults() {
        // restrict number of demands - no ordering is guaranteed!
        final int maxDemands = 5;
        final List<Demand> allDemandsMaxResults = this.demandService.getAll(new ResultCriteria.Builder()
                .maxResults(5)
                .build());
        Assert.assertEquals(5, allDemandsMaxResults.size());

    }

    @Test
    public void testGetAllWithFirstResultAndMaxResults() {
        // restrict number of demands - no ordering is guaranteed!
        final int maxDemands = 5;
        final int firstResult = 2;
        final List<Demand> allDemandsMaxResults = this.demandService.getAll(new ResultCriteria.Builder()
                .maxResults(maxDemands)
                .firstResult(firstResult)
                .build());
        Assert.assertEquals(maxDemands, allDemandsMaxResults.size());
    }


    @Test
    public void testGetAllWithFirstResultAndMaxResultsOrderBy() {
        // restrict number of demands - no ordering is guaranteed!
        final int maxDemands = 5;
        final int firstResult = 2;
        final List<Demand> allDemandsMaxResults = this.demandService.getAll(new ResultCriteria.Builder()
                .maxResults(maxDemands)
                .firstResult(firstResult)
                .orderByColumns(Arrays.asList("id"))
                .build());
        Assert.assertEquals(maxDemands, allDemandsMaxResults.size());
        checkDemandExistence(3L, allDemandsMaxResults);
        checkDemandExistence(4L, allDemandsMaxResults);
        checkDemandExistence(5L, allDemandsMaxResults);
        checkDemandExistence(6L, allDemandsMaxResults);
        checkDemandExistence(7L, allDemandsMaxResults);
        checkDemandNonExistence(2L, allDemandsMaxResults);
        checkDemandNonExistence(10L, allDemandsMaxResults);
    }


    @Test
    public void testGetAllWithFirstResultOrderyBy() {
        // restrict number of demands - no ordering is guaranteed!
        final int firstResult = 8;
        final List<Demand> allDemandsMaxResults = this.demandService.getAll(new ResultCriteria.Builder()
                .firstResult(firstResult)
                .orderByColumns(Arrays.asList("id"))
                .build());
        Assert.assertEquals(2, allDemandsMaxResults.size());
        checkDemandExistence(9L, allDemandsMaxResults);
        checkDemandExistence(10L, allDemandsMaxResults);
    }

    @Test
    public void testGetAllWithFirstResultBeyondMaximum() {
        // restrict number of demands - no ordering is guaranteed!
        final int firstResult = 10;
        final List<Demand> allDemands = this.demandService.getAll(new ResultCriteria.Builder()
                .firstResult(firstResult)
                .orderByColumns(Arrays.asList("id"))
                .build());
        Assert.assertTrue(allDemands.isEmpty());
    }


    @Test
    public void findClientByExampleWithNoCriteria() {
        final List<Client> clientsByNamesBroken = this.clientService.findByExample(this.exampleClient,
                ResultCriteria.EMPTY_CRITERIA);
        // filtering by association type Person has no effect, check the documentation
        // for GenericService#findByExample() method
        Assert.assertEquals(4, clientsByNamesBroken.size());
    }


    @Test
    public void findClientByExampleWithMaxResults() {
        final int maxClients = 2;
        final List<Client> clientsByNamesBroken = this.clientService.findByExample(this.exampleClient,
                new ResultCriteria.Builder()
                        .maxResults(maxClients)
                        .build());
        // filtering by association type Person has no effect, check the documentation
        // for GenericService#findByExample() method
        Assert.assertEquals(maxClients, clientsByNamesBroken.size());
    }

    @Test
    public void findClientByExampleWithFirstResultOrderBy() {
        final int firstResult = 3;
        final List<Client> clientsByNamesBroken = this.clientService.findByExample(this.exampleClient,
                new ResultCriteria.Builder()
                        .firstResult(firstResult)
                        .orderByColumns(Arrays.asList("id"))
                        .build());
        // filtering by association type Person has no effect, check the documentation
        // for GenericService#findByExample() method
        Assert.assertEquals(1, clientsByNamesBroken.size());
    }





    //----------------------------------  HELPER METHODS ---------------------------------------------------------------

    private Client createExampleClient() {
        final Client client = new Client();
        client.setPerson(new Person("Elv\u00edra", "Vytret\u00e1"));
        return client;
    }


    /**
     * Check if locality with code <code>localityCode</code> is in collection <code>allLocalities</code>.
     *
     * @param localityCode code of locality that will be checked for existence
     * @param allLocalities collection which is searched
     */
    private void checkLocalityExistence(final String localityCode, List<Locality> allLocalities) {
        Assert.assertTrue(CollectionUtils.exists(allLocalities, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                return localityCode.equals(((Locality) object).getCode());
            }
        }));

    }

    private void checkDemandExistence(final Long id, Collection<Demand> allDemands) {
        Assert.assertTrue(CollectionUtils.exists(allDemands, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                return id.equals(((Demand) object).getId());
            }
        }));
    }

    private void checkDemandNonExistence(final Long id, Collection<Demand> allDemands) {
        Assert.assertFalse(CollectionUtils.exists(allDemands, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                return id.equals(((Demand) object).getId());
            }
        }));
    }



    private void checkDemandsByStatus(DemandStatus status, int expectedCount) {
        final Demand demandsByStatus = new Demand();
        demandsByStatus.setStatus(status);
        final List<Demand> newDemands = this.demandService.findByExample(demandsByStatus);
        Assert.assertEquals(expectedCount, newDemands.size());
    }


    private void checkDemandsByEndDate(String endDateString, int expectedCount) {
        final Demand demandWithEndDate = new Demand();
        final Date endDate = DateUtils.parseDate(endDateString);
        demandWithEndDate.setEndDate(endDate);
        final List<Demand> demandsByEndDate = this.demandService.findByExample(demandWithEndDate);
        Assert.assertEquals(expectedCount, demandsByEndDate.size());
    }


}
