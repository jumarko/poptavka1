/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.server.service.homesuppliers;

import com.google.gwt.core.client.GWT;
import com.googlecode.genericdao.search.Search;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.service.demand.HomeSuppliersRPCService;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.domain.user.BusinessUserData;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.domain.user.SupplierCategory;
import cz.poptavka.sample.domain.user.SupplierLocality;
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

import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.demand.CategoryService;
import cz.poptavka.sample.service.user.SupplierService;
import cz.poptavka.sample.shared.domain.CategoryDetail;

/**
 *
 * @author Praso
 * TODO Praso - optimize this PRC service as other RPC services
 */
public class HomeSuppliersRPCServiceImpl extends AutoinjectingRemoteService implements HomeSuppliersRPCService {

    private static final Logger LOGGER = Logger.getLogger("HomeSuppliersRPCServiceImpl");
    private GeneralService generalService;
    private TreeItemService treeItemService;
    private LocalityService localityService;
    private CategoryService categoryService;
    private SupplierService supplierService;

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
    public long filterSuppliersCount(SearchModuleDataHolder detail) {
        return this.filter(detail, null).size();
    }

    @Override
    public List<FullSupplierDetail> filterSuppliers(
            int start, int count, SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        List<FullSupplierDetail> searchResult = this.filter(detail, orderColumns);
        if (searchResult.size() < (start + count)) {
            return searchResult.subList(start, searchResult.size());
        } else {
            return searchResult.subList(start, count);
        }
    }

    private List<FullSupplierDetail> filter(SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        //detail nikdy nebude null, pretoze vzdy tam bude filter na categoriu
//        if (detail == null) {
//            Search search = this.getFilter(null, orderColumns);
//            return this.createSupplierDetailList(this.generalService.search(search));
//        }
        //nikdy nebude null oba - vzdy podla nejakej kategorie - 0 0
//        if (detail.getHomeSuppliers().getSupplierCategory() == null
//                && detail.getHomeSuppliers().getSupplierLocality() == null) {
//            Search search = this.getCategoryFilter(detail, orderColumns);
//            return this.createSupplierDetailList(this.generalService.search(search));
//        }
        //1 0
        if (detail.getHomeSuppliers().getSupplierCategory() != null
                && detail.getHomeSuppliers().getSupplierLocality() == null) {
            Search search = this.getCategoryFilter(detail, orderColumns);
            return this.createSupplierDetailListCat(this.generalService.searchAndCount(search).getResult());
        }
        //0 1
        if (detail.getHomeSuppliers().getSupplierCategory() == null
                && detail.getHomeSuppliers().getSupplierLocality() != null) {
            Search search = this.getLocalityFilter(detail, orderColumns);
            return this.createSupplierDetailListLoc(this.generalService.searchAndCount(search).getResult());
        }
        //1 1  --> perform join if filtering by category and locality was used
        if (detail.getHomeSuppliers().getSupplierCategory() != null
                && detail.getHomeSuppliers().getSupplierLocality() != null) {
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
        final List<Category> allSubCategories = Arrays.asList(
                this.getAllSubCategories(detail.getHomeSuppliers().getSupplierCategory().getId()));
        categorySearch.addFilterIn("category", allSubCategories);

        Search supplierSearch = this.getSupplierFilter(detail, orderColumns);
        if (supplierSearch != null) {
            categorySearch.addFilterIn("supplier", generalService.search(supplierSearch));
        }

        return categorySearch;
    }

    private ArrayList<FullSupplierDetail> createSupplierDetailListCat(Collection<SupplierCategory> suppliersCat) {
        ArrayList<FullSupplierDetail> userDetails = new ArrayList<FullSupplierDetail>();
        for (SupplierCategory supplierCat : suppliersCat) {
            userDetails.add(FullSupplierDetail.createFullSupplierDetail(supplierCat.getSupplier()));
        }
        GWT.log("supplierDetailList created: " + userDetails.size());
        return userDetails;
    }

    private ArrayList<FullSupplierDetail> createSupplierDetailListLoc(Collection<SupplierLocality> suppliersLoc) {
        ArrayList<FullSupplierDetail> userDetails = new ArrayList<FullSupplierDetail>();
        for (SupplierLocality supplierLoc : suppliersLoc) {
            userDetails.add(FullSupplierDetail.createFullSupplierDetail(supplierLoc.getSupplier()));
        }
        GWT.log("supplierDetailList created: " + userDetails.size());
        return userDetails;
    }

    private Search getLocalityFilter(SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        Search localitySearch = new Search(SupplierLocality.class);
        final List<Locality> allSubLocalities = Arrays.asList(
                this.getAllSublocalities(detail.getHomeSuppliers().getSupplierLocality().getCode()));
        localitySearch.addFilterIn("locality", allSubLocalities);

        Search suppSearch = this.getSupplierFilter(detail, orderColumns);
        if (suppSearch != null) {
            localitySearch.addFilterIn("supplier", generalService.search(suppSearch));
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
        if (detail.getHomeSuppliers().getSupplierName() != null) {
            Collection<BusinessUserData> data = generalService.search(
                    new Search(BusinessUserData.class).addFilterLike("companyName",
                        "%" + detail.getAdminSuppliers().getSupplierName() + "%"));
            search.addFilterIn("businessUser.businessUserData", data);
            filterApplied = true;
        }

        if (detail.getHomeSuppliers().getRatingFrom() != null) {
            search.addFilterGreaterOrEqual("overalRating", detail.getHomeSuppliers().getRatingFrom());
            filterApplied = true;
        }
        if (detail.getHomeSuppliers().getRatingTo() != null) {
            search.addFilterLessOrEqual("overalRating", detail.getHomeSuppliers().getRatingTo());
            filterApplied = true;
        }
        if (detail.getHomeSuppliers().getSupplierDescription() != null) {
            Collection<BusinessUserData> descsData = new ArrayList<BusinessUserData>();
            String[] descs = detail.getHomeSuppliers().getSupplierDescription().split("\\s+");

            for (int i = 0; i < descs.length; i++) {
                descsData.addAll(generalService.search(
                        new Search(BusinessUserData.class).addFilterLike("description",
                            "%" + detail.getAdminSuppliers().getSupplierDescription() + "%")));
            }
            search.addFilterIn("businessUser.businessUserData", descsData);
            filterApplied = true;
        }
        if (filterApplied) {
            return this.getSortSearch(search, orderColumns, "supplier");
        } else {
            return null;
        }
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
    public ArrayList<CategoryDetail> getCategories() {
        final List<Category> categories = categoryService.getRootCategories();
        return createCategoryDetailList(categories);
    }

    /**
     * Return all parents of given category within given category.
     * @param category - given category id
     * @return list of parents and given category
     */
    @Override
    public ArrayList<CategoryDetail> getCategoryParents(Long category) {
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
    public ArrayList<CategoryDetail> getCategoryChildren(Long category) {
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
        // TODO uncomment, when implemented
//        CategoryDetail detail = new CategoryDetail(cat.getId(), cat.getName(),
//              cat.getAdditionalInfo().getDemandsCount(), cat.getAdditionalInfo().getSuppliersCount());
        if (category.getChildren().size() != 0) {
            detail.setParent(true);
        } else {
            detail.setParent(false);
        }
        return detail;
    }
}
