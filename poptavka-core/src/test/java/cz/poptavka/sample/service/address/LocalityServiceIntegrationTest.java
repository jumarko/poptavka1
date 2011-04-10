package cz.poptavka.sample.service.address;

import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.service.common.TreeItemService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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


}
