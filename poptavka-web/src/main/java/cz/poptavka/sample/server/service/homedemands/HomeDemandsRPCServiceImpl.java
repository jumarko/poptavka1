/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.server.service.homedemands;

import com.googlecode.genericdao.search.Search;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.main.common.search.dataHolders.FilterItem;
import cz.poptavka.sample.client.service.demand.HomeDemandsRPCService;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.demand.DemandCategory;
import cz.poptavka.sample.domain.demand.DemandLocality;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.server.service.demand.DemandRPCServiceImpl;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.service.address.LocalityService;
import cz.poptavka.sample.service.audit.AuditService;
import cz.poptavka.sample.service.common.TreeItemService;
import cz.poptavka.sample.service.demand.DemandService;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import cz.poptavka.sample.shared.exceptions.RPCException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Praso
 *
 * TODO Praso - Check which method are shared amongst more RPC services. Probably
 * Locality and categories are going to be used in more RPC. The best idea
 * is to make an parent class with locality/category methods and other RPC will
 * extend this class.
 *
 * TODO Praso - doplnit komentare k metodam a optimalizovat na stranke backendu
 */
@Component(HomeDemandsRPCService.URL)
public class HomeDemandsRPCServiceImpl extends AutoinjectingRemoteService implements HomeDemandsRPCService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DemandRPCServiceImpl.class);
    private GeneralService generalService;
    private DemandService demandService;
    private AuditService auditService;
    private TreeItemService treeItemService;
    private LocalityService localityService;

    @Autowired
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
    }

    @Autowired
    public void setDemandService(DemandService demandService) {
        this.demandService = demandService;
    }

    @Override
    public long filterDemandsCount(SearchModuleDataHolder detail) throws RPCException {
        return this.filter(detail, null).size();
    }

    @Autowired
    public void setAuditService(AuditService auditService) {
        this.auditService = auditService;
    }

    @Autowired
    public void setTreeItemService(TreeItemService treeItemService) {
        this.treeItemService = treeItemService;
    }

    @Autowired
    public void setLocalityService(LocalityService localityService) {
        this.localityService = localityService;
    }

    @Override
    public List<FullDemandDetail> filterDemands(int start, int count,
        SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) throws RPCException {
        List<FullDemandDetail> searchResult = this.filter(detail, orderColumns);
        if (searchResult.size() < (start + count)) {
            return searchResult.subList(start, searchResult.size());
        } else {
            return searchResult.subList(start, count);
        }
    }

    private List<FullDemandDetail> filter(SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        //null
        if (detail == null) {
            Search search = this.getFilter(null, orderColumns);
            return this.createDemandDetailList(this.generalService.search(search));
        }
        //0 0
        if (detail.getCategories() == null && detail.getLocalities() == null) {
            Search search = this.getFilter(detail, orderColumns);
            return this.createDemandDetailList(this.generalService.search(search));
        }
        //1 0
        if (detail.getCategories() != null && detail.getLocalities() == null) {
            Search search = this.getFilter(detail, orderColumns);
            return this.createDemandDetailListCat(this.generalService.searchAndCount(search).getResult());
        }
        //0 1
        if (detail.getCategories() == null && detail.getLocalities() != null) {
            Search search = this.getFilter(detail, orderColumns);
            return this.createDemandDetailListLoc(this.generalService.searchAndCount(search).getResult());
        }
        //1 1  --> perform join if filtering by category and locality was used
        if (detail.getCategories() != null && detail.getLocalities() != null) {
            List<FullDemandDetail> demandsCat = this.createDemandDetailListCat(
                    this.generalService.searchAndCount(this.getFilter(detail, orderColumns)).getResult());

            List<FullDemandDetail> demandsLoc = this.createDemandDetailListLoc(
                    this.generalService.searchAndCount(this.getFilter(detail, orderColumns)).getResult());

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

    private Search getFilter(SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        Search search = null;
        String prefix = "";
        if (detail != null) {

            /** simple **/
            if (detail.getCategories() != null) {
                search = new Search(DemandCategory.class);
                prefix = "demand.";

                List<Category> allSubCategories = new ArrayList<Category>();
                for (CategoryDetail cat : detail.getCategories()) {
                    allSubCategories.addAll(Arrays.asList(this.getAllSubcategories(cat.getId())));
                }
                search.addFilterIn("category", allSubCategories);
            } else if (detail.getLocalities() != null) {
                search = new Search(DemandLocality.class);
                prefix = "demand.";
                List<Locality> allSubLocalities = new ArrayList<Locality>();
                for (LocalityDetail loc : detail.getLocalities()) {
                    allSubLocalities.addAll(Arrays.asList(this.getAllSublocalities(loc.getCode())));
                }
                search.addFilterIn("locality", allSubLocalities);
            } else {
                search = new Search(Demand.class);
            }
            for (FilterItem item : detail.getAttibutes()) {
                if (item.getItem().equals("type")) {
                    search.addFilterEqual(prefix + "type",
                            demandService.getDemandType(item.getValue().toString()));
                } else if (item.getItem().equals("createdDate")) {
                    //created date
                    Calendar calendarDate = Calendar.getInstance(); //today -> case 0
                    //Musi byt? ved to je list, vzdy bude nasetovany nie?
                    if (item.getValue() != null) {
                        switch (Integer.valueOf(item.getValue().toString())) {
                            case 1:
                                calendarDate.add(Calendar.DATE, -1);  //yesterday
                                break;
                            case 2:
                                calendarDate.add(Calendar.DATE, -7);  //last week
                                break;
                            case 3:
                                calendarDate.add(Calendar.MONTH, -1);  //last month
                                break;
                            default:
                                break;
                        }
                        if (Integer.valueOf(item.getValue().toString()) != 4) {
                            search.addFilterGreaterOrEqual(prefix + "createdDate",
                                    new Date(calendarDate.getTimeInMillis()));
                        }
                    }
                } else {
                    this.filter(search, prefix, item);
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

    // TODO Praso - toto je mozno duplikat na inych RPC
    private Category[] getAllSubcategories(long id) {
        final Category cat = this.generalService.find(Category.class, id);
        final List<Category> allSubCategories = this.treeItemService.getAllDescendants(cat, Category.class);
        allSubCategories.add(cat);
        return allSubCategories.toArray(new Category[allSubCategories.size()]);
    }

    private Locality[] getAllSublocalities(String code) {
        final Locality loc = this.localityService.getLocality(code);
        final List<Locality> allSubLocalites = this.treeItemService.getAllDescendants(loc, Locality.class);
        allSubLocalites.add(loc);
        return allSubLocalites.toArray(new Locality[allSubLocalites.size()]);
    }

    private Search filter(Search search, String prefix, FilterItem item) {
        prefix += ".";
        switch (item.getOperation()) {
            case FilterItem.OPERATION_EQUALS:
                search.addFilterEqual(prefix + item.getItem(), item.getValue());
                break;
            case FilterItem.OPERATION_LIKE:
                search.addFilterLike(prefix + item.getItem(), "%" + item.getValue().toString() + "%");
                break;
            case FilterItem.OPERATION_IN:
                search.addFilterIn(prefix + item.getItem(), item.getValue());
                break;
            case FilterItem.OPERATION_FROM:
                search.addFilterGreaterOrEqual(prefix + item.getItem(), item.getValue());
                break;
            case FilterItem.OPERATION_TO:
                search.addFilterLessOrEqual(prefix + item.getItem(), item.getValue());
                break;
            default:
                break;
        }
        return search;
    }
}
