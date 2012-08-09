package com.eprovement.poptavka.client.homesuppliers;

import com.eprovement.poptavka.client.common.SecuredAsyncCallback;
import java.util.ArrayList;
import java.util.logging.Logger;


import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.common.errorDialog.ErrorDialogPopupView;
import com.eprovement.poptavka.client.service.demand.HomeSuppliersRPCServiceAsync;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;

import com.eprovement.poptavka.shared.search.SearchDefinition;
import java.util.List;

@EventHandler
public class HomeSuppliersHandler extends BaseEventHandler<HomeSuppliersEventBus> {

//    private CategoryRPCServiceAsync categoryService = null;
//    private SupplierRPCServiceAsync supplierService = null;
    private HomeSuppliersRPCServiceAsync homeSuppliersService = null;
    private ErrorDialogPopupView errorDialog;
    private static final Logger LOGGER = Logger.getLogger("MainHandler");

//    @Inject
//    public void setCategoryService(CategoryRPCServiceAsync service) {
//        categoryService = service;
//    }
//
//    @Inject
//    public void setSupplierService(SupplierRPCServiceAsync service) {
//        supplierService = service;
//    }
    @Inject
    public void setHomeSuppliersService(HomeSuppliersRPCServiceAsync service) {
        homeSuppliersService = service;
    }

    // *** GET CATEGORIES
    // ***************************************************************************
    /**
     * Return all parents of given category within given category.
     * @param category - given category id
     * @return list of parents and given category
     */
    public void onGetCategoryParents(Long categoryId) {
        homeSuppliersService.getCategoryParents(categoryId, new SecuredAsyncCallback<ArrayList<CategoryDetail>>() {
            @Override
            public void onSuccess(ArrayList<CategoryDetail> result) {
                eventBus.updatePath(result);
            }
        });
    }

    /**
     * Get all categories. Used for display in listBox categories.
     */
    public void onGetCategories() {
        homeSuppliersService.getCategories(new SecuredAsyncCallback<ArrayList<CategoryDetail>>() {
            @Override
            public void onSuccess(ArrayList<CategoryDetail> list) {
                eventBus.displayRootcategories(list);
            }
        });
    }

    public void onGetSubCategories(final Long category) {
        homeSuppliersService.getCategoryChildren(category, new SecuredAsyncCallback<ArrayList<CategoryDetail>>() {
            @Override
            public void onSuccess(ArrayList<CategoryDetail> result) {
                LOGGER.info("Found subcategories: " + result.size());
                eventBus.displaySubCategories(result, category);
            }
        });
    }

    //*************** GET SUPPLIERS DATA *********************
    public void onGetDataCount(final UniversalAsyncGrid grid, SearchModuleDataHolder detail) {
        homeSuppliersService.getSuppliersCount(detail, new SecuredAsyncCallback<Long>() {
            @Override
            public void onSuccess(Long result) {
                grid.createAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onGetData(SearchDefinition searchDefinition) {
        homeSuppliersService.getSuppliers(searchDefinition,
                new SecuredAsyncCallback<List<FullSupplierDetail>>() {
                    @Override
                    public void onSuccess(List<FullSupplierDetail> result) {
                        eventBus.displaySuppliers(result);
                    }
                });
    }
}
