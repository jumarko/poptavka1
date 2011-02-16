package cz.poptavka.sample.domain.demand;

import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import cz.poptavka.sample.dao.demand.CategoryDao;
import cz.poptavka.sample.domain.common.TreeItem;
import cz.poptavka.sample.service.common.TreeItemService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 6.2.11
 */
@DataSet(path = "CategoryDataSet.xml", dtd = "classpath:test.dtd")
@DirtiesContext
public class CategoryTest extends DBUnitBaseTest {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private TreeItemService treeItemService;


    @Test
    public void testGetChildren() {
        checkGetCategoryChildren("1", 1, "11");
        checkGetCategoryChildren("2", 3, "21", "22", "23");
        checkGetCategoryChildren("3", 1, "31");

        checkGetCategoryChildren("11", 3, "111", "112", "113");
        checkGetCategoryChildren("113", 2, "1131", "1132");

        checkGetCategoryChildren("31", 2, "311", "312");
    }


    @Test
    public void testGetAllChildren() {
        checkGetAllCategoryChildren(null, 16);
        checkGetAllCategoryChildren("11", 5);
        checkGetAllCategoryChildren("113", 2);
        checkGetAllCategoryChildren("2", 3);
    }


    //----------------------------------- HELPER METHODS ---------------------------------------------------------------
    private void checkGetCategoryChildren(String parentCategoryCode,
                                          int expectedChildrenCount, String... expectedChildrenCodes) {
        final Category cat = categoryDao.getCategory(parentCategoryCode);
        Assert.assertNotNull(cat);

        // check children
        final List<TreeItem> catChildren = cat.getChildren();
        Assert.assertEquals(expectedChildrenCount, catChildren.size());
        for (String childCode : expectedChildrenCodes) {
            checkIsCategoryChild(parentCategoryCode, catChildren, childCode);
        }

    }

    private void checkIsCategoryChild(String parentCategoryCode, List<TreeItem> categoryChildren, String childCode) {
        boolean isCatChild = false;

        for (TreeItem catChild : categoryChildren) {
            if (((Category) catChild).getCode().equals(childCode)) {
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
        Assert.assertEquals(subCategoriesCount, allCategories.size()); //15 real categories plus 1 virtual root category
    }


}
