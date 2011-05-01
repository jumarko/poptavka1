package cz.poptavka.sample.domain.demand;

import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import cz.poptavka.sample.common.ResultCriteria;
import cz.poptavka.sample.dao.demand.CategoryDao;
import cz.poptavka.sample.service.common.TreeItemService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 6.2.11
 */
@DataSet(path = "classpath:cz/poptavka/sample/domain/demand/CategoryDataSet.xml", dtd = "classpath:test.dtd")
public class CategoryTest extends DBUnitBaseTest {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private TreeItemService treeItemService;


    @Test
    public void testGetChildren() {
        checkGetCategoryChildren("cat1", 1, "cat11");
        checkGetCategoryChildren("cat2", 3, "cat21", "cat22", "cat23");
        checkGetCategoryChildren("cat3", 1, "cat31");

        checkGetCategoryChildren("cat11", 3, "cat111", "cat112", "cat113");
        checkGetCategoryChildren("cat113", 2, "cat1131", "cat1132");

        checkGetCategoryChildren("cat31", 2, "cat311", "cat312");
    }


    @Test
    public void testGetAllChildren() {
        checkGetAllCategoryChildren(null, 17);
        checkGetAllCategoryChildren("cat11", 5);
        checkGetAllCategoryChildren("cat113", 2);
        checkGetAllCategoryChildren("cat2", 3);
    }


    @Test
    public void testGetAllChildrenWithMaxResults() {
        final ResultCriteria maxResults = new ResultCriteria.Builder()
                .maxResults(4)
                .build();
        checkGetAllCategoryChildrenWithAdditionalCriteria(null, maxResults, 4);
        checkGetAllCategoryChildrenWithAdditionalCriteria("cat11", maxResults, 4);
        checkGetAllCategoryChildrenWithAdditionalCriteria("cat113", maxResults, 2);
        checkGetAllCategoryChildrenWithAdditionalCriteria("cat2", maxResults, 3);
    }

    @Test
    public void testGetAllChildrenWithFirstResultMaxResults() {
        final ResultCriteria maxResults = new ResultCriteria.Builder()
                .maxResults(4)
                .firstResult(2)
                .build();
        checkGetAllCategoryChildrenWithAdditionalCriteria(null, maxResults, 4);
        checkGetAllCategoryChildrenWithAdditionalCriteria("cat11", maxResults, 3);
        checkGetAllCategoryChildrenWithAdditionalCriteria("cat113", maxResults, 0);
        checkGetAllCategoryChildrenWithAdditionalCriteria("cat2", maxResults, 1);
    }

    @Test
    public void testGetAllChildrenWithFirstResultOrderBy() {
        final ResultCriteria criteria = new ResultCriteria.Builder()
                .firstResult(2)
                .orderByColumns(Arrays.asList("name"))
                .build();
        checkGetAllCategoryChildrenWithAdditionalCriteria(null, criteria, 15);
        checkGetAllCategoryChildrenWithAdditionalCriteria("cat11", criteria, 3);
        checkGetAllCategoryChildrenWithAdditionalCriteria("cat113", criteria, 0);
        final List<Category> cat2 = checkGetAllCategoryChildrenWithAdditionalCriteria("cat2", criteria, 1);
        Assert.assertEquals("Category 23", (cat2.get(0).getName()));
    }


    //----------------------------------- HELPER METHODS ---------------------------------------------------------------
    private void checkGetCategoryChildren(String parentCategoryCode,
                                          int expectedChildrenCount, String... expectedChildrenCodes) {
        final Category cat = categoryDao.getCategory(parentCategoryCode);
        Assert.assertNotNull(cat);

        // check children
        final List<Category> catChildren = cat.getChildren();
        Assert.assertEquals(expectedChildrenCount, catChildren.size());
        for (String childCode : expectedChildrenCodes) {
            checkIsCategoryChild(parentCategoryCode, catChildren, childCode);
        }

    }

    private void checkIsCategoryChild(String parentCategoryCode, List<Category> categoryChildren, String childCode) {
        boolean isCatChild = false;

        for (Category catChild : categoryChildren) {
            if (catChild.getCode().equals(childCode)) {
                isCatChild = true;
                break;
            }
        }

        Assert.assertTrue("Category with code [" + childCode + "] is not in children of category ["
                + parentCategoryCode + "] as expected",
                isCatChild);
        // continue to test another expected category code
    }


    /**
     * Check if category with given <code>categoryCode</code> has expected number of ALL children.
     *
     * @param categoryCode code of category that will be checked.
     */
    private void checkGetAllCategoryChildren(String categoryCode, int subCategoriesCount) {
        Category category = null;
        if (categoryCode != null) {
            category = this.categoryDao.getCategory(categoryCode);
        }
        final List<Category> allCategories = this.treeItemService.getAllChildren(category, Category.class);
        Assert.assertNotNull(allCategories);
        Assert.assertEquals(subCategoriesCount, allCategories.size());
    }


    /**
     * Check if category with given <code>categoryCode</code> and satisfying <code>additionaCriteria</code>
     * has expected number of ALL children.
     *
     * @param categoryCode code of category that will be checked.
     */
    private List<Category> checkGetAllCategoryChildrenWithAdditionalCriteria(String categoryCode,
                                                                             ResultCriteria resultCriteria,
                                                                             int subCategoriesCount) {
        Category category = null;
        if (categoryCode != null) {
            category = this.categoryDao.getCategory(categoryCode);
        }
        final List<Category> allCategories = this.treeItemService.getAllChildren(
                category, Category.class, resultCriteria);
        Assert.assertNotNull(allCategories);
        //15 real categories plus 2 virtual root categories
        Assert.assertEquals(subCategoriesCount, allCategories.size());
        return allCategories;
    }

}
