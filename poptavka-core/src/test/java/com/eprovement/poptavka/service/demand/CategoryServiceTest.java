package com.eprovement.poptavka.service.demand;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import com.eprovement.poptavka.base.integration.DBUnitIntegrationTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.demand.Category;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Juraj Martinka
 *         Date: 26.2.11
 */
@DataSet(path = "classpath:com/eprovement/poptavka/domain/demand/CategoryDataSet.xml", dtd = "classpath:test.dtd")
public class CategoryServiceTest extends DBUnitIntegrationTest {

    @Autowired
    private CategoryService categoryService;


    @Test
    public void testGetRootCategories() {
        final List<Category> rootCategories = categoryService.getRootCategories();
        Assert.assertNotNull(rootCategories);
        Assert.assertEquals(2, rootCategories.size());
        checkCategory(rootCategories, 0, 0L, "Root Category");
        checkCategory(rootCategories, 1, 9L, "Root Category 2");
    }

    public void testGetRootCategoriesWithNoCriteria() {
        final List<Category> rootCategories = categoryService.getRootCategories(ResultCriteria.EMPTY_CRITERIA);
        Assert.assertNotNull(rootCategories);
        Assert.assertEquals(2, rootCategories.size());
        checkCategory(rootCategories, 0, 0L, "Root Category");
        checkCategory(rootCategories, 1, 0L, "Root Category 2");
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
        checkCategory(rootCategories, 0, 9L, "Root Category 2");
    }

    @Test
    public void testGetCategoriesByMaxLengthExcl() {
        List<Category> categories = categoryService.getCategoriesByMaxLengthExcl(11, "Cate");
        Assert.assertEquals(3, categories.size());
        checkCategory(categories, "Category 1");
        checkCategory(categories, "Category 2");
        checkCategory(categories, "Category 3");
        categories = categoryService.getCategoriesByMaxLengthExcl(10, "Cate");
        Assert.assertTrue(categories.isEmpty());
        categories = categoryService.getCategoriesByMaxLengthExcl(12, "cate");
        assertThat(categories.size(), is(8));
        checkCategory(categories, "Category 1");
        checkCategory(categories, "Category 2");
        checkCategory(categories, "Category 3");
        checkCategory(categories, "Category 11");
        checkCategory(categories, "Category 21");
        checkCategory(categories, "Category 22");
        checkCategory(categories, "Category 23");
        checkCategory(categories, "Category 31");
    }

    @Test
    public void testGetCategoriesByMaxLengthExclWithSearchStringInsideName() throws Exception {
        final List<Category> categories = categoryService.getCategoriesByMaxLengthExcl(12, "ory 2");
        assertThat(categories.size(), is(4));
        checkCategory(categories, "Category 2");
        checkCategory(categories, "Category 21");
        checkCategory(categories, "Category 22");
        checkCategory(categories, "Category 23");
    }

    @Test
    public void testGetCategoriesByMinLength() {
        List<Category> categories = categoryService.getCategoriesByMinLength(11, "rooT Ca");
        assertThat(categories.size(), is(2));
        checkCategory(categories, "Root Category 2");
        checkCategory(categories, "Root Category");
        categories = categoryService.getCategoriesByMinLength(14, "category 1");
        Assert.assertTrue(categories.isEmpty());
        categories = categoryService.getCategoriesByMinLength(12, "Category 3");
        assertThat(categories.size(), is(2));
        checkCategory(categories, "Category 312");
        checkCategory(categories, "Category 311");
    }

    @Test
    public void testGetCategoriesByMinLengthWithSearchStringInsideName() {
        final List<Category> categories = categoryService.getCategoriesByMinLength(11, "ot Cat");
        assertThat(categories.size(), is(2));
        checkCategory(categories, "Root Category 2");
        checkCategory(categories, "Root Category");
    }

    @Test
    public void testGetCategoryBySicCode() throws Exception {
        checkCategoryBySicCode("01", "Category 1");
        checkCategoryBySicCode("011", "Category 1");
        checkCategoryBySicCode("0111abcx", "Category 1");
        checkCategoryBySicCode("51", "Category 3");
    }

    @Test
    public void testGetCategoryByInvalidSicCode() throws Exception {
        assertThat(categoryService.getCategoryBySicCode("19"), nullValue());
        assertThat(categoryService.getCategoryBySicCode("ax"), nullValue());
    }

    //---------------------------------------------- HELPER METHODS ----------------------------------------------------

    private void checkCategory(List<Category> rootCategories, int categoryIndex,
                               Long expectedId, String expectedName) {
        Assert.assertEquals(expectedId, rootCategories.get(categoryIndex).getId());
        Assert.assertEquals(expectedName, rootCategories.get(categoryIndex).getName());
    }

    private void checkCategory(List<Category> allCategories, final String expectedName) {
        Assert.assertTrue(CollectionUtils.exists(allCategories, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                return expectedName.equals(((Category) object).getName());
            }
        }));
    }

    private void checkCategoryBySicCode(String sicCode, String expectedCategoryName) {
        final Category category = categoryService.getCategoryBySicCode(sicCode);
        assertNotNull("category for sic code=" + sicCode + " should not be null", category);
        assertThat(category.getName(), is(expectedCategoryName));
    }


}
