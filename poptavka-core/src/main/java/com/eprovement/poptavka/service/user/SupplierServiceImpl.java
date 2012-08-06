package com.eprovement.poptavka.service.user;

import com.eprovement.poptavka.domain.enums.CommonAccessRoles;
import com.eprovement.poptavka.domain.user.rights.AccessRole;
import com.google.common.base.Preconditions;
import com.googlecode.ehcache.annotations.Cacheable;
import com.eprovement.poptavka.dao.user.SupplierDao;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.register.Registers;
import com.eprovement.poptavka.domain.settings.NotificationItem;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.demand.DemandService;
import com.eprovement.poptavka.service.register.RegisterService;
import com.eprovement.poptavka.util.notification.NotificationUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Juraj Martinka
 *         Date: 12.2.11
 */
public class SupplierServiceImpl extends BusinessUserRoleServiceImpl<Supplier, SupplierDao> implements SupplierService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SupplierServiceImpl.class);

    private final NotificationUtils notificationUtils;
    private final List<AccessRole> supplierAccessRoles;

    public SupplierServiceImpl(GeneralService generalService, SupplierDao supplierDao,
            RegisterService registerService, BusinessUserVerificationService userVerificationService) {
        super(generalService, registerService, userVerificationService);
        Preconditions.checkNotNull(supplierDao);
        setDao(supplierDao);
        this.notificationUtils = new NotificationUtils(registerService);
        this.supplierAccessRoles = Arrays.asList(
                getRegisterService().getValue(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE, AccessRole.class));
    }

    @Override
    public Supplier create(Supplier businessUserRole) {
        LOGGER.info("action=create_supplier status=start supplier={}", businessUserRole);
        createDefaultNotifications(businessUserRole);
        createDefaultAccessRole(businessUserRole);
        final Supplier createdSupplier = super.create(businessUserRole);
        LOGGER.info("action=create_supplier status=finish supplier={}", createdSupplier);
        return createdSupplier;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Set<Supplier> getSuppliers(Locality... localities) {
        return getSuppliers(ResultCriteria.EMPTY_CRITERIA, localities);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Set<Supplier> getSuppliers(ResultCriteria resultCriteria, Locality... localities) {
        return this.getDao().getSuppliers(localities, resultCriteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Set<Supplier> getSuppliers(ResultCriteria resultCriteria,
                                      Category[] categories, Locality[] localities) {
        return this.getDao().getSuppliers(categories, localities,
                resultCriteria);
    }


    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public Map<Locality, Long> getSuppliersCountForAllLocalities() {
        final List<Map<String, Object>> suppliersCountForAllLocalities =
                this.getDao().getSuppliersCountForAllLocalities();

        // convert to suitable Map: <locality, suppliersCountForLocality>
        final Map<Locality, Long> suppliersCountForLocalitiesMap =
                new HashMap<Locality, Long>(DemandService.ESTIMATED_NUMBER_OF_LOCALITIES);
        for (Map<String, Object> suppliersCountForLocality : suppliersCountForAllLocalities) {
            suppliersCountForLocalitiesMap.put((Locality) suppliersCountForLocality.get("locality"),
                    (Long) suppliersCountForLocality.get("suppliersCount"));
        }

        return suppliersCountForLocalitiesMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public long getSuppliersCount(Locality... localities) {
        return this.getDao().getSuppliersCount(localities);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public long getSuppliersCountQuick(Locality locality) {
        return this.getDao().getSuppliersCountQuick(locality);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public long getSuppliersCountWithoutChildren(Locality locality) {
        return this.getDao().getSuppliersCountWithoutChildren(locality);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Set<Supplier> getSuppliers(Category... categories) {
        return getSuppliers(ResultCriteria.EMPTY_CRITERIA, categories);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Supplier> getSuppliers(ResultCriteria resultCriteria, Category... categories) {
        return this.getDao().getSuppliers(categories, resultCriteria);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public Map<Category, Long> getSuppliersCountForAllCategories() {
        final List<Map<String, Object>> suppliersCountForAllCategories =
                this.getDao().getSuppliersCountForAllCategories();

        // convert to suitable Map: <locality, suppliersCountForLocality>
        final Map<Category, Long> suppliersCountForCategoriesMap =
                new HashMap<Category, Long>(DemandService.ESTIMATED_NUMBER_OF_CATEGORIES);
        for (Map<String, Object> suppliersCountForCategory : suppliersCountForAllCategories) {
            suppliersCountForCategoriesMap.put((Category) suppliersCountForCategory.get("category"),
                    (Long) suppliersCountForCategory.get("suppliersCount"));
        }

        return suppliersCountForCategoriesMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(cacheName = "cache5min")
    @Transactional(readOnly = true)
    public long getSuppliersCount(Category... categories) {
        return this.getDao().getSuppliersCount(categories);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public long getSuppliersCount(Category[] categories, Locality[] localities) {
        return this.getDao().getSuppliersCount(categories, localities, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public long getSuppliersCount(Category[] categories, Locality[] localities,
                                  ResultCriteria resultCriteria) {
        return this.getDao().getSuppliersCount(categories, localities,
                resultCriteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public long getSuppliersCountQuick(Category category) {
        return this.getDao().getSuppliersCountQuick(category);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public long getSuppliersCountWithoutChildren(Category category) {
        return this.getDao().getSuppliersCountWithoutChildren(category);
    }

    //--------------------------------------------------- HELPER METHODS -----------------------------------------------
    private void createDefaultNotifications(Supplier businessUserRole) {
        final List<NotificationItem> notificationItems = new ArrayList<NotificationItem>();
        notificationItems.add(
                this.notificationUtils.createInstantNotificationItem(
                        Registers.Notification.SUPPLIER_NEW_DEMAND, true));
        notificationItems.add(
                this.notificationUtils.createInstantNotificationItem(
                        Registers.Notification.SUPPLIER_NEW_MESSAGE, true));
        notificationItems.add(
                this.notificationUtils.createInstantNotificationItem(
                        Registers.Notification.SUPPLIER_NEW_OPERATOR, true));
        notificationItems.add(
                this.notificationUtils.createInstantNotificationItem(
                        Registers.Notification.SUPPLIER_NEW_INFO, false));
        notificationItems.add(
                this.notificationUtils.createInstantNotificationItem(
                        Registers.Notification.SUPPLIER_OFFER_STATUS_CHANGED, false));
        LOGGER.info("action=client_create_default_notifications supplier={} notifications={}",
                businessUserRole, notificationItems);
        businessUserRole.getBusinessUser().getSettings().setNotificationItems(notificationItems);
    }


    private void createDefaultAccessRole(Supplier businessUserRole) {
        Validate.notNull(businessUserRole);
        Preconditions.checkNotNull(businessUserRole.getBusinessUser(), "Supplier.businessUser must not be null!");
        if (CollectionUtils.isEmpty(businessUserRole.getBusinessUser().getAccessRoles())) {
            LOGGER.info("action=client_create_default_access_roles client={} roles={}",
                    businessUserRole, supplierAccessRoles);
            businessUserRole.getBusinessUser().setAccessRoles(supplierAccessRoles);
        }

    }

}
