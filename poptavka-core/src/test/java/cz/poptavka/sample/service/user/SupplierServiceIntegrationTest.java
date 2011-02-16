package cz.poptavka.sample.service.user;

import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.service.address.LocalityService;
import cz.poptavka.sample.service.demand.CategoryService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Set;

/**
 * @author Juraj Martinka
 *         Date: 12.2.11
 */
@DataSet(path = {
        "classpath:cz/poptavka/sample/base/BaseDataSet.xml",
        "classpath:cz/poptavka/sample/domain/demand/CategoryDataSet.xml",
        "classpath:cz/poptavka/sample/domain/user/SupplierDataSet.xml" },
        dtd = "classpath:test.dtd")
@DirtiesContext
public class SupplierServiceIntegrationTest extends DBUnitBaseTest {

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private LocalityService localityService;

    @Autowired
    private CategoryService categoryService;


    @Test
    public void testGetSuppliersForLocalities() {
        checkSuppliersForLocalities(2, "loc1");
        checkSuppliersForLocalities(1, "loc11");
        checkSuppliersForLocalities(0, "loc121");
        checkSuppliersForLocalities(3, "loc2");
        checkSuppliersForLocalities(0, "loc214");
    }



    @Test
    public void testGetSuppliersCountForLocalities() {
        checkSuppliersCountForLocalities(2, "loc1");
        checkSuppliersCountForLocalities(1, "loc11");
        checkSuppliersCountForLocalities(0, "loc121");
        checkSuppliersCountForLocalities(3, "loc2");
        checkSuppliersCountForLocalities(0, "loc214");
    }



    @Test
    public void testGetSuppliersForCategories() {
        checkSuppliersForCategories(2, "11");
        checkSuppliersForCategories(2, "3");
        checkSuppliersForCategories(1, "113");
        checkSuppliersForCategories(1, "312");
        checkSuppliersForCategories(0, "2");
    }

    @Test
    public void testGetSuppliersCountForCategories() {
//        checkSuppliersCountForCategories(2, "11");
        checkSuppliersCountForCategories(2, "3");
        checkSuppliersCountForCategories(1, "31");
        checkSuppliersCountForCategories(1, "113");
        checkSuppliersCountForCategories(1, "312");
        checkSuppliersCountForCategories(0, "2");
    }


    //----------------------------------------- HELPER METHODS ---------------------------------------------------------
    private void checkSuppliersForLocalities(int expectedCount, String... localitiesCodes) {
        final Set<Supplier> suppliers = this.supplierService.getSuppliers(getLocalities(localitiesCodes));
        Assert.assertNotNull(suppliers);
        Assert.assertEquals(expectedCount, suppliers.size());
    }

    private void checkSuppliersCountForLocalities(int expectedCount, String... localitiesCodes) {
        Assert.assertEquals(expectedCount,
                this.supplierService.getSuppliersCount(getLocalities(localitiesCodes)));
    }

    private Locality[] getLocalities(String[] localitiesCodes) {
        Locality[] localities = new Locality[localitiesCodes.length];
        for (int i = 0; i < localitiesCodes.length; i++) {
            localities[i] = this.localityService.getLocality(localitiesCodes[i]);
        }
        return localities;
    }

    private void checkSuppliersForCategories(int expectedCount, String... categoriesCodes) {
        final Set<Supplier> suppliers = this.supplierService.getSuppliers(getCategories(categoriesCodes));
        Assert.assertNotNull(suppliers);
        Assert.assertEquals(expectedCount, suppliers.size());
    }

    private void checkSuppliersCountForCategories(int expectedCount, String... categoriesCodes) {
        Assert.assertEquals(expectedCount,
                this.supplierService.getSuppliersCount(getCategories(categoriesCodes)));
    }

    private Category[] getCategories(String[] categoriesCodes) {
        Category[] categories = new Category[categoriesCodes.length];
        for (int i = 0; i < categoriesCodes.length; i++) {
            categories[i] = this.categoryService.getCategory(categoriesCodes[i]);
        }
        return categories;
    }
}
