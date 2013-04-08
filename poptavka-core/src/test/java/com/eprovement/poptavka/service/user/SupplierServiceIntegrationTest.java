package com.eprovement.poptavka.service.user;

import com.eprovement.poptavka.base.integration.DBUnitIntegrationTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.address.Address;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.demand.PotentialSupplier;
import com.eprovement.poptavka.domain.enums.LocalityType;
import com.eprovement.poptavka.domain.enums.Period;
import com.eprovement.poptavka.domain.product.UserService;
import com.eprovement.poptavka.domain.register.Registers;
import com.eprovement.poptavka.domain.settings.Notification;
import com.eprovement.poptavka.domain.settings.Settings;
import com.eprovement.poptavka.domain.user.BusinessUserData;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.domain.user.rights.AccessRole;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.service.demand.CategoryService;
import com.eprovement.poptavka.service.demand.PotentialDemandService;
import com.eprovement.poptavka.service.register.RegisterService;
import com.eprovement.poptavka.util.aop.AopUtils;
import com.eprovement.poptavka.util.user.UserTestUtils;
import com.googlecode.genericdao.search.Search;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;

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
public class SupplierServiceIntegrationTest extends DBUnitIntegrationTest {

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
        checkSuppliersForLocalities(2, 1L);
        checkSuppliersForLocalities(1, 11L);
        checkSuppliersForLocalities(0, 121L);
        checkSuppliersForLocalities(3, 2L);
        checkSuppliersForLocalities(0, 214L);
    }


    @Test
    public void testGetSuppliersCountForAllLocalities() {

        final Map<Locality, Long> suppliersCountForAllLocalities =
                this.supplierService.getSuppliersCountForAllLocalities();
        Assert.assertEquals(12, suppliersCountForAllLocalities.size());

        checkSuppliersCountForLocality(1L, 2L, suppliersCountForAllLocalities);
        checkSuppliersCountForLocality(11L, 1L, suppliersCountForAllLocalities);
        checkSuppliersCountForLocality(121L, 0L, suppliersCountForAllLocalities);
        checkSuppliersCountForLocality(2L, 3L, suppliersCountForAllLocalities);
        checkSuppliersCountForLocality(214L, 0L, suppliersCountForAllLocalities);
    }

    @Test
    public void testGetSuppliersCountForLocalities() {
        checkSuppliersCountForLocalities(2, 1L);
        checkSuppliersCountForLocalities(1, 11L);
        checkSuppliersCountForLocalities(0, 121L);
        checkSuppliersCountForLocalities(3, 2L);
        checkSuppliersCountForLocalities(0, 214L);
    }


    @Test
    public void testSuppliersCountWithoutChildrenByLocality() {
        checkSuppliersCountWithoutChildrenByLocality(1L, 1);
        checkSuppliersCountWithoutChildrenByLocality(2L, 1);
        checkSuppliersCountWithoutChildrenByLocality(11L, 1);
        checkSuppliersCountWithoutChildrenByLocality(21L, 1);
        checkSuppliersCountWithoutChildrenByLocality(213L, 1);

        checkSuppliersCountWithoutChildrenByLocality(0L, 0);
        checkSuppliersCountWithoutChildrenByLocality(211L, 0);
        checkSuppliersCountWithoutChildrenByLocality(1211L, 0);
        checkSuppliersCountWithoutChildrenByLocality(12L, 0);
    }


    @Test
    public void testGetSuppliersForCategories() {
        checkSuppliersForCategories(2, 11L);
        checkSuppliersForCategories(2, 3L);
        checkSuppliersForCategories(1, 113L);
        checkSuppliersForCategories(1, 312L);
        checkSuppliersForCategories(0, 2L);
    }

    @Test
    public void testGetSuppliersForCategoriesAndLocalities() {
        Long[] catArr1 = {1L, 2L};
        Long[] locArr1 = {1L, 2L};
        checkSuppliersForCategoriesAndLocalities(2, catArr1,
                locArr1);
        Long[] catArr2 = {312L};
        Long[] locArr2 = {21L};
        checkSuppliersForCategoriesAndLocalities(1, catArr2,
                locArr2);

    }

    @Test
    public void testGetSuppliersCountForCategoriesAndLocalities() {
//        checkSuppliersCountForCategories(2, "11");
        Long[] catArr1 = {1L, 2L};
        Long[] locArr1 = {1L, 2L};
        checkSuppliersCountForCategoriesAndLocalities(2, catArr1,
                locArr1);
        Long[] catArr2 = {312L};
        Long[] locArr2 = {21L};
        checkSuppliersCountForCategoriesAndLocalities(1, catArr2,
                locArr2);
    }

    @Test
    public void testGetSuppliersForCategoriesAndLocalitiesIncludingParents() {
        final Long[] catArr1 = {11L, 312L};
        final Long[] locArr1 = {11L, 21L};
        checkSuppliersForCategoriesAndLocalitiesIncludingParents(3, catArr1, locArr1);

        final Long[] catArr2 = {312L};
        final Long[] locArr2 = {21L};
        checkSuppliersForCategoriesAndLocalitiesIncludingParents(1, catArr2, locArr2);

        final Long[] catArr3 = {1L, 3L};
        final Long[] locArr3 = {2L};
        checkSuppliersForCategoriesAndLocalitiesIncludingParents(0, catArr3, locArr3);
    }


    @Test
    public void testGetSuppliersForAllCategories() {
        final Map<Category, Long> suppliersCountForAllCategories =
                this.supplierService.getSuppliersCountForAllCategories();
        Assert.assertEquals(17, suppliersCountForAllCategories.size());

        checkSuppliersForCategory(11L, 2L, suppliersCountForAllCategories);
        checkSuppliersForCategory(3L, 2L, suppliersCountForAllCategories);
        checkSuppliersForCategory(113L, 1L, suppliersCountForAllCategories);
        checkSuppliersForCategory(312L, 1L, suppliersCountForAllCategories);
        checkSuppliersForCategory(2L, 0L, suppliersCountForAllCategories);
    }


    @Test
    public void testGetSuppliersCountForCategories() {
//        checkSuppliersCountForCategories(2, "11");
        checkSuppliersCountForCategories(2, 3L);
        checkSuppliersCountForCategories(1, 31L);
        checkSuppliersCountForCategories(1, 113L);
        checkSuppliersCountForCategories(1, 312L);
        checkSuppliersCountForCategories(0, 2L);
    }


    @Test
    public void testSuppliersCountWithoutChildrenByCategory() {
        checkSuppliersCountWithoutChildrenByCategory(3L, 1);
        checkSuppliersCountWithoutChildrenByCategory(11L, 1);
        checkSuppliersCountWithoutChildrenByCategory(113L, 1);
        checkSuppliersCountWithoutChildrenByCategory(312L, 1);

        checkSuppliersCountWithoutChildrenByCategory(1L, 0);
        checkSuppliersCountWithoutChildrenByCategory(2L, 0);
        checkSuppliersCountWithoutChildrenByCategory(22L, 0);
        checkSuppliersCountWithoutChildrenByCategory(111L, 0);
        checkSuppliersCountWithoutChildrenByCategory(1131L, 0);
        checkSuppliersCountWithoutChildrenByCategory(1132L, 0);
    }


    @Test
    public void testCreateSupplier() {
        final PotentialDemandService potentialDemandServiceMock = mock(PotentialDemandService.class);
        ((SupplierServiceImpl) AopUtils.unproxy(supplierService)).setPotentialDemandService(potentialDemandServiceMock);

        final Supplier newSupplier = new Supplier();
        newSupplier.getBusinessUser().setEmail("new@supplier.com");
        newSupplier.getBusinessUser().setPassword("myPassword");
        newSupplier.getBusinessUser().setAccessRoles(Arrays.asList(this.generalService.find(AccessRole.class, 1L)));
        final Address supplierAddress = new Address();
        supplierAddress.setCity(this.localityService.getLocality(211L));
        supplierAddress.setStreet("Gotham city");
        supplierAddress.setZipCode("12");
        newSupplier.getBusinessUser().setAddresses(Arrays.asList(supplierAddress));
        newSupplier.getBusinessUser().setBusinessUserData(
                new BusinessUserData.Builder().personFirstName("New").personLastName("Supplier").build());
        newSupplier.getBusinessUser().setSettings(new Settings());
        newSupplier.setLocalities(localityService.getLocalities(LocalityType.CITY));
        newSupplier.setCategories(Arrays.asList(categoryService.getCategory(112L)));
        this.supplierService.create(newSupplier);

        // CHECKS
        final List<Supplier> persistedSuppliers = this.supplierService.searchByCriteria(
                UserSearchCriteria.Builder.userSearchCriteria()
                        .withName("New")
                        .withSurName("Supplier")
                        .build());
        assertNotNull(persistedSuppliers);
        final Supplier createdSupplier = persistedSuppliers.get(0);
        assertNotNull(createdSupplier.getId());
        Assert.assertEquals("new@supplier.com", createdSupplier.getBusinessUser().getEmail());

        // check BusinessUserRole-s
        Assert.assertFalse(createdSupplier.getBusinessUser().getBusinessUserRoles().isEmpty());
        assertThat(createdSupplier.getBusinessUser().getBusinessUserRoles().get(0).getId(),
                is(createdSupplier.getId()));


        final Search userServiceSearch = new Search(UserService.class);
        userServiceSearch.addFilterEqual("user", createdSupplier.getBusinessUser());
        final List<UserService> serviceAssignedToClient = this.generalService.search(userServiceSearch);
        assertNotNull(serviceAssignedToClient.get(0));
        assertThat(serviceAssignedToClient.get(0).getUser().getEmail(), is("new@supplier.com"));
        assertThat(serviceAssignedToClient.get(0).getService().getCode(), is(Registers.Service.CLASSIC));

        // check if new supplier has also all supplier notifications set to the sensible values
        assertNotNull(createdSupplier.getBusinessUser().getSettings());
        checkNotifications(createdSupplier, Registers.Notification.NEW_DEMAND, Registers.Notification.NEW_MESSAGE,
                Registers.Notification.NEW_INFO, Registers.Notification.NEW_MESSAGE_OPERATOR,
                Registers.Notification.OFFER_STATUS_CHANGED);

        // check that potential demand has been sent to supplier immediately after he has been created
        final ArgumentCaptor<Demand> demandCaptor = ArgumentCaptor.forClass(Demand.class);
        final ArgumentCaptor<PotentialSupplier> supplierCaptor = ArgumentCaptor.forClass(PotentialSupplier.class);
        verify(potentialDemandServiceMock).sendDemandToPotentialSupplier(
                demandCaptor.capture(), supplierCaptor.capture());
        assertNotNull("Potential demand cannot be null!", demandCaptor.getValue());
        assertThat("Incorrect demand has been sent to supplier", demandCaptor.getValue().getId(), is(8L));
        assertNotNull("Potential supplier cannot be null!", supplierCaptor.getValue());
        assertThat("Incorrect supplier has been passed",
                supplierCaptor.getValue().getSupplier().getId(), is(newSupplier.getId()));
    }


    @Test
    public void testUpdateSupplier() {
        // company name by which the tested supplier can be found
        final String supplierCompanyName = "My Third Company";

        final List<Supplier> suppliers = this.supplierService.searchByCriteria(
                UserSearchCriteria.Builder.userSearchCriteria()
                        .withCompanyName(supplierCompanyName)
                        .build());
        assertNotNull(suppliers);
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
        assertNotNull(persistedSupplier);
        assertNotNull(persistedSupplier.get(0).getId());
        // check if certification has been changed correctly
        Assert.assertEquals(!isCertified, persistedSupplier.get(0).isCertified());
    }


    //----------------------------------------- HELPER METHODS ---------------------------------------------------------
    private void checkSuppliersForLocalities(int expectedCount, Long... localitiesCodes) {
        final Set<Supplier> suppliers = this.supplierService.getSuppliers(getLocalities(localitiesCodes));
        assertNotNull(suppliers);
        Assert.assertEquals(expectedCount, suppliers.size());
    }

    private void checkSuppliersCountForLocalities(int expectedCount, Long... localitiesCodes) {
        Assert.assertEquals(expectedCount,
                this.supplierService.getSuppliersCount(getLocalities(localitiesCodes)));
    }

    private Locality[] getLocalities(Long[] localitiesCodes) {
        Locality[] localities = new Locality[localitiesCodes.length];
        for (int i = 0; i < localitiesCodes.length; i++) {
            localities[i] = this.localityService.getLocality(localitiesCodes[i]);
        }
        return localities;
    }


    private void checkSuppliersCountWithoutChildrenByLocality(Long localityCode, int expectedCount) {
        final String message = "Locality code [" + localityCode + "]";
        Assert.assertEquals(message,
                expectedCount,
                this.supplierService.getSuppliersCountWithoutChildren(this.localityService.getLocality(localityCode)));
    }


    private void checkSuppliersForCategories(int expectedCount, Long... categoriesIds) {
        final Set<Supplier> suppliers = this.supplierService.getSuppliers(getCategories(categoriesIds));
        assertNotNull(suppliers);
        Assert.assertEquals(expectedCount, suppliers.size());
    }

    private void checkSuppliersCountForCategories(int expectedCount, Long... categoriesIds) {
        Assert.assertEquals(expectedCount,
                this.supplierService.getSuppliersCount(getCategories(categoriesIds)));
    }

    private void checkSuppliersCountWithoutChildrenByCategory(Long categoryId, int expectedCount) {
        final String message = "Category id [" + categoryId + "]";
        Assert.assertEquals(message,
                expectedCount,
                this.supplierService.getSuppliersCountWithoutChildren(this.categoryService.getCategory(categoryId)));
    }

    private Category[] getCategories(Long[] categoriesIds) {
        Category[] categories = new Category[categoriesIds.length];
        for (int i = 0; i < categoriesIds.length; i++) {
            categories[i] = this.categoryService.getCategory(categoriesIds[i]);
        }
        return categories;
    }


    private void checkSuppliersCountForLocality(Long localityCode, Long expectedSupplierCount,
                                                Map<Locality, Long> suppliersCountForAllLocalities) {
        final String message = "Locality code [" + localityCode + "]";
        Assert.assertEquals(message,
                expectedSupplierCount,
                suppliersCountForAllLocalities.get(this.localityService.getLocality(localityCode)));
    }



    private void checkSuppliersForCategory(Long categoryId, Long expectedSuppliersCount,
                                           Map<Category, Long> suppliersCountForAllCategories) {
        final String message = "Category id [" + categoryId + "]";
        Assert.assertEquals(message,
                expectedSuppliersCount,
                suppliersCountForAllCategories.get(this.categoryService.getCategory(categoryId)));
    }


    private void checkSuppliersForCategoriesAndLocalities(int expectedCount,
                                                          Long[] categoryIds, Long[] localityIds) {
        final Set<Supplier> suppliers = this.supplierService.getSuppliers(null,
                Arrays.asList(getCategories(categoryIds)), Arrays.asList(getLocalities(localityIds)));
        assertNotNull(suppliers);
        Assert.assertEquals(expectedCount, suppliers.size());
    }

    private void checkSuppliersCountForCategoriesAndLocalities(int expectedCount,
                                                               Long[] categoryIds, Long[] localityIds) {
        final long suppliersCount = this.supplierService.getSuppliersCount(
                Arrays.asList(getCategories(categoryIds)), Arrays.asList(getLocalities(localityIds)));

        Assert.assertEquals(expectedCount, suppliersCount);
    }

    private void checkSuppliersForCategoriesAndLocalitiesIncludingParents(int expectedCount,
            Long[] categoryIds, Long[] localityIds) {
        final Set<Supplier> suppliers = this.supplierService.getSuppliersIncludingParents(
                Arrays.asList(getCategories(categoryIds)), Arrays.asList(getLocalities(localityIds)), null);
        assertNotNull(suppliers);
        Assert.assertEquals(expectedCount, suppliers.size());
    }

    private void checkNotifications(Supplier createdSupplier, Registers.Notification... notifications) {
        for (Registers.Notification notification : notifications) {
            UserTestUtils.checkHasNotification(createdSupplier.getBusinessUser(),
                    this.registerService.getValue(notification.getCode(), Notification.class), true, Period.DAILY);
        }

    }
}
