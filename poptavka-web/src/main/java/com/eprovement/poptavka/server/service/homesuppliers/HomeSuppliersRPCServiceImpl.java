/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.server.service.homesuppliers;

import com.eprovement.poptavka.client.main.common.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.main.common.search.dataHolders.FilterItem;
import com.eprovement.poptavka.client.service.demand.HomeSuppliersRPCService;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.common.OrderType;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.user.BusinessUserData;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.domain.user.SupplierCategory;
import com.eprovement.poptavka.domain.user.SupplierLocality;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.service.common.TreeItemService;
import com.eprovement.poptavka.service.demand.CategoryService;
import com.eprovement.poptavka.service.user.SupplierService;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.converter.SupplierConverter;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.google.gwt.core.client.GWT;
import com.googlecode.genericdao.search.Search;
import java.util.*;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Praso TODO Praso - optimize this PRC service as other RPC services
 */
@Component(HomeSuppliersRPCService.URL)
public class HomeSuppliersRPCServiceImpl extends AutoinjectingRemoteService implements HomeSuppliersRPCService {

    private static final Logger LOGGER = Logger.getLogger("HomeSuppliersRPCServiceImpl");
    private GeneralService generalService;
    private TreeItemService treeItemService;
    private LocalityService localityService;
    private CategoryService categoryService;
    private SupplierService supplierService;
    private SupplierConverter supplierConverter = new SupplierConverter();

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
    
    // ***********************************************************************
    // Categories getter methods
    // ***********************************************************************
    @Override
    public ArrayList<CategoryDetail> getCategories() throws RPCException {
        final List<Category> categories = categoryService.getRootCategories();
        return createCategoryDetailList(categories);
    }

    /**
     * Return all parents of given category within given category.
     *
     * @param category - given category id
     * @return list of parents and given category
     */
    @Override
    public ArrayList<CategoryDetail> getCategoryParents(Long category) throws RPCException {
        System.out.println("Getting parent categories");
        Category cat = categoryService.getById(category);
        List<Category> parents = new ArrayList<Category>();
        //add cat itself
        parents.add(cat);
        while (cat.getParent() != null) {
            parents.add(cat.getParent());
            cat = cat.getParent();
        }

        return createCategoryDetailList(parents);
    }

    @Override
    public ArrayList<CategoryDetail> getCategoryChildren(Long category) throws RPCException {
        System.out.println("Getting children categories");
        try {
            if (category != null) {
                final Category cat = categoryService.getById(category);
                if (cat != null) {
                    return createCategoryDetailList(cat.getChildren());
                }
            }
        } catch (NullPointerException ex) {
            LOGGER.info("NullPointerException while executing getCategoryChildren");
        }
        return new ArrayList<CategoryDetail>();
    }

