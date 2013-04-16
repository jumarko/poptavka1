/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.server.service.homedemands;

import com.eprovement.poptavka.client.service.demand.HomeDemandsRPCService;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.demand.DemandCategory;
import com.eprovement.poptavka.domain.demand.DemandLocality;
import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.service.common.TreeItemService;
import com.eprovement.poptavka.service.demand.CategoryService;
import com.eprovement.poptavka.service.demand.DemandService;
import com.eprovement.poptavka.service.fulltext.FulltextSearchService;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.search.FilterItem;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.googlecode.genericdao.search.Field;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.Sort;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
 * SearchDefinition searchDefinition, Map<String, OrderType> orderColumns)</li>
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
    private TreeItemService treeItemService;
    private Converter<Demand, FullDemandDetail> demandConverter;
    private Converter<Category, CategoryDetail> categoryConverter;
    private Converter<ResultCriteria, SearchDefinition> criteriaConverter;
    private Converter<Filter, FilterItem> filterConverter;
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
    public void setCategoryConverter(
            @Qualifier("categoryConverter") Converter<Category, CategoryDetail> categoryConverter) {
        this.categoryConverter = categoryConverter;
    }

    @Autowired
    public void setCriteriaConverter(
            @Qualifier("criteriaConverter") Converter<ResultCriteria, SearchDefinition> criteriaConverter) {
        this.criteriaConverter = criteriaConverter;
    }

    @Autowired
    public void setFilterConverter(
            @Qualifier("filterConverter") Converter<Filter, FilterItem> filterConverter) {
        this.filterConverter = filterConverter;
    }

    /**************************************************************************/
    /*  Categories                                                            */
    /**************************************************************************/
    @Override
    public CategoryDetail getCategory(long categoryID) throws RPCException {
        return categoryConverter.convertToTarget(categoryService.getById(categoryID));
    }

    /**************************************************************************/
    /*  Demands                                                               */
    /**************************************************************************/
    @Override
    public FullDemandDetail getDemand(long demandID) throws RPCException {
        return demandConverter.convertToTarget(demandService.getById(demandID));
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
     * @param definition - define filter criteria
     * @return demands count
     * @throws RPCException
     */
    @Override
    public long getDemandsCount(SearchDefinition definition) throws RPCException {
        if (definition == null || definition.getFilter() == null) {
//            return filterWithoutAttributesCount(definition);
            //general
            return getDemandsGeneralCount();
        } else {
            //fulltext
            if (!definition.getFilter().getSearchText().isEmpty()) {
                return fullTextSearchCount(definition);
                //criteria search
            } else {
                return filterWithAttributesCount(definition);
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
     * @param definition search filters
     * @return list of demand details
     * @throws RPCException
     */
    @Override
    public List<FullDemandDetail> getDemands(SearchDefinition definition) throws RPCException {
        if (definition == null || definition.getFilter() == null) {
            //genera;
            return getDemandsGeneral(definition);
        } else {
            //fulltext
            if (!definition.getFilter().getSearchText().isEmpty()) {
                return fullTextSearch(definition);
                //criteria search
            } else {
                return filterWithAttributes(definition);
            }
        }
    }

    /**************************************************************************/
    /*  Get demands in generall                                             */
    /**************************************************************************/
    /**
     * Get all demand's count.
     *
     * @return demand's count
     */
    private Long getDemandsGeneralCount() {
        return demandService.getCount();
    }

    /**
     * Get demands limited by FirstResult and MaxResult attributes.
     *
     * @param definition holder for FirstResult and MaxResult attributes
     * @return demands
     */
    private List<FullDemandDetail> getDemandsGeneral(SearchDefinition definition) {
        Search search = new Search(Demand.class);
        search.setFirstResult(definition.getFirstResult());
        search.setMaxResults(definition.getMaxResult());
        for (String column : definition.getOrderColumns().keySet()) {
            if (definition.getOrderColumns().get(column) == OrderType.ASC) {
                search.addSort(Sort.asc(column));
            } else {
                search.addSort(Sort.desc(column));
            }
        }
        return demandConverter.convertToTargetList(generalService.search(search));
    }

    /**************************************************************************/
    /*  Get demands in fulltext search                                      */
    /**************************************************************************/
    /**
     * Get demands count by full text search.
     *
     * @param searchText - text to be searched
     * @return demands count
     * @throws RPCException
     */
    public long fullTextSearchCount(SearchDefinition definition) throws RPCException {
        return this.fulltextSearchService.searchCount(
                Demand.class, Demand.DEMAND_FULLTEXT_FIELDS, definition.getFilter().getSearchText());
    }

    /**
     * Get demands by full text search.
     *
     * @param searchText - text to be search
     * @return demands
     * @throws RPCException
     */
    public List<FullDemandDetail> fullTextSearch(SearchDefinition definition) throws RPCException {
        final List<Demand> foundDemands = this.fulltextSearchService.search(
                Demand.class, Demand.DEMAND_FULLTEXT_FIELDS, definition.getFilter().getSearchText());
        //TODO RELEASE Juraj - implementd SearchDefinition - first, max result
        if (foundDemands.size() < (definition.getFirstResult() + definition.getMaxResult())) {
            return demandConverter.convertToTargetList(foundDemands);
        } else {
            return demandConverter.convertToTargetList(
                    foundDemands.subList(definition.getFirstResult(), definition.getMaxResult()));
        }
    }

    /**************************************************************************/
    /*  Get demands in search by representer be seraching criteria          */
    /**************************************************************************/
    /**
     * This method decide which method is used to create Search object for retrieve counts.
     *
     * @param definition - define filtering criteria
     * @return demands count
     */
    private long filterWithAttributesCount(SearchDefinition definition) {
        //0 0
        if (definition.getFilter().getCategories().isEmpty()
                && definition.getFilter().getLocalities().isEmpty()) {
            Search search = this.getSortSearch(this.getDemandFilter(definition), definition.getOrderColumns(), "");
            search.addField("id", Field.OP_COUNT);
            search.setResultMode(Search.RESULT_SINGLE);
            return (Long) this.generalService.searchUnique(search);
        }
        //1 0
        if (!definition.getFilter().getCategories().isEmpty()
                && definition.getFilter().getLocalities().isEmpty()) {
            Search search = this.getCategoryFilter(definition);
            search.addField("id", Field.OP_COUNT);
            search.setResultMode(Search.RESULT_SINGLE);
            return (Long) generalService.searchUnique(search);
        }
        //0 1
        if (definition.getFilter().getCategories().isEmpty()
                && !definition.getFilter().getLocalities().isEmpty()) {
            Search search = this.getLocalityFilter(definition);
            search.addField("id", Field.OP_COUNT);
            search.setResultMode(Search.RESULT_SINGLE);
            return (Long) generalService.searchUnique(search);
        }
        //1 1  --> perform join if filtering by category and locality was used
        if (!definition.getFilter().getCategories().isEmpty()
                && !definition.getFilter().getLocalities().isEmpty()) {
            //TODO LATER : Martin 16.4.2013, thing about better solution due to performance
            return getCategoryLocality(definition).size();
        }
        return -1L;
    }

    /**
     * This method decide which method is used to create Search object for retrieve data.
     *
     * @param definition define filtering criteria
     * @return demands
     */
    private List<FullDemandDetail> filterWithAttributes(SearchDefinition definition) {
        //0 0
        if (definition.getFilter().getCategories().isEmpty()
                && definition.getFilter().getLocalities().isEmpty()) {
            Search search = this.getSortSearch(this.getDemandFilter(definition), definition.getOrderColumns(), "");
            search.setFirstResult(definition.getFirstResult());
            search.setMaxResults(definition.getMaxResult());
            return demandConverter.convertToTargetList(this.generalService.search(search));
        }
        //1 0
        if (!definition.getFilter().getCategories().isEmpty()
                && definition.getFilter().getLocalities().isEmpty()) {
            Search search = this.getCategoryFilter(definition);
            search.setFirstResult(definition.getFirstResult());
            search.setMaxResults(definition.getMaxResult());
            return this.createDemandDetailListCat(this.generalService.search(search));
        }
        //0 1
        if (definition.getFilter().getCategories().isEmpty()
                && !definition.getFilter().getLocalities().isEmpty()) {
            Search search = this.getLocalityFilter(definition);
            search.setFirstResult(definition.getFirstResult());
            search.setMaxResults(definition.getMaxResult());
            return this.createDemandDetailListLoc(this.generalService.searchAndCount(search).getResult());
        }
        //1 1  --> perform join if filtering by category and locality was used
        if (!definition.getFilter().getCategories().isEmpty()
                && !definition.getFilter().getLocalities().isEmpty()) {
            //TODO LATER : Martin 16.4.2013, thing about better solution due to performance
            List<FullDemandDetail> catLocDemands = getCategoryLocality(definition);
            int upperBound = definition.getFirstResult() + definition.getMaxResult();
            if (upperBound > catLocDemands.size()) {
                upperBound = catLocDemands.size();
            }
            return new ArrayList<FullDemandDetail>(catLocDemands.subList(definition.getFirstResult(), upperBound));
        }
        return null;
    }

    /**************************************************************************/
    /*  Helper methods used in "Get demands in search" methods              */
    /**************************************************************************/
    /**
     * Create Search object for searching in categories and demand's attributes.
     *
     * @param definition - represents searching criteria
     * @return
     */
    private Search getCategoryFilter(SearchDefinition definition) {
        Search categorySearch = new Search(DemandCategory.class);

        List<Category> allSubCategories = new ArrayList<Category>();

        for (CategoryDetail cat : definition.getFilter().getCategories()) {
            allSubCategories = Arrays.asList(getAllSubCategories(cat.getId()));
        }
        categorySearch.addFilterIn("category", allSubCategories);

        if (!definition.getFilter().getAttributes().isEmpty()) {
            categorySearch.addFilterIn("demand", generalService.search(getDemandFilter(definition)));
        }

        return this.getSortSearch(categorySearch, definition.getOrderColumns(), "demand");
    }

    /**
     * Create Search object for searching in localities and demand's attributes.
     *
     * @param definition - represents searching criteria
     * @return
     */
    private Search getLocalityFilter(SearchDefinition definition) {
        Search localitySearch = new Search(DemandLocality.class);

        List<Locality> allSubLocalities = new ArrayList<Locality>();
        for (LocalityDetail loc : definition.getFilter().getLocalities()) {
            allSubLocalities = Arrays.asList(
                    this.getAllSublocalities(loc.getId()));
        }
        localitySearch.addFilterIn("locality", allSubLocalities);

        if (!definition.getFilter().getAttributes().isEmpty()) {
            localitySearch.addFilterIn("demand", generalService.search(getDemandFilter(definition)));
        }

        return this.getSortSearch(localitySearch, definition.getOrderColumns(), "demand");
    }

    /**
     * Create Search object for searching in categories and localities and demands attributes.
     *
     * @param definition - represents searching criteria
     * @return
     */
    private List<FullDemandDetail> getCategoryLocality(SearchDefinition definition) {
        List<FullDemandDetail> demandsCat = this.createDemandDetailListCat(
                this.generalService.searchAndCount(
                this.getCategoryFilter(definition))
                .getResult());

        List<FullDemandDetail> demandsLoc = this.createDemandDetailListLoc(
                this.generalService.searchAndCount(
                this.getLocalityFilter(definition))
                .getResult());

        List<FullDemandDetail> demands = new ArrayList<FullDemandDetail>();
        for (FullDemandDetail demandCat : demandsCat) {
            if (demandsLoc.contains(demandCat)) {
                demands.add(demandCat);
            }
        }
        return demands;
    }

    /**
     * Create Search object for searching in demand's attributes.
     *
     * @param definition - represents searching criteria
     * @return
     */
    private Search getDemandFilter(SearchDefinition definition) {
        Search search = new Search(Demand.class);

        ArrayList<Filter> filtersOr = filterConverter.convertToSourceList(definition.getFilter().getAttributes());
        search.addFilterAnd(filtersOr.toArray(new Filter[filtersOr.size()]));
        return search;
    }

    private Search getSortSearch(Search search, Map<String, OrderType> orderColumns, String prefix) {
        if (orderColumns != null) {
            for (String item : orderColumns.keySet()) {
                if (prefix != null && !prefix.isEmpty()) {
                    item = prefix.concat(".").concat(item);
                }
                if (orderColumns.get(item).getValue().equals(OrderType.ASC.getValue())) {
                    search.addSortAsc(item, true);
                } else {
                    search.addSortDesc(item, true);
                }
            }
        }
        return search;
    }

    /**************************************************************************/
    /*  Helper methods - List convertions                                     */
    /**************************************************************************/
    private ArrayList<FullDemandDetail> createDemandDetailListCat(Collection<DemandCategory> demandsCat) {
        ArrayList<FullDemandDetail> userDetails = new ArrayList<FullDemandDetail>();
        for (DemandCategory demandCat : demandsCat) {
            userDetails.add(demandConverter.convertToTarget(demandCat.getDemand()));
        }
        return userDetails;
    }

    private ArrayList<FullDemandDetail> createDemandDetailListLoc(Collection<DemandLocality> demandsLoc) {
        ArrayList<FullDemandDetail> userDetails = new ArrayList<FullDemandDetail>();
        for (DemandLocality demandLoc : demandsLoc) {
            userDetails.add(demandConverter.convertToTarget(demandLoc.getDemand()));
        }
        return userDetails;
    }

    /**************************************************************************/
    /*  Helper methods - others                                               */
    /**************************************************************************/
    private Category[] getAllSubCategories(Long id) {
        final Category cat = this.generalService.find(Category.class, id);
        final List<Category> allSubCategories = this.treeItemService.getAllDescendants(cat, Category.class);
        allSubCategories.add(cat);
        return allSubCategories.toArray(new Category[allSubCategories.size()]);
    }

    private Locality[] getAllSublocalities(Long id) {
        final Locality loc = this.localityService.getLocality(id);
        final List<Locality> allSubLocalites = this.treeItemService.getAllDescendants(loc, Locality.class);
        allSubLocalites.add(loc);
        return allSubLocalites.toArray(new Locality[allSubLocalites.size()]);
    }
}
