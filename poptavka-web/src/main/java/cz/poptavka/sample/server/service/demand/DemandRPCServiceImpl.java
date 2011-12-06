/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.server.service.demand;

import com.googlecode.genericdao.search.Search;
import cz.poptavka.sample.client.main.common.search.SearchDataHolder;
import cz.poptavka.sample.client.service.demand.DemandRPCService;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.demand.DemandCategory;
import cz.poptavka.sample.domain.demand.DemandLocality;
import cz.poptavka.sample.domain.demand.DemandStatus;
import cz.poptavka.sample.domain.demand.DemandType;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.exception.MessageException;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.service.address.LocalityService;
import cz.poptavka.sample.service.audit.AuditService;
import cz.poptavka.sample.service.common.TreeItemService;
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
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
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
    private TreeItemService treeItemService;

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

    @Autowired
    public void setTreeItemService(TreeItemService treeItemService) {
        this.treeItemService = treeItemService;
    }
//    @Autowired
//    private TreeItemService treeItemService;
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
        } catch (MessageException e) {
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
    public Long getSortedDemandsCount(Map<String, OrderType> orderColumns) {
        Search search = new Search(Demand.class);
        /** sort **/
        for (String item : orderColumns.keySet()) {
            if (orderColumns.get(item).getValue().equals(OrderType.ASC.getValue())) {
                search.addSortAsc(item, true);
            } else {
                search.addSortDesc(item, true);
            }
        }
        return (long) generalService.searchAndCount(search).getTotalCount();
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
        final Category cat = this.generalService.find(Category.class, id);
        final List<Category> allSubCategories = this.treeItemService.getAllDescendants(cat, Category.class);
        allSubCategories.add(cat);
        return allSubCategories.toArray(new Category[allSubCategories.size()]);
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
        final Locality loc = this.localityService.getLocality(code);
        final List<Locality> allSubLocalites = this.treeItemService.getAllDescendants(loc, Locality.class);
        allSubLocalites.add(loc);
        return allSubLocalites.toArray(new Locality[allSubLocalites.size()]);
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
            FullDemandDetail demandDetail = FullDemandDetail.createDemandDetail(demand);
            fullDemandDetails.add(demandDetail);
        }
        return fullDemandDetails;
    }

    private List<FullDemandDetail> createDemandDetailListCat(Collection<DemandCategory> demands) {
        List<FullDemandDetail> fullDemandDetails = new ArrayList<FullDemandDetail>();
        for (DemandCategory demand : demands) {
            List<Number> revisions = auditService.getRevisions(Demand.class, demand.getDemand().getId());
            Date createdDate = auditService.getRevisionDate(revisions.get(0));
            FullDemandDetail demandDetail = FullDemandDetail.createDemandDetail(demand.getDemand());
            demandDetail.setCreated(createdDate);
            fullDemandDetails.add(demandDetail);
        }
        return fullDemandDetails;
    }

    private List<FullDemandDetail> createDemandDetailListLoc(Collection<DemandLocality> demands) {
        List<FullDemandDetail> fullDemandDetails = new ArrayList<FullDemandDetail>();
        for (DemandLocality demand : demands) {
            List<Number> revisions = auditService.getRevisions(Demand.class, demand.getDemand().getId());
            Date createdDate = auditService.getRevisionDate(revisions.get(0));
            FullDemandDetail demandDetail = FullDemandDetail.createDemandDetail(demand.getDemand());
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

    // TODO FIX this, it's not working nullPointerException. -- who use it anyway???
    public Locality getLocality(String code) {
        System.out.println("Locality code value: " + code + ", localityService is null? " + (localityService == null));
        return localityService.getLocality(code);
//        return localityService.getById(10);
    }

    public Category getCategory(Long id) {
        return categoryService.getById(id);
    }

    @Override
    public long filterDemandsCount(SearchDataHolder detail) {
        return this.filter(detail, null).size();
    }

    @Override
    public List<FullDemandDetail> filterDemands(
            int start, int count, SearchDataHolder detail, Map<String, OrderType> orderColumns) {
        List<FullDemandDetail> searchResult = this.filter(detail, orderColumns);
        if (searchResult.size() < (start + count)) {
            return searchResult.subList(start, searchResult.size());
        } else {
            return searchResult.subList(start, count);
        }
    }

    private List<FullDemandDetail> filter(SearchDataHolder detail, Map<String, OrderType> orderColumns) {
        //null
        if (detail == null) {
            Search search = this.getFilter(null, null, orderColumns);
            return this.createDemandDetailList(this.generalService.search(search));
        }
        //0 0
        if (detail.getCategory() == null && detail.getLocality() == null) {
            Search search = this.getFilter("else", detail, orderColumns);
            return this.createDemandDetailList(this.generalService.search(search));
        }
        //1 0
        if (detail.getCategory() != null && detail.getLocality() == null) {
            Search search = this.getFilter("category", detail, orderColumns);
            return this.createDemandDetailListCat(this.generalService.searchAndCount(search).getResult());
        }
        //0 1
        if (detail.getCategory() == null && detail.getLocality() != null) {
            Search search = this.getFilter("locality", detail, orderColumns);
            return this.createDemandDetailListLoc(this.generalService.searchAndCount(search).getResult());
        }
        //1 1  --> perform join if filtering by category and locality was used
        if (detail.getCategory() != null && detail.getLocality() != null) {
            List<FullDemandDetail> demandsCat = this.createDemandDetailListCat(
                    this.generalService.searchAndCount(this.getFilter("category", detail, orderColumns)).getResult());

            List<FullDemandDetail> demandsLoc = this.createDemandDetailListLoc(
                    this.generalService.searchAndCount(this.getFilter("locality", detail, orderColumns)).getResult());

            List<FullDemandDetail> demands = new ArrayList<FullDemandDetail>();
            for (FullDemandDetail demandCat : demandsCat) {
                if (demandsLoc.contains(demandCat)) {
                    demands.add(demandCat);
                }
            }
            return demands;
        }
        return null;
    }

    private Search getFilter(String type, SearchDataHolder detail, Map<String, OrderType> orderColumns) {
        Search search = null;
        String prefix = "";
        if (detail != null) {

            /** simple **/
            if (type.equals("category")) {
                search = new Search(DemandCategory.class);
                prefix = "demand.";
                if (detail.getCategory() != null) {
                    final List<Category> allSubCategories = Arrays.asList(
                            this.getAllSubcategories(detail.getCategory().getId()));
                    search.addFilterIn("category", allSubCategories);
                }
            } else if (type.equals("locality")) {
                search = new Search(DemandLocality.class);
                prefix = "demand.";
                if (detail.getLocality() != null) {
                    final List<Locality> allSubLocalities = Arrays.asList(
                            this.getAllSublocalities(detail.getLocality().getCode()));
                    search.addFilterIn("locality", allSubLocalities);
                }
            } else {
                search = new Search(Demand.class);
            }
            if (!detail.getText().equals("")) {
                search.addFilterLike(prefix + "title", "%" + detail.getText() + "%");
            }

            /** additional **/
            if (detail.isAdditionalInfo()) {
                if (detail.getPriceFrom() != -1) {
                    search.addFilterGreaterOrEqual(prefix + "rating", detail.getPriceFrom());
                }
                if (detail.getPriceTo() != -1) {
                    search.addFilterLessOrEqual(prefix + "rating", detail.getPriceTo());
                }

                if (detail.getDemandType() != null) {
                    search.addFilterEqual(prefix + "type", demandService.getDemandType(detail.getDemandType()));
                }

                if (detail.getEndDate() != null) {
                    search.addFilterGreaterOrEqual(prefix + "endDate", detail.getEndDate());
                }
                //created date
                Calendar calendarDate = Calendar.getInstance(); //today -> case 0
                switch (detail.getCreationDate()) {
                    case 1:
                        calendarDate.add(Calendar.DATE, -1);  //yesterday
                        break;
                    case 2:
                        calendarDate.add(Calendar.DATE, -7);  //last week
                        break;
                    case 3:
                        calendarDate.add(Calendar.MONTH, -1);  //last month
                        break;
                    default:;
                }
                if (detail.getCreationDate() != 4) {
                    search.addFilterGreaterOrEqual(prefix + "createdDate", new Date(calendarDate.getTimeInMillis()));
                }
            }
        } else {
            search = new Search(Demand.class);
        }
        /** sort **/
        if (orderColumns != null) {
            for (String item : orderColumns.keySet()) {
                if (orderColumns.get(item).getValue().equals(OrderType.ASC.getValue())) {
                    search.addSortAsc(prefix + item, true);
                } else {
                    search.addSortDesc(prefix + item, true);
                }
            }
        }
        return search;
    }
}
