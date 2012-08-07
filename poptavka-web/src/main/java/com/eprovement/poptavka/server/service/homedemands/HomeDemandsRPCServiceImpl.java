/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.server.service.homedemands;

import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.service.demand.HomeDemandsRPCService;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.demand.DemandCategory;
import com.eprovement.poptavka.domain.demand.DemandLocality;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.service.audit.AuditService;
import com.eprovement.poptavka.service.common.TreeItemService;
import com.eprovement.poptavka.service.demand.CategoryService;
import com.eprovement.poptavka.service.demand.DemandService;
import com.eprovement.poptavka.service.fulltext.FulltextSearchService;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.search.FilterItem;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * <b>HomeDemandsRPCServiceImpl</b> is RPC service for HomeDemands module. It
 * contains two methods: <ul> <li>@see #getDemandsCount(SearchModuleDataHolder
 * detail)</li> <li>@see #getDemands(int start, int count,
 * SearchModuleDataHolder detail, Map<String, OrderType> orderColumns)</li>
 * </ul> The retriving proces is optimized by using different backend methods
 * for different situations depending on given <b>SearchModuleDataHolder</b>
 * detail.
 *
 * @author Praso
 *
 * TODO Praso - Check which method are shared amongst more RPC services.
 * Probably Locality and categories are going to be used in more RPC. The best
 * idea is to make an parent class with locality/category methods and other RPC
 * will extend this class.
 *
 * TODO Praso - doplnit komentare k metodam a optimalizovat na stranke backendu
 */
