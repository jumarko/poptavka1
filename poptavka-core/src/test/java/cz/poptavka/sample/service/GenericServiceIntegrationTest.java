package cz.poptavka.sample.service;

import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
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
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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

        final Client client = new Client();
        client.setPerson(new Person("Elv\u00edra", "Vytret\u00e1"));
        final List<Client> clientsByNamesBroken = this.clientService.findByExample(client);
        // filtering by association type Person has no effect, check the documentation
        // for GenericService#findByExample() method
        Assert.assertEquals(4, clientsByNamesBroken.size());

    }

    @Test
    public void findDemandByExample() {
        checkDemandsByStatus(DemandStatus.NEW, 6);

        checkDemandsByEndDate("2011-10-10", 3);
        checkDemandsByEndDate("2011-04-04", 1);
        checkDemandsByEndDate("2011-04-05", 0);
    }


    @Test
    public void findCategoryByExample() {
        // TODO: implement test
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



    //----------------------------------  HELPER METHODS ---------------------------------------------------------------
    /**
     * Check if locality with code <code>localityCode</code> is in collection <code>allLocalities</code>.
     *
     * @param localityCode code of locality that will be checked for existence
     * @param allLocalities collection which is searched
     */
    private void checkLocalityExistence(final String localityCode, List<Locality> allLocalities) {
        CollectionUtils.exists(allLocalities, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                return localityCode.equals(((Locality) object).getCode());
            }
        });
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
