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
        checkGetCategoryChildren(1L, 1, 11L);
        checkGetCategoryChildren(2L, 3, 21L, 22L, 23L);
        checkGetCategoryChildren(3L, 1, 31L);

        checkGetCategoryChildren(11L, 3, 111L, 112L, 113L);
        checkGetCategoryChildren(113L, 2, 1131L, 1132L);

        checkGetCategoryChildren(31L, 2, 311L, 312L);
    }


    @Test
    public void testGetAllDescendants() {
        checkGetAllCategoryDescendants(null, 17);
        checkGetAllCategoryDescendants(11L, 5);
        checkGetAllCategoryDescendants(113L, 2);
        checkGetAllCategoryDescendants(2L, 3);
    }

    @Test
    public void testGetChildrenCounts() {
        checkGetCategoryChildren(null, 2);
        checkGetCategoryChildren(11L, 3);
        checkGetCategoryChildren(113L, 2);
        checkGetCategoryChildren(2L, 3);
    }



    @Test
    public void testGetDescendantsWithMaxResults() {
        final ResultCriteria maxResults = new ResultCriteria.Builder()
                .maxResults(4)
                .build();
        checkGetCategoryDescendantsWithAdditionalCriteria(null, maxResults, 4);
        checkGetCategoryDescendantsWithAdditionalCriteria(11L, maxResults, 4);
        checkGetCategoryDescendantsWithAdditionalCriteria(113L, maxResults, 2);
        checkGetCategoryDescendantsWithAdditionalCriteria(2L, maxResults, 3);
    }

    @Test
    public void testGetChildrenWithMaxResults() {
        final ResultCriteria maxResults = new ResultCriteria.Builder()
                .maxResults(2)
                .build();
        checkGetCategoryChildrenWithAdditionalCriteria(null, maxResults, 2);
        checkGetCategoryChildrenWithAdditionalCriteria(11L, maxResults, 2);
        checkGetCategoryChildrenWithAdditionalCriteria(113L, maxResults, 2);
        checkGetCategoryChildrenWithAdditionalCriteria(2L, maxResults, 2);
    }

    @Test
    public void testGetDescendantsWithFirstResultMaxResults() {
        final ResultCriteria maxResults = new ResultCriteria.Builder()
                .maxResults(4)
                .firstResult(2)
                .build();
        checkGetCategoryDescendantsWithAdditionalCriteria(null, maxResults, 4);
        checkGetCategoryDescendantsWithAdditionalCriteria(11L, maxResults, 3);
        checkGetCategoryDescendantsWithAdditionalCriteria(113L, maxResults, 0);
        checkGetCategoryDescendantsWithAdditionalCriteria(2L, maxResults, 1);
    }

    @Test
    public void testGetChildrenWithFirstResultMaxResults() {
        final ResultCriteria maxResults = new ResultCriteria.Builder()
                .maxResults(2)
                .firstResult(1)
                .build();
        checkGetCategoryChildrenWithAdditionalCriteria(null, maxResults, 1);
        checkGetCategoryChildrenWithAdditionalCriteria(11L, maxResults, 2);
        checkGetCategoryChildrenWithAdditionalCriteria(113L, maxResults, 1);
        checkGetCategoryChildrenWithAdditionalCriteria(2L, maxResults, 2);
    }

    @Test
    public void testGetDescendantsWithFirstResultOrderBy() {
        final ResultCriteria criteria = new ResultCriteria.Builder()
                .firstResult(2)
                .orderByColumns(Arrays.asList("name"))
                .build();
        checkGetCategoryDescendantsWithAdditionalCriteria(null, criteria, 15);
        checkGetCategoryDescendantsWithAdditionalCriteria(11L, criteria, 3);
        checkGetCategoryDescendantsWithAdditionalCriteria(113L, criteria, 0);
        final List<Category> cat2 = checkGetCategoryDescendantsWithAdditionalCriteria(2L, criteria, 1);
        Assert.assertEquals("Category 23", (cat2.get(0).getName()));
    }

    //----------------------------------- HELPER METHODS ---------------------------------------------------------------
    private void checkGetCategoryChildren(Long parentCategoryId,
                                          int expectedChildrenCount, Long... expectedChildrenIds) {
        final Category cat = categoryDao.getCategory(parentCategoryId);
        Assert.assertNotNull(cat);

        // check children
        final List<Category> catChildren = cat.getChildren();
        Assert.assertEquals(expectedChildrenCount, catChildren.size());
        for (Long childId : expectedChildrenIds) {
            checkIsCategoryChild(parentCategoryId, catChildren, childId);
        }

    }

    private void checkIsCategoryChild(Long parentCategoryId, List<Category> categoryChildren, Long childId) {
        boolean isCatChild = false;

        for (Category catChild : categoryChildren) {
            if (catChild.getId().equals(childId)) {
                isCatChild = true;
                break;
            }
        }

        Assert.assertTrue("Category with id [" + childId + "] is not in children of category ["
                + parentCategoryId + "] as expected",
                isCatChild);
        // continue to test another expected category code
    }


    /**
     * Check if category with given <code>categoryCode</code> has expected number of descendants.
     *
     * @param categoryCode code of category that will be checked.
     */
    private void checkGetAllCategoryDescendants(Long categoryId, int subCategoriesCount) {
        Category category = null;
        if (categoryId != null) {
            category = this.categoryDao.getCategory(categoryId);
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
    private void checkGetCategoryChildren(Long categoryId, int subCategoriesCount) {
        Category category = null;
        if (categoryId != null) {
            category = this.categoryDao.getCategory(categoryId);
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
    private List<Category> checkGetCategoryDescendantsWithAdditionalCriteria(Long categoryId,
                                                                             ResultCriteria resultCriteria,
                                                                             int subCategoriesCount) {
        Category category = null;
        if (categoryId != null) {
            category = this.categoryDao.getCategory(categoryId);
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
    private List<Category> checkGetCategoryChildrenWithAdditionalCriteria(Long categoryId,
                                                                             ResultCriteria resultCriteria,
                                                                             int subCategoriesCount) {
        Category category = null;
        if (categoryId != null) {
            category = this.categoryDao.getCategory(categoryId);
        }
        final List<Category> childCategories = this.treeItemService.getAllChildren(
                category, Category.class, resultCriteria);
        Assert.assertNotNull(childCategories);

        Assert.assertEquals(subCategoriesCount, childCategories.size());
        return childCategories;
    }


}
