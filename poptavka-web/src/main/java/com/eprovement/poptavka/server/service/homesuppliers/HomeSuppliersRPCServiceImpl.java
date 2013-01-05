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
import com.eprovement.poptavka.service.user.SupplierService;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.search.FilterItem;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.gwt.core.client.GWT;
import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.Sort;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 *
 * @author Praso TODO Praso - optimize this PRC service as other RPC services
 */
@Configurable
public class HomeSuppliersRPCServiceImpl extends AutoinjectingRemoteService implements HomeSuppliersRPCService {

    private static final Logger LOGGER = Logger.getLogger("HomeSuppliersRPCServiceImpl");
    private GeneralService generalService;
    private TreeItemService treeItemService;
    private LocalityService localityService;
    private CategoryService categoryService;
    private SupplierService supplierService;
    private Converter<Supplier, FullSupplierDetail> supplierConverter;
    private Converter<Category, CategoryDetail> categoryConverter;
    private Converter<ResultCriteria, SearchDefinition> criteriaConverter;

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

    /**************************************************************************/
    /*  Categories                                                            */
    /**************************************************************************/
    @Override
    public CategoryDetail getCategory(long categoryID) throws RPCException {
        return categoryConverter.convertToTarget(categoryService.getById(categoryID));
    }

//    @Override
//    public List<Integer> getCategoryParentsWithIndexes(Long category) throws RPCException {
//        System.out.println("Getting parent categories");
//        Category cat = categoryService.getById(category);
//        List<Integer> parentsWithIdxs = new ArrayList<Integer>();
//        while (cat != null) {
//            parentsWithIdxs.add(getCategoriesByItsLevel(cat.getLevel(), cat.getParent()).indexOf(cat));
//            cat = cat.getParent();
//        }
//        Collections.reverse(parentsWithIdxs);
//        return parentsWithIdxs;
//    }

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
        //TODO Martin implement fulltext search
        if (definition == null || definition.getFilter() == null) {
            return supplierService.getCount();
        } else {
            if (definition.getFilter().getAttributes().isEmpty()) {
                return filterWithoutAttributesCount(definition);
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
        //TODO Martin implement fulltext search
        if (definition.getFilter() == null) {
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
        } else {
            if (definition.getFilter().getAttributes().isEmpty()) {
                return filterWithoutAttributes(definition);
            } else {
                return filterWithAttributes(definition);
            }
        }
    }

    /**************************************************************************/
    /*  Get category suppliers                                                */
    /**************************************************************************/
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
    private List<FullSupplierDetail> getSortedCategorySuppliers(SearchDefinition searchDefinition) {
        List<Category> cats = new ArrayList<Category>();
        for (CategoryDetail catDetail : searchDefinition.getFilter().getCategories()) {
            cats.add(categoryService.getById(catDetail.getId()));
        }
        return supplierConverter.convertToTargetList(supplierService.getSuppliers(
                criteriaConverter.convertToSource(searchDefinition), cats.toArray(new Category[cats.size()])));
    }

    /**************************************************************************/
    /*  Get category locality suppliers                                       */
    /**************************************************************************/
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
            locs.add(localityService.getLocality(locDetail.getId()));
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
    private List<FullSupplierDetail> getSortedCategoryLocalitySuppliers(SearchDefinition searchDefinition) {
        List<Category> cats = new ArrayList<Category>();
        for (CategoryDetail catDetail : searchDefinition.getFilter().getCategories()) {
            cats.add(categoryService.getById(catDetail.getId()));
        }
        List<Locality> locs = new ArrayList<Locality>();
        for (LocalityDetail catDetail : searchDefinition.getFilter().getLocalities()) {
            locs.add(localityService.getLocality(catDetail.getId()));
        }
        return supplierConverter.convertToTargetList(supplierService.getSuppliers(
                criteriaConverter.convertToSource(searchDefinition),
                cats.toArray(new Category[cats.size()]),
                locs.toArray(new Locality[locs.size()])));
    }

    /**************************************************************************/
    /*  Fileter methods                                                       */
    /**************************************************************************/
    /**
     * This method decide which backend method used to retrieve data. Method is
     * used when <b>no additional attributes filtering</b> is required,
     * therefore there is no need to use backend methods which use
     * <i>general.Search</i> for retrieving data.
     *
     * @param definition - define filtering criteria, which helps this method to
     * make decision
     * @return suppliers count
     */
    private long filterWithoutAttributesCount(SearchDefinition definition) {
        //1 0
        if (!definition.getFilter().getCategories().isEmpty()
                && definition.getFilter().getLocalities().isEmpty()) {
            return getCategorySuppliersCount(definition.getFilter().getCategories());
        }
        //1 1  --> perform join if filtering by category and locality was used
        if (!definition.getFilter().getCategories().isEmpty()
                && !definition.getFilter().getLocalities().isEmpty()) {
            return getCategoryLocalitySuppliersCount(
                    definition.getFilter().getCategories(),
                    definition.getFilter().getLocalities());
        }
        return -1L;
    }

    /**
     * This method decide which backend method used to retrieve data. Method is
     * used when <b>additional attributes filtering</b> is required, therefore
     * there is need to use backend methods which use <i>general.Search</i> for
     * retrieving data.
     *
     * @param definition - define filtering criteria, which helps this method to
     * make decision
     * @return suppliers count
     */
    private long filterWithAttributesCount(SearchDefinition definition) {
        //1 0
        if (!definition.getFilter().getCategories().isEmpty()
                && definition.getFilter().getLocalities().isEmpty()) {
            Search search = this.getCategoryFilter(definition);
            return (long) generalService.searchAndCount(search).getTotalCount();
        }
        //1 1  --> perform join if filtering by category and locality was used
        if (!definition.getFilter().getCategories().isEmpty()
                && !definition.getFilter().getLocalities().isEmpty()) {
            return generalService.searchAndCount(getCategoryFilter(definition)).getTotalCount()
                    + generalService.searchAndCount(getLocalityFilter(definition)).getTotalCount();
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
    private List<FullSupplierDetail> filterWithoutAttributes(SearchDefinition searchDefinition) {
        //1 0
        if (!searchDefinition.getFilter().getCategories().isEmpty()
                && searchDefinition.getFilter().getLocalities().isEmpty()) {
            return getSortedCategorySuppliers(searchDefinition);
        }
        //1 1  --> perform join if filtering by category and locality was used
        if (!searchDefinition.getFilter().getCategories().isEmpty()
                && !searchDefinition.getFilter().getLocalities().isEmpty()) {
            return getSortedCategoryLocalitySuppliers(searchDefinition);
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
    private List<FullSupplierDetail> filterWithAttributes(SearchDefinition definition) {
        //1 0
        if (!definition.getFilter().getCategories().isEmpty()
                && definition.getFilter().getLocalities().isEmpty()) {
            Search search = this.getCategoryFilter(definition);
            return this.createSupplierDetailListCat(this.generalService.search(search));
        }
        //1 1  --> perform join if filtering by category and locality was used
        if (!definition.getFilter().getCategories().isEmpty()
                && !definition.getFilter().getLocalities().isEmpty()) {
            List<FullSupplierDetail> suppliersCat = this.createSupplierDetailListCat(
                    this.generalService.searchAndCount(
                    this.getCategoryFilter(definition))
                    .getResult());

            List<FullSupplierDetail> suppliersLoc = this.createSupplierDetailListLoc(
                    this.generalService.searchAndCount(
                    this.getLocalityFilter(definition))
                    .getResult());

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

    private Search getCategoryFilter(SearchDefinition definition) {
        Search categorySearch = new Search(SupplierCategory.class);
        List<Category> allSubCategories = new ArrayList<Category>();
        //nemusi byt foreach pretoze homeSuppliers ma vzdy len jednu kategoriu
        for (CategoryDetail cat : definition.getFilter().getCategories()) {
            allSubCategories = Arrays.asList(getAllSubCategories(cat.getId()));
        }
        categorySearch.addFilterIn("category", allSubCategories);

        if (!definition.getFilter().getAttributes().isEmpty()) {
            categorySearch.addFilterIn("supplier", generalService.search(getSupplierFilter(definition)));
        }

        return this.getSortSearch(categorySearch, definition.getOrderColumns(), "supplier");
    }

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

    private Search getSupplierFilter(SearchDefinition definition) {
        Boolean filterApplied = false;
        Search search = new Search(Supplier.class);
        for (FilterItem item : definition.getFilter().getAttributes()) {
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
        return this.getSortSearch(search, definition.getOrderColumns(), "supplier");
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
            userDetails.add(supplierConverter.convertToTarget(supplierCat.getSupplier()));
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

    private Locality[] getAllSublocalities(Long id) {
        final Locality loc = this.localityService.getLocality(id);
        final List<Locality> allSubLocalites = this.treeItemService.getAllDescendants(loc, Locality.class);
        allSubLocalites.add(loc);
        return allSubLocalites.toArray(new Locality[allSubLocalites.size()]);
    }

    /**
     * Gets categories of given level and given parent.
     * @param level
     * @param parent
     * @return
     *
     */
    private List<Category> getCategoriesByItsLevel(int level, Category parent) {
        if (level == 1) {
            return categoryService.getRootCategories();
        } else {
            Search search = new Search(Category.class);
            search.addFilterEqual("level", level);
            search.addFilterEqual("parent", parent);
            return generalService.search(search);
        }
    }
}
