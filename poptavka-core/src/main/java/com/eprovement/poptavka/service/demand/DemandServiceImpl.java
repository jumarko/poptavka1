/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.service.demand;

import com.eprovement.poptavka.dao.demand.DemandDao;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.demand.DemandOrigin;
import com.eprovement.poptavka.domain.demand.DemandType;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.enums.DemandTypeType;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.service.GenericServiceImpl;
import com.eprovement.poptavka.service.ResultProvider;
import com.eprovement.poptavka.service.register.RegisterService;
import com.eprovement.poptavka.service.user.ClientService;
import com.eprovement.poptavka.service.user.SupplierService;
import com.google.common.base.Preconditions;
import com.googlecode.ehcache.annotations.Cacheable;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
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

    /** Default number of max count of suppliers to which the demand is sent. */
    private static final Integer DEFAULT_MAX_SUPPLIERS = Integer.valueOf(50);

    private static final Logger LOGGER = LoggerFactory.getLogger(DemandServiceImpl.class);

    private ClientService clientService;
    private SupplierService supplierService;
    private RegisterService registerService;

    public DemandServiceImpl(DemandDao demandDao) {
        setDao(demandDao);
    }

    /**
     * Create new demand.
     * <p>
     *     Some default values can be filled if they are not specified in <code>demand</code> object.
     *     <ul>
     *        <li>Demand#type -- set to "normal" if it is not specified (null)</li>
     *     </ul>
     * @param demand
     * @return
     */
    @Override
    @Transactional
    public Demand create(Demand demand) {
        LOGGER.info("Action=demand_create status=start demand={}", demand);
        if (demand.getType() == null) {
            LOGGER.debug("Action=demand_create No demand type specified. Setting demand type  to default NORMAL");
            demand.setType(getDemandType(DemandTypeType.NORMAL.getValue()));
        }

        if (isNewClient(demand)) {
            LOGGER.info("Action=demand_create demand={} creating new client={}", demand.getClient().getId());
            this.clientService.create(demand.getClient());
        }

        final Demand createdDemand = super.create(demand);
        LOGGER.info("Action=demand_create status=finish demand={}", demand);
        return createdDemand;
    }

    //----------------------------------  Methods for DemandType-s -----------------------------------------------------
    @Override
    public List<DemandType> getDemandTypes() {
        return this.registerService.getAllValues(DemandType.class);
    }

    @Override
    public DemandType getDemandType(String code) {
        Preconditions.checkArgument(StringUtils.isNotBlank(code), "Code for demand type is empty!");
        return this.registerService.getValue(code, DemandType.class);
    }

    //----------------------------------  Methods for DemandOrigin-s ---------------------------------------------------
    @Override
    public List<DemandOrigin> getDemandOrigins() {
        return this.registerService.getAllValues(DemandOrigin.class);
    }

    @Override
    public DemandOrigin getDemandOrigin(String code) {
        Preconditions.checkArgument(StringUtils.isNotBlank(code), "Code for demand origin is empty!");
        return this.registerService.getValue(code, DemandOrigin.class);
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

        return new LinkedHashSet<Demand>(applyOrderByCriteria(demandProvider, resultCriteria));
    }

    /** {@inheritDoc} */
    @Transactional(readOnly = true)
    public Map<Locality, Long> getDemandsCountForAllLocalities() {
        final List<Map<String, Object>> demandsCountForAllLocalities = this.getDao().getDemandsCountForAllLocalities();

        // convert to suitable Map: <locality, demandsCountForLocality>
        final Map<Locality, Long> demandsCountForLocalitiesMap =
                new HashMap<Locality, Long>(ESTIMATED_NUMBER_OF_LOCALITIES);
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
        return locality.getAdditionalInfo() != null
                ? locality.getAdditionalInfo().getDemandsCount()
                : getDao().getDemandsCountQuick(locality);
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
    public Set<Demand> getDemands(ResultCriteria resultCriteria, Category... categories) {
        return this.getDao().getDemands(categories, resultCriteria);
    }

    /** {@inheritDoc} */
    @Transactional(readOnly = true)
    public Map<Category, Long> getDemandsCountForAllCategories() {
        final List<Map<String, Object>> demandsCountForAllCategories = this.getDao().getDemandsCountForAllCategories();

        // convert to suitable Map: <locality, demandsCountForLocality>
        final Map<Category, Long> demandsCountForCategoriesMap =
                new HashMap<Category, Long>(ESTIMATED_NUMBER_OF_CATEGORIES);
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
        return category.getAdditionalInfo() != null
                ? category.getAdditionalInfo().getDemandsCount()
                : getDao().getDemandsCountQuick(category);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public long getDemandsCountWithoutChildren(Category category) {
        return this.getDao().getDemandsCountWithoutChildren(category);
    }

    /** {@inheritDoc} */
    @Override
    public long getAllDemandsCount() {
        return this.getDao().getAllDemandsCount();
    }

    /** {@inheritDoc} */
    @Override
    public long getOfferCount(Demand demand) {
        return demand.getOffers().size();
    }


    @Override
    public Set<Demand> getDemands(ResultCriteria resultCriteria, List<Category> categories, List<Locality> localities) {
        return this.getDao().getDemands(categories, localities, resultCriteria);
    }

    @Override
    public long getDemandsCount(List<Category> categories, List<Locality> localities) {
        return this.getDao().getDemandsCount(categories, localities, ResultCriteria.EMPTY_CRITERIA);
    }

    @Override
    public Set<Demand> getDemandsIncludingParents(List<Category> categories, List<Locality> localities,
                                                  ResultCriteria resultCriteria) {
        return this.getDao().getDemandsIncludingParents(categories, localities, resultCriteria);
    }

    @Override
    public long getClientDemandsWithOfferCount(Client client) {
        Preconditions.checkNotNull(client, "Client must be specified for finding potential demands");
        LOGGER.debug("action=get_client_demands_with_offer_count status=start client{}", client);

        final long demandsCount = getDao().getClientDemandsWithOfferCount(client);
        LOGGER.debug("action=get_client_demands_with_offer_count status=finish client{} demands_count_size={}",
                client, demandsCount);
        return demandsCount;
    }

    @Override
    public List<Demand> getClientDemandsWithOffer(Client client) {
        Preconditions.checkNotNull(client, "Client must be specified for finding potential demands");
        LOGGER.debug("action=get_client_demands_with_offer_count status=start client{}", client);

        final List<Demand> demands = getDao().getClientDemandsWithOffer(client);
        LOGGER.debug("action=get_client_demands_with_offer_count status=finish client{} demands_count_size={}",
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

    //---------------------------------- GETTERS AND SETTERS -----------------------------------------------------------
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    public void setSupplierService(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    public void setRegisterService(RegisterService registerService) {
        this.registerService = registerService;
    }


    //---------------------------------------------- HELPER METHODS ----------------------------------------------------
    private boolean isNewClient(Demand demand) {
        return demand.getClient().getId() == null;
    }


}
