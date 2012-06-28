/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.service.demand;

import com.google.common.base.Preconditions;
import com.googlecode.ehcache.annotations.Cacheable;
import com.eprovement.poptavka.dao.demand.DemandDao;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.demand.DemandOrigin;
import com.eprovement.poptavka.domain.enums.DemandType;
import com.eprovement.poptavka.domain.demand.PotentialSupplier;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.enums.MessageContext;
import com.eprovement.poptavka.domain.enums.MessageState;
import com.eprovement.poptavka.domain.message.MessageUserRole;
import com.eprovement.poptavka.domain.enums.MessageUserRoleType;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.exception.MessageException;
import com.eprovement.poptavka.service.GenericServiceImpl;
import com.eprovement.poptavka.service.ResultProvider;
import com.eprovement.poptavka.service.message.MessageService;
import com.eprovement.poptavka.service.register.RegisterService;
import com.eprovement.poptavka.service.user.ClientService;
import com.eprovement.poptavka.service.user.SupplierService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
    private MessageService messageService;
    private SuppliersSelection suppliersSelection;

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
    @Transactional
    public void sendDemandToSuppliers(Demand demand) throws MessageException {

        fillDefaultValues(demand);

        // TODO ivlcek - do tejto message nemusime vyplnat vsetky udaje. Pretoze message samotna je hlavne
        // drzitelom objektu demand, ktoru ukazeme dodavatelom na vypise potencialne demandy
        // Napriklad message.body moze byt prazdne = demand.description
        // message subject moze byt prazdne = demand.title
        Message message = new Message();
        // TODO Vojto there should be some intro message for the user
        message.setMessageState(MessageState.COMPOSED);
        message.setBody(demand.getDescription() + " Description might be empty");
        message.setCreated(new Date());
        message.setDemand(demand);
        message.setLastModified(new Date());
        message.setSender(demand.getClient().getBusinessUser());
        message.setSubject(demand.getTitle());
        message.setThreadRoot(message);

        // TODO ivlcek - chceme aby kazdy dodavatel mal moznost vidiet
        // vsetkych prijemocov spravy s novou poptavkou? Cyklus nizsie to umoznuje
        final Set<PotentialSupplier> potentialSuppliers = this.suppliersSelection.getPotentialSuppliers(demand);
        final List<MessageUserRole> messageUserRoles = new ArrayList<MessageUserRole>();
        for (PotentialSupplier potentialSupplier : potentialSuppliers) {
            MessageUserRole messageUserRole = new MessageUserRole();
            messageUserRole.setMessage(message);
            messageUserRole.setUser(potentialSupplier.getSupplier().getBusinessUser());
            messageUserRole.setType(MessageUserRoleType.TO);
            messageUserRole.setMessageContext(MessageContext.POTENTIAL_SUPPLIERS_DEMAND);
            messageUserRoles.add(messageUserRole);
        }

        message.setRoles(messageUserRoles);

        message.setMessageState(MessageState.COMPOSED);

        message = messageService.create(message);

        messageService.send(message);
    }

    private void fillDefaultValues(Demand demand) {
        if (demand.getMaxSuppliers() == null) {
            demand.setMaxSuppliers(DEFAULT_MAX_SUPPLIERS);
        }
    }

    @Override
    public void sendDemandsToSuppliers() {

        // TODO try to parallelling this task - maybe in Scala? or Gpars http://gpars.codehaus.org/Parallelizer :)


        final List<Demand> allNewDemands = getAllNewDemands();
        for (Demand newDemand : allNewDemands) {
            try {
                sendDemandToSuppliers(newDemand);
            } catch (MessageException e) {
                LOGGER.error("An error occured while trying to send message to suppliers for demand " + newDemand, e);
            }
        }
    }

    public List<Demand> getAllNewDemands() {
        return getDao().getAllNewDemands(ResultCriteria.EMPTY_CRITERIA);
    }


    @Override
    public Set<Demand> getDemands(ResultCriteria resultCriteria, Category[] categories, Locality[] localities) {
        return this.getDao().getDemands(categories, localities, resultCriteria);
    }

    @Override
    public long getDemandsCount(Category[] categories, Locality[] localities) {
        return this.getDao().getDemandsCount(categories, localities, ResultCriteria.EMPTY_CRITERIA);
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

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public void setSuppliersSelection(SuppliersSelection suppliersSelection) {
        this.suppliersSelection = suppliersSelection;
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
