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
import cz.poptavka.sample.domain.message.MessageState;
import cz.poptavka.sample.domain.message.MessageUserRole;
import cz.poptavka.sample.domain.message.MessageUserRoleType;
import cz.poptavka.sample.domain.message.UserMessage;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.service.address.LocalityService;
import cz.poptavka.sample.service.demand.CategoryService;
import cz.poptavka.sample.service.demand.DemandService;
import cz.poptavka.sample.service.message.MessageService;
import cz.poptavka.sample.service.user.ClientService;
import cz.poptavka.sample.service.user.SupplierService;
import cz.poptavka.sample.service.usermessage.UserMessageService;
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.demand.BaseDemandDetail;
import cz.poptavka.sample.shared.domain.demand.DemandDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import cz.poptavka.sample.shared.domain.type.ViewType;

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

    public DemandService getDemandService() {
        return demandService;
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

        Demand newDemand = demandService.create(demand);
        // TODO ivlcek - test sending demand to proper suppliers
        sendDemandToSuppliersTest(newDemand);
        return (FullDemandDetail) FullDemandDetail.createDemandDetail(newDemand);
    }

    /**
     * Method creates a message that is associated with created demand. Message
     * is sent to all suppliers that complies with the demand criteria
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
        message.setBody(demand.getDescription() + " Description might be empty");
        message.setCreated(new Date());
        message.setDemand(demand);
        message.setLastModified(new Date());
        message.setMessageState(MessageState.SENT);
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
        MessageUserRole messageUserRole = new MessageUserRole();
        messageUserRole.setMessage(message);
        messageUserRole.setType(MessageUserRoleType.SENDER);
        messageUserRole.setMessageContext(MessageContext.NEW_CLIENTS_DEMAND);
        messageUserRole.setUser(demand.getClient().getBusinessUser());
        messageUserRoles.add(messageUserRole);
        message.setRoles(messageUserRoles);
        message.setSender(demand.getClient().getBusinessUser());
        message.setSent(new Date());
        message.setSubject(demand.getTitle());
        message.setThreadRoot(message);
        message = messageService.create(message);
        for (Supplier supplier : suppliers) {
            UserMessage userMessage = new UserMessage();
            userMessage.setIsRead(false);
            userMessage.setIsStarred(false);
            userMessage.setMessage(message);
            userMessage.setUser(supplier.getBusinessUser());
            generalService.save(userMessage);
        }
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
//        demand.setStatus(null);
        demand.setTitle(fullDemandDetail.getTitle());
        demand.setType(this.demandService.getDemandType(fullDemandDetail.getDemandType()));
        demand.setValidTo(fullDemandDetail.getValidToDate());
        demandService.update(demand);
        return fullDemandDetail;
    }

    @Override
    public List<DemandDetail> getAllDemands() {
        List<DemandDetail> fullDemandDetails = new ArrayList<DemandDetail>();
        for (Demand demand : demandService.getAll()) {
            fullDemandDetails.add(FullDemandDetail.createDemandDetail(demand));
        }
        return fullDemandDetails;
    }

    @Override
    public List<DemandDetail> getDemands(Locality... localities) {
        return this.createDemandDetailList(demandService.getDemands(localities));
    }

    @Override
    public List<DemandDetail> getDemands(Category... categories) {
        return this.createDemandDetailList(demandService.getDemands(categories));
    }

    @Override
    public List<DemandDetail> getDemands(int fromResult, int toResult) {
        final ResultCriteria resultCriteria = new ResultCriteria.Builder()
                .firstResult(fromResult)
                .maxResults(toResult)
                .build();
        return this.createDemandDetailList(demandService.getAll(resultCriteria));
    }

    @Override
    public List<DemandDetail> getDemands(ResultCriteria resultCriteria, Locality[] localities) {
        return this.createDemandDetailList(demandService.getDemands(resultCriteria, localities));
    }

    @Override
    public List<DemandDetail> getDemandsByCategory(int fromResult, int toResult, long id) {
        List<Category> categories = new LinkedList<Category>();
        final ResultCriteria resultCriteria = new ResultCriteria.Builder()
                .firstResult(fromResult)
                .maxResults(toResult)
                .build();

        //level 0
        categories.add(categoryService.getById(id));
        //other levels
        int i = 0;
        List<Category> workingCatList;
        while (categories.size() != i) {
            workingCatList = new LinkedList<Category>();
            workingCatList = categoryService.getById(categories.get(i++).getId()).getChildren();
            if (workingCatList != null && workingCatList.size() > 0) {
                //and children categories
                categories.addAll(workingCatList);
            }
        }
        //tested for here - working
        //I should have now all subcategories of given id (chosen category vrom combobox)
        //Now, get demands
        // TODO - nasledujuci riadok nefunguje. Preco?
        return this.createDemandDetailList(demandService.getDemands(resultCriteria, (Category[]) categories.toArray()));
    }

    @Override
    public List<DemandDetail> getDemandsByLocality(int fromResult, int toResult, String code) {
        List<Locality> localities = new LinkedList<Locality>();
        final ResultCriteria resultCriteria = new ResultCriteria.Builder()
                .firstResult(fromResult)
                .maxResults(toResult)
                .build();

        //level 0
        localities.add(localityService.getLocality(code));
        //other levels
        int i = 0;
        List<Locality> workingCatList;
        while (localities.size() != i) {
            workingCatList = new LinkedList<Locality>();
            workingCatList = localityService.getLocality(localities.get(i++).getCode()).getChildren();
            if (workingCatList != null && workingCatList.size() > 0) {
                //and children categories
                localities.addAll(workingCatList);
            }
        }
        //tested for here - working
        //I should have now all subcategories of given id (chosen category vrom combobox)
        //Now, get demands
        // TODO - nasledujuci riadok nefunguje. Preco?
        return this.createDemandDetailList(demandService.getDemands(resultCriteria, (Locality[]) localities.toArray()));
    }

    @Override
    public ArrayList<DemandDetail> getClientDemands(long id) {
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

    private List<DemandDetail> createDemandDetailList(Collection<Demand> demands) {
        List<DemandDetail> fullDemandDetails = new ArrayList<DemandDetail>();
        for (Demand demand : demands) {
            fullDemandDetails.add(FullDemandDetail.createDemandDetail(demand));
        }
        return fullDemandDetails;
    }

    @Override
    public List<DemandDetail> getDemands(ResultCriteria resultCriteria) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<DemandDetail> getDemands(ResultCriteria resultCriteria, Category[] categories) {
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
    public DemandDetail getDemandDetail(Long demandId, ViewType typeOfDetail) {
        switch (typeOfDetail) {
            case EDITABLE:
                return FullDemandDetail.createDemandDetail(this.demandService.getById(demandId));
            case POTENTIAL:
                return BaseDemandDetail.createDemandDetail(this.demandService.getById(demandId));
            default:
                return null;
        }
    }
}
