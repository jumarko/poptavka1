/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.server.service.homesuppliers;

import com.eprovement.poptavka.client.service.demand.HomeSuppliersRPCService;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.domain.user.BusinessUserData;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.domain.user.SupplierCategory;
import com.eprovement.poptavka.domain.user.SupplierLocality;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.converter.SortConverter;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.service.common.TreeItemService;
import com.eprovement.poptavka.service.demand.CategoryService;
import com.eprovement.poptavka.service.fulltext.FulltextSearchService;
import com.eprovement.poptavka.service.user.SupplierService;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.search.FilterItem;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SortPair;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.Sort;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 *
 * This RPC hadnled all requests from HomeSuppliers module such as suppliers catalogue and their details.
 * @author ivlcek
 */
@Configurable
public class HomeSuppliersRPCServiceImpl extends AutoinjectingRemoteService implements HomeSuppliersRPCService {

    private GeneralService generalService;
    private TreeItemService treeItemService;
    private LocalityService localityService;
    private CategoryService categoryService;
    private SupplierService supplierService;
    private FulltextSearchService fulltextSearchService;
    private Converter<Supplier, FullSupplierDetail> supplierConverter;
    private Converter<Category, ICatLocDetail> categoryConverter;
    private Converter<Filter, FilterItem> filterConverter;
    private SortConverter sortConverter;

    @Autowired
    public void setSupplierService(SupplierService supplierService) {
        this.supplierService = supplierService;
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
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
    }

    @Autowired
    public void setTreeItemService(TreeItemService treeItemService) {
        this.treeItemService = treeItemService;
    }

    @Autowired
    public void setFulltextSearchService(FulltextSearchService fulltextSearchService) {
        this.fulltextSearchService = fulltextSearchService;
    }

    @Autowired
    public void setSupplierConverter(
            @Qualifier("supplierConverter") Converter<Supplier, FullSupplierDetail> supplierConverter) {
        this.supplierConverter = supplierConverter;
    }

    @Autowired
    public void setCategoryConverter(
            @Qualifier("categoryConverter") Converter<Category, ICatLocDetail> categoryConverter) {
        this.categoryConverter = categoryConverter;
    }

    @Autowired
    public void setFilterConverter(
            @Qualifier("filterConverter") Converter<Filter, FilterItem> filterConverter) {
        this.filterConverter = filterConverter;
    }

    @Autowired
    public void setSortConverter(@Qualifier("sortConverter") SortConverter sortConverter) {
        this.sortConverter = sortConverter;
    }

    /**************************************************************************/
    /*  Categories                                                            */
    /**************************************************************************/
    @Override
    public ICatLocDetail getCategory(long categoryID) throws RPCException {
        return categoryConverter.convertToTarget(categoryService.getById(categoryID));
    }

    /**************************************************************************/
    /*  Suppliers                                                             */
    /**************************************************************************/
    @Override
    public FullSupplierDetail getSupplier(long supplierID) throws RPCException {
        return supplierConverter.convertToTarget(supplierService.getById(supplierID));
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
     * @param definition - define filter criteria
     * @return Suppliers count
     * @throws RPCException
     */
    @Override
    public Integer getSuppliersCount(SearchDefinition definition) throws RPCException {
        if (definition == null || definition.getFilter() == null) {
            //general
            return getSuppliersGeneralCount().intValue();
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
     * Method in general gets Suppliers data according to given filter criteria
     * represented by start, maxResult, SearchModuleDataHolder, ordering. If
     * user don't specify attribute to filter throught, there is no need to use
     * generalService.Search for retrieving data. Therefore different set of
     * methods is used for optimizing the proces.
     *
     * @param start - define begin range of pagination
     * @param maxResult - define how many data will be retrieved
     * @param detail - define filter criteria
     * @param orderColumns - define ordering (attribute, type)
     * @return list of Supplier details
     * @throws RPCException
     */
    @Override
    public List<FullSupplierDetail> getSuppliers(SearchDefinition definition) throws RPCException {
        if (definition == null || definition.getFilter() == null) {
            //general
            return getSuppliersGeneral(definition);
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
    /*  Get suppliers in generall                                             */
    /**************************************************************************/
    /**
     * Get all supplier's count.
     *
     * @return supplier's count
     */
    private Long getSuppliersGeneralCount() {
        return supplierService.getCount();
    }

    /**
     * Get suppliers limited by FirstResult and MaxResult attributes.
     *
     * @param definition holder for FirstResult and MaxResult attributes
     * @return suppliers
     */
    private List<FullSupplierDetail> getSuppliersGeneral(SearchDefinition definition) {
        Search search = new Search(Supplier.class);
        search.setFirstResult(definition.getFirstResult());
        search.setMaxResults(definition.getMaxResult());
        for (SortPair column : definition.getSortOrder()) {
            if (column.getColumnOrderType() == OrderType.ASC) {
                search.addSort(Sort.asc(column.getColumnName()));
            } else {
                search.addSort(Sort.desc(column.getColumnName()));
            }
        }
        return supplierConverter.convertToTargetList(generalService.search(search));
    }

    /**************************************************************************/
    /*  Get suppliers in fulltext search                                      */
    /**************************************************************************/
    /**
     * Get suppliers count by full text search.
     *
     * @param searchText - text to be searched
     * @return suppliers count
     * @throws RPCException
     */
    public int fullTextSearchCount(SearchDefinition definition) throws RPCException {
        final List<BusinessUserData> foundUsers = this.fulltextSearchService.search(
                BusinessUserData.class, BusinessUserData.USER_FULLTEXT_FIELDS, definition.getFilter().getSearchText());

        Search search = new Search(Supplier.class);
        search.addFilterIn("businessUser.businessUserData", foundUsers);

        return generalService.count(search);
    }

    /**
     * Get suppliers by full text search.
     *
     * @param searchText - text to be search
     * @return suppliers
     * @throws RPCException
     */
    public List<FullSupplierDetail> fullTextSearch(SearchDefinition definition) throws RPCException {
        final List<BusinessUserData> foundUsers = this.fulltextSearchService.search(
                BusinessUserData.class, BusinessUserData.USER_FULLTEXT_FIELDS, definition.getFilter().getSearchText());

        Search search = new Search(Supplier.class);
        search.addFilterIn("businessUser.businessUserData", foundUsers);
        search.setFirstResult(definition.getFirstResult());
        search.setMaxResults(definition.getMaxResult());

        return supplierConverter.convertToTargetList(generalService.search(search));
    }

    /**************************************************************************/
    /*  Get suppliers in search by representer be seraching criteria          */
    /**************************************************************************/
    /**
     * This method decide which method is used to create Search object for retrieve counts.
     *
     * @param definition - define filtering criteria
     * @return suppliers count
     */
    private int filterWithAttributesCount(SearchDefinition definition) {
        //0 0
        if (definition.getFilter().getCategories().isEmpty()
                && definition.getFilter().getLocalities().isEmpty()) {
            Search search = this.getSupplierFilter(definition);
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
     * @return suppliers
     */
    private List<FullSupplierDetail> filterWithAttributes(SearchDefinition definition) {
        //0 0
        if (definition.getFilter().getCategories().isEmpty()
                && definition.getFilter().getLocalities().isEmpty()) {
            Search search = this.getSupplierFilter(definition);
            search.setFirstResult(definition.getFirstResult());
            search.setMaxResults(definition.getMaxResult());
            return this.supplierConverter.convertToTargetList(this.generalService.search(search));
        }
        //1 0
        if (!definition.getFilter().getCategories().isEmpty()
                && definition.getFilter().getLocalities().isEmpty()) {
            Search search = this.getCategoryFilter(definition);
            search.setFirstResult(definition.getFirstResult());
            search.setMaxResults(definition.getMaxResult());
            return this.supplierConverter.convertToTargetList(this.generalService.search(search));
        }
        //0 1
        if (definition.getFilter().getCategories().isEmpty()
                && !definition.getFilter().getLocalities().isEmpty()) {
            Search search = this.getLocalityFilter(definition);
            search.setFirstResult(definition.getFirstResult());
            search.setMaxResults(definition.getMaxResult());
            return this.supplierConverter.convertToTargetList(this.generalService.search(search));
        }
        //1 1  --> perform join if filtering by category and locality was used
        if (!definition.getFilter().getCategories().isEmpty()
                && !definition.getFilter().getLocalities().isEmpty()) {
            //TODO LATER : Martin 16.4.2013, thing about better solution due to performance
            List<FullSupplierDetail> catLocDemands = getCategoryLocality(definition);
            int upperBound = definition.getFirstResult() + definition.getMaxResult();
            if (upperBound > catLocDemands.size()) {
                upperBound = catLocDemands.size();
            }
            return new ArrayList<FullSupplierDetail>(catLocDemands.subList(definition.getFirstResult(), upperBound));
        }
        return null;
    }

    /**************************************************************************/
    /*  Helper methods used in "Get suppliers in search" methods              */
    /**************************************************************************/
    /**
     * Create Search object for searching in categories and supplier's attributes.
     *
     * @param definition - represents searching criteria
     * @return
     */
    private Search getCategoryFilter(SearchDefinition definition) {
        Search categorySearch = new Search(SupplierCategory.class);
        // return only distinct demands
        categorySearch.addField("supplier");
        categorySearch.setDistinct(true);
        //filters
        List<Category> allSubCategories = new ArrayList<Category>();
        for (ICatLocDetail cat : definition.getFilter().getCategories()) {
            allSubCategories = Arrays.asList(getAllSubCategories(cat.getId()));
        }
        categorySearch.addFilterIn("category", allSubCategories);

        if (!definition.getFilter().getAttributes().isEmpty()) {
            categorySearch.addFilterIn("supplier", generalService.search(getSupplierFilter(definition)));
        }
        //sorts
        categorySearch.addSorts(sortConverter.convertToSourceList(
                categorySearch.getSearchClass(), definition.getSortOrder()));
        return categorySearch;
    }

    /**
     * Create Search object for searching in localities and supplier's attributes.
     *
     * @param definition - represents searching criteria
     * @return
     */
    private Search getLocalityFilter(SearchDefinition definition) {
        Search localitySearch = new Search(SupplierLocality.class);
        // return only distinct demands
        localitySearch.addField("supplier");
        localitySearch.setDistinct(true);
        //filters
        List<Locality> allSubLocalities = new ArrayList<Locality>();
        for (ICatLocDetail loc : definition.getFilter().getLocalities()) {
            allSubLocalities = Arrays.asList(
                    this.getAllSublocalities(loc.getId()));
        }
        localitySearch.addFilterIn("locality", allSubLocalities);

        if (!definition.getFilter().getAttributes().isEmpty()) {
            localitySearch.addFilterIn("supplier", generalService.search(getSupplierFilter(definition)));
        }
        //sorts
        localitySearch.addSorts(sortConverter.convertToSourceList(
                localitySearch.getSearchClass(), definition.getSortOrder()));
        return localitySearch;
    }

    /**
     * Create Search object for searching in categories and localities and suppliers attributes.
     *
     * @param definition - represents searching criteria
     * @return
     */
    private List<FullSupplierDetail> getCategoryLocality(SearchDefinition definition) {
        List<FullSupplierDetail> demandsCat = supplierConverter.convertToTargetList(
                this.generalService.searchAndCount(
                this.getCategoryFilter(definition))
                .getResult());

        List<FullSupplierDetail> demandsLoc = supplierConverter.convertToTargetList(
                this.generalService.searchAndCount(
                this.getLocalityFilter(definition))
                .getResult());

        List<FullSupplierDetail> demands = new ArrayList<FullSupplierDetail>();
        for (FullSupplierDetail demandCat : demandsCat) {
            if (demandsLoc.contains(demandCat)) {
                demands.add(demandCat);
            }
        }
        //TODO LATER MARTIN 24.4.2013 - how to sort intersection??
        return demands;
    }

    /**
     * Create Search object for searching in supplier's attributes.
     *
     * @param definition - represents searching criteria
     * @return
     */
    private Search getSupplierFilter(SearchDefinition definition) {
        Search search = new Search(Supplier.class);

        ArrayList<Filter> filtersOr = filterConverter.convertToSourceList(definition.getFilter().getAttributes());
        //filters
        search.addFilterAnd(filtersOr.toArray(new Filter[filtersOr.size()]));
        //sorts
        search.addSorts(sortConverter.convertToSourceList(search.getSearchClass(), definition.getSortOrder()));
        return search;
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
