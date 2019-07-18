/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.service.homesuppliers;

import com.eprovement.poptavka.client.service.demand.HomeSuppliersRPCService;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.domain.user.SupplierCategory;
import com.eprovement.poptavka.domain.user.SupplierLocality;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.converter.FilterConverter;
import com.eprovement.poptavka.server.converter.SortConverter;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.fulltext.FulltextSearchService;
import com.eprovement.poptavka.service.user.SupplierService;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.domain.supplier.LesserSupplierDetail;
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
 * This RPC handles all requests from HomeSuppliers module.
 * @author ivlcek
 */
@Configurable
public class HomeSuppliersRPCServiceImpl extends AutoinjectingRemoteService implements HomeSuppliersRPCService {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private GeneralService generalService;
    private SupplierService supplierService;
    private FulltextSearchService fulltextSearchService;
    private Converter<Supplier, LesserSupplierDetail> lesserSupplierConverter;
    private Converter<Category, ICatLocDetail> categoryConverter;
    private FilterConverter filterConverter;
    private SortConverter sortConverter;

    /**************************************************************************/
    /* Autowire services and converters                                       */
    /**************************************************************************/
    @Autowired
    public void setSupplierService(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @Autowired
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
    }

    @Autowired
    public void setFulltextSearchService(FulltextSearchService fulltextSearchService) {
        this.fulltextSearchService = fulltextSearchService;
    }

    @Autowired
    public void setLesserSupplierConverter(
        @Qualifier("lesserSupplierConverter") Converter<Supplier, LesserSupplierDetail> lesserSupplierConverter) {
        this.lesserSupplierConverter = lesserSupplierConverter;
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
        return categoryConverter.convertToTarget(generalService.find(Category.class, categoryID));
    }

    /**************************************************************************/
    /*  Suppliers                                                             */
    /**************************************************************************/
    /**
     * Get supplier detail.
     * @param supplierID
     * @return supplier detail
     * @throws RPCException
     */
    @Override
    public LesserSupplierDetail getSupplier(long supplierID) throws RPCException {
        return lesserSupplierConverter.convertToTarget(supplierService.getById(supplierID));
    }

    /**************************************************************************/
    /*  Get filtered demands                                                  */
    /**************************************************************************/
    /**
     * Method in general gets Suppliers count according to given filter criteria
     * represented by SearchModuleDataHolder. If user don't specify attribute to
     * filter throught, there is no need to use generalService.Search for
     * retrieving data. Therefore different set of methods is used for
     * optimizing the proces.
     *
     * @param definition defines filtering criteria
     * @return Suppliers count
     * @throws RPCException
     */
    @Override
    public Integer getSuppliersCount(SearchDefinition definition) throws RPCException {
        if (definition != null && definition.getFilter() != null
            && !definition.getFilter().getSearchText().isEmpty()) {
            //Basic search - quick seach - full text search
            return getSuppliersSearchFulltextCount(definition);
        } else {
            //Advance search - attributes - categories - localities
            return getSuppliersSearchCount(definition);
        }
    }

    /**
     * Method in general gets Suppliers data according to given filter criteria
     * represented by start, maxResult, SearchModuleDataHolder, ordering. If
     * user don't specify attribute to filter throught, there is no need to use
     * generalService.Search for retrieving data. Therefore different set of
     * methods is used for optimizing the proces.
     *
     * @param definition defines filtering criteria
     * @return list of Supplier details
     */
    @Override
    public List<LesserSupplierDetail> getSuppliers(SearchDefinition definition) throws RPCException {
        if (definition != null && definition.getFilter() != null
            && !definition.getFilter().getSearchText().isEmpty()) {
            //Basic search - quick seach - full text search
            return getSuppliersSearchFulltext(definition);
        } else {
            //Advance search - attributes - categories - localities
            return getSuppliersSearch(definition);
        }
    }

    /**************************************************************************/
    /*  Get suppliers in fulltext search                                      */
    /**************************************************************************/
    /**
     * Get suppliers count by full text search.
     *
     * @param definition defines filtering criteria
     * @return suppliers count
     */
    public int getSuppliersSearchFulltextCount(SearchDefinition definition) throws RPCException {
        final List<Supplier> foundSuppliers = this.fulltextSearchService.search(
            Supplier.class, Supplier.SUPPLIER_FULLTEXT_FIELDS, definition.getFilter().getSearchText());

        return foundSuppliers.size();
    }

    /**
     * Get suppliers by full text search.
     *
     * @param definition defines filtering criteria
     * @return suppliers
     */
    public List<LesserSupplierDetail> getSuppliersSearchFulltext(SearchDefinition definition) throws RPCException {
        final List<Supplier> foundSuppliers = this.fulltextSearchService.search(
            Supplier.class, Supplier.SUPPLIER_FULLTEXT_FIELDS, definition.getFilter().getSearchText());

        if (foundSuppliers.size() <= definition.getFirstResult() + definition.getMaxResult()) {
            return lesserSupplierConverter.convertToTargetList(foundSuppliers.subList(
                definition.getFirstResult(),
                foundSuppliers.size()));
        } else {
            return lesserSupplierConverter.convertToTargetList(foundSuppliers.subList(
                definition.getFirstResult(),
                definition.getFirstResult() + definition.getMaxResult()));
        }
    }

