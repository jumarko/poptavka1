package cz.poptavka.sample.service.user;

import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.settings.Settings;
import cz.poptavka.sample.domain.user.BusinessUserData;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.service.address.LocalityService;
import cz.poptavka.sample.service.demand.CategoryService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Juraj Martinka
 *         Date: 12.2.11
 */
@DataSet(path = {
        "classpath:cz/poptavka/sample/domain/address/LocalityDataSet.xml",
        "classpath:cz/poptavka/sample/domain/demand/CategoryDataSet.xml",
        "classpath:cz/poptavka/sample/domain/demand/DemandDataSet.xml",
        "classpath:cz/poptavka/sample/domain/user/SupplierDataSet.xml" },
        dtd = "classpath:test.dtd")
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
    public void testGetSuppliersCountForAllLocalities() {

        final Map<Locality, Long> suppliersCountForAllLocalities =
                this.supplierService.getSuppliersCountForAllLocalities();
        Assert.assertEquals(12, suppliersCountForAllLocalities.size());

        checkSuppliersCountForLocality("loc1", 2L, suppliersCountForAllLocalities);
        checkSuppliersCountForLocality("loc11", 1L, suppliersCountForAllLocalities);
        checkSuppliersCountForLocality("loc121", 0L, suppliersCountForAllLocalities);
        checkSuppliersCountForLocality("loc2", 3L, suppliersCountForAllLocalities);
        checkSuppliersCountForLocality("loc214", 0L, suppliersCountForAllLocalities);
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
    public void testSuppliersCountWithoutChildrenByLocality() {
        checkSuppliersCountWithoutChildrenByLocality("loc1", 1);
        checkSuppliersCountWithoutChildrenByLocality("loc2", 1);
        checkSuppliersCountWithoutChildrenByLocality("loc11", 1);
        checkSuppliersCountWithoutChildrenByLocality("loc21", 1);
        checkSuppliersCountWithoutChildrenByLocality("loc213", 1);

        checkSuppliersCountWithoutChildrenByLocality("CZ", 0);
        checkSuppliersCountWithoutChildrenByLocality("loc211", 0);
        checkSuppliersCountWithoutChildrenByLocality("loc1211", 0);
        checkSuppliersCountWithoutChildrenByLocality("loc12", 0);
    }


    @Test
    public void testGetSuppliersForCategories() {
        checkSuppliersForCategories(2, "cat11");
        checkSuppliersForCategories(2, "cat3");
        checkSuppliersForCategories(1, "cat113");
        checkSuppliersForCategories(1, "cat312");
        checkSuppliersForCategories(0, "cat2");
    }

    @Test
    public void testGetSuppliersForAllCategories() {
        final Map<Category, Long> suppliersCountForAllCategories =
                this.supplierService.getSuppliersCountForAllCategories();
        Assert.assertEquals(17, suppliersCountForAllCategories.size());

        checkSuppliersForCategory("cat11", 2L, suppliersCountForAllCategories);
        checkSuppliersForCategory("cat3", 2L, suppliersCountForAllCategories);
        checkSuppliersForCategory("cat113", 1L, suppliersCountForAllCategories);
        checkSuppliersForCategory("cat312", 1L, suppliersCountForAllCategories);
        checkSuppliersForCategory("cat2", 0L, suppliersCountForAllCategories);
    }


    @Test
    public void testGetSuppliersCountForCategories() {
//        checkSuppliersCountForCategories(2, "11");
        checkSuppliersCountForCategories(2, "cat3");
        checkSuppliersCountForCategories(1, "cat31");
        checkSuppliersCountForCategories(1, "cat113");
        checkSuppliersCountForCategories(1, "cat312");
        checkSuppliersCountForCategories(0, "cat2");
    }


    @Test
    public void testSuppliersCountWithoutChildrenByCategory() {
        checkSuppliersCountWithoutChildrenByCategory("cat3", 1);
        checkSuppliersCountWithoutChildrenByCategory("cat11", 1);
        checkSuppliersCountWithoutChildrenByCategory("cat113", 1);
        checkSuppliersCountWithoutChildrenByCategory("cat312", 1);

        checkSuppliersCountWithoutChildrenByCategory("cat1", 0);
        checkSuppliersCountWithoutChildrenByCategory("cat2", 0);
        checkSuppliersCountWithoutChildrenByCategory("cat22", 0);
        checkSuppliersCountWithoutChildrenByCategory("cat111", 0);
        checkSuppliersCountWithoutChildrenByCategory("cat1131", 0);
        checkSuppliersCountWithoutChildrenByCategory("cat1132", 0);
    }


    @Test
    public void testCreateSupplier() {
        final Supplier newSupplier = new Supplier();
        newSupplier.getBusinessUser().setEmail("new@supplier.com");
        newSupplier.getBusinessUser().setBusinessUserData(
                new BusinessUserData.Builder().personFirstName("New").personLastName("Supplier").build());
        newSupplier.getBusinessUser().setSettings(new Settings());
        this.supplierService.create(newSupplier);

        final List<Supplier> persistedSupplier = this.supplierService.searchByCriteria(
                UserSearchCriteria.Builder.userSearchCriteria()
                        .withName("New")
                        .withSurName("Supplier")
                        .build());
        Assert.assertNotNull(persistedSupplier);
        Assert.assertNotNull(persistedSupplier.get(0).getId());
        Assert.assertEquals("new@supplier.com", persistedSupplier.get(0).getBusinessUser().getEmail());
    }



    @Test
    public void testUpdateSupplier() {
        // company name by which the tested supplier can be found
        final String supplierCompanyName = "My Third Company";

        final List<Supplier> suppliers = this.supplierService.searchByCriteria(
                UserSearchCriteria.Builder.userSearchCriteria()
                        .withCompanyName(supplierCompanyName)
                        .build());
        Assert.assertNotNull(suppliers);
        Assert.assertEquals(1, suppliers.size());

        final Supplier supplierToModify = suppliers.get(0);
        // remember original certification of supplier
        final boolean isCertified = supplierToModify.isCertified() != null ? supplierToModify.isCertified() : false;

        // change certification to opposite value
        supplierToModify.setCertified(!isCertified);
        this.supplierService.update(supplierToModify);

        final List<Supplier> persistedSupplier = this.supplierService.searchByCriteria(
                UserSearchCriteria.Builder.userSearchCriteria()
                        .withCompanyName(supplierCompanyName)
                        .build());
        Assert.assertNotNull(persistedSupplier);
        Assert.assertNotNull(persistedSupplier.get(0).getId());
        // check if certification has been changed correctly
        Assert.assertEquals(!isCertified, persistedSupplier.get(0).isCertified());
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


    private void checkSuppliersCountWithoutChildrenByLocality(String localityCode, int expectedCount) {
        final String message = "Locality code [" + localityCode + "]";
        Assert.assertEquals(message,
                expectedCount,
                this.supplierService.getSuppliersCountWithoutChildren(this.localityService.getLocality(localityCode)));
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

    private void checkSuppliersCountWithoutChildrenByCategory(String categoryCode, int expectedCount) {
        final String message = "Category code [" + categoryCode + "]";
        Assert.assertEquals(message,
                expectedCount,
                this.supplierService.getSuppliersCountWithoutChildren(this.categoryService.getCategory(categoryCode)));
    }

    private Category[] getCategories(String[] categoriesCodes) {
        Category[] categories = new Category[categoriesCodes.length];
        for (int i = 0; i < categoriesCodes.length; i++) {
            categories[i] = this.categoryService.getCategory(categoriesCodes[i]);
        }
        return categories;
    }


    private void checkSuppliersCountForLocality(String localityCode, Long expectedSupplierCount,
                                                Map<Locality, Long> suppliersCountForAllLocalities) {
        final String message = "Locality code [" + localityCode + "]";
        Assert.assertEquals(message,
                expectedSupplierCount,
                suppliersCountForAllLocalities.get(this.localityService.getLocality(localityCode)));
    }



    private void checkSuppliersForCategory(String categoryCode, Long expectedSuppliersCount,
                                           Map<Category, Long> suppliersCountForAllCategories) {
        final String message = "Category code [" + categoryCode + "]";
        Assert.assertEquals(message,
                expectedSuppliersCount,
                suppliersCountForAllCategories.get(this.categoryService.getCategory(categoryCode)));
    }
}
