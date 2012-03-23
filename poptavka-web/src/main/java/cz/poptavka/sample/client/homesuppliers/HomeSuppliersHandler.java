package cz.poptavka.sample.client.homesuppliers;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.service.demand.CategoryRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.SupplierRPCServiceAsync;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;
import java.util.List;
import java.util.Map;

@EventHandler
public class HomeSuppliersHandler extends BaseEventHandler<HomeSuppliersEventBus> {

    private CategoryRPCServiceAsync categoryService = null;
    private SupplierRPCServiceAsync supplierService = null;
    private static final Logger LOGGER = Logger.getLogger("MainHandler");

    @Inject
    public void setCategoryService(CategoryRPCServiceAsync service) {
        categoryService = service;
    }

    @Inject
    public void setSupplierService(SupplierRPCServiceAsync service) {
        supplierService = service;
    }

    // *** GET CATEGORIES
    // ***************************************************************************
    /**
     * Return all parents of given category within given category.
     * @param category - given category id
     * @return list of parents and given category
     */
    public void onGetCategoryParents(Long categoryId) {
        categoryService.getCategoryParents(categoryId, new AsyncCallback<ArrayList<CategoryDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
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
        categoryService.getCategories(
                new AsyncCallback<ArrayList<CategoryDetail>>() {

                    @Override
                    public void onSuccess(ArrayList<CategoryDetail> list) {
                        eventBus.displayRootcategories(list);
                    }

                    @Override
                    public void onFailure(Throwable arg0) {
                        LOGGER.info("onFailureCategory");
                    }
                });
    }

    public void onGetSubCategories(final Long category) {
        categoryService.getCategoryChildren(category, new AsyncCallback<ArrayList<CategoryDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
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
        supplierService.filterSuppliersCount(detail, new AsyncCallback<Long>() {

            @Override
            public void onFailure(Throwable caught) {
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
        supplierService.filterSuppliers(start, count, search, orderColumns,
                new AsyncCallback<List<FullSupplierDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
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
