package com.eprovement.poptavka.client.homesuppliers;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.service.demand.HomeSuppliersRPCServiceAsync;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import java.util.List;

@EventHandler
public class HomeSuppliersHandler extends BaseEventHandler<HomeSuppliersEventBus> {

    private HomeSuppliersRPCServiceAsync homeSuppliersService = null;

    @Inject
    public void setHomeSuppliersService(HomeSuppliersRPCServiceAsync service) {
        homeSuppliersService = service;
    }

    /**************************************************************************/
    /* Get Suppliers data                                                     */
    /**************************************************************************/
    public void onGetDataCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        homeSuppliersService.getSuppliersCount(searchDefinition, new SecuredAsyncCallback<Long>() {
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
