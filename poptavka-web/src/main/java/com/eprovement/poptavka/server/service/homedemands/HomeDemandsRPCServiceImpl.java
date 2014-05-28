/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.service.homedemands;

import com.eprovement.poptavka.client.service.demand.HomeDemandsRPCService;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.demand.DemandCategory;
import com.eprovement.poptavka.domain.demand.DemandLocality;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.converter.FilterConverter;
import com.eprovement.poptavka.server.converter.SortConverter;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.service.common.TreeItemService;
import com.eprovement.poptavka.service.demand.CategoryService;
import com.eprovement.poptavka.service.demand.DemandService;
import com.eprovement.poptavka.service.fulltext.FulltextSearchService;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.util.search.Searcher;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * This RPC handles all requests for HomeDemands module.
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
 * TODO LATER ivlcek - Check which method are shared amongst more RPC services.
 * Probably Locality and categories are going to be used in more RPC. The best
 * idea is to make an parent class with locality/category methods and other RPC
 * will extend this class.
 *
 * TODO LATER ivlcek: optimalizovat na strane backendu
 */
@Configurable
public class HomeDemandsRPCServiceImpl extends AutoinjectingRemoteService implements HomeDemandsRPCService {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private GeneralService generalService;
    private DemandService demandService;
    private CategoryService categoryService;
    private LocalityService localityService;
    private TreeItemService treeItemService;
    private Converter<Demand, FullDemandDetail> demandConverter;
    private Converter<Category, ICatLocDetail> categoryConverter;
    private FulltextSearchService fulltextSearchService;
    private FilterConverter filterConverter; //TODO MARTIN LATER - why not searchConverter used
    private SortConverter sortConverter;

