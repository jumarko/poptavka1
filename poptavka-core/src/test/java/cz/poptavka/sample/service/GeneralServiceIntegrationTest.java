package cz.poptavka.sample.service;

import com.googlecode.genericdao.search.Search;
import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.demand.DemandCategory;
import cz.poptavka.sample.service.common.TreeItemService;
import java.util.List;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Only very simple test of {@link GeneralService}.
 * The main goal is verify if configuration of {@link GeneralService} and its internal
 * {@link com.googlecode.genericdao.dao.jpa.GeneralDAO} is OK.
 *
 * @author Juraj Martinka
 *         Date: 3.5.11
 */
@DataSet(path = {
        "classpath:cz/poptavka/sample/domain/address/LocalityDataSet.xml",
        "classpath:cz/poptavka/sample/domain/demand/CategoryDataSet.xml",
        "classpath:cz/poptavka/sample/domain/demand/RatingDataSet.xml",
        "classpath:cz/poptavka/sample/domain/user/UsersDataSet.xml",
        "classpath:cz/poptavka/sample/domain/demand/DemandDataSet.xml" },
        dtd = "classpath:test.dtd")

public class GeneralServiceIntegrationTest extends DBUnitBaseTest {

    @Autowired
    private GeneralService generalService;

    @Autowired
    private TreeItemService treeItemService;


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

    @Test
    public void testSearchDemandsByCategory() {
        final Search demandCategorySearch = new Search(DemandCategory.class);
        final Category cat1 = this.generalService.find(Category.class, 2L);
        Assert.assertNotNull(cat1);
        final List<Category> allSubCategories = this.treeItemService.getAllDescendants(cat1, Category.class);
        allSubCategories.add(cat1);
        Assert.assertThat("There must be 6 subcategories for category with id 2", allSubCategories.size(), Is.is(7));

        demandCategorySearch.addFilterIn("category", allSubCategories);
        demandCategorySearch.addSortAsc("demand.title");
        demandCategorySearch.setFirstResult(0);
        demandCategorySearch.setMaxResults(3);

        final List<DemandCategory> demandsForCategory = this.generalService.search(demandCategorySearch);
        Assert.assertThat("Incorrect number of demands in result set", demandsForCategory.size(), Is.is(3));
        Assert.assertThat("Unexpected demand", demandsForCategory.get(0).getDemand().getId(), Is.is(10L));
        Assert.assertThat("Unexpected demand", demandsForCategory.get(1).getDemand().getId(), Is.is(2L));
        Assert.assertThat("Unexpected demand", demandsForCategory.get(2).getDemand().getId(), Is.is(5L));
    }



    @Test
    public void testSortDeamndsByCreatedDate() {
        final Search demandSearch = new Search(Demand.class);
        // the newest one should be the first
        demandSearch.addSortDesc("createdDate");
        demandSearch.setMaxResults(5);
        final List<Demand> demandsSortedByCreatedDate = this.generalService.search(demandSearch);
        Assert.assertThat(demandsSortedByCreatedDate.size(), Is.is(5));

        Assert.assertThat("Unexpected demand", demandsSortedByCreatedDate.get(0).getId(), Is.is(2L));
        Assert.assertThat("Unexpected demand", demandsSortedByCreatedDate.get(1).getId(), Is.is(1L));
        Assert.assertThat("Unexpected demand", demandsSortedByCreatedDate.get(2).getId(), Is.is(10L));
        Assert.assertThat("Unexpected demand", demandsSortedByCreatedDate.get(3).getId(), Is.is(5L));
        // the last one could be any demand which has an empty createdDate
        Assert.assertNull("Last demand should have an empty createdDate",
                demandsSortedByCreatedDate.get(4).getCreatedDate());
    }



    private void checkLocality(List<Locality> localitySearchResult, String expectedLocalityName, int localityIndex) {
        Assert.assertThat("Incorrect locality",
                localitySearchResult.get(localityIndex).getName(), Is.is(expectedLocalityName));
    }
}
