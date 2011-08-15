/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.server.service.demand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import cz.poptavka.sample.client.service.demand.DemandRPCService;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.demand.DemandStatus;
import cz.poptavka.sample.domain.message.Message;
import cz.poptavka.sample.domain.message.MessageContext;
import cz.poptavka.sample.domain.message.MessageUserRole;
import cz.poptavka.sample.domain.message.MessageUserRoleType;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.domain.user.Supplier;
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
import cz.poptavka.sample.shared.domain.DemandDetailForDisplayDemands;
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.demand.BaseDemandDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;

/**
 * @author Excalibur
 */
public class DemandRPCServiceImpl extends AutoinjectingRemoteService implements DemandRPCService {

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
        demand.setMaxSuppliers(detail.getMaxOffers());
        demand.setMinRating(detail.getMinRating());
        demand.setStatus(DemandStatus.TEMPORARY);
        demand.setEndDate(detail.getEndDate());
        demand.setValidTo(detail.getValidToDate());
        demand.setClient(this.generalService.find(Client.class, cliendId));

        /** localities **/
        List<Locality> locs = new ArrayList<Locality>();
        for (String localityCode : detail.getLocalities()) {
            locs.add(getLocality(localityCode));
        }
        demand.setLocalities(locs);
        /** categories **/
        List<Category> categories = new ArrayList<Category>();
        for (String categoryID : detail.getCategories()) {
            categories.add(getCategory(categoryID));
        }
        demand.setCategories(categories);

        Demand newDemandFromDB = demandService.create(demand);
        // TODO ivlcek - test sending demand to proper suppliers
        sendDemandToSuppliersTest(newDemandFromDB);
        return (FullDemandDetail) FullDemandDetail.createDemandDetail(
                newDemandFromDB);
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
        // TODO Refaktorovat celu metody s Jurajom a presunut do nejakeho JOBu
        Set<Supplier> suppliers = new HashSet<Supplier>();
        suppliers.addAll(supplierService.getSuppliers(demand.getCategories().
                toArray(new Category[demand.getCategories().size()])));
        suppliers.addAll(supplierService.getSuppliers(demand.getLocalities().
                toArray(new Locality[demand.getLocalities().size()])));

        // TODO ivlcek - do tejto message nemusime vyplnat vsetky udaje. Pretoze message samotna je hlavne
        // drzitelom objektu demand, ktoru ukazeme dodavatelom na vypise potencialne demandy
        // Napriklad message.body moze byt prazdne = demand.description
        // message subject moze byt prazdne = demand.title
        Message message = new Message();
        // TODO Vojto there should be some intro message for the user
        message.setBody(demand.getDescription() + " Description might be empty");
        message.setCreated(new Date());
        message.setDemand(demand);
        message.setLastModified(new Date());
        // TODO ivlcek - chceme aby kazdy dodavatel mal moznost vidiet
        // vsetkych prijemocov spravy s novou poptavkou? Cyklus nizsie to umoznuje
        List<MessageUserRole> messageUserRoles = new ArrayList<MessageUserRole>();
        for (Supplier supplierRole : suppliers) {
            MessageUserRole messageUserRole = new MessageUserRole();
            messageUserRole.setMessage(message);
            messageUserRole.setUser(supplierRole.getBusinessUser());
            messageUserRole.setType(MessageUserRoleType.TO);
            messageUserRole.setMessageContext(MessageContext.POTENTIAL_SUPPLIERS_DEMAND);
            messageUserRoles.add(messageUserRole);
        }
        message.setSender(demand.getClient().getBusinessUser());