    // ***********************************************************************
    // Get filtered demands
    // ***********************************************************************
    /**
     * Method in general gets Suppliers count according to given filter criteria
     * represented by SearchModuleDataHolder. If user don't specify attribute to
     * filter throught, there is no need to use generalService.Search for
     * retrieving data. Therefore different set of methods is used for
     * optimizing the proces.
     *
     * @param detail - define filter criteria
     * @return Suppliers count
     * @throws RPCException
     */
    @Override
    public long getSuppliersCount(SearchModuleDataHolder detail) throws RPCException {
        //TODO Martin implement fulltext search
        if (detail == null || detail.getAttibutes().isEmpty()) {
            return filterWithoutAttributesCount(detail);
        } else {
            return filterWithAttributesCount(detail);
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
    public List<FullSupplierDetail> getSuppliers(int start, int maxResult,
            SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) throws RPCException {
        //TODO Martin implement fulltext search
        if (detail == null || detail.getAttibutes().isEmpty()) {
            return filterWithoutAttributes(start, maxResult, detail, orderColumns);
        } else {
            return filterWithAttributes(detail, orderColumns);
        }
    }

    // ***********************************************************************
    // Get category suppliers
    // ***********************************************************************
    /**
     * This mehtod is used when <b>category filtering</b> is required. Get
     * suppliers count of given categories.
     *
     * @param categories - define categories to filter throught
     * @return suppliers count of given categories
     */
    private Long getCategorySuppliersCount(List<CategoryDetail> categories) {
        List<Category> cats = new ArrayList<Category>();
        for (CategoryDetail catDetail : categories) {
            cats.add(categoryService.getById(catDetail.getId()));
        }
        return supplierService.getSuppliersCount(cats.toArray(new Category[cats.size()]));
    }

    /**
     * This method is used when <b>category filtering</b> is required. Get
     * suppliers data of given categories
     *
     * @param categories - define categories to filter through
     * @param maxResult - define how many data will be retrieved
     * @param orderColumns - define ordering (attribute, type)
     * @return supplier details list of given categories
     */
    private List<FullSupplierDetail> getSortedCategorySuppliers(List<CategoryDetail> categories,
            int start, int maxResult, Map<String, OrderType> orderColumns) {
        List<Category> cats = new ArrayList<Category>();
        for (CategoryDetail catDetail : categories) {
            cats.add(categoryService.getById(catDetail.getId()));
        }
        final ResultCriteria resultCriteria =
                new ResultCriteria.Builder().firstResult(start).maxResults(maxResult).orderByColumns(orderColumns).build();
        return supplierConverter.convertToTargetList(supplierService.getSuppliers(
                resultCriteria, cats.toArray(new Category[cats.size()])));
    }

    // ***********************************************************************
    // Get category locality suppliers
    // ***********************************************************************
    /**
     * This mehtod is used when both <b>category & locality filtering</b> is
     * required. Get suppliers count of given categories & localities.
     *
     * @param categories- define categories to filter throught
     * @param localities - define localities to filter throught
     * @return suppliers count of given categories & localities
     */
    private Long getCategoryLocalitySuppliersCount(List<CategoryDetail> categories, List<LocalityDetail> localities) {
        List<Category> cats = new ArrayList<Category>();
        for (CategoryDetail catDetail : categories) {
            cats.add(categoryService.getById(catDetail.getId()));
        }
        List<Locality> locs = new ArrayList<Locality>();
        for (LocalityDetail locDetail : localities) {
            locs.add(localityService.getLocality(locDetail.getCode()));
        }
        return supplierService.getSuppliersCount(
                cats.toArray(new Category[cats.size()]),
                locs.toArray(new Locality[locs.size()]));
    }

    /**
     * This method is used when both <b>categories & localities filtering</b> is
     * required. Get suppliers data of given categories & localities
     *
     * @param categories - define categories to filter through
     * @param localities - define localities to filter through
     * @param maxResult - define how many data will be retrieved
     * @param orderColumns - define ordering (attribute, type)
     * @return supplier details list of given categories & localities
     */
    private List<FullSupplierDetail> getSortedCategoryLocalitySuppliers(List<CategoryDetail> categories,
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
                new ResultCriteria.Builder().firstResult(start).maxResults(count).orderByColumns(orderColumns).build();
        return supplierConverter.convertToTargetList(supplierService.getSuppliers(
                resultCriteria, cats.toArray(new Category[cats.size()]), locs.toArray(new Locality[locs.size()])));
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
     * @return suppliers count
     */
    private long filterWithoutAttributesCount(SearchModuleDataHolder detail) {
        //1 0
        if (!detail.getCategories().isEmpty() && detail.getLocalities().isEmpty()) {
            return getCategorySuppliersCount(detail.getCategories());
        }
        //1 1  --> perform join if filtering by category and locality was used
        if (!detail.getCategories().isEmpty() && !detail.getLocalities().isEmpty()) {
            return getCategoryLocalitySuppliersCount(detail.getCategories(), detail.getLocalities());
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
     * @return suppliers count
     */
    private long filterWithAttributesCount(SearchModuleDataHolder detail) {
        //1 0
        if (!detail.getCategories().isEmpty() && detail.getLocalities().isEmpty()) {
            Search search = this.getCategoryFilter(detail, null);
            return (long) generalService.searchAndCount(search).getTotalCount();
        }
        //1 1  --> perform join if filtering by category and locality was used
        if (!detail.getCategories().isEmpty() && !detail.getLocalities().isEmpty()) {
            return generalService.searchAndCount(getCategoryFilter(detail, null)).getTotalCount()
                    + generalService.searchAndCount(getLocalityFilter(detail, null)).getTotalCount();
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
     * @return supplier detail list
     */
    private List<FullSupplierDetail> filterWithoutAttributes(int start, int count,
            SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        //1 0
        if (!detail.getCategories().isEmpty() && detail.getLocalities().isEmpty()) {
            return getSortedCategorySuppliers(detail.getCategories(), start, count, orderColumns);
        }
        //1 1  --> perform join if filtering by category and locality was used
        if (!detail.getCategories().isEmpty() && !detail.getLocalities().isEmpty()) {
            return getSortedCategoryLocalitySuppliers(detail.getCategories(), detail.getLocalities(),
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
     * @return supplier detail list
     */
    private List<FullSupplierDetail> filterWithAttributes(SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        //1 0
        if (!detail.getCategories().isEmpty() && detail.getLocalities().isEmpty()) {
            Search search = this.getCategoryFilter(detail, orderColumns);
            return this.createSupplierDetailListCat(this.generalService.search(search));
        }
        //1 1  --> perform join if filtering by category and locality was used
        if (!detail.getCategories().isEmpty() && !detail.getLocalities().isEmpty()) {
            List<FullSupplierDetail> suppliersCat = this.createSupplierDetailListCat(
                    this.generalService.searchAndCount(this.getCategoryFilter(detail, orderColumns)).getResult());

            List<FullSupplierDetail> suppliersLoc = this.createSupplierDetailListLoc(
                    this.generalService.searchAndCount(this.getLocalityFilter(detail, orderColumns)).getResult());

            List<FullSupplierDetail> suppliers = new ArrayList<FullSupplierDetail>();
            for (FullSupplierDetail supplierCat : suppliersCat) {
                if (suppliersLoc.contains(supplierCat)) {
                    suppliers.add(supplierCat);
                }
            }
            return suppliers;
        }
        return null;
    }

    private Search getCategoryFilter(SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        Search categorySearch = new Search(SupplierCategory.class);
        List<Category> allSubCategories = new ArrayList<Category>();
        //nemusi byt foreach pretoze homeSuppliers ma vzdy len jednu kategoriu
        for (CategoryDetail cat : detail.getCategories()) {
            allSubCategories = Arrays.asList(getAllSubCategories(cat.getId()));
        }
        categorySearch.addFilterIn("category", allSubCategories);

        if (!detail.getAttibutes().isEmpty()) {
            categorySearch.addFilterIn("supplier", generalService.search(getSupplierFilter(detail, orderColumns)));
        }

        return categorySearch;
    }

    private Search getLocalityFilter(SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        Search localitySearch = new Search(SupplierLocality.class);
        List<Locality> allSubLocalities = new ArrayList<Locality>();
        for (LocalityDetail loc : detail.getLocalities()) {
            allSubLocalities = Arrays.asList(
                    this.getAllSublocalities(loc.getCode()));
        }
        localitySearch.addFilterIn("locality", allSubLocalities);

        if (!detail.getAttibutes().isEmpty()) {
            localitySearch.addFilterIn("supplier", generalService.search(getSupplierFilter(detail, orderColumns)));
        }

        return localitySearch;
    }

    private Search getSupplierFilter(SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        Boolean filterApplied = false;
        Search search = new Search(Supplier.class);
        for (FilterItem item : detail.getAttibutes()) {
            if (item.getValue().equals("companyName")) {
                Collection<BusinessUserData> data = generalService.search(
                        this.filter(new Search(BusinessUserData.class), "", item));
                search.addFilterIn("businessUser.businessUserData", data);
            } else if (item.getValue().equals("description")) {
                Collection<BusinessUserData> descsData = new ArrayList<BusinessUserData>();
                String[] descs = item.getValue().toString().split("\\s+");

                for (int i = 0; i < descs.length; i++) {
                    descsData.addAll(generalService.search(
                            this.filter(new Search(BusinessUserData.class), "", item)));
                }
                search.addFilterIn("businessUser.businessUserData", descsData);
            } else {
                this.filter(search, "", item);
            }
        }
        return this.getSortSearch(search, orderColumns, "supplier");
    }

    private Search getSortSearch(Search search, Map<String, OrderType> orderColumns, String prefix) {
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

    private ArrayList<FullSupplierDetail> createSupplierDetailListCat(Collection<SupplierCategory> suppliersCat) {
        ArrayList<FullSupplierDetail> userDetails = new ArrayList<FullSupplierDetail>();
        for (SupplierCategory supplierCat : suppliersCat) {
            userDetails.add(new SupplierConverter().convertToTarget(supplierCat.getSupplier()));
        }
        GWT.log("supplierDetailList created: " + userDetails.size());
        return userDetails;
    }

    private ArrayList<FullSupplierDetail> createSupplierDetailListLoc(Collection<SupplierLocality> suppliersLoc) {
        ArrayList<FullSupplierDetail> userDetails = new ArrayList<FullSupplierDetail>();
        for (SupplierLocality supplierLoc : suppliersLoc) {
            userDetails.add(supplierConverter.convertToTarget(supplierLoc.getSupplier()));
        }
        GWT.log("supplierDetailList created: " + userDetails.size());
        return userDetails;
    }

    private Category[] getAllSubCategories(Long id) {
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

    /**
     * Inner method for transforming domain Entity to front-end representation.
     * *
     */
    private ArrayList<CategoryDetail> createCategoryDetailList(List<Category> categories) {
        final ArrayList<CategoryDetail> categoryDetails = new ArrayList<CategoryDetail>();

        for (Category cat : categories) {
            categoryDetails.add(createCategoryDetail(cat));
        }

        return categoryDetails;
    }

    private CategoryDetail createCategoryDetail(Category category) {
        long suppliersCount = supplierService.getSuppliersCountQuick(category);
        CategoryDetail detail = new CategoryDetail(category.getId(), category.getName(), 0, suppliersCount);

        if (category.getChildren().isEmpty()) {
            detail.setParent(false);
        } else {
            detail.setParent(true);
        }
        return detail;
    }
}
