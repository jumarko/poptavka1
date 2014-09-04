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
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.converter.FilterConverter;
import com.eprovement.poptavka.server.converter.SortConverter;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.demand.CategoryService;
import com.eprovement.poptavka.service.demand.DemandService;
import com.eprovement.poptavka.service.fulltext.FulltextSearchService;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.domain.demand.LesserDemandDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import java.util.ArrayList;
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
    private Converter<Demand, LesserDemandDetail> lesserDemandConverter;
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
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setFulltextSearchService(FulltextSearchService fulltextSearchService) {
        this.fulltextSearchService = fulltextSearchService;
    }

    @Autowired
    public void setLesserDemandConverter(
        @Qualifier("lesserDemandConverter") Converter<Demand, LesserDemandDetail> lesserDemandConverter) {
        this.lesserDemandConverter = lesserDemandConverter;
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
    public LesserDemandDetail getDemand(long demandID) throws RPCException {
        return lesserDemandConverter.convertToTarget(demandService.getById(demandID));
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
        if (definition != null && definition.getFilter() != null
            && !definition.getFilter().getSearchText().isEmpty()) {
            //Basic search - quick seach - full text search
            return getDemandsSearchFulltextCount(definition);
        } else {
            //Advance search - attributes - categories - localities
            return getDemandsSearchCount(definition);
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
    public List<LesserDemandDetail> getDemands(SearchDefinition definition) throws RPCException {
        if (definition != null && definition.getFilter() != null
            && !definition.getFilter().getSearchText().isEmpty()) {
            //Basic search - quick seach - full text search
            return getDemandsSearchFulltext(definition);
        } else {
            //Advance search - attributes - categories - localities
            return getDemandsSearch(definition);
        }
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
    public int getDemandsSearchFulltextCount(SearchDefinition definition) throws RPCException {
        final List<Demand> foundDemands = this.fulltextSearchService.search(
            Demand.class, Demand.DEMAND_FULLTEXT_FIELDS, definition.getFilter().getSearchText());

        return foundDemands.size();
    }

    /**
     * Get demands by full text search.
     *
     * @param definition defines filtering criteria
     * @return list of LesserDemandDetail objects
     */
    public List<LesserDemandDetail> getDemandsSearchFulltext(SearchDefinition definition) throws RPCException {
        final List<Demand> foundDemands = this.fulltextSearchService.search(
            Demand.class, Demand.DEMAND_FULLTEXT_FIELDS, definition.getFilter().getSearchText());

        if (foundDemands.size() <= definition.getFirstResult() + definition.getMaxResult()) {
            return lesserDemandConverter.convertToTargetList(foundDemands.subList(
                definition.getFirstResult(),
                foundDemands.size()));
        } else {
            return lesserDemandConverter.convertToTargetList(foundDemands.subList(
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
     * @param definition defines filtering criteria
     * @return demands count
     */
    private int getDemandsSearchCount(SearchDefinition definition) {
        Search search = null;
        //0 0
        if (definition == null || definition.getFilter() == null
            || (definition.getFilter().getCategories().isEmpty()
            && definition.getFilter().getLocalities().isEmpty())) {
            search = this.getDemandFilter(definition, true);
            //1 0
        } else if (!definition.getFilter().getCategories().isEmpty()
            && definition.getFilter().getLocalities().isEmpty()) {
            search = this.getCategoryFilter(definition, true);
            //0 1
        } else if (definition.getFilter().getCategories().isEmpty()
            && !definition.getFilter().getLocalities().isEmpty()) {
            search = this.getLocalityFilter(definition, true);
            //1 1
        } else if (!definition.getFilter().getCategories().isEmpty()
            && !definition.getFilter().getLocalities().isEmpty()) {
            search = this.getCategoryLocalityFilter(definition, true);
        }
        return this.generalService.count(search);
    }

    /**
     * This method decide which method is used to create Search object for retrieve data.
     *
     * @param definition defines filtering criteria
     * @return demands
     */
    private List<LesserDemandDetail> getDemandsSearch(SearchDefinition definition) {
        Search search = null;
        //0 0
        if (definition == null || definition.getFilter() == null
            || (definition.getFilter().getCategories().isEmpty()
            && definition.getFilter().getLocalities().isEmpty())) {
            search = this.getDemandFilter(definition, false);
            //1 0
        } else if (!definition.getFilter().getCategories().isEmpty()
            && definition.getFilter().getLocalities().isEmpty()) {
            search = this.getCategoryFilter(definition, false);
            //0 1
        } else if (definition.getFilter().getCategories().isEmpty()
            && !definition.getFilter().getLocalities().isEmpty()) {
            search = this.getLocalityFilter(definition, false);
            //1 1
        } else if (!definition.getFilter().getCategories().isEmpty()
            && !definition.getFilter().getLocalities().isEmpty()) {
            search = this.getCategoryLocalityFilter(definition, false);
        }
        search.setFirstResult(definition.getFirstResult());
        search.setMaxResults(definition.getMaxResult());
        return this.lesserDemandConverter.convertToTargetList(this.generalService.search(search));
    }

    /**************************************************************************/
    /*  Helper methods used in "Get demands in search" methods              */
    /**************************************************************************/
    /**
     * Create Search object for filtering demand attributes.
     *
     * @param definition defines filtering criteria
     * @return
     */
    private Search getDemandFilter(SearchDefinition definition, boolean count) {
        return getSearch(Demand.class, definition, count);
    }

    /**
     * Create Search object for filtering demand attributes and categories.
     *
     * @param definition defines filtering criteria
     * @return search object
     */
    private Search getCategoryFilter(SearchDefinition definition, boolean count) {
        Search categorySearch = getSearch(DemandCategory.class, definition, count);
        // return only distinct demands
        if (count) {
            categorySearch.addField("demand.id");
        } else {
            categorySearch.addField("demand");
        }
        categorySearch.setDistinct(true);
        //categories
        for (ICatLocDetail cat : definition.getFilter().getCategories()) {
            Category category = this.generalService.find(Category.class, cat.getId());
            categorySearch.addFilterGreaterOrEqual("category.leftBound", category.getLeftBound());
            categorySearch.addFilterLessOrEqual("category.leftBound", category.getRightBound());
        }
        return categorySearch;
    }

    /**
     * Create Search object for filtering demand attributes and localities .
     *
     * @param definition defines filtering criteria
     * @return search object
     */
    private Search getLocalityFilter(SearchDefinition definition, boolean count) {
        Search localitySearch = getSearch(DemandLocality.class, definition, count);
        //return only distinct demands
        if (count) {
            localitySearch.addField("demand.id");
        } else {
            localitySearch.addField("demand");
        }
        localitySearch.setDistinct(true);
        //localities
        for (ICatLocDetail cat : definition.getFilter().getLocalities()) {
            Locality locality = this.generalService.find(Locality.class, cat.getId());
            localitySearch.addFilterGreaterOrEqual("locality.leftBound", locality.getLeftBound());
            localitySearch.addFilterLessOrEqual("locality.leftBound", locality.getRightBound());
        }
        return localitySearch;
    }

    /**
     * Create Search object for filtering demand attributes, categories and localities.
     *
     * @param definition defines filtering criteria
     * @return search object
     */
    private Search getCategoryLocalityFilter(SearchDefinition definition, boolean count) {
        Search search = getSearch(Demand.class, definition, count);
        //categories
        List<Filter> categoryFilters = new ArrayList<Filter>();
        for (ICatLocDetail catDetail : definition.getFilter().getCategories()) {
            Category category = this.generalService.find(Category.class, catDetail.getId());
            categoryFilters.add(Filter.and(
                Filter.greaterOrEqual("leftBound", category.getLeftBound()),
                Filter.lessOrEqual("rightBound", category.getRightBound())
            ));
        }
        search.addFilterSome("categories",
            Filter.or(categoryFilters.toArray(new Filter[categoryFilters.size()])));
        //localities
        List<Filter> localityFilters = new ArrayList<Filter>();
        for (ICatLocDetail localityDetail : definition.getFilter().getLocalities()) {
            Locality locality = this.generalService.find(Locality.class, localityDetail.getId());
            localityFilters.add(Filter.and(
                Filter.greaterOrEqual("leftBound", locality.getLeftBound()),
                Filter.lessOrEqual("rightBound", locality.getRightBound())
            ));
        }
        search.addFilterSome("localities",
            Filter.or(localityFilters.toArray(new Filter[localityFilters.size()])));
        return search;
    }

    /**
     * Creates search object for given searchClass and fills common filters and sorts.
     * @param searchClass defines searching class
     * @param definition defines filtering criteria
     * @param count true if creating search for count, false otherwise
     * @return search object
     */
    private Search getSearch(Class searchClass, SearchDefinition definition, boolean count) {
        Search search = new Search(searchClass);
        //attributes
        if (definition != null && definition.getFilter() != null
            && !definition.getFilter().getAttributes().isEmpty()) {
            search.setFilters(filterConverter.convertToSourceList(
                searchClass, definition.getFilter().getAttributes()));
        }
        //sorts
        if (!count && definition.getSortOrder() != null && !definition.getSortOrder().isEmpty()) {
            search.addSorts(sortConverter.convertToSourceList(searchClass, definition.getSortOrder()));
        }
        return search;
    }
}
