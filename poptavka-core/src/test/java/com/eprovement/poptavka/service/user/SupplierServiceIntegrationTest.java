package com.eprovement.poptavka.service.user;

import com.eprovement.poptavka.domain.address.Address;
import com.eprovement.poptavka.domain.enums.LocalityType;
import com.eprovement.poptavka.domain.user.rights.AccessRole;
import com.googlecode.genericdao.search.Search;
import com.eprovement.poptavka.base.integration.DBUnitBaseTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.product.UserService;
import com.eprovement.poptavka.domain.register.Registers;
import com.eprovement.poptavka.domain.settings.Notification;
import com.eprovement.poptavka.domain.enums.Period;
import com.eprovement.poptavka.domain.settings.Settings;
import com.eprovement.poptavka.domain.user.BusinessUserData;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.service.demand.CategoryService;
import com.eprovement.poptavka.service.register.RegisterService;
import com.eprovement.poptavka.util.user.UserTestUtils;
import static org.hamcrest.core.Is.is;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Juraj Martinka
 *         Date: 12.2.11
 */
@DataSet(path = {
        "classpath:com/eprovement/poptavka/domain/address/LocalityDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/CategoryDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/RatingDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/UsersDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/DemandDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/SupplierDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/register/RegisterDataSet.xml" },
        dtd = "classpath:test.dtd")
public class SupplierServiceIntegrationTest extends DBUnitBaseTest {

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private LocalityService localityService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private GeneralService generalService;

    @Autowired
    private RegisterService registerService;


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
    public void testGetSuppliersForCategoriesAndLocalities() {
        String[] catArr1 = {"cat1", "cat2"};
        String[] locArr1 = {"loc1", "loc2"};
        checkSuppliersForCategoriesAndLocalities(2, catArr1,
                locArr1);
        String[] catArr2 = {"cat312"};
        String[] locArr2 = {"loc21"};
        checkSuppliersForCategoriesAndLocalities(1, catArr2,
                locArr2);

    }

    @Test
    public void testGetSuppliersCountForCategoriesAndLocalities() {
//        checkSuppliersCountForCategories(2, "11");
        String[] catArr1 = {"cat1", "cat2"};
        String[] locArr1 = {"loc1", "loc2"};
        checkSuppliersCountForCategoriesAndLocalities(2, catArr1,
                locArr1);
        String[] catArr2 = {"cat312"};
        String[] locArr2 = {"loc21"};
        checkSuppliersCountForCategoriesAndLocalities(1, catArr2,
                locArr2);
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
        newSupplier.getBusinessUser().setPassword("myPassword");
        newSupplier.getBusinessUser().setAccessRoles(Arrays.asList(this.generalService.find(AccessRole.class, 1L)));
        final Address clientAddress = new Address();
        clientAddress.setCity(this.localityService.getLocality("loc211"));
        clientAddress.setStreet("Gotham city");
        clientAddress.setZipCode("12");
        newSupplier.getBusinessUser().setAddresses(Arrays.asList(clientAddress));
        newSupplier.getBusinessUser().setBusinessUserData(
                new BusinessUserData.Builder().personFirstName("New").personLastName("Supplier").build());
        newSupplier.getBusinessUser().setSettings(new Settings());
        newSupplier.setLocalities(localityService.getLocalities(LocalityType.CITY));
        newSupplier.setCategories(categoryService.getRootCategories());
        this.supplierService.create(newSupplier);

        final List<Supplier> persistedSuppliers = this.supplierService.searchByCriteria(
                UserSearchCriteria.Builder.userSearchCriteria()
                        .withName("New")
                        .withSurName("Supplier")
                        .build());
        Assert.assertNotNull(persistedSuppliers);
        final Supplier createdSupplier = persistedSuppliers.get(0);
        Assert.assertNotNull(createdSupplier.getId());
        Assert.assertEquals("new@supplier.com", createdSupplier.getBusinessUser().getEmail());

        // check BusinessUserRole-s
        Assert.assertFalse(createdSupplier.getBusinessUser().getBusinessUserRoles().isEmpty());
        Assert.assertThat(createdSupplier.getBusinessUser().getBusinessUserRoles().get(0).getId(),
                is(createdSupplier.getId()));


        final Search userServiceSearch = new Search(UserService.class);
        userServiceSearch.addFilterEqual("user", createdSupplier.getBusinessUser());
        final List<UserService> serviceAssignedToClient = this.generalService.search(userServiceSearch);
        Assert.assertNotNull(serviceAssignedToClient.get(0));
        Assert.assertThat(serviceAssignedToClient.get(0).getUser().getEmail(), is("new@supplier.com"));
        Assert.assertThat(serviceAssignedToClient.get(0).getService().getCode(), is(Registers.Service.CLASSIC));

        // check if new supplier has also all supplier notifications set to the sensible values
        Assert.assertNotNull(createdSupplier.getBusinessUser().getSettings());
        UserTestUtils.checkHasNotification(createdSupplier.getBusinessUser(),
                this.registerService.getValue(Registers.Notification.SUPPLIER_NEW_DEMAND, Notification.class),
                true, Period.INSTANTLY);
        UserTestUtils.checkHasNotification(createdSupplier.getBusinessUser(),
                this.registerService.getValue(Registers.Notification.SUPPLIER_NEW_MESSAGE, Notification.class),
                true, Period.INSTANTLY);
        UserTestUtils.checkHasNotification(createdSupplier.getBusinessUser(),
                this.registerService.getValue(Registers.Notification.SUPPLIER_NEW_OPERATOR, Notification.class),
                true, Period.INSTANTLY);
        UserTestUtils.checkHasNotification(createdSupplier.getBusinessUser(),
                this.registerService.getValue(Registers.Notification.SUPPLIER_NEW_INFO, Notification.class),
                false, Period.INSTANTLY);
        UserTestUtils.checkHasNotification(createdSupplier.getBusinessUser(),
                this.registerService.getValue(Registers.Notification.SUPPLIER_OFFER_STATUS_CHANGED, Notification.class),
                false, Period.INSTANTLY);
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


    @Ignore
    @Test
    public void testSortSuppliersByCompanyName() {
        final ResultCriteria sortByCompanyNameCriteria = new ResultCriteria.Builder()
                .firstResult(0)
                .maxResults(2)
                .orderByColumns(Arrays.asList("businessUser.businessUserData.companyName"))
                .build();

        final Set<Supplier> loc1SuppliersByCompanyName = this.supplierService.getSuppliers(sortByCompanyNameCriteria,
                this.localityService.getLocality("loc1"));


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

    private void checkSuppliersForCategoriesAndLocalities(int expectedCount,
            String[] categoryCodes, String[] localityCodes) {
        final Set<Supplier> suppliers = this.supplierService.getSuppliers(null,
                getCategories(categoryCodes), getLocalities(localityCodes));
        Assert.assertNotNull(suppliers);
        Assert.assertEquals(expectedCount, suppliers.size());
    }

    private void checkSuppliersCountForCategoriesAndLocalities(int expectedCount,
            String[] categoryCodes, String[] localityCodes) {
        final long suppliersCount = this.supplierService.getSuppliersCount(
                getCategories(categoryCodes), getLocalities(localityCodes));

        Assert.assertEquals(expectedCount, suppliersCount);
    }

}