@Configurable
public class HomeDemandsRPCServiceImpl extends AutoinjectingRemoteService implements HomeDemandsRPCService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(HomeDemandsRPCServiceImpl.class);
    private GeneralService generalService;
    private DemandService demandService;
    private CategoryService categoryService;
    private LocalityService localityService;
    private AuditService auditService;
    private TreeItemService treeItemService;
    private Converter<Demand, FullDemandDetail> demandConverter;
    private Converter<Search, SearchModuleDataHolder> searchConverter;
    private FulltextSearchService fulltextSearchService;

    // ***********************************************************************
    // Autowired methods
    // ***********************************************************************
    @Autowired
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
    }

    @Autowired
    public void setDemandService(DemandService demandService) {
        this.demandService = demandService;
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
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setLocalityService(LocalityService localityService) {
        this.localityService = localityService;
    }

    @Autowired
    public void setFulltextSearchService(FulltextSearchService fulltextSearchService) {
        this.fulltextSearchService = fulltextSearchService;
    }

    @Autowired
    public void setDemandConverter(
            @Qualifier("fullDemandConverter") Converter<Demand, FullDemandDetail> demandConverter) {
        this.demandConverter = demandConverter;
    }

    @Autowired
    public void setSearchConverter(
            @Qualifier("searchConverter") Converter<Search, SearchModuleDataHolder> searchConverter) {
        this.searchConverter = searchConverter;
    }

    // ***********************************************************************
    // Get filtered demands
    // ***********************************************************************
    /**
     * Method in general gets demands count according to given filter criteria
     * represented by SearchModuleDataHolder. If user don't specify attribute to
     * filter throught, there is no need to use generalService.Search for
     * retrieving data. Therefore different set of methods is used for
     * optimizing the proces.
     *
     * @param detail - define filter criteria
     * @return demands count
     * @throws RPCException
     */
    @Override
    public long getDemandsCount(SearchModuleDataHolder detail) throws RPCException {
//        Search search = searchConverter.converToSource(detail);
//        search.setSearchClass(Demand.class);
//        return (long) generalService.searchAndCount(search).getTotalCount();
        if (detail == null) {
            return filterWithoutAttributesCount(null);
        } else {
            if (!detail.getSearchText().isEmpty()) {
                return fullTextSearchCount(detail.getSearchText());
            } else if (detail.getAttibutes().isEmpty()) {
                return filterWithoutAttributesCount(detail);
            } else {
                return filterWithAttributesCount(detail);
            }
        }
    }

    /**
     * Method in general gets demands data according to given filter criteria
     * represented by start, maxResult, SearchModuleDataHolder, ordering. If
     * user don't specify attribute to filter throught, there is no need to use
     * generalService.Search for retrieving data. Therefore different set of
     * methods is used for optimizing the proces.
     *
     * @param start - define begin range of pagination
     * @param maxResult - define how many data will be retrieved
     * @param detail - define filter criteria
     * @param orderColumns - define ordering (attribute, type)
     * @return list of demand details
     * @throws RPCException
     */
    @Override
    public List<FullDemandDetail> getDemands(int start, int maxResult,
            SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) throws RPCException {
//        Search search = searchConverter.converToSource(detail);
//        search.setSearchClass(Demand.class);
//        search.setFirstResult(start);
//        search.setMaxResults(maxResult);
//        for (String column : orderColumns.keySet()) {
//            if (orderColumns.get(column) == OrderType.ASC) {
//                search.addSort(Sort.asc(column));
//            } else {
//                search.addSort(Sort.desc(column));
//            }
//        }
//        return generalService.search(search);
        if (detail == null) {
            return filterWithoutAttributes(start, maxResult, null, orderColumns);
        } else {
            if (!detail.getSearchText().isEmpty()) {
                return fullTextSearch(detail.getSearchText()).subList(start, maxResult);
            } else if (detail.getAttibutes().isEmpty()) {
                return filterWithoutAttributes(start, maxResult, detail, orderColumns);
            } else {
                return filterWithAttributes(start, maxResult, detail, orderColumns);
            }
        }
    }

    // ***********************************************************************
    // Get all demands
    // ***********************************************************************
    /**
     * This method is used when <b>no filtering</b> is required. Get all demands
     * count.
     *
     * @return all demands count
     */
    private Long getAllDemandsCount() {
        return demandService.getAllDemandsCount();
    }

    /**
     * This method is used when <b>no filtering</b> is required. Get demands
     * defined by range(start, maxResult) and ordering.
     *
     * @param start - define begin range of pagination
     * @param maxResult - define how many data will be retrieved
     * @param orderColumns - define ordering (attribute, type)
     * @return list of demand details
     */
    private List<FullDemandDetail> getSortedDemands(int start, int maxResult, Map<String, OrderType> orderColumns) {
        final ResultCriteria resultCriteria =
                new ResultCriteria.Builder().firstResult(start).maxResults(maxResult).
                orderByColumns(orderColumns).build();
        List<Demand> demands = demandService.getAll(resultCriteria);
        return demandConverter.convertToTargetList(demands);
    }

    // ***********************************************************************
    // Get category demands
    // ***********************************************************************
    /**
     * This mehtod is used when <b>category filtering</b> is required. Get
     * demands count of given categories.
     *
     * @param categories - define categories to filter throught
     * @return demands count of given categories
     */
    private Long getCategoryDemandsCount(List<CategoryDetail> categories) {
        List<Category> cats = new ArrayList<Category>();
        for (CategoryDetail catDetail : categories) {
            cats.add(categoryService.getById(catDetail.getId()));
        }
        return demandService.getDemandsCount(cats.toArray(new Category[cats.size()]));
    }

    /**
     * This method is used when <b>category filtering</b> is required. Get
     * demands data of given categories
     *
     * @param categories - define categories to filter through
     * @param maxResult - define how many data will be retrieved
     * @param orderColumns - define ordering (attribute, type)
     * @return demand details list of given categories
     */
    private List<FullDemandDetail> getSortedCategoryDemands(List<CategoryDetail> categories,
            int start, int maxResult, Map<String, OrderType> orderColumns) {
        List<Category> cats = new ArrayList<Category>();
        for (CategoryDetail catDetail : categories) {
            cats.add(categoryService.getById(catDetail.getId()));
        }
        final ResultCriteria resultCriteria =
                new ResultCriteria.Builder().firstResult(start).maxResults(maxResult).
                orderByColumns(orderColumns).build();
        return demandConverter.convertToTargetList(demandService.getDemands(
                resultCriteria, cats.toArray(new Category[cats.size()])));
    }

    // ***********************************************************************
    // Get locality demands
    // ***********************************************************************
    /**
     * This mehtod is used when <b>locality filtering</b> is required. Get
     * demands count of given localities.
     *
     * @param localities - define localities to filter throught
     * @return demands count of given localities
     */
    private Long getLocalityDemandsCount(List<LocalityDetail> localities) {
        List<Locality> locs = new ArrayList<Locality>();
        for (LocalityDetail locDetail : localities) {
            locs.add(localityService.getLocality(locDetail.getCode()));
        }
        return demandService.getDemandsCount(locs.toArray(new Locality[locs.size()]));
    }

    /**
     * This method is used when <b>localities filtering</b> is required. Get
     * demands data of given localities
     *
     * @param localities - define localities to filter through
     * @param maxResult - define how many data will be retrieved
     * @param orderColumns - define ordering (attribute, type)
     * @return demand details list of given localities
     */
    private List<FullDemandDetail> getSortedLocalityDemands(List<LocalityDetail> localities,
            int start, int maxResult, Map<String, OrderType> orderColumns) {
        List<Locality> locs = new ArrayList<Locality>();
        for (LocalityDetail catDetail : localities) {
            locs.add(localityService.getLocality(catDetail.getCode()));
        }
        final ResultCriteria resultCriteria =
                new ResultCriteria.Builder().firstResult(start).maxResults(maxResult).
                orderByColumns(orderColumns).build();
        return demandConverter.convertToTargetList(demandService.getDemands(
                resultCriteria, locs.toArray(new Locality[locs.size()])));
    }

    // ***********************************************************************
    // Get category locality demands
    // ***********************************************************************
    /**
     * This mehtod is used when both <b>category & locality filtering</b> is
     * required. Get demands count of given categories & localities.
     *
     * @param categories- define categories to filter throught
     * @param localities - define localities to filter throught
     * @return demands count of given categories & localities
     */
    private Long getCategoryLocalityDemandsCount(List<CategoryDetail> categories, List<LocalityDetail> localities) {
        List<Category> cats = new ArrayList<Category>();
        for (CategoryDetail catDetail : categories) {
            cats.add(categoryService.getById(catDetail.getId()));
        }
        List<Locality> locs = new ArrayList<Locality>();
        for (LocalityDetail locDetail : localities) {
            locs.add(localityService.getLocality(locDetail.getCode()));
        }
        return demandService.getDemandsCount(
                cats.toArray(new Category[cats.size()]),
                locs.toArray(new Locality[locs.size()]));
    }

    /**
     * This method is used when both <b>categories & localities filtering</b> is
     * required. Get demands data of given categories & localities
     *
     * @param categories - define categories to filter through
     * @param localities - define localities to filter through
     * @param maxResult - define how many data will be retrieved
     * @param orderColumns - define ordering (attribute, type)
     * @return demand details list of given categories & localities
     */
    private List<FullDemandDetail> getSortedCategoryLocalityDemands(List<CategoryDetail> categories,
            List<LocalityDetail> localities, int start, int count, Map<String, OrderType> orderColumns) {
        List<Category> cats = new ArrayList<Category>();
        for (CategoryDetail catDetail : categories) {
            cats.add(categoryService.getById(catDetail.getId()));
        }
        List<Locality> locs = new ArrayList<Locality>();
        for (LocalityDetail catDetail : localities) {
            locs.add(localityService.getLocality(catDetail.getCode()));
        }
        final ResultCriteria resultCriteria =
                new ResultCriteria.Builder().firstResult(start).maxResults(count).
                orderByColumns(orderColumns).build();
        return demandConverter.convertToTargetList(demandService.getDemands(
                resultCriteria, cats.toArray(new Category[cats.size()]), locs.toArray(new Locality[locs.size()])));
    }

    // ***********************************************************************
    // Get demands by Fulltext search
    // ***********************************************************************
    public long fullTextSearchCount(String searchText) {
        return this.fulltextSearchService.searchCount(Demand.class, Demand.DEMAND_FULLTEXT_FIELDS, searchText);
    }

    public List<FullDemandDetail> fullTextSearch(String searchText) {
        final List<Demand> foundDemands =
                this.fulltextSearchService.search(Demand.class, Demand.DEMAND_FULLTEXT_FIELDS, searchText);
        return demandConverter.convertToTargetList(foundDemands);
    }

    // ***********************************************************************
    // Fileter methods
    // ***********************************************************************
    /**
     * This method decide which backend method used to retrieve data. Method is
     * used when <b>no additional attributes filtering</b> is required,
     * therefore there is no need to use backend methods which use
     * <i>general.Search</i> for retrieving data.
     *
     * @param detail - define filtering criteria, which helps this method to
     * make decision
     * @return demands count
     */
    private long filterWithoutAttributesCount(SearchModuleDataHolder detail) {
        //null || 0 0
        if (detail == null || (detail.getCategories().isEmpty() && detail.getLocalities().isEmpty())) {
            return getAllDemandsCount();
        }
        //1 0
        if (!detail.getCategories().isEmpty() && detail.getLocalities().isEmpty()) {
            return getCategoryDemandsCount(detail.getCategories());
        }
        //0 1
        if (detail.getCategories().isEmpty() && !detail.getLocalities().isEmpty()) {
            return getLocalityDemandsCount(detail.getLocalities());
        }
        //1 1  --> perform join if filtering by category and locality was used
        if (!detail.getCategories().isEmpty() && !detail.getLocalities().isEmpty()) {
            return getCategoryLocalityDemandsCount(detail.getCategories(), detail.getLocalities());
        }
        return -1L;
    }

    /**
     * This method decide which backend method used to retrieve data. Method is
     * used when <b>additional attributes filtering</b> is required, therefore
     * there is need to use backend methods which use <i>general.Search</i> for
     * retrieving data.
     *
     * @param detail - define filtering criteria, which helps this method to
     * make decision
     * @return demands count
     */
    private long filterWithAttributesCount(SearchModuleDataHolder detail) {
        //0 0
        if (detail.getCategories().isEmpty() && detail.getLocalities().isEmpty()) {
            Search search = this.getFilter(detail, null);
            return (long) generalService.searchAndCount(search).getTotalCount();
        }
        //1 0
        if (!detail.getCategories().isEmpty() && detail.getLocalities().isEmpty()) {
            Search search = this.getFilter(detail, null);
            return (long) generalService.searchAndCount(search).getTotalCount();
        }
        //0 1
        if (detail.getCategories().isEmpty() && !detail.getLocalities().isEmpty()) {
            Search search = this.getFilter(detail, null);
            return (long) generalService.searchAndCount(search).getTotalCount();
        }
        //1 1  --> perform join if filtering by category and locality was used
        if (!detail.getCategories().isEmpty() && !detail.getLocalities().isEmpty()) {
            return generalService.searchAndCount(this.getFilter(detail, null)).getTotalCount()
                    + generalService.searchAndCount(this.getFilter(detail, null)).getTotalCount();
        }
        return -1L;
    }

    /**
     * This method decide which backend method used to retrieve data. Method is
     * used when <b>no additional attributes filtering</b> is required,
     * therefore there is no need to use backend methods which use
     * <i>general.Search</i> for retrieving data.
     *
     * @param detail - define filtering criteria, which helps this method to
     * make decision
     * @return demand detail list
     */
    private List<FullDemandDetail> filterWithoutAttributes(int start, int count,
            SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        //0 0
        if (detail == null || (detail.getCategories().isEmpty() && detail.getLocalities().isEmpty())) {
            return getSortedDemands(start, count, orderColumns);
        }
        //1 0
        if (!detail.getCategories().isEmpty() && detail.getLocalities().isEmpty()) {
            return getSortedCategoryDemands(detail.getCategories(), start, count, orderColumns);
        }
        //0 1
        if (detail.getCategories().isEmpty() && !detail.getLocalities().isEmpty()) {
            return getSortedLocalityDemands(detail.getLocalities(), start, count, orderColumns);
        }
        //1 1  --> perform join if filtering by category and locality was used
        if (!detail.getCategories().isEmpty() && !detail.getLocalities().isEmpty()) {
            return getSortedCategoryLocalityDemands(detail.getCategories(), detail.getLocalities(),
                    start, count, orderColumns);
        }
        return null;
    }

    /**
     * This method decide which backend method used to retrieve data. Method is
     * used when <b>additional attributes filtering</b> is required, therefore
     * there is need to use backend methods which use <i>general.Search</i> for
     * retrieving data.
     *
     * @param detail - define filtering criteria, which helps this method to
     * make decision
     * @return demand detail list
     */
    private List<FullDemandDetail> filterWithAttributes(int start, int count,
            SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        //0 0
        if (detail.getCategories().isEmpty() && detail.getLocalities().isEmpty()) {
            Search search = this.getFilter(detail, orderColumns);
            search.setFirstResult(start);
            search.setMaxResults(count);
            return demandConverter.convertToTargetList(this.generalService.search(search));
        }
        //1 0
        if (!detail.getCategories().isEmpty() && detail.getLocalities().isEmpty()) {
            Search search = this.getFilter(detail, orderColumns);
            search.setFirstResult(start);
            search.setMaxResults(count);
            return this.createDemandDetailListCat(this.generalService.searchAndCount(search).getResult());
        }
        //0 1
        if (detail.getCategories().isEmpty() && !detail.getLocalities().isEmpty()) {
            Search search = this.getFilter(detail, orderColumns);
            search.setFirstResult(start);
            search.setMaxResults(count);
            return this.createDemandDetailListLoc(this.generalService.searchAndCount(search).getResult());
        }
        //1 1  --> perform join if filtering by category and locality was used
        if (!detail.getCategories().isEmpty() && !detail.getLocalities().isEmpty()) {
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
            return demands.subList(start, count);
        }
        return null;
    }

    /**
     * This method create object <b>Search</b> from given
     * <b>SearchModuleDataHolder</b> provided by search module. This object
     * Search is then used in backend methods, which use generalService.Search
     * methods.
     */
    private Search getFilter(SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        Search search = null;
        String prefix = "";
        if (detail != null) {

            //if category filtering is required
            if (!detail.getCategories().isEmpty()) {
                search = new Search(DemandCategory.class);
                prefix = "demand.";

                List<Category> allSubCategories = new ArrayList<Category>();
                for (CategoryDetail cat : detail.getCategories()) {
                    allSubCategories.addAll(Arrays.asList(this.getAllSubcategories(cat.getId())));
                }
                search.addFilterIn("category", allSubCategories);
                //if locality filtering is required
            } else if (!detail.getLocalities().isEmpty()) {
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
            //if attribute filtering is required
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
//                    this.filter(search, prefix, item);
                }
            }
        } else {
            search = new Search(Demand.class);
        }
        //if sorting is required
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

    /**
     * This method adds new item to object Search accoriding to given inputs.
     * This object Search is then used in backend methods using
     * generalService.Search.
     *
     * @param search - object Search to which new item will be added
     * @param prefix - in some cases a prefix have to be defined (when category
     * or locality filtering is required)
     * @param item - define that new attribute to add
     * @return update Search object
     */
    private Filter filter(FilterItem item) {
        Filter f;
        switch (item.getOperation()) {
            case FilterItem.OPERATION_EQUALS:
                f = new Filter(item.getItem(), item.getValue(), Filter.OP_EQUAL);
                break;
            case FilterItem.OPERATION_LIKE:
                f = new Filter(item.getItem(), item.getValue(), Filter.OP_LIKE);
                break;
            case FilterItem.OPERATION_IN:
                f = new Filter(item.getItem(), item.getValue(), Filter.OP_IN);
                break;
            case FilterItem.OPERATION_FROM:
                f = new Filter(item.getItem(), item.getValue(), Filter.OP_GREATER_OR_EQUAL);
                break;
            case FilterItem.OPERATION_TO:
                f = new Filter(item.getItem(), item.getValue(), Filter.OP_LESS_OR_EQUAL);
                break;
            default:
                f = new Filter();
                break;
        }
        return f;
    }

    // ***********************************************************************
    // Other methods
    // ***********************************************************************
    //TODO Martin - musim stale zistovat createdDate z auditServicy, alebo sa to pridani
    //atributu createDate do Domain objektu vyplna samo?
    //TODO Martin - da sa tu pouzit converter?
    private List<FullDemandDetail> createDemandDetailListCat(Collection<DemandCategory> demands) {
        List<FullDemandDetail> fullDemandDetails = new ArrayList<FullDemandDetail>();
        for (DemandCategory demand : demands) {
            List<Number> revisions = auditService.getRevisions(Demand.class, demand.getDemand().getId());
            Date createdDate = auditService.getRevisionDate(revisions.get(0));
            FullDemandDetail demandDetail = demandConverter.convertToTarget(demand.getDemand());
            demandDetail.setCreated(createdDate);
            fullDemandDetails.add(demandDetail);
        }
        return fullDemandDetails;
    }

    //TODO Martin - detto vid komentar k createDemandDetailListCat
    private List<FullDemandDetail> createDemandDetailListLoc(Collection<DemandLocality> demands) {
        List<FullDemandDetail> fullDemandDetails = new ArrayList<FullDemandDetail>();
        for (DemandLocality demand : demands) {
            List<Number> revisions = auditService.getRevisions(Demand.class, demand.getDemand().getId());
            Date createdDate = auditService.getRevisionDate(revisions.get(0));
            FullDemandDetail demandDetail = demandConverter.convertToTarget(demand.getDemand());
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
