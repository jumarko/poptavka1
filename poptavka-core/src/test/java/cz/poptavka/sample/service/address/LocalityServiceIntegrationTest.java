package cz.poptavka.sample.service.address;

import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import cz.poptavka.sample.common.ResultCriteria;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.service.common.TreeItemService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 5.2.11
 */
@DataSet(path = "classpath:cz/poptavka/sample/domain/address/LocalityDataSet.xml", dtd = "classpath:test.dtd")
public class LocalityServiceIntegrationTest extends DBUnitBaseTest {

    @Autowired
    private LocalityService localityService;

    @Autowired
    private TreeItemService treeItemService;


    @Test
    public void testGetLocalityByCode() {
        checkLocalityByCode("loc111", "locality111");
        checkLocalityByCode("loc2", "locality2");
    }


    @Test
    public void testGetLocalitiesByType() {
        checkLocalitiesByType(LocalityType.COUNTRY, 1);
        checkLocalitiesByType(LocalityType.REGION, 2);
        checkLocalitiesByType(LocalityType.DISTRICT, 3);
        checkLocalitiesByType(LocalityType.CITY, 6);

        checkLocalitiesByType(null, 0);
    }

    @Test
    public void testGetLocalitiesByTypeWithNoCriteria() {
        checkLocalitiesByTypeAdditionalCriteria(LocalityType.COUNTRY, ResultCriteria.EMPTY_CRITERIA, 1);
        checkLocalitiesByTypeAdditionalCriteria(LocalityType.REGION, ResultCriteria.EMPTY_CRITERIA, 2);
        checkLocalitiesByTypeAdditionalCriteria(LocalityType.DISTRICT, ResultCriteria.EMPTY_CRITERIA, 3);
        checkLocalitiesByTypeAdditionalCriteria(LocalityType.CITY, ResultCriteria.EMPTY_CRITERIA, 6);

        checkLocalitiesByTypeAdditionalCriteria(null, ResultCriteria.EMPTY_CRITERIA, 0);
    }


    @Test
    public void testGetLocalitiesByTypeWithMaxResults() {
        final int maxResults = 3;
        final ResultCriteria criteria = new ResultCriteria.Builder()
                .maxResults(maxResults)
                .build();
        checkLocalitiesByTypeAdditionalCriteria(LocalityType.COUNTRY, criteria, 1);
        checkLocalitiesByTypeAdditionalCriteria(LocalityType.REGION, criteria, 2);
        checkLocalitiesByTypeAdditionalCriteria(LocalityType.DISTRICT, criteria, 3);
        checkLocalitiesByTypeAdditionalCriteria(LocalityType.CITY, criteria, 3);

        checkLocalitiesByTypeAdditionalCriteria(null, criteria, 0);
    }


    @Test
    public void testGetLocalitiesByTypeWithFirstResultOrderBy() {
        final int firstResult = 2;
        final ResultCriteria criteria = new ResultCriteria.Builder()
                .firstResult(firstResult)
                .orderByColumns(Arrays.asList("name"))
                .build();
        checkLocalitiesByTypeAdditionalCriteria(LocalityType.COUNTRY, criteria, 0);
        checkLocalitiesByTypeAdditionalCriteria(LocalityType.REGION, criteria, 0);

        final Collection<Locality> districts =
                checkLocalitiesByTypeAdditionalCriteria(LocalityType.DISTRICT, criteria, 1);
        Assert.assertEquals("locality21", districts.iterator().next().getName());

        final Collection<Locality> cities = checkLocalitiesByTypeAdditionalCriteria(LocalityType.CITY, criteria, 4);
        checkLocalityIn(cities, "locality211");
        checkLocalityIn(cities, "locality212");
        checkLocalityIn(cities, "locality213");
        checkLocalityIn(cities, "locality214");

        checkLocalitiesByTypeAdditionalCriteria(null, criteria, 0);
    }


    @Test
    public void testGetAllLocalitiesForParent() {
        final Locality czechRepublic = this.localityService.getLocality("CZ");
        final List<Locality> allLocalitiesInCzechRepublic = this.treeItemService.getAllChildren(czechRepublic,
                Locality.class);

        Assert.assertEquals(11, allLocalitiesInCzechRepublic.size());
    }


    //--------------------- HELPER METHODS -----------------------------------------------------------------------------
    private void checkLocalityByCode(String localityCode, String expectedName) {
        final Locality loc111 = this.localityService.getLocality(localityCode);
        Assert.assertNotNull(loc111);
        Assert.assertEquals(expectedName, loc111.getName());
    }

    /**
     * Basic check of localities of given type.
     *
     * @param type
     * @param expectedNumber how many lcalities of given type exists.
     * @return checked localities to allow more specialized checks.
     */
    private Collection<Locality> checkLocalitiesByType(LocalityType type, int expectedNumber) {
        final List<Locality> localities = this.localityService.getLocalities(type);
        Assert.assertNotNull(localities);
        Assert.assertEquals(expectedNumber, localities.size());
        return localities;
    }

    /**
     * Basic check of localities of given type.
     *
     * @param type
     * @param expectedNumber how many lcalities of given type exists.
     * @return checked localities to allow more specialized checks.
     */
    private Collection<Locality> checkLocalitiesByTypeAdditionalCriteria(LocalityType type,
                                                                         ResultCriteria resultCriteria,
                                                                         int expectedNumber) {
        final List<Locality> localities = this.localityService.getLocalities(type, resultCriteria);
        Assert.assertNotNull(localities);
        Assert.assertEquals(expectedNumber, localities.size());
        return localities;
    }


    /**
     * Check if locality with name <code>localityName</code> is in collection of given localities.
     * @param allLocalities
     * @param localityName
     */
    private void checkLocalityIn(Collection<Locality> allLocalities, final String localityName) {
        Assert.assertTrue(CollectionUtils.exists(allLocalities, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                return localityName.equals(((Locality) object).getName());
            }
        }));
    }


}
