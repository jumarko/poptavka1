package com.eprovement.poptavka.service.demand;

import com.eprovement.poptavka.base.integration.DBUnitBaseTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.demand.Category;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 26.2.11
 */
@DataSet(path = "classpath:com/eprovement/poptavka/domain/demand/CategoryDataSet.xml", dtd = "classpath:test.dtd")
public class CategoryServiceTest extends DBUnitBaseTest {

    @Autowired
    private CategoryService categoryService;


    @Test
    public void testGetRootCategories() {
        final List<Category> rootCategories = categoryService.getRootCategories();
        Assert.assertNotNull(rootCategories);
        Assert.assertEquals(2, rootCategories.size());
        checkCategory(rootCategories, 0, "cat0", "Root Category");
        checkCategory(rootCategories, 1, "cat00", "Root Category 2");
    }


    public void testGetRootCategoriesWithNoCriteria() {
        final List<Category> rootCategories = categoryService.getRootCategories(ResultCriteria.EMPTY_CRITERIA);
        Assert.assertNotNull(rootCategories);
        Assert.assertEquals(2, rootCategories.size());
        checkCategory(rootCategories, 0, "cat0", "Root Category");
        checkCategory(rootCategories, 1, "cat00", "Root Category 2");
    }

    public void testGetRootCategoriesWithMaxResults() {
        final List<Category> rootCategories = categoryService.getRootCategories(new ResultCriteria.Builder()
                .maxResults(1).build());
        Assert.assertNotNull(rootCategories);
        Assert.assertEquals(1, rootCategories.size());
    }


    public void testGetRootCategoriesWithFirstResultOrderBy() {
        final List<Category> rootCategories = categoryService.getRootCategories(new ResultCriteria.Builder()
                .firstResult(1)
                .orderByColumns(Arrays.asList("name"))
                .build());
        Assert.assertNotNull(rootCategories);
        Assert.assertEquals(1, rootCategories.size());
        checkCategory(rootCategories, 0, "cat00", "Root Category 2");
    }


//---------------------------------------------- HELPER METHODS -------------------------------------------------------

    private void checkCategory(List<Category> rootCategories, int categoryIndex,
                               String expectedCode, String expectedName) {
        Assert.assertEquals(expectedCode, rootCategories.get(categoryIndex).getCode());
        Assert.assertEquals(expectedName, rootCategories.get(categoryIndex).getName());
    }

}