    /**************************************************************************/
    /*  Get suppliers in search by representer be seraching criteria          */
    /**************************************************************************/
    /**
     * This method decide which method is used to create Search object for retrieve counts.
     *
     * @param definition defines filtering criteria
     * @return suppliers count
     */
    private int getSuppliersSearchCount(SearchDefinition definition) {
        Search search = null;
        //0 0
        if (definition == null || definition.getFilter() == null
            || (definition.getFilter().getCategories().isEmpty()
            && definition.getFilter().getLocalities().isEmpty())) {
            search = this.getSupplierFilter(definition, true);
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
     * @return suppliers
     */
    private List<LesserSupplierDetail> getSuppliersSearch(SearchDefinition definition) {
        Search search = null;
        //0 0
        if (definition == null || definition.getFilter() == null
            || (definition.getFilter().getCategories().isEmpty()
            && definition.getFilter().getLocalities().isEmpty())) {
            search = this.getSupplierFilter(definition, false);
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
        return this.lesserSupplierConverter.convertToTargetList(this.generalService.search(search));
    }

    /**************************************************************************/
    /*  Helper methods used in "Get suppliers in search" methods              */
    /**************************************************************************/
    /**
     * Create Search object for filtering supplier attributes.
     *
     * @param definition defines filtering criteria
     * @return
     */
    private Search getSupplierFilter(SearchDefinition definition, boolean count) {
        return getSearch(Supplier.class, definition, count);
    }

    /**
     * Create Search object for filtering supplier attributes and categories.
     *
     * @param definition defines filtering criteria
     * @return search object
     */
    private Search getCategoryFilter(SearchDefinition definition, boolean count) {
        Search categorySearch = getSearch(SupplierCategory.class, definition, count);
        // return only distinct demands
        if (count) {
            categorySearch.addField("supplier.id");
        } else {
            categorySearch.addField("supplier");
        }
        categorySearch.setDistinct(true);
        //categories
        List<Filter> filterAnd = new ArrayList<Filter>();
        for (ICatLocDetail cat : definition.getFilter().getCategories()) {
            Category category = this.generalService.find(Category.class, cat.getId());
            filterAnd.add(
                Filter.and(
                    Filter.greaterOrEqual("category.leftBound", category.getLeftBound()),
                    Filter.lessOrEqual("category.leftBound", category.getRightBound())
                )
            );
        }
        categorySearch.addFilterOr(filterAnd.toArray(new Filter[filterAnd.size()]));
        return categorySearch;
    }

    /**
     * Create Search object for filtering supplier attributes and localities .
     *
     * @param definition defines filtering criteria
     * @return search object
     */
    private Search getLocalityFilter(SearchDefinition definition, boolean count) {
        Search localitySearch = getSearch(SupplierLocality.class, definition, count);
        //return only distinct suppliers
        if (count) {
            localitySearch.addField("supplier.id");
        } else {
            localitySearch.addField("supplier");
        }
        localitySearch.setDistinct(true);
        //localities
        List<Filter> filterAnd = new ArrayList<Filter>();
        for (ICatLocDetail cat : definition.getFilter().getLocalities()) {
            Locality locality = this.generalService.find(Locality.class, cat.getId());
            filterAnd.add(
                Filter.and(
                    Filter.greaterOrEqual("locality.leftBound", locality.getLeftBound()),
                    Filter.lessOrEqual("locality.leftBound", locality.getRightBound()
                    )
                )
            );
        }
        localitySearch.addFilterOr(filterAnd.toArray(new Filter[filterAnd.size()]));
        return localitySearch;
    }

    /**
     * Create Search object for filtering supplier attributes, categories and localities.
     *
     * @param definition defines filtering criteria
     * @return search object
     */
    private Search getCategoryLocalityFilter(SearchDefinition definition, boolean count) {
        Search search = getSearch(Supplier.class, definition, count);
        //categories
        List<Filter> categoryFilters = new ArrayList<Filter>();
        for (ICatLocDetail catDetail : definition.getFilter().getCategories()) {
            Category category = this.generalService.find(Category.class, catDetail.getId());
            categoryFilters.add(
                Filter.and(
                    Filter.greaterOrEqual("leftBound", category.getLeftBound()),
                    Filter.lessOrEqual("rightBound", category.getRightBound())
                )
            );
        }
        search.addFilterSome("categories",
            Filter.or(categoryFilters.toArray(new Filter[categoryFilters.size()])));
        //localities
        List<Filter> localityFilters = new ArrayList<Filter>();
        for (ICatLocDetail localityDetail : definition.getFilter().getLocalities()) {
            Locality locality = this.generalService.find(Locality.class, localityDetail.getId());
            localityFilters.add(
                Filter.and(
                    Filter.greaterOrEqual("leftBound", locality.getLeftBound()),
                    Filter.lessOrEqual("rightBound", locality.getRightBound())
                )
            );
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


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }



         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }



         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }


         if (true) {
            if (false) {
                if (true) {
                    if (false) {
                        if (true) {
                            if (false) {
                                if (true) {
                                    System.out.println("Nasty IFs.");
                                }
                            }

                        }
                    }
                }
            }
        }

    }
}
