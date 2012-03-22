/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.server.service.homedemands;

import com.googlecode.genericdao.search.Search;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
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
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 */
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
    public long filterDemandsCount(SearchModuleDataHolder detail) {
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
    public List<FullDemandDetail> filterDemands(
            int start, int count, SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
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
            Search search = this.getFilter(null, null, orderColumns);
            return this.createDemandDetailList(this.generalService.search(search));
        }
        //0 0
        if (detail.getHomeDemands().getDemandCategory() == null
                && detail.getHomeDemands().getDemandLocality() == null) {
            Search search = this.getFilter("else", detail, orderColumns);
            return this.createDemandDetailList(this.generalService.search(search));
        }
        //1 0
        if (detail.getHomeDemands().getDemandCategory() != null
                && detail.getHomeDemands().getDemandLocality() == null) {
            Search search = this.getFilter("category", detail, orderColumns);
            return this.createDemandDetailListCat(this.generalService.searchAndCount(search).getResult());
        }
        //0 1
        if (detail.getHomeDemands().getDemandCategory() == null
                && detail.getHomeDemands().getDemandLocality() != null) {
            Search search = this.getFilter("locality", detail, orderColumns);
            return this.createDemandDetailListLoc(this.generalService.searchAndCount(search).getResult());
        }
        //1 1  --> perform join if filtering by category and locality was used
        if (detail.getHomeDemands().getDemandCategory() != null
                && detail.getHomeDemands().getDemandLocality() != null) {
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

    private Search getFilter(String type, SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        Search search = null;
        String prefix = "";
        if (detail != null) {

            /** simple **/
            if (detail.getHomeDemands().getDemandCategory() != null) {
                search = new Search(DemandCategory.class);
                prefix = "demand.";

                final List<Category> allSubCategories = Arrays.asList(
                        this.getAllSubcategories(detail.getHomeDemands().getDemandCategory().getId()));
                search.addFilterIn("category", allSubCategories);
            } else if (detail.getHomeDemands().getDemandLocality() != null) {
                search = new Search(DemandLocality.class);
                prefix = "demand.";
                final List<Locality> allSubLocalities = Arrays.asList(
                        this.getAllSublocalities(detail.getHomeDemands().getDemandLocality().getCode()));
                search.addFilterIn("locality", allSubLocalities);
            } else {
                search = new Search(Demand.class);
            }
            if (detail.getHomeDemands().getDemandTitle() != null) {
                search.addFilterLike(prefix + "title", "%" + detail.getHomeDemands().getDemandTitle() + "%");
            }

            /** additional **/
//            if (detail.isAdditionalInfo()) {
            if (detail.getHomeDemands().getPriceFrom() != null) {
                search.addFilterGreaterOrEqual(prefix + "price", detail.getHomeDemands().getPriceFrom());
            }
            if (detail.getHomeDemands().getPriceTo() != null) {
                search.addFilterLessOrEqual(prefix + "price", detail.getHomeDemands().getPriceTo());
            }

            if (detail.getHomeDemands().getDemandType() != null) {
                search.addFilterEqual(prefix + "type",
                        demandService.getDemandType(detail.getHomeDemands().getDemandType()));
            }

            if (detail.getHomeDemands().getEndDate() != null) {
                search.addFilterGreaterOrEqual(prefix + "endDate", detail.getHomeDemands().getEndDate());
            }
            //created date
            Calendar calendarDate = Calendar.getInstance(); //today -> case 0
            //Musi byt? ved to je list, vzdy bude nasetovany nie?
            if (detail.getHomeDemands().getCreationDate() != null) {
                switch (detail.getHomeDemands().getCreationDate()) {
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
                        ;
                }
                if (detail.getHomeDemands().getCreationDate() != 4) {
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
}
