/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.server.service.demand;

import cz.poptavka.sample.client.service.demand.DemandRPCService;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.demand.DemandStatus;
import cz.poptavka.sample.domain.demand.DemandType;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.exception.MessageCannotBeSentException;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.service.address.LocalityService;
import cz.poptavka.sample.service.audit.AuditService;
import cz.poptavka.sample.service.demand.CategoryService;
import cz.poptavka.sample.service.demand.DemandService;
import cz.poptavka.sample.service.message.MessageService;
import cz.poptavka.sample.service.user.ClientService;
import cz.poptavka.sample.service.user.SupplierService;
import cz.poptavka.sample.service.usermessage.UserMessageService;
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.demand.BaseDemandDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Excalibur
 */
public class DemandRPCServiceImpl extends AutoinjectingRemoteService implements DemandRPCService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DemandRPCServiceImpl.class);
    /**
     * generated serialVersonUID.
     */
    private static final long serialVersionUID = -4661806018739964100L;
    private DemandService demandService;
    private ClientService clientService;
    private MessageService messageService;
    private SupplierService supplierService;
    private LocalityService localityService;
    private CategoryService categoryService;
    private GeneralService generalService;
    private UserMessageService userMessageService;
    private AuditService auditService;

    public DemandService getDemandService() {
        return demandService;
    }

    @Autowired
    public void setAuditService(AuditService auditService) {
        this.auditService = auditService;
    }

    @Autowired
    public void setDemandService(DemandService demandService) {
        this.demandService = demandService;
    }

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public void setSupplierService(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @Autowired
    public void setUserMessageService(UserMessageService userMessageService) {
        this.userMessageService = userMessageService;
    }

    @Autowired
    public void setLocalityService(LocalityService localityService) {
        this.localityService = localityService;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
    }
    //Last computed categories and localities
    private List<Category> categoriesHistory = new ArrayList<Category>();
    private List<Locality> localitiesHistory = new ArrayList<Locality>();

    /**
     * Dorobit komentar.
     *
     * TODO - skontrolovat ci si butbe musime predavat parametre ako Long clientId.
     * BOlo by lepsie optimalizovat toto riesenie s vyuzitim GWT SPRING Security?
     *
     * @param detail
     * @param cliendId
     * @return
     */
    @Override
    public FullDemandDetail createNewDemand(FullDemandDetail detail, Long cliendId) {
        final Demand demand = new Demand();
        demand.setTitle(detail.getTitle());
        demand.setDescription(detail.getDescription());
        demand.setType(this.demandService.getDemandType(detail.getDemandType()));
        demand.setPrice(detail.getPrice());
        // if max suppliers has not been specified, default value is used. @See Demand#DEFAULT_MAX_SUPPLIERS
        if (maxOffersSpecified(detail)) {
            demand.setMaxSuppliers(detail.getMaxOffers());
        }
        demand.setMinRating(detail.getMinRating());
        demand.setStatus(DemandStatus.TEMPORARY);
        demand.setEndDate(detail.getEndDate());
        demand.setValidTo(detail.getValidToDate());
        demand.setClient(this.generalService.find(Client.class, cliendId));

        /** localities **/
        List<Locality> locs = new ArrayList<Locality>();
        for (String localityCode : detail.getLocalities().keySet()) {
            locs.add(getLocality(localityCode));
        }
        demand.setLocalities(locs);
        /** categories **/
        List<Category> categories = new ArrayList<Category>();
        for (Long categoryID : detail.getCategories().keySet()) {
            categories.add(getCategory(categoryID));
        }
        demand.setCategories(categories);

        Demand newDemandFromDB = demandService.create(demand);
        // TODO ivlcek - test sending demand to proper suppliers
        sendDemandToSuppliersTest(newDemandFromDB);
        return (FullDemandDetail) FullDemandDetail.createDemandDetail(
                newDemandFromDB);
    }

    private boolean maxOffersSpecified(FullDemandDetail detail) {
        return detail.getMaxOffers() > 0;
    }

    /**
     * Method creates a message that is associated with created demand. Message
     * is sent to all suppliers that complies with the demand criteria
     *
     * TODO design some heuristic to choose suitable suppliers to whom the
     * demand messages should be sent and possibly separate this heuristic
     *
     * @param demand
     */
    // TODO should send messages as we're sending messages to display potential demands. Beho
    //
    private void sendDemandToSuppliersTest(Demand demand) {
        // send message and handle exception if any
        try {
            this.demandService.sendDemandToSuppliers(demand);
        } catch (MessageCannotBeSentException e) {
            LOGGER.error("Demand " + demand + " has not been sent to suppliers. "
                    + "The next try will be made by regular job.");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * Method updates demand object in database.
     *
     * @param fullDemandDetail - updated demandDetail from front end
     * @return FullDemandDetail
     */
    @Override
    public FullDemandDetail updateDemand(FullDemandDetail demandDetail) {
        // TODO ivlcek - update entity by sa mal robit jednoduchsie ako toto?
        Demand demand = demandService.getById(demandDetail.getDemandId());
        // -- demand
        demand.setTitle(demandDetail.getTitle());
        demand.setDescription(demandDetail.getDescription());
        demand.setPrice(demandDetail.getPrice());
        if (demandDetail.getDemandStatus() != null && !demandDetail.getDemandStatus().equals("")) {
            demand.setStatus(DemandStatus.valueOf(demandDetail.getDemandStatus()));
        }

        if (demandDetail.getDemandType() != null && !demandDetail.getDemandType().equals("")) {
            demand.setType(demandService.getDemandType(
                    DemandType.Type.fromValue(demandDetail.getDemandType()).getValue()));
        }
        // -- categories
        List<Category> newCategories = new ArrayList<Category>();
        for (Category category : demand.getCategories()) {
            if (demandDetail.getCategories().containsKey(category.getId())) {
                //add category - if there already is data, don't go to DB
                newCategories.add(category);
                //remove if added, the rest will be obtained from DB
                demandDetail.getCategories().remove(category.getId());
            }
        }
        for (Long id : demandDetail.getCategories().keySet()) {
            newCategories.add(categoryService.getById(id));
        }
        // -- localities
        List<Locality> newLocalities = new ArrayList<Locality>();
        for (Locality locality : demand.getLocalities()) {
            if (demandDetail.getLocalities().containsKey(locality.getCode())) {
                newLocalities.add(locality);
                demandDetail.getLocalities().remove(locality.getCode());
            }
        }
        for (String code : demandDetail.getLocalities().keySet()) {
            newLocalities.add(localityService.getLocality(code));
        }

        // -- rest
        demand.setValidTo(demandDetail.getValidToDate());
        demand.setEndDate(demandDetail.getEndDate());
//            demand.setExcludedSuppliers(null);
        demand.setMaxSuppliers(Integer.valueOf(demandDetail.getMaxOffers()));
        demand.setMinRating(Integer.valueOf(demandDetail.getMinRating()));
//            demand.setOffers(null);
//            demand.setOrigin(null);

        demandService.update(demand);
        return demandDetail;
    }

    @Override
    public List<FullDemandDetail> getAllDemands() {
        List<FullDemandDetail> fullDemandDetails = new ArrayList<FullDemandDetail>();
        for (Demand demand : demandService.getAll()) {
            fullDemandDetails.add(FullDemandDetail.createDemandDetail(demand));
        }
        return fullDemandDetails;
    }

    @Override
    public List<FullDemandDetail> getSortedDemands(int start, int count, Map<String, OrderType> orderColumns) {
        final ResultCriteria resultCriteria =
                new ResultCriteria.Builder().firstResult(start).maxResults(count).orderByColumns(orderColumns).build();
        return this.createDemandDetailList(demandService.getAll(resultCriteria));
    }

    @Override
    public List<FullDemandDetail> getDemands(Locality... localities) {

        return this.createDemandDetailList(demandService.getDemands(localities));
    }

    @Override
    public List<FullDemandDetail> getDemands(Category... categories) {

        return this.createDemandDetailList(demandService.getDemands(categories));
    }

    @Override
    public List<FullDemandDetail> getDemands(int fromResult, int toResult) {
        final ResultCriteria resultCriteria =
                new ResultCriteria.Builder().firstResult(fromResult).maxResults(toResult).build();
        return this.createDemandDetailList(demandService.getAll(resultCriteria));
    }

    @Override
    public List<FullDemandDetail> getDemands(ResultCriteria resultCriteria, Locality[] localities) {

        return this.createDemandDetailList(demandService.getDemands(resultCriteria, localities));
    }

    @Override
    public List<FullDemandDetail> getDemandsByCategory(int fromResult, int toResult, long id) {
        final ResultCriteria resultCriteria =
                new ResultCriteria.Builder().firstResult(fromResult).maxResults(toResult).build();

        return this.createDemandDetailList(demandService.getDemands(resultCriteria, this.getAllSubcategories(id)));
    }

    private Category[] getAllSubcategories(long id) {
        //if stored are not what i am looking for, retrieve new/actual
        if (categoriesHistory.isEmpty() || categoriesHistory.get(0).getId() != id) {
            //clear
            categoriesHistory = new LinkedList<Category>();
            //level 0
            categoriesHistory.add(categoryService.getById(id));
            //other levels
            for (int i = 0; i < categoriesHistory.size(); i++) {
                final List<Category> workingCatList = categoriesHistory.get(i).getChildren();
                if (workingCatList != null && workingCatList.size() > 0) {
                    //and children categories
                    categoriesHistory.addAll(workingCatList);
                }
            }
        }
        return categoriesHistory.toArray(new Category[categoriesHistory.size()]);
    }

    @Override
    public List<FullDemandDetail> getDemandsByLocality(int fromResult, int toResult, String code) {

        final ResultCriteria resultCriteria =
                new ResultCriteria.Builder().firstResult(fromResult).maxResults(toResult).build();

        return this.createDemandDetailList(demandService.getDemands(resultCriteria, this.getAllSublocalities(code)));
    }

    @Override
    public List<FullDemandDetail> getDemandsByCategoryLocality(int fromResult, int toResult, long id, String code) {
        final ResultCriteria resultCriteria =
                new ResultCriteria.Builder().firstResult(fromResult).maxResults(toResult).build();
        final List<Category> category = Arrays.asList(this.categoryService.getById(id));
        final List<Locality> locality = Arrays.asList(this.localityService.getLocality(code));

        return this.createDemandDetailList(demandService.getDemands(resultCriteria,
                category.toArray(new Category[category.size()]),
                locality.toArray(new Locality[locality.size()])));
    }

    private Locality[] getAllSublocalities(String code) {
        //if stored are not what i am looking for, retrieve new/actual
        if (localitiesHistory.isEmpty() || !localitiesHistory.get(0).getCode().equals(code)) {
            //clear
            localitiesHistory = new LinkedList<Locality>();
            //level 0
            localitiesHistory.add(localityService.getLocality(code));
            //other levels
            int i = 0;
            List<Locality> workingCatList;
            while (localitiesHistory.size() != i) {
                workingCatList = new LinkedList<Locality>();
                workingCatList = localitiesHistory.get(i++).getChildren();
                if (workingCatList != null && workingCatList.size() > 0) {
                    //and children categories
                    localitiesHistory.addAll(workingCatList);
                }
            }
        }
        return localitiesHistory.toArray(new Locality[localitiesHistory.size()]);
    }

    @Override
    public ArrayList<FullDemandDetail> getClientDemands(long id) {
        Client client = clientService.getById(id);
        return this.toDemandDetailList(client.getDemands());
    }

    @Override
    public ArrayList<ArrayList<OfferDetail>> getDemandOffers(ArrayList<Long> idList) {
        ArrayList<ArrayList<OfferDetail>> offerList = new ArrayList<ArrayList<OfferDetail>>();
        for (Long id : idList) {
            Demand demand = demandService.getById(id);
            offerList.add(this.toOfferDetailList(demand.getOffers()));
        }
        return offerList;
    }

    @Override
    public Demand getWholeDemand(Long demandId) {
        return this.demandService.getById(demandId);
    }

    private List<FullDemandDetail> createDemandDetailList(Collection<Demand> demands) {
        List<FullDemandDetail> fullDemandDetails = new ArrayList<FullDemandDetail>();
        for (Demand demand : demands) {
            List<Number> revisions = auditService.getRevisions(Demand.class, demand.getId());
            Date createdDate = auditService.getRevisionDate(revisions.get(0));
            FullDemandDetail demandDetail = FullDemandDetail.createDemandDetail(demand);
            demandDetail.setCreated(createdDate);
            fullDemandDetails.add(demandDetail);
        }
        return fullDemandDetails;
    }

    @Override
    public List<FullDemandDetail> getDemands(ResultCriteria resultCriteria) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<FullDemandDetail> getDemands(ResultCriteria resultCriteria, Category[] categories) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long getAllDemandsCount() {
        //TODO Martin - lepsie .getCount() ?
        return demandService.getAllDemandsCount();
    }

    @Override
    public Long getDemandsCount(Category[] categories) {
        return demandService.getDemandsCount(categories);
    }

    @Override
    public Long getDemandsCount(Locality[] localities) {
        return demandService.getDemandsCount(localities);
    }

    @Override
    public FullDemandDetail getFullDemandDetail(Long demandId) {

        return FullDemandDetail.createDemandDetail(this.demandService.getById(demandId));

    }

    @Override
    public BaseDemandDetail getBaseDemandDetail(Long demandId) {

        return BaseDemandDetail.createDemandDetail(this.demandService.getById(demandId));

    }

    @Override
    public Long getDemandsCountByCategory(long id) {
//        return this.getDemandsCount(this.getAllSubcategories(id));
        List<Category> category = Arrays.asList(this.categoryService.getById(id));
        return demandService.getDemandsCount(category.toArray(new Category[category.size()]));
    }

    @Override
    public Long getDemandsCountByLocality(String code) {
//        return this.getDemandsCount(this.getAllSublocalities(code));
        List<Locality> locality = Arrays.asList(this.localityService.getLocality(code));
        return demandService.getDemandsCount(locality.toArray(new Locality[locality.size()]));
    }

    @Override
    public Long getDemandsCountByCategoryLocality(long id, String code) {
        final List<Category> category = Arrays.asList(this.categoryService.getById(id));
        final List<Locality> locality = Arrays.asList(this.localityService.getLocality(code));
        return demandService.getDemandsCount(
                category.toArray(new Category[category.size()]),
                locality.toArray(new Locality[locality.size()]));
    }

    // TODO FIX this, it's not working nullPointerException.
    public Locality getLocality(String code) {
        System.out.println("Locality code value: " + code + ", localityService is null? " + (localityService == null));
        return localityService.getLocality(code);
//        return localityService.getById(10);
    }

    public Category getCategory(Long id) {
        return categoryService.getById(id);
    }
}