    /**************************************************************************/
    /* Autowire services and converters                                       */
    /**************************************************************************/
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
            @Qualifier("categoryConverter") Converter<Category, ICatLocDetail> categoryConverter) {
        this.categoryConverter = categoryConverter;
    }

    @Autowired
    public void setFilterConverter(@Qualifier("filterConverter") FilterConverter filterConverter) {
        this.filterConverter = filterConverter;
    }

    @Autowired
    public void setSortConverter(@Qualifier("sortConverter") SortConverter sortConverter) {
        this.sortConverter = sortConverter;
    }

    /**************************************************************************/
    /*  Categories                                                            */
    /**************************************************************************/
    /**
     * Get category detail.
     * @param categoryID
     * @return category detail
     * @throws RPCException
     */
    @Override
    public ICatLocDetail getCategory(long categoryID) throws RPCException {
        return categoryConverter.convertToTarget(categoryService.getById(categoryID));
    }

    /**************************************************************************/
    /*  Demands                                                               */
    /**************************************************************************/
    /**
     * Get demand detail.
     * @param demandID
     * @return demand detail
     * @throws RPCException
     */
    @Override
    public FullDemandDetail getDemand(long demandID) throws RPCException {
        return demandConverter.convertToTarget(demandService.getById(demandID));
    }

    /**************************************************************************/
    /* Get filtered demands                                                   */
    /**************************************************************************/
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
    public Integer getDemandsCount(SearchDefinition definition) throws RPCException {
        if (definition == null || definition.getFilter() == null) {
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
    private int getDemandsGeneralCount() {
        return generalService.count(getDemandFilter(null));
    }

    /**
     * Get demands limited by FirstResult and MaxResult attributes.
     *
     * @param definition holder for FirstResult and MaxResult attributes
     * @return demands
     */
    private List<FullDemandDetail> getDemandsGeneral(SearchDefinition definition) {
        final Search search = getDemandFilter(definition);
        search.setFirstResult(definition.getFirstResult());
        search.setMaxResults(definition.getMaxResult());
        return demandConverter.convertToTargetList(generalService.search(search));
    }

    /**************************************************************************/
    /*  Get demands in fulltext search                                      */
    /**************************************************************************/
    /**
     * Get demands count by full text search.
     *
     * @param definition
     * @return demands count
     * @throws RPCException
     */
    public int fullTextSearchCount(SearchDefinition definition) throws RPCException {
        final List<Demand> foundDemands = this.fulltextSearchService.search(
                Demand.class, Demand.DEMAND_FULLTEXT_FIELDS, definition.getFilter().getSearchText());

        final Search search = getDemandFilter(null);
        final List<Demand> foundDemandsFilteredByStatus = Searcher.searchCollection(foundDemands, search);

        return foundDemandsFilteredByStatus.size();
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

        final Search search = getDemandFilter(definition);
        final List<Demand> foundDemandsFilteredByStatus = Searcher.searchCollection(foundDemands, search);

        if (foundDemandsFilteredByStatus.size() <= definition.getFirstResult() + definition.getMaxResult()) {
            return demandConverter.convertToTargetList(foundDemandsFilteredByStatus.subList(
                definition.getFirstResult(),
                foundDemandsFilteredByStatus.size()));
        } else {
            return demandConverter.convertToTargetList(foundDemandsFilteredByStatus.subList(
                definition.getFirstResult(),
                definition.getFirstResult() + definition.getMaxResult()));
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
    private int filterWithAttributesCount(SearchDefinition definition) {
        //0 0
        if (definition.getFilter().getCategories().isEmpty()
                && definition.getFilter().getLocalities().isEmpty()) {
            Search search = this.getDemandFilter(definition);
            return this.generalService.count(search);
        }
        //1 0
        if (!definition.getFilter().getCategories().isEmpty()
                && definition.getFilter().getLocalities().isEmpty()) {
            Search search = this.getCategoryFilter(definition);
            return this.generalService.count(search);
        }
        //0 1
        if (definition.getFilter().getCategories().isEmpty()
                && !definition.getFilter().getLocalities().isEmpty()) {
            Search search = this.getLocalityFilter(definition);
            return this.generalService.count(search);
        }
        //1 1  --> perform join if filtering by category and locality was used
        if (!definition.getFilter().getCategories().isEmpty()
                && !definition.getFilter().getLocalities().isEmpty()) {
            //TODO LATER : Martin 16.4.2013, thing about better solution due to performance
            return getCategoryLocality(definition).size();
        }
        return 0;
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
            Search search = this.getDemandFilter(definition);
            search.setFirstResult(definition.getFirstResult());
            search.setMaxResults(definition.getMaxResult());
            return this.demandConverter.convertToTargetList(this.generalService.search(search));
        }
        //1 0
        if (!definition.getFilter().getCategories().isEmpty()
                && definition.getFilter().getLocalities().isEmpty()) {
            Search search = this.getCategoryFilter(definition);
            search.setFirstResult(definition.getFirstResult());
            search.setMaxResults(definition.getMaxResult());
            return this.demandConverter.convertToTargetList(generalService.search(search));
        }
        //0 1
        if (definition.getFilter().getCategories().isEmpty()
                && !definition.getFilter().getLocalities().isEmpty()) {
            Search search = this.getLocalityFilter(definition);
            search.setFirstResult(definition.getFirstResult());
            search.setMaxResults(definition.getMaxResult());
            return this.demandConverter.convertToTargetList(this.generalService.search(search));
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

    private Search getCategoryFilter(SearchDefinition definition) {
        return getCategoryFilter(definition, -1);
    }
    /**
     * Create Search object for searching in categories and demand's attributes.
     *
     * @param definition - represents searching criteria
     * @param fieldOperation special operation to be performed with field "demand"
     *                       pass -1 if you do not want to specify any operation
     * @return
     */
    private Search getCategoryFilter(SearchDefinition definition, int fieldOperation) {
        Search categorySearch = new Search(DemandCategory.class);
        // return only distinct demands
        if (fieldOperation > -1) {
            categorySearch.addField("demand", fieldOperation);
        } else {
            categorySearch.addField("demand");
        }
        categorySearch.setDistinct(true);
        //filters
        List<Category> allSubCategories = new ArrayList<Category>();
        for (ICatLocDetail cat : definition.getFilter().getCategories()) {
            allSubCategories = Arrays.asList(getAllSubCategories(cat.getId()));
        }
        categorySearch.addFilterIn("category", allSubCategories);

        if (!definition.getFilter().getAttributes().isEmpty()) {
            categorySearch.addFilterIn("demand", generalService.search(getDemandFilter(definition)));
        } else {
            categorySearch.addFilterIn("demand.status", Arrays.asList(DemandStatus.ACTIVE, DemandStatus.OFFERED));
        }
        //sorts
        categorySearch.addSorts(sortConverter.convertToSourceList(
                categorySearch.getSearchClass(), definition.getSortOrder()));
        return categorySearch;
    }


    private Search getLocalityFilter(SearchDefinition definition) {
        return getLocalityFilter(definition, -1);
    }
    /**
     * Create Search object for searching in localities and demand's attributes.
     *
     * @param definition - represents searching criteria
     * @param fieldOperation special operation to be performed with field "demand"
     *                       pass -1 if you do not want to specify any operation
     * @return
     */
    private Search getLocalityFilter(SearchDefinition definition, int fieldOperation) {
        Search localitySearch = new Search(DemandLocality.class);
        // return only distinct demands
        if (fieldOperation > -1) {
            localitySearch.addField("demand", fieldOperation);
        } else {
            localitySearch.addField("demand");
        }
        localitySearch.setDistinct(true);

        //filters
        List<Locality> allSubLocalities = new ArrayList<Locality>();
        for (ICatLocDetail loc : definition.getFilter().getLocalities()) {
            allSubLocalities = Arrays.asList(
                    this.getAllSublocalities(loc.getId()));
        }
        localitySearch.addFilterIn("locality", allSubLocalities);

        if (!definition.getFilter().getAttributes().isEmpty()) {
            localitySearch.addFilterIn("demand", generalService.search(getDemandFilter(definition)));
        } else {
            localitySearch.addFilterIn("demand.status", Arrays.asList(DemandStatus.ACTIVE, DemandStatus.OFFERED));
        }
        //sorts
        localitySearch.addSorts(sortConverter.convertToSourceList(
                localitySearch.getSearchClass(), definition.getSortOrder()));
        return localitySearch;
    }

    /**
     * Create Search object for searching in categories and localities and demands attributes.
     *
     * @param definition - represents searching criteria
     * @return
     */
    private List<FullDemandDetail> getCategoryLocality(SearchDefinition definition) {
        List<FullDemandDetail> demandsCat = this.demandConverter.convertToTargetList(
                this.generalService.searchAndCount(
                this.getCategoryFilter(definition))
                .getResult());

        List<FullDemandDetail> demandsLoc = this.demandConverter.convertToTargetList(
                this.generalService.searchAndCount(
                this.getLocalityFilter(definition))
                .getResult());

        List<FullDemandDetail> demands = new ArrayList<FullDemandDetail>();
        for (FullDemandDetail demandCat : demandsCat) {
            if (demandsLoc.contains(demandCat)) {
                demands.add(demandCat);
            }
        }
        //TODO LATER MARTIN 24.4.2013 - how to sort intersection??
        return demands;
    }

    /**
     * Create Search object for searching in demand's attributes.
     *
     * @param definition - represents searching criteria
     * @return
     */
    private Search getDemandFilter(SearchDefinition definition) {
        final Search search = new Search(Demand.class);
        search.addFilterIn("status", Arrays.asList(DemandStatus.ACTIVE, DemandStatus.OFFERED));
        if (definition != null) {
            //filters
            if (definition.getFilter() != null
                    && !definition.getFilter().getAttributes().isEmpty()) {
                final ArrayList<Filter> filtersOr = filterConverter.convertToSourceList(
                        search.getSearchClass(),
                        definition.getFilter().getAttributes());
                search.addFilterAnd(filtersOr.toArray(new Filter[filtersOr.size()]));
            }
            //sorts
            if (definition.getSortOrder() != null
                    && !definition.getSortOrder().isEmpty()) {
                search.addSorts(sortConverter.convertToSourceList(
                        search.getSearchClass(), definition.getSortOrder()));
            }
        }
        return search;
    }

    /**************************************************************************/
    /*  Helper methods - others                                               */
    /**************************************************************************/
    /**
     * Get all sub categories.
     * @param id
     * @return sub categories array
     */
    private Category[] getAllSubCategories(Long id) {
        final Category cat = this.generalService.find(Category.class, id);
        final List<Category> allSubCategories = this.treeItemService.getAllDescendants(cat, Category.class);
        allSubCategories.add(cat);
        return allSubCategories.toArray(new Category[allSubCategories.size()]);
    }

    /**
     * Get all sub localities.
     * @param id
     * @return sub localities array
     */
    private Locality[] getAllSublocalities(Long id) {
        final Locality loc = this.localityService.getLocality(id);
        final List<Locality> allSubLocalites = this.treeItemService.getAllDescendants(loc, Locality.class);
        allSubLocalites.add(loc);
        return allSubLocalites.toArray(new Locality[allSubLocalites.size()]);
    }
}