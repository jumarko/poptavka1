/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.server.service.homesuppliers;

import com.eprovement.poptavka.client.service.demand.HomeSuppliersRPCService;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.domain.user.BusinessUserData;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.domain.user.SupplierCategory;
import com.eprovement.poptavka.domain.user.SupplierLocality;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.service.common.TreeItemService;
import com.eprovement.poptavka.service.demand.CategoryService;
import com.eprovement.poptavka.service.fulltext.FulltextSearchService;
import com.eprovement.poptavka.service.user.SupplierService;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
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
    private Converter<Category, CategoryDetail> categoryConverter;
    private Converter<ResultCriteria, SearchDefinition> criteriaConverter;
    private Converter<Filter, FilterItem> filterConverter;

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
    public long getSuppliersCount(SearchDefinition definition) throws RPCException {
        if (definition == null || definition.getFilter() == null) {
            //general
            return getSuppliersGeneralCount();
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
        for (String column : definition.getOrderColumns().keySet()) {
            if (definition.getOrderColumns().get(column) == OrderType.ASC) {
                search.addSort(Sort.asc(column));
            } else {
                search.addSort(Sort.desc(column));
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
    public long fullTextSearchCount(SearchDefinition definition) throws RPCException {
        final List<BusinessUserData> foundUsers = this.fulltextSearchService.search(
                BusinessUserData.class, BusinessUserData.USER_FULLTEXT_FIELDS, definition.getFilter().getSearchText());

        Search search = new Search(Supplier.class);
        search.addFilterIn("businessUser.businessUserData", foundUsers);
        search.setFirstResult(definition.getFirstResult());
        search.setMaxResults(definition.getMaxResult());
        search.addField("id", Field.OP_COUNT);
        search.setResultMode(Search.RESULT_SINGLE);

        return (Long) generalService.searchUnique(search);
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
    private long filterWithAttributesCount(SearchDefinition definition) {
        //0 0
        if (definition.getFilter().getCategories().isEmpty()
                && definition.getFilter().getLocalities().isEmpty()) {
            Search search = this.getSortSearch(this.getSupplierFilter(definition), definition.getOrderColumns(), "");
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
     * @return suppliers
     */
    private List<FullSupplierDetail> filterWithAttributes(SearchDefinition definition) {
        //0 0
        if (definition.getFilter().getCategories().isEmpty()
                && definition.getFilter().getLocalities().isEmpty()) {
            Search search = this.getSortSearch(this.getSupplierFilter(definition), definition.getOrderColumns(), "");
            search.setFirstResult(definition.getFirstResult());
            search.setMaxResults(definition.getMaxResult());
            return supplierConverter.convertToTargetList(this.generalService.search(search));
        }
        //1 0
        if (!definition.getFilter().getCategories().isEmpty()
                && definition.getFilter().getLocalities().isEmpty()) {
            Search search = this.getCategoryFilter(definition);
            search.setFirstResult(definition.getFirstResult());
            search.setMaxResults(definition.getMaxResult());
            return this.createSupplierDetailListCat(this.generalService.search(search));
        }
        //0 1
        if (definition.getFilter().getCategories().isEmpty()
                && !definition.getFilter().getLocalities().isEmpty()) {
            Search search = this.getLocalityFilter(definition);
            search.setFirstResult(definition.getFirstResult());
            search.setMaxResults(definition.getMaxResult());
            return this.createSupplierDetailListLoc(this.generalService.searchAndCount(search).getResult());
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

        List<Category> allSubCategories = new ArrayList<Category>();

        for (CategoryDetail cat : definition.getFilter().getCategories()) {
            allSubCategories = Arrays.asList(getAllSubCategories(cat.getId()));
        }
        categorySearch.addFilterIn("category", allSubCategories);

        if (!definition.getFilter().getAttributes().isEmpty()) {
            categorySearch.addFilterIn("supplier", generalService.search(getSupplierFilter(definition)));
        }

        return this.getSortSearch(categorySearch, definition.getOrderColumns(), "supplier");
    }

    /**
     * Create Search object for searching in localities and supplier's attributes.
     *
     * @param definition - represents searching criteria
     * @return
     */
    private Search getLocalityFilter(SearchDefinition definition) {
        Search localitySearch = new Search(SupplierLocality.class);

        List<Locality> allSubLocalities = new ArrayList<Locality>();
        for (LocalityDetail loc : definition.getFilter().getLocalities()) {
            allSubLocalities = Arrays.asList(
                    this.getAllSublocalities(loc.getId()));
        }
        localitySearch.addFilterIn("locality", allSubLocalities);

        if (!definition.getFilter().getAttributes().isEmpty()) {
            localitySearch.addFilterIn("supplier", generalService.search(getSupplierFilter(definition)));
        }

        return this.getSortSearch(localitySearch, definition.getOrderColumns(), "supplier");
    }

    /**
     * Create Search object for searching in categories and localities and suppliers attributes.
     *
     * @param definition - represents searching criteria
     * @return
     */
    private List<FullSupplierDetail> getCategoryLocality(SearchDefinition definition) {
        List<FullSupplierDetail> demandsCat = this.createSupplierDetailListCat(
                this.generalService.searchAndCount(
                this.getCategoryFilter(definition))
                .getResult());

        List<FullSupplierDetail> demandsLoc = this.createSupplierDetailListLoc(
                this.generalService.searchAndCount(
                this.getLocalityFilter(definition))
                .getResult());

        List<FullSupplierDetail> demands = new ArrayList<FullSupplierDetail>();
        for (FullSupplierDetail demandCat : demandsCat) {
            if (demandsLoc.contains(demandCat)) {
                demands.add(demandCat);
            }
        }
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
    private ArrayList<FullSupplierDetail> createSupplierDetailListCat(Collection<SupplierCategory> suppliersCat) {
        ArrayList<FullSupplierDetail> userDetails = new ArrayList<FullSupplierDetail>();
        for (SupplierCategory supplierCat : suppliersCat) {
            userDetails.add(supplierConverter.convertToTarget(supplierCat.getSupplier()));
        }
        return userDetails;
    }

    private ArrayList<FullSupplierDetail> createSupplierDetailListLoc(Collection<SupplierLocality> suppliersLoc) {
        ArrayList<FullSupplierDetail> userDetails = new ArrayList<FullSupplierDetail>();
        for (SupplierLocality supplierLoc : suppliersLoc) {
            userDetails.add(supplierConverter.convertToTarget(supplierLoc.getSupplier()));
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