        message.setSubject(demand.getTitle());
        message.setThreadRoot(message);
        message = messageService.create(message);
        messageService.send(message);
    }

    /**
     * Method updates demand object in database.
     *
     * @param fullDemandDetail - updated demandDetail from front end
     * @return FullDemandDetail
     */
    @Override
    public FullDemandDetail updateDemand(FullDemandDetail fullDemandDetail) {
        // TODO ivlcek - update entity by sa mal robit jednoduchsie ako toto?
        Demand demand = demandService.getById(fullDemandDetail.getDemandId());
//        demand.setCategories(null);
        demand.setClient(clientService.getById(fullDemandDetail.getClientId()));
//        demand.setDescription(null);
        demand.setEndDate(fullDemandDetail.getEndDate());
//        demand.setExcludedSuppliers(null);
//        demand.setLocalities(null);
        demand.setMaxSuppliers(Integer.valueOf(fullDemandDetail.getMaxOffers()));
        demand.setMinRating(Integer.valueOf(fullDemandDetail.getMinRating()));
//        demand.setOffers(null);
//        demand.setOrigin(null);
        demand.setPrice(fullDemandDetail.getPrice());
        demand.setStatus(DemandStatus.valueOf(fullDemandDetail.getDemandStatus()));
        demand.setTitle(fullDemandDetail.getTitle());
        demand.setType(this.demandService.getDemandType(fullDemandDetail.getDemandType()));
        demand.setValidTo(fullDemandDetail.getValidToDate());
        demandService.update(demand);
        return fullDemandDetail;
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
    public List<DemandDetailForDisplayDemands> getDemands(Locality... localities) {

        return this.createDemandDetailList(demandService.getDemands(localities));
    }

    @Override
    public List<DemandDetailForDisplayDemands> getDemands(Category... categories) {

        return this.createDemandDetailList(demandService.getDemands(categories));
    }

    @Override
    public List<DemandDetailForDisplayDemands> getDemands(int fromResult, int toResult) {
        final ResultCriteria resultCriteria = new ResultCriteria.Builder()
                .firstResult(fromResult)
                .maxResults(toResult)
                .build();
        return this.createDemandDetailList(demandService.getAll(resultCriteria));
    }

    public List<DemandDetailForDisplayDemands> getDemands(ResultCriteria resultCriteria, Locality[] localities) {

        return this.createDemandDetailList(demandService.getDemands(resultCriteria, localities));
    }

    public List<DemandDetailForDisplayDemands> getDemandsByCategory(int fromResult, int toResult, long id) {
        final ResultCriteria resultCriteria = new ResultCriteria.Builder()
                .firstResult(fromResult)
                .maxResults(toResult)
                .build();

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
            int i = 0;
            List<Category> workingCatList;
            while (categoriesHistory.size() != i) {
                workingCatList = new LinkedList<Category>();
                workingCatList = categoriesHistory.get(i++).getChildren();
                if (workingCatList != null && workingCatList.size() > 0) {
                    //and children categories
                    categoriesHistory.addAll(workingCatList);
                }
            }
        }
        return categoriesHistory.toArray(new Category[categoriesHistory.size()]);
    }

    public List<DemandDetailForDisplayDemands> getDemandsByLocality(int fromResult, int toResult, String code) {

        final ResultCriteria resultCriteria = new ResultCriteria.Builder()
                .firstResult(fromResult)
                .maxResults(toResult)
                .build();

        return this.createDemandDetailList(demandService.getDemands(resultCriteria, this.getAllSublocalities(code)));
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

    private List<DemandDetailForDisplayDemands> createDemandDetailList(Collection<Demand> demands) {
        List<DemandDetailForDisplayDemands> fullDemandDetails = new ArrayList<DemandDetailForDisplayDemands>();
        for (Demand demand : demands) {
            List<Number> revisions = auditService.getRevisions(Demand.class, demand.getId());
            Date createdDate = auditService.getRevisionDate(revisions.get(0));
            fullDemandDetails.add(DemandDetailForDisplayDemands.createDetail(createdDate, demand));
        }
        return fullDemandDetails;
    }


    public List<DemandDetailForDisplayDemands> getDemands(ResultCriteria resultCriteria) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<DemandDetailForDisplayDemands> getDemands(ResultCriteria resultCriteria, Category[] categories) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long getAllDemandsCount() {
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
        return this.getDemandsCount(this.getAllSubcategories(id));
    }

    @Override
    public Long getDemandsCountByLocality(String code) {
        return this.getDemandsCount(this.getAllSublocalities(code));
    }

    // TODO FIX this, it's not working nullPointerException.
    public Locality getLocality(String code) {
        System.out.println("Locality code value: " + code + ", localityService is null? " + (localityService == null));
        return localityService.getLocality(code);
//        return localityService.getById(10);
    }

    public Category getCategory(String id) {
        return categoryService.getById(Long.parseLong(id));
    }
}
