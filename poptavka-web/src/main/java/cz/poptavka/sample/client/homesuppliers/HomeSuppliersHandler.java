package cz.poptavka.sample.client.homesuppliers;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.main.errorDialog.ErrorDialogPopupView;
import cz.poptavka.sample.client.service.demand.HomeSuppliersRPCServiceAsync;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;
import cz.poptavka.sample.shared.exceptions.ExceptionUtils;
import cz.poptavka.sample.shared.exceptions.RPCException;

import java.util.List;
import java.util.Map;

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
        homeSuppliersService.getCategoryParents(categoryId, new AsyncCallback<ArrayList<CategoryDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
                throw new UnsupportedOperationException("Not supported yet.");
            }

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
        homeSuppliersService.getCategories(
                new AsyncCallback<ArrayList<CategoryDetail>>() {

                    @Override
                    public void onSuccess(ArrayList<CategoryDetail> list) {
                        eventBus.displayRootcategories(list);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof RPCException) {
                            ExceptionUtils.showErrorDialog(errorDialog, caught);
                        }
                        LOGGER.info("onFailureCategory");
                    }
                });
    }

    public void onGetSubCategories(final Long category) {
        homeSuppliersService.getCategoryChildren(category, new AsyncCallback<ArrayList<CategoryDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
                throw new UnsupportedOperationException("Get children categories failed.");
            }

            @Override
            public void onSuccess(ArrayList<CategoryDetail> result) {
                LOGGER.info("Found subcategories: " + result.size());
                eventBus.displaySubCategories(result, category);
            }
        });
    }

    //*************** GET SUPPLIERS DATA *********************
    public void onGetSuppliersCount(SearchModuleDataHolder detail) {
        homeSuppliersService.filterSuppliersCount(detail, new AsyncCallback<Long>() {

            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
                throw new UnsupportedOperationException("onFilterSuppliers "
                        + "(HomeSuppliersHandler) - not supported yet.");
            }

            @Override
            public void onSuccess(Long result) {
//                eventBus.resetDisplaySuppliersPager(result.intValue());
                eventBus.createAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onGetSuppliers(int start, int count, SearchModuleDataHolder search,
            Map<String, OrderType> orderColumns) {
        homeSuppliersService.filterSuppliers(start, count, search, orderColumns,
                new AsyncCallback<List<FullSupplierDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof RPCException) {
                            ExceptionUtils.showErrorDialog(errorDialog, caught);
                        }
                        throw new UnsupportedOperationException(
                                "onFilterSuppliers (HomeSupliersHandler) - not supported yet.");
                    }

                    @Override
                    public void onSuccess(List<FullSupplierDetail> result) {
                        eventBus.displaySuppliers(result);
                    }
                });
    }
}
