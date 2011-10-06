package cz.poptavka.sample.service;

import com.googlecode.genericdao.search.Search;
import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import cz.poptavka.sample.domain.address.Locality;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Only very simple test of {@link GeneralService}.
 * The main goal is verify if configuration of {@link GeneralService} and its internal
 * {@link com.googlecode.genericdao.dao.jpa.GeneralDAO} is OK.
 *
 * @author Juraj Martinka
 *         Date: 3.5.11
 */
@DataSet(path = "classpath:cz/poptavka/sample/domain/address/LocalityDataSet.xml",
        dtd = "classpath:test.dtd")
public class GeneralServiceIntegrationTest extends DBUnitBaseTest {


    @Autowired
    private GeneralService generalService;

    @Test
    public void testFindAllLocalities() {
        final List<Locality> allLocalities = this.generalService.findAll(Locality.class);

        Assert.assertEquals("All localities must be found - nothing less nothing more.", 12, allLocalities.size());
    }

    @Test
    public void testSearchLocalities() {
        final Search localitySearch = new Search(Locality.class);
        localitySearch.setFirstResult(0);
        localitySearch.setMaxResults(4);
        localitySearch.addSortAsc("name");

        final List<Locality> localitySearchResult = this.generalService.search(localitySearch);
        Assert.assertNotNull(localitySearchResult);
        Assert.assertThat("Incorrect size of locality search result", localitySearchResult.size(), Is.is(4));

        // check if localities appear in expected order
        checkLocality(localitySearchResult, "Czech Republic", 0);
        checkLocality(localitySearchResult, "locality1", 1);
        checkLocality(localitySearchResult, "locality11", 2);
        checkLocality(localitySearchResult, "locality111", 3);
    }

    private void checkLocality(List<Locality> localitySearchResult, String expectedLocalityName, int localityIndex) {
        Assert.assertThat("Incorrect locality",
                localitySearchResult.get(localityIndex).getName(), Is.is(expectedLocalityName));
    }
}
