/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.service.demand;

import com.google.common.base.Preconditions;
import com.googlecode.ehcache.annotations.Cacheable;
import cz.poptavka.sample.dao.demand.DemandDao;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.demand.DemandOrigin;
import cz.poptavka.sample.domain.demand.DemandType;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.service.GenericServiceImpl;
import cz.poptavka.sample.service.ResultProvider;
import cz.poptavka.sample.service.register.RegisterService;
import cz.poptavka.sample.service.user.ClientService;
import cz.poptavka.sample.service.user.SupplierService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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
        if (demand.getType() == null) {
            // default demand type is "normal"
            demand.setType(getDemandType(DemandType.Type.NORMAL.getValue()));
        }

        if (isNewClient(demand)) {
            this.clientService.create(demand.getClient());
        }

        createSuppliersIfNecessary(demand);

        return super.create(demand);
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

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public long getDemandsCountQuick(Locality locality) {
        return this.getDao().getDemandsCountQuick(locality);
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

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public long getDemandsCountQuick(Category category) {
        return this.getDao().getDemandsCountQuick(category);
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
    public void sendDemandToSuppliers(Demand demand) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void sendDemandsToSuppliers() {
        final List<Demand> allNewDemands = this.getDao().getAllNewDemands(ResultCriteria.EMPTY_CRITERIA);
        for (Demand newDemand : allNewDemands) {
            sendDemandToSuppliers(newDemand);
        }
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

    private void createSuppliersIfNecessary(Demand demand) {
        if (CollectionUtils.isNotEmpty(demand.getSuppliers())) {
            for (Supplier supplier : demand.getSuppliers()) {
                if (supplier.getId() == null) {
                    this.supplierService.create(supplier);
                }
            }
        }
    }



}
