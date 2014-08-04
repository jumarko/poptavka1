package com.eprovement.poptavka.service.demand;

import com.eprovement.poptavka.dao.demand.DemandDao;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.demand.DemandType;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.enums.DemandTypeType;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.GenericServiceImpl;
import com.eprovement.poptavka.service.ResultProvider;
import com.eprovement.poptavka.service.message.MessageService;
import com.eprovement.poptavka.service.register.RegisterService;
import com.eprovement.poptavka.service.system.SystemPropertiesService;
import com.eprovement.poptavka.service.user.ClientService;
import com.eprovement.poptavka.util.search.Searcher;
import com.google.common.base.Preconditions;
import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Excalibur
 * @author Juraj Martinka
 */
public class DemandServiceImpl extends GenericServiceImpl<Demand, DemandDao> implements DemandService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemandServiceImpl.class);

    private final MessageService messageService;
    private GeneralService generalService;
    private ClientService clientService;
    private RegisterService registerService;
    private SystemPropertiesService systemPropertiesService;

    public DemandServiceImpl(DemandDao demandDao, GeneralService generalService,
        MessageService messageService, SystemPropertiesService systemPropertiesService) {
        Validate.notNull(demandDao, "demandDao cannot be null!");
        Validate.notNull(generalService, "generalService cannot be null!");
        Validate.notNull(messageService, "messageService cannot be null!");
        setDao(demandDao);
        this.messageService = messageService;
        this.generalService = generalService;
    }

    @Override
    @Transactional
    public Demand create(Demand demand) {
        Validate.notNull(demand, "demand cannot be null");
        Validate.notNull(demand.getClient(), "demand's client cannot be null");

        LOGGER.info("action=create_demand status=start demand={}", demand);
        if (demand.getType() == null) {
            LOGGER.debug("action=create_demand No demand type specified. Setting demand type  to default NORMAL");
            demand.setType(getDemandType(DemandTypeType.NORMAL.getValue()));
        }
        demand.setStatus(DemandStatus.NEW);

        if (isNewClient(demand)) {
            LOGGER.info("action=create_demand demand={} creating new client={}", demand.getClient().getId());
            this.clientService.create(demand.getClient());
        }

        final Demand createdDemand = super.create(demand);

        createDemandThreadRootMesssage(demand);

        LOGGER.info("action=create_demand status=finish demand={}", demand);
        return createdDemand;
    }

    private void createDemandThreadRootMesssage(Demand demand) {
        LOGGER.debug("action=create_demand_thread_root status=start demand={}", demand);
        final UserMessage demandUserMessage = messageService.newThreadRoot(demand.getClient().getBusinessUser());
        final Message demandMessage = demandUserMessage.getMessage();
        demandMessage.setDemand(demand);
        demandMessage.setBody(demand.getDescription());
        demandMessage.setSubject(demand.getTitle());
        demandMessage.setThreadRoot(demandMessage);
        messageService.update(demandMessage);
        LOGGER.debug("action=create_demand_thread_root status=finish demand={}", demand);
    }

    //----------------------------------  Methods for DemandType-s -----------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<DemandType> getDemandTypes() {
        return this.registerService.getAllValues(DemandType.class);
    }

    @Override
    @Transactional(readOnly = true)
    public DemandType getDemandType(String code) {
        Preconditions.checkArgument(StringUtils.isNotBlank(code), "Code for demand type is empty!");
        return this.registerService.getValue(code, DemandType.class);
    }

    //----------------------------------  Methods for Demands ----------------------------------------------------------
    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Set<Demand> getDemands(Locality... localities) {
        return getDemands(ResultCriteria.EMPTY_CRITERIA, localities);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Set<Demand> getDemands(final ResultCriteria resultCriteria, final Locality... localities) {
        final ResultProvider<Demand> demandProvider = new ResultProvider<Demand>(resultCriteria) {
            @Override
            public Collection<Demand> getResult() {
                return DemandServiceImpl.this.getDao().getDemands(localities, getResultCriteria());
            }
        };

        return new LinkedHashSet<>(applyOrderByCriteria(demandProvider, resultCriteria));
    }

    /** {@inheritDoc} */
    @Transactional(readOnly = true)
    public Map<Locality, Long> getDemandsCountForAllLocalities() {
        final List<Map<String, Object>> demandsCountForAllLocalities = this.getDao().getDemandsCountForAllLocalities();

        // convert to suitable Map: <locality, demandsCountForLocality>
        final Map<Locality, Long> demandsCountForLocalitiesMap
            = new HashMap<>(ESTIMATED_NUMBER_OF_LOCALITIES);
        for (Map<String, Object> demandsCountForLocality : demandsCountForAllLocalities) {
            demandsCountForLocalitiesMap.put((Locality) demandsCountForLocality.get("locality"),
                (Long) demandsCountForLocality.get("demandsCount"));
        }

        return demandsCountForLocalitiesMap;
    }

    /**
     * {@inheritDoc}
     * <p>
     * The result of this operation is cached therefore client cannot rely on the freshness of result.
     */
    @Override
    @Cacheable(cacheName = "cache5min")
    @Transactional(readOnly = true)
    public long getDemandsCount(Locality... localities) {
        return this.getDao().getDemandsCount(localities);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The result of this operation is cached therefore client cannot rely on the freshness of result.
     */
    @Override
    @Cacheable(cacheName = "cache5min")
    @Transactional(readOnly = true)
    public long getDemandsCountQuick(Locality locality) {
        Validate.notNull(locality, "locality cannot be null!");
        return getDao().getDemandsCountQuick(locality);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public long getDemandsCountWithoutChildren(Locality locality) {
        return this.getDao().getDemandsCountWithoutChildren(locality);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Set<Demand> getDemands(Category... categories) {
        return getDemands(ResultCriteria.EMPTY_CRITERIA, categories);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Set<Demand> getDemands(ResultCriteria resultCriteria, Category... categories) {
        return this.getDao().getDemands(categories, resultCriteria);
    }

    /** {@inheritDoc} */
    @Transactional(readOnly = true)
    public Map<Category, Long> getDemandsCountForAllCategories() {
        final List<Map<String, Object>> demandsCountForAllCategories = this.getDao().getDemandsCountForAllCategories();

        // convert to suitable Map: <locality, demandsCountForLocality>
        final Map<Category, Long> demandsCountForCategoriesMap
            = new HashMap<>(ESTIMATED_NUMBER_OF_CATEGORIES);
        for (Map<String, Object> demandsCountForCategory : demandsCountForAllCategories) {
            demandsCountForCategoriesMap.put((Category) demandsCountForCategory.get("category"),
                (Long) demandsCountForCategory.get("demandsCount"));
        }

        return demandsCountForCategoriesMap;
    }

    /**
     * {@inheritDoc}
     * <p>
     * The result of this operation is cached therefore client cannot rely on the freshness of result.
     */
    @Override
    @Cacheable(cacheName = "cache5min")
    @Transactional(readOnly = true)
    public long getDemandsCount(Category... categories) {
        return this.getDao().getDemandsCount(categories);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The result of this operation is cached therefore client cannot rely on the freshness of result.
     */
    @Override
    @Cacheable(cacheName = "cache5min")
    @Transactional(readOnly = true)
    public long getDemandsCountQuick(Category category) {
        Validate.notNull(category, "category cannot be null!");
        return getDao().getDemandsCountQuick(category);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public long getDemandsCountWithoutChildren(Category category) {
        return this.getDao().getDemandsCountWithoutChildren(category);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public long getAllDemandsCount() {
        return this.getDao().getAllDemandsCount();
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public long getOfferCount(Demand demand) {
        return demand.getOffers().size();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Demand> getDemands(ResultCriteria resultCriteria, List<Category> categories, List<Locality> localities) {
        return this.getDao().getDemands(categories, localities, resultCriteria);
    }

    @Override
    @Transactional(readOnly = true)
    public long getDemandsCount(List<Category> categories, List<Locality> localities) {
        return this.getDao().getDemandsCount(categories, localities, ResultCriteria.EMPTY_CRITERIA);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Demand> getDemandsIncludingParents(List<Category> categories, List<Locality> localities,
        ResultCriteria resultCriteria) {
        return this.getDao().getDemandsIncludingParents(categories, localities, resultCriteria);
    }

    @Override
    @Transactional(readOnly = true)
    public long getClientDemandsWithOfferCount(Client client) {
        Preconditions.checkNotNull(client, "Client must be specified for finding potential demands");
        LOGGER.debug("action=get_client_demands_with_offer_count status=start client{}", client);

        final long demandsCount = getDao().getClientDemandsWithOfferCount(client);
        LOGGER.debug("action=get_client_demands_with_offer_count status=finish client{} demands_count_size={}",
            client, demandsCount);
        return demandsCount;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Demand> getClientDemandsWithOffer(Client client) {
        Preconditions.checkNotNull(client, "Client must be specified for finding potential demands");
        LOGGER.debug("action=get_client_demands_with_offer_count status=start client{}", client);

        final List<Demand> demands = getDao().getClientDemandsWithOffer(client);
        LOGGER.debug("action=get_client_demands_with_offer_count status=finish client={} demands_count_size={}",
            client, demands);
        return demands;
    }

    @Override
    @Transactional
    public void activateDemand(Demand demand) throws IllegalArgumentException {
        Validate.notNull(demand, "demand for activation cannot be null!");
        Validate.isTrue(demand.getStatus() != DemandStatus.ACTIVE, "demand=" + demand + " has already been activated!");
        demand.setStatus(DemandStatus.ACTIVE);
        update(demand);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Demand, Integer> getClientDemandsWithUnreadSubMsgs(BusinessUser businessUser) {
        return getDao().getClientDemandsWithUnreadSubMsgs(businessUser);

    }

    @Override
    @Transactional(readOnly = true)
    public Map<Demand, Integer> getClientDemandsWithUnreadSubMsgs(BusinessUser businessUser,
        Search search) {
        return Searcher.searchMapByKeys(getDao().getClientDemandsWithUnreadSubMsgs(businessUser), search);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public long getClientDemandsCount(BusinessUser businessUser) {
        return getDao().getClientDemandsCount(businessUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Demand, Integer> getClientOfferedDemandsWithUnreadOfferSubMsgs(BusinessUser businessUser) {
        return getDao().getClientOfferedDemandsWithUnreadOfferSubMsgs(businessUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Demand, Integer> getClientOfferedDemandsWithUnreadOfferSubMsgs(BusinessUser businessUser,
        Search search) {
        return Searcher.searchMapByKeys(getDao().getClientOfferedDemandsWithUnreadOfferSubMsgs(businessUser), search);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public long getClientOfferedDemandsCount(BusinessUser businessUser) {
        return getDao().getClientOfferedDemandsCount(businessUser);
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    @Transactional
    public void incrementDemandCount(Demand demand) {
        if (systemPropertiesService.isImediateDemandCount()) {
            getDao().incrementCategoryDemandCount(getCategoriesAndItsParentsIds(demand.getCategories()));
            getDao().incrementLocalityDemandCount(getLocalitiesAndItsParentsIds(demand.getLocalities()));
        }
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    @Transactional
    public void decrementDemandCount(Demand demand) {
        if (systemPropertiesService.isImediateSupplierCount()) {
            getDao().decrementCategoryDemandCount(getCategoriesAndItsParentsIds(demand.getCategories()));
            getDao().decrementLocalityDemandCount(getLocalitiesAndItsParentsIds(demand.getLocalities()));
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

    //---------------------------------- GETTERS AND SETTERS -----------------------------------------------------------
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    public void setRegisterService(RegisterService registerService) {
        this.registerService = registerService;
    }

    //---------------------------------------------- HELPER METHODS ----------------------------------------------------
    private boolean isNewClient(Demand demand) {
        return demand.getClient().getId() == null;
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
        for (Category category : categories) {
            //add category itself
            set.add(category.getId());
            //add category parents
            while (category.getParent() != null) {
                set.add(category.getParent().getId());
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
        for (Locality locality : localities) {
            //add locality itself
            set.add(locality.getId());
            //add locality parents
            while (locality.getParent() != null) {
                set.add(locality.getParent().getId());
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
        category.setDemandCount(Long.valueOf(getDemandsCountQuick(category)).intValue());
        generalService.save(category);
    }

    /**
     * Calculates and updates supplier count for given locality.
     * @param locality to be updated
     */
    @Transactional
    private void calculateAndUpdatedCountForLocality(Locality locality) {
        locality.setDemandCount(Long.valueOf(getDemandsCountQuick(locality)).intValue());
        generalService.save(locality);
    }
}
