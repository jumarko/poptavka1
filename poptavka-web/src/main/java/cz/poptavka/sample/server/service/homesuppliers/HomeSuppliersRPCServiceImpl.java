/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.server.service.homesuppliers;

import com.google.gwt.core.client.GWT;
import com.googlecode.genericdao.search.Search;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.main.common.search.dataHolders.FilterItem;
import cz.poptavka.sample.client.service.demand.HomeSuppliersRPCService;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.domain.user.BusinessUserData;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.domain.user.SupplierCategory;
import cz.poptavka.sample.domain.user.SupplierLocality;
import cz.poptavka.sample.shared.domain.converter.SupplierConverter;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.service.address.LocalityService;
import cz.poptavka.sample.service.common.TreeItemService;
import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.demand.CategoryService;
import cz.poptavka.sample.service.user.SupplierService;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import cz.poptavka.sample.shared.exceptions.RPCException;

/**
 *
 * @author Praso
 * TODO Praso - optimize this PRC service as other RPC services
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

    @Override
    public long filterSuppliersCount(SearchModuleDataHolder detail) throws RPCException {
        //najlepsie by bolo pouzit supplierService.getSuppliersQuickCount, ale zatial nemozem, pretoze
        //nie je implementovane search
        if (detail.getAttibutes().isEmpty()) {
            return supplierService.getSuppliersCountQuick(
                    categoryService.getById(detail.getCategories().get(0).getId()));
        } else {
            return this.filter(detail, null).size();
        }
    }

    @Override
    public List<FullSupplierDetail> filterSuppliers(int start, int count,
            SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) throws RPCException {
        List<FullSupplierDetail> searchResult = this.filter(detail,
                orderColumns);
        if (searchResult.size() < (start + count)) {
            return searchResult.subList(start, searchResult.size());
        } else {
            return searchResult.subList(start, count);
        }
    }

    private List<FullSupplierDetail> filter(SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        //1 0
        if (detail.getCategories() != null && detail.getLocalities() == null) {
            Search search = this.getCategoryFilter(detail, orderColumns);
            return this.createSupplierDetailListCat(this.generalService.search(search));
        }
        //0 1
        if (detail.getCategories() == null && detail.getLocalities() != null) {
            Search search = this.getLocalityFilter(detail, orderColumns);
            return this.createSupplierDetailListLoc(this.generalService.searchAndCount(search).getResult());
        }
        //1 1  --> perform join if filtering by category and locality was used
        if (detail.getCategories() != null
                && detail.getLocalities() != null) {
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

    /** Category Services **/
    @Override
    public ArrayList<CategoryDetail> getCategories() throws RPCException {
        final List<Category> categories = categoryService.getRootCategories();
        return createCategoryDetailList(categories);
    }

    /**
     * Return all parents of given category within given category.
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

    /** Inner method for transforming domain Entity to front-end representation. **/
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
}
