/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.homedemands;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.service.demand.HomeDemandsRPCServiceAsync;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import java.util.List;

/**
 * Handler for HomeDemands module.
 *
 * @author Martin Slavkovsky
 */
@EventHandler
public class HomeDemandsHandler extends BaseEventHandler<HomeDemandsEventBus> {

    /**************************************************************************/
    /* Inject RPC services                                                    */
    /**************************************************************************/
    private HomeDemandsRPCServiceAsync homeDemandsService = null;

    @Inject
    void setHomeDemandsService(HomeDemandsRPCServiceAsync service) {
        homeDemandsService = service;
    }

    /**************************************************************************/
    /* Get Suppliers data                                                     */
    /**************************************************************************/
    /**
     * Request for selected demand.
     * @param demandID
     */
    public void onGetDemand(long demandID) {
        homeDemandsService.getDemand(demandID, new SecuredAsyncCallback<FullDemandDetail>(eventBus) {
            @Override
            public void onSuccess(FullDemandDetail result) {
                eventBus.displayDemandDetail(result);
            }
        });
    }

    /**
     * Request table data count.
     * @param grid to be updated
     * @param searchDefinition - search filters
     */
    public void onGetDataCount(final UniversalAsyncGrid grid, final SearchDefinition searchDefinition) {
        homeDemandsService.getDemandsCount(searchDefinition, new SecuredAsyncCallback<Integer>(eventBus) {
            @Override
            public void onSuccess(Integer result) {
                grid.getDataProvider().updateRowCount(result, true);
            }
        });
    }

    /**
     * Request table data.
     * @param searchDefinition - search filters
     */
    public void onGetData(SearchDefinition searchDefinition) {
        homeDemandsService.getDemands(searchDefinition,
                new SecuredAsyncCallback<List<FullDemandDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<FullDemandDetail> result) {
                        eventBus.displayDemands(result);
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
            eventBus.goToHomeDemandsModuleByHistory(searchDataHolder, null, page, supplierId);
        } else {
            homeDemandsService.getCategory(categoryId, new SecuredAsyncCallback<ICatLocDetail>(eventBus) {
                @Override
                public void onSuccess(ICatLocDetail result) {
                    eventBus.goToHomeDemandsModuleByHistory(searchDataHolder, result, page, supplierId);
                }
            });
        }
    }
}