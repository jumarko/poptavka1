package com.eprovement.poptavka.service.address;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import com.eprovement.poptavka.base.integration.DBUnitIntegrationTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.enums.LocalityType;
import com.eprovement.poptavka.service.common.TreeItemService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 5.2.11
 */
@DataSet(path = "classpath:com/eprovement/poptavka/domain/address/LocalityDataSet.xml", dtd = "classpath:test.dtd")
public class LocalityServiceIntegrationTest extends DBUnitIntegrationTest {

    @Autowired
    private LocalityService localityService;

    @Autowired
    private TreeItemService treeItemService;


    @Test
    public void testGetLocalityByCode() {
        checkLocalityById(111L, "locality111");
        checkLocalityById(2L, "locality2");
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
        final Locality czechRepublic = this.localityService.getLocality(0L);
        final List<Locality> allLocalitiesInCzechRepublic = this.treeItemService.getAllDescendants(czechRepublic,
                Locality.class);

        Assert.assertEquals(11, allLocalitiesInCzechRepublic.size());
    }

    @Test
    public void testGetLocalitiesByMaxLengthExcl() {
        List<Locality> districts = this.localityService.getLocalitiesByMaxLengthExcl(
                11, "locality1", LocalityType.DISTRICT);
        Assert.assertThat(districts.size(), is(2));
        checkLocalityIn(districts, "locality11");
        checkLocalityIn(districts, "locality12");
        List<Locality> localities = this.localityService.getLocalitiesByMaxLengthExcl(
                11, "locality1");
        Assert.assertThat(localities.size(), is(3));
        checkLocalityIn(localities, "locality1");
        checkLocalityIn(localities, "locality11");
        checkLocalityIn(localities, "locality12");

        districts = this.localityService.getLocalitiesByMaxLengthExcl(
                12, "locality1", LocalityType.DISTRICT);
        Assert.assertThat(districts.size(), is(2));
        checkLocalityIn(districts, "locality11");
        checkLocalityIn(districts, "locality12");
        districts = this.localityService.getLocalitiesByMaxLengthExcl(
                10, "locality1", LocalityType.DISTRICT);
        Assert.assertThat(districts, is(Collections.<Locality>emptyList()));
        List<Locality> cities = this.localityService.getLocalitiesByMaxLengthExcl(
                12, "locality21", LocalityType.CITY);
        Assert.assertThat(cities.size(), is(4));
        checkLocalityIn(cities, "locality211");
        checkLocalityIn(cities, "locality212");
        checkLocalityIn(cities, "locality213");
        checkLocalityIn(cities, "locality214");
    }

    @Test
    public void testGetLocalitiesByMaxLengthExclWithSearchStringInsideWord() {
        List<Locality> districts = this.localityService.getLocalitiesByMaxLengthExcl(
                11, "cality1", LocalityType.DISTRICT);
        Assert.assertThat(districts.size(), is(2));
        checkLocalityIn(districts, "locality11");
        checkLocalityIn(districts, "locality12");
    }

    @Test
    public void testGetLocalitiesByMinLength() {
        List<Locality> districts = this.localityService.getLocalitiesByMinLength(
                10, "locality1", LocalityType.DISTRICT);
        Assert.assertThat(districts.size(), is(2));
        checkLocalityIn(districts, "locality11");
        checkLocalityIn(districts, "locality12");
        districts = this.localityService.getLocalitiesByMinLength(
                9, "locality1", LocalityType.DISTRICT);
        Assert.assertThat(districts.size(), is(2));
        checkLocalityIn(districts, "locality11");
        checkLocalityIn(districts, "locality12");
        districts = this.localityService.getLocalitiesByMinLength(
                11, "locality1", LocalityType.DISTRICT);
        Assert.assertThat(districts, is(Collections.<Locality>emptyList()));
        List<Locality> cities = this.localityService.getLocalitiesByMinLength(
                11, "locality21", LocalityType.CITY);
        Assert.assertThat(cities.size(), is(4));
        checkLocalityIn(cities, "locality211");
        checkLocalityIn(cities, "locality212");
        checkLocalityIn(cities, "locality213");
        checkLocalityIn(cities, "locality214");
        List<Locality> localities = this.localityService.getLocalitiesByMinLength(
                10, "locality21");
        Assert.assertThat(localities.size(), is(5));
        checkLocalityIn(localities, "locality21");
        checkLocalityIn(localities, "locality211");
        checkLocalityIn(localities, "locality212");
        checkLocalityIn(localities, "locality213");
        checkLocalityIn(localities, "locality214");
    }

    @Test
    public void testGetLocalitiesByMinLengthWithSearchStringInsideWord() {
        List<Locality> districts = this.localityService.getLocalitiesByMinLength(
                10, "cality1", LocalityType.DISTRICT);
        Assert.assertThat(districts.size(), is(2));
        checkLocalityIn(districts, "locality11");
        checkLocalityIn(districts, "locality12");
    }

    @Test
    public void findCityByName() throws Exception {
        final Locality city = localityService.findCityByName("locality1", "locality11", "locality111");
        assertNotNull("city locality111 should exist", city);
        assertThat(city.getName(), is("locality111"));
        assertThat(city.getParent(), notNullValue());
        assertThat(city.getParent().getParent(), notNullValue());
        assertThat(city.getParent().getParent().getName(), is("locality1"));
    }

    @Test
    public void findCityByNameCityDoesntExist() throws Exception {
        assertThat("no city localityXYZ should exist",
                localityService.findCityByName("locality1", "locality11", "localityXYZ"), nullValue());
    }

    @Test
    public void findCityByNameDistrictDoesntExist() throws Exception {
        assertThat("no district locality1X should exist",
                localityService.findCityByName("locality1", "locality1X", "locality111"), nullValue());
    }

    @Test
    public void findCityByNameRegionDoesntExist() throws Exception {
        assertThat("no region localityX should exist",
                localityService.findCityByName("localityX", "locality11", "locality111"), nullValue());
    }

    @Test
    public void findDistrictByName() throws Exception {
        final Locality district = localityService.findDistrictByName("locality1", "locality12");
        assertNotNull("district locality12 should exist", district);
        assertThat(district.getName(), is("locality12"));
        assertThat(district.getParent(), notNullValue());
        assertThat(district.getParent().getName(), is("locality1"));
    }

    @Test
    public void findDistrictByNameThatDoesNotExist() throws Exception {
        // note, that "locality111" exists but that's a city so no district should be found
        assertThat("no district locality111 should exist",
                localityService.findDistrictByName("locality1", "locality111"), nullValue());
    }

    @Test
    public void findRegionByName() throws Exception {
        final Locality region = localityService.findRegion("locality2");
        assertNotNull("state locality2 should exist", region);
        assertThat(region.getName(), is("locality2"));
    }

    @Test
    public void findRegionByNameThatDoesNotExist() throws Exception {
        // note, that "locality11" exists but that's a district so no region should be found
        assertThat("no region locality111 should exist",
                localityService.findRegion("locality11"), nullValue());
    }

    @Test
    public void findRegionByAbbreviation() throws Exception {
        // note that abbreviations are case insensitive
        final Locality region = localityService.findRegion("Mm");
        assertNotNull("region abbreviation Mm should represent existing locality2", region);
        assertThat(region.getName(), is("locality2"));
    }

    @Test
    public void findRegionByAbbreviationThatDoesNotExist() throws Exception {
        assertThat("no region with abbreviation XY should exist",
                localityService.findRegion("XY"), nullValue());
    }

    //--------------------- HELPER METHODS -----------------------------------------------------------------------------
    private void checkLocalityById(Long localityId, String expectedName) {
        final Locality loc111 = this.localityService.getLocality(localityId);
        Assert.assertNotNull(loc111);
        Assert.assertEquals(expectedName, loc111.getName());
    }

    /**
     * Basic check of localities of given type.
     *
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
