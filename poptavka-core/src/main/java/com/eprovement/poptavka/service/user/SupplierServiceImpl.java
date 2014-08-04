package com.eprovement.poptavka.service.user;

import static com.eprovement.poptavka.domain.common.ResultCriteria.EMPTY_CRITERIA;

import com.eprovement.poptavka.dao.user.SupplierDao;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.demand.PotentialSupplier;
import com.eprovement.poptavka.domain.enums.CommonAccessRoles;
import com.eprovement.poptavka.domain.enums.Period;
import com.eprovement.poptavka.service.system.SystemPropertiesService;
import com.eprovement.poptavka.domain.register.Registers;
import com.eprovement.poptavka.domain.settings.Notification;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.domain.user.rights.AccessRole;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.demand.DemandService;
import com.eprovement.poptavka.service.demand.PotentialDemandService;
import com.eprovement.poptavka.service.demand.SuppliersSelection;
import com.eprovement.poptavka.service.notification.NotificationTypeService;
import com.eprovement.poptavka.service.register.RegisterService;
import com.googlecode.ehcache.annotations.Cacheable;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Juraj Martinka
 *         Date: 12.2.11
 */
public class SupplierServiceImpl extends BusinessUserRoleServiceImpl<Supplier, SupplierDao> implements SupplierService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SupplierServiceImpl.class);

    private final List<AccessRole> supplierAccessRoles;

    private DemandService demandService;
    private PotentialDemandService potentialDemandService;
    private SuppliersSelection suppliersSelection;
    private SystemPropertiesService systemPropertiesService;

    public SupplierServiceImpl(GeneralService generalService, SupplierDao supplierDao,
        RegisterService registerService, UserVerificationService userVerificationService,
        NotificationTypeService notificationTypeService, SystemPropertiesService systemPropertiesService) {
        super(Supplier.class, generalService, registerService, userVerificationService, notificationTypeService);
        Validate.notNull(supplierDao);
        Validate.notNull(systemPropertiesService);
        setDao(supplierDao);
        this.systemPropertiesService = systemPropertiesService;
        this.supplierAccessRoles = Arrays.asList(
            getRegisterService().getValue(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE, AccessRole.class),
            getRegisterService().getValue(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE, AccessRole.class));
    }

    /**
     * Setter injection is used instead of constructor because {@link SupplierServiceImpl} is used in
     * {@link com.eprovement.poptavka.service.demand.DemandServiceImpl}.
     */
    public void setDemandService(DemandService demandService) {
        Validate.notNull(demandService, "demandService cannot be null");
        this.demandService = demandService;
    }

    /**
     * Setter injection is used instead of constructor because {@link SupplierServiceImpl} is used in
     * {@link com.eprovement.poptavka.service.demand.NaiveSuppliersSelection}.
     */
    public void setPotentialDemandService(PotentialDemandService potentialDemandService) {
        Validate.notNull(potentialDemandService, "potentialDemandService cannot be null");
        this.potentialDemandService = potentialDemandService;
    }

    /**
     * Setter injection is used instead of constructor because {@link SupplierServiceImpl} is used in
     * {@link com.eprovement.poptavka.service.demand.NaiveSuppliersSelection}.
     */
    public void setSuppliersSelection(SuppliersSelection suppliersSelection) {
        this.suppliersSelection = suppliersSelection;
    }

    @Override
    public Supplier create(Supplier businessUserRole) {
        LOGGER.info("action=create_supplier status=start supplier={}", businessUserRole);
        final Supplier createdSupplier = super.create(businessUserRole);
        sendPotentialDemandsToNewSupplier(businessUserRole);
        LOGGER.info("action=create_supplier status=finish supplier={}", createdSupplier);
        return createdSupplier;
    }

    @Override
    protected List<AccessRole> getDefaultAccessRoles() {
        return supplierAccessRoles;
    }

    @Override
    protected List<Notification> getNotificationsWithDefaultPeriod() {
        return notificationTypeService.getNotificationsForSupplier();
    }

    @Override
    protected Map<Notification, Period> getNotificationsWithCustomPeriod() {
        final HashMap<Notification, Period> customNotifications = new HashMap<>();
        customNotifications.put(getWelcomeNotification(Registers.Notification.WELCOME_SUPPLIER), Period.INSTANTLY);
        return customNotifications;
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
        List<Category> categories, List<Locality> localities) {
        return this.getDao().getSuppliers(categories, localities, resultCriteria);
    }

    @Override
    public Set<Supplier> getSuppliersIncludingParentsAndChildren(List<Category> categories, List<Locality> localities,
        ResultCriteria resultCriteria) {
        return getDao().getSuppliersIncludingParentsAndChildren(categories, localities, resultCriteria);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public Map<Locality, Long> getSuppliersCountForAllLocalities() {
        final List<Map<String, Object>> suppliersCountForAllLocalities
            = this.getDao().getSuppliersCountForAllLocalities();

        // convert to suitable Map: <locality, suppliersCountForLocality>
        final Map<Locality, Long> suppliersCountForLocalitiesMap
            = new HashMap<>(DemandService.ESTIMATED_NUMBER_OF_LOCALITIES);
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
     * <p>
     * The result of this operation is cached therefore client cannot rely on the freshness of result.
     */
    @Override
    @Cacheable(cacheName = "cache5min")
    @Transactional(readOnly = true)
    public long getSuppliersCountQuick(Locality locality) {
        Validate.notNull(locality, "locality cannot be null!");
        return getDao().getSuppliersCountQuick(locality);
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
        final List<Map<String, Object>> suppliersCountForAllCategories
            = this.getDao().getSuppliersCountForAllCategories();

        // convert to suitable Map: <locality, suppliersCountForLocality>
        final Map<Category, Long> suppliersCountForCategoriesMap
            = new HashMap<>(DemandService.ESTIMATED_NUMBER_OF_CATEGORIES);
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
    public long getSuppliersCount(List<Category> categories, List<Locality> localities) {
        return this.getDao().getSuppliersCount(categories, localities, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public long getSuppliersCount(List<Category> categories, List<Locality> localities, ResultCriteria resultCriteria) {
        return this.getDao().getSuppliersCount(categories, localities, resultCriteria);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The result of this operation is cached therefore client cannot rely on the freshness of result.
     */
    @Override
    @Cacheable(cacheName = "cache5min")
    @Transactional(readOnly = true)
    public long getSuppliersCountQuick(Category category) {
        Validate.notNull(category, "category cannot be null!");
        return getDao().getSuppliersCountQuick(category);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public long getSuppliersCountWithoutChildren(Category category) {
        return this.getDao().getSuppliersCountWithoutChildren(category);
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    @Transactional
    public void incrementSupplierCount(Supplier supplier) {
        if (systemPropertiesService.isImediateDemandCount()) {
            getDao().incrementCategorySupplierCount(getCategoriesAndItsParentsIds(supplier.getCategories()));
            getDao().incrementLocalitySupplierCount(getLocalitiesAndItsParentsIds(supplier.getLocalities()));
        }
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    @Transactional
    public void decrementSupplierCount(Supplier supplier) {
        if (systemPropertiesService.isImediateSupplierCount()) {
            getDao().decrementCategorySupplierCount(getCategoriesAndItsParentsIds(supplier.getCategories()));
            getDao().decrementLocalitySupplierCount(getLocalitiesAndItsParentsIds(supplier.getLocalities()));
        }
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    public void calculateCounts() {
        List<Category> allCategories = generalService.findAll(Category.class);
        for (Category category : allCategories) {
            calculateAndUpdatedCountForCategory(category);
        }
        List<Locality> allLocalities = generalService.findAll(Locality.class);
        for (Locality locality : allLocalities) {
            calculateAndUpdatedCountForLocality(locality);
        }
    }

    //--------------------------------------------------- HELPER METHODS -----------------------------------------------
    /**
     * Existing (potential) demands are sent to the new supplier.
     * @param newSupplier supplier that has just been registered
     */
    void sendPotentialDemandsToNewSupplier(Supplier newSupplier) {
        Validate.notNull(newSupplier, "newSupplier cannot be null");

        if (newSupplier.getBusinessUser().isUserFromExternalSystem()) {
            LOGGER.info("action=send_potential_demands_to_supplier status=skip reason=external_supplier supplier={} ",
                newSupplier);
            return;
        }
        LOGGER.info("action=send_potential_demands_to_supplier status=start supplier={}", newSupplier);

        final PotentialSupplier newPotentialSupplier = new PotentialSupplier(newSupplier);
        for (Demand demandsCandidate : getPotentialDemandsCandidates(newSupplier)) {
            // TODO LATER juraj: call of isRealPotentialSupplier can be quite inefficient since
            // getSuppliers is called subsequently try to make uniform solution
            if (isRealPotentialSupplier(newPotentialSupplier, demandsCandidate)) {
                potentialDemandService.sendDemandToPotentialSupplier(demandsCandidate, newPotentialSupplier);
            }
        }

        LOGGER.info("action=send_potential_demands_to_supplier status=finish supplier={}", newSupplier);
    }

    private Set<Demand> getPotentialDemandsCandidates(Supplier newSupplier) {
        return demandService.getDemandsIncludingParents(newSupplier.getCategories(), newSupplier.getLocalities(),
            EMPTY_CRITERIA);
    }

    /**
     * Decides whether given supplier is really a good candidate to be a supplier for given demand.
     * This should filter low rating and similar unsuitable suppliers.
     * This also filter demands in invalid states.
     *
     * @param newPotentialSupplier new potential supplier candidate
     * @param demand demand for which potential supplier candidates
     * @return true if given supplier is a good potential supplier for demand, false otherwise
     * @see com.eprovement.poptavka.service.demand.NaiveSuppliersSelection
     */
    private boolean isRealPotentialSupplier(final PotentialSupplier newPotentialSupplier, Demand demand) {
        final Set<PotentialSupplier> potentialSuppliers = suppliersSelection.getPotentialSuppliers(demand);
        return CollectionUtils.exists(potentialSuppliers, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                if (!(object instanceof PotentialSupplier)) {
                    throw new IllegalStateException("Potential suppliers collection must contain only elements"
                        + " of type " + PotentialSupplier.class);
                }
                return newPotentialSupplier.getSupplier().equals(((PotentialSupplier) object).getSupplier());
            }
        });
    }

    /**
     * Get list of given categories and its parents ids.
     * For each category from given categories, its parents are retrieved. Given categories are included.
     *
     * @param categories for which hierarchy is retrieved
     * @return categories and its parents ids
     */
    private Set<Long> getCategoriesAndItsParentsIds(List<Category> categories) {
        Set<Long> set = new HashSet<Long>();
        Category parent = null;
        for (Category category : categories) {
            //add category itself
            set.add(category.getId());
            //add category parents
            parent = category.getParent();
            while (parent != null) {
                set.add(parent.getId());
                parent = parent.getParent();
            }
        }
        return set;
    }

    /**
     * Get list of given localities and its parents ids.
     * For each locality from given localities, its parents are retrieved. Given localities are included.
     *
     * @param localities for which hierarchy is retrieved
     * @return localities and its parents ids
     */
    private Set<Long> getLocalitiesAndItsParentsIds(List<Locality> localities) {
        Set<Long> set = new HashSet<Long>();
        Locality parent = null;
        for (Locality locality : localities) {
            //add locality itself
            set.add(locality.getId());
            //add locality parents
            parent = locality.getParent();
            while (parent != null) {
                set.add(parent.getId());
                parent = parent.getParent();
            }
        }
        return set;
    }

    /**
     * Calculates and updates supplier count for given category.
     * @param category to be updated
     */
    @Transactional
    private void calculateAndUpdatedCountForCategory(Category category) {
        category.setSupplierCount(Long.valueOf(getSuppliersCountQuick(category)).intValue());
        generalService.save(category);
    }

    /**
     * Calculates and updates supplier count for given locality.
     * @param locality to be updated
     */
    @Transactional
    private void calculateAndUpdatedCountForLocality(Locality locality) {
        locality.setSupplierCount(Long.valueOf(getSuppliersCountQuick(locality)).intValue());
        generalService.save(locality);
    }
}
