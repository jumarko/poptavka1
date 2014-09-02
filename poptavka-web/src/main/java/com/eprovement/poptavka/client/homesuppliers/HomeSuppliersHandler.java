/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.homesuppliers;

import com.eprovement.poptavka.client.common.security.GetDataCallback;
import com.eprovement.poptavka.client.common.security.GetDataCountCallback;
import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.service.demand.HomeSuppliersRPCServiceAsync;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.domain.supplier.LesserSupplierDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import java.util.List;

/**
 * Handler for HomeSuppliers module.
 *
 * @author Martin Slavkovsky
 */
@EventHandler
public class HomeSuppliersHandler extends BaseEventHandler<HomeSuppliersEventBus> {

    /**************************************************************************/
    /* Inject RPC services                                                    */
    /**************************************************************************/
    private HomeSuppliersRPCServiceAsync homeSuppliersService = null;

    @Inject
    public void setHomeSuppliersService(HomeSuppliersRPCServiceAsync service) {
        this.homeSuppliersService = service;
    }

    /**************************************************************************/
    /* Get Suppliers data                                                     */
    /**************************************************************************/
    /**
     * Request for supplier.
     * @param supplierID
     */
    public void onGetSupplier(long supplierID) {
        homeSuppliersService.getSupplier(supplierID, new SecuredAsyncCallback<LesserSupplierDetail>(eventBus) {
            @Override
            public void onSuccess(LesserSupplierDetail result) {
                eventBus.displaySupplierDetail(result);
            }
        });
    }

    /**
     * Request table data count.
     * @param grid to be updated
     * @param searchDefinition - search filters
     */
    public void onGetDataCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        homeSuppliersService.getSuppliersCount(searchDefinition, new GetDataCountCallback(eventBus, grid));
    }

    /**
     * Request table data.
     * @param searchDefinition - search filters
     */
    public void onGetData(final UniversalAsyncGrid grid, SearchDefinition searchDefinition, final int requestId) {
        homeSuppliersService.getSuppliers(searchDefinition,
            new GetDataCallback<LesserSupplierDetail>(eventBus, grid, requestId) {
                @Override
                public void onSuccess(List<LesserSupplierDetail> result) {
                    super.onSuccess(result);
                    eventBus.responseGetData();
                }
            });
    }

    /**************************************************************************/
    /* Get Categories data                                                    */
    /**************************************************************************/
    /**
     * Restore module state from history.
     *
     * @param searchDataHolder - search filters
     * @param categoryIdStr - selected category id
     * @param pageStr - selected table page
     * @param supplierIdStr - selected supplier id
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
