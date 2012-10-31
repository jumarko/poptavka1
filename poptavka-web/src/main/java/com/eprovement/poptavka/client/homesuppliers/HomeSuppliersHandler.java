package com.eprovement.poptavka.client.homesuppliers;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.service.demand.HomeSuppliersRPCServiceAsync;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import java.util.LinkedList;
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
                eventBus.selectSupplier(result);
            }
        });
    }

    public void onGetDataCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        homeSuppliersService.getSuppliersCount(searchDefinition, new SecuredAsyncCallback<Long>(eventBus) {
            @Override
            public void onSuccess(Long result) {
//                grid.createAsyncDataProvider(result.intValue());
                grid.getDataProvider().updateRowCount(result.intValue(), true);
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
    public void onGetCategoryAndSetModuleByHistory(
            final LinkedList<TreeItem> tree, final long categoryID, final int page, final long supplierID) {
        homeSuppliersService.getCategory(categoryID, new SecuredAsyncCallback<CategoryDetail>(eventBus) {
            @Override
            public void onSuccess(CategoryDetail result) {
                eventBus.setModuleByHistory(tree, result, page, supplierID);
            }
        });
    }
//    public void onGetParentsWithIndexes(long categoryId) {
//        homeSuppliersService.getCategoryParentsWithIndexes(categoryId,
//                new SecuredAsyncCallback<LinkedList<CategoryDetail, Integer>>(eventBus) {
//                    @Override
//                    public void onSuccess(LinkedList<CategoryDetail, Integer> result) {
//                        eventBus.openNodesAccoirdingToHistory(result);
//                    }
//                });
//    }
}
