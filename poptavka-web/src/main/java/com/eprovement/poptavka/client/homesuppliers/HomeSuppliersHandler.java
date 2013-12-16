package com.eprovement.poptavka.client.homesuppliers;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.service.demand.HomeSuppliersRPCServiceAsync;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import java.util.List;

@EventHandler
public class HomeSuppliersHandler extends BaseEventHandler<HomeSuppliersEventBus> {

    private HomeSuppliersRPCServiceAsync homeSuppliersService = null;

    @Inject
    public void setHomeSuppliersService(HomeSuppliersRPCServiceAsync service) {
        this.homeSuppliersService = service;
    }

    /**************************************************************************/
    /* Get Suppliers data                                                     */
    /**************************************************************************/
    public void onGetSupplier(long supplierID) {
        homeSuppliersService.getSupplier(supplierID, new SecuredAsyncCallback<FullSupplierDetail>(eventBus) {
            @Override
            public void onSuccess(FullSupplierDetail result) {
                eventBus.displaySupplierDetail(result);
            }
        });
    }

    public void onGetDataCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        homeSuppliersService.getSuppliersCount(searchDefinition, new SecuredAsyncCallback<Integer>(eventBus) {
            @Override
            public void onSuccess(Integer result) {
                grid.getDataProvider().updateRowCount(result, true);
            }
        });
    }

    public void onGetData(SearchDefinition searchDefinition) {
        homeSuppliersService.getSuppliers(searchDefinition,
            new SecuredAsyncCallback<List<FullSupplierDetail>>(eventBus) {
                @Override
                public void onSuccess(List<FullSupplierDetail> result) {
                    eventBus.displaySuppliers(result);
                }
            });
    }

    /**************************************************************************/
    /* Get Categories data                                                    */
    /**************************************************************************/
    /**
     * Restore module from history.
     * Given strings represents
     */
    public void onSetModuleByHistory(final SearchModuleDataHolder searchDataHolder,
            String categoryIdStr, String pageStr, String supplierIdStr) {
        final int categoryId = categoryIdStr.equals("null") ? -1 : Integer.parseInt(categoryIdStr);
        final int page = pageStr.equals("null") ? -1 : Integer.parseInt(pageStr);
        final long supplierId = supplierIdStr.equals("null") ? -1 : Long.parseLong(supplierIdStr);

        if (categoryId == -1) {
            eventBus.goToHomeSuppliersModuleByHistory(searchDataHolder, null, page, supplierId);
        } else {
            homeSuppliersService.getCategory(categoryId, new SecuredAsyncCallback<ICatLocDetail>(eventBus) {
                @Override
                public void onSuccess(ICatLocDetail result) {
                    eventBus.goToHomeSuppliersModuleByHistory(searchDataHolder, result, page, supplierId);
                }
            });
        }
    }
}
