package com.eprovement.poptavka.domain.demand;

import com.eprovement.poptavka.base.integration.DBUnitIntegrationTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.dao.demand.CategoryDao;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.service.common.TreeItemService;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Juraj Martinka
 *         Date: 6.2.11
 */
@DataSet(path = "classpath:com/eprovement/poptavka/domain/demand/CategoryDataSet.xml", dtd = "classpath:test.dtd")
public class CategoryTest extends DBUnitIntegrationTest {

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
    public void testGetAllDescendants() {
        checkGetAllCategoryDescendants(null, 17);
        checkGetAllCategoryDescendants("cat11", 5);
        checkGetAllCategoryDescendants("cat113", 2);
        checkGetAllCategoryDescendants("cat2", 3);
    }

    @Test
    public void testGetChildrenCounts() {
        checkGetCategoryChildren(null, 2);
        checkGetCategoryChildren("cat11", 3);
        checkGetCategoryChildren("cat113", 2);
        checkGetCategoryChildren("cat2", 3);
    }



    @Test
    public void testGetDescendantsWithMaxResults() {
        final ResultCriteria maxResults = new ResultCriteria.Builder()
                .maxResults(4)
                .build();
        checkGetCategoryDescendantsWithAdditionalCriteria(null, maxResults, 4);
        checkGetCategoryDescendantsWithAdditionalCriteria("cat11", maxResults, 4);
        checkGetCategoryDescendantsWithAdditionalCriteria("cat113", maxResults, 2);
        checkGetCategoryDescendantsWithAdditionalCriteria("cat2", maxResults, 3);
    }

    @Test
    public void testGetChildrenWithMaxResults() {
        final ResultCriteria maxResults = new ResultCriteria.Builder()
                .maxResults(2)
                .build();
        checkGetCategoryChildrenWithAdditionalCriteria(null, maxResults, 2);
        checkGetCategoryChildrenWithAdditionalCriteria("cat11", maxResults, 2);
        checkGetCategoryChildrenWithAdditionalCriteria("cat113", maxResults, 2);
        checkGetCategoryChildrenWithAdditionalCriteria("cat2", maxResults, 2);
    }

    @Test
    public void testGetDescendantsWithFirstResultMaxResults() {
        final ResultCriteria maxResults = new ResultCriteria.Builder()
                .maxResults(4)
                .firstResult(2)
                .build();
        checkGetCategoryDescendantsWithAdditionalCriteria(null, maxResults, 4);
        checkGetCategoryDescendantsWithAdditionalCriteria("cat11", maxResults, 3);
        checkGetCategoryDescendantsWithAdditionalCriteria("cat113", maxResults, 0);
        checkGetCategoryDescendantsWithAdditionalCriteria("cat2", maxResults, 1);
    }

    @Test
    public void testGetChildrenWithFirstResultMaxResults() {
        final ResultCriteria maxResults = new ResultCriteria.Builder()
                .maxResults(2)
                .firstResult(1)
                .build();
        checkGetCategoryChildrenWithAdditionalCriteria(null, maxResults, 1);
        checkGetCategoryChildrenWithAdditionalCriteria("cat11", maxResults, 2);
        checkGetCategoryChildrenWithAdditionalCriteria("cat113", maxResults, 1);
        checkGetCategoryChildrenWithAdditionalCriteria("cat2", maxResults, 2);
    }

    @Test
    public void testGetDescendantsWithFirstResultOrderBy() {
        final ResultCriteria criteria = new ResultCriteria.Builder()
                .firstResult(2)
                .orderByColumns(Arrays.asList("name"))
                .build();
        checkGetCategoryDescendantsWithAdditionalCriteria(null, criteria, 15);
        checkGetCategoryDescendantsWithAdditionalCriteria("cat11", criteria, 3);
        checkGetCategoryDescendantsWithAdditionalCriteria("cat113", criteria, 0);
        final List<Category> cat2 = checkGetCategoryDescendantsWithAdditionalCriteria("cat2", criteria, 1);
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
     * Check if category with given <code>categoryCode</code> has expected number of descendants.
     *
     * @param categoryCode code of category that will be checked.
     */
    private void checkGetAllCategoryDescendants(String categoryCode, int subCategoriesCount) {
        Category category = null;
        if (categoryCode != null) {
            category = this.categoryDao.getCategory(categoryCode);
        }
        final List<Category> allCategories = this.treeItemService.getAllDescendants(category, Category.class);
        Assert.assertNotNull(allCategories);
        Assert.assertEquals(subCategoriesCount, allCategories.size());
    }

    /**
     * Check if category with given <code>categoryCode</code> has expected number of children.
     *
     * @param categoryCode code of category that will be checked.
     */
    private void checkGetCategoryChildren(String categoryCode, int subCategoriesCount) {
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
     * has expected number of ALL descendants.
     *
     * @param categoryCode code of category that will be checked.
     */
    private List<Category> checkGetCategoryDescendantsWithAdditionalCriteria(String categoryCode,
                                                                             ResultCriteria resultCriteria,
                                                                             int subCategoriesCount) {
        Category category = null;
        if (categoryCode != null) {
            category = this.categoryDao.getCategory(categoryCode);
        }
        final List<Category> allCategories = this.treeItemService.getAllDescendants(
                category, Category.class, resultCriteria);
        Assert.assertNotNull(allCategories);

        Assert.assertEquals(subCategoriesCount, allCategories.size());
        return allCategories;
    }

    /**
     * Check if category with given <code>categoryCode</code> and satisfying <code>additionaCriteria</code>
     * has expected number of children.
     *
     * @param categoryCode code of category that will be checked.
     */
    private List<Category> checkGetCategoryChildrenWithAdditionalCriteria(String categoryCode,
                                                                             ResultCriteria resultCriteria,
                                                                             int subCategoriesCount) {
        Category category = null;
        if (categoryCode != null) {
            category = this.categoryDao.getCategory(categoryCode);
        }
        final List<Category> childCategories = this.treeItemService.getAllChildren(
                category, Category.class, resultCriteria);
        Assert.assertNotNull(childCategories);

        Assert.assertEquals(subCategoriesCount, childCategories.size());
        return childCategories;
    }


}
