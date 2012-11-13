package com.eprovement.poptavka.client.homedemands;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.homesuppliers.TreeItem;
import com.eprovement.poptavka.client.service.demand.HomeDemandsRPCServiceAsync;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import java.util.LinkedList;
import java.util.List;

@EventHandler
public class HomeDemandsHandler extends BaseEventHandler<HomeDemandsEventBus> {

    private HomeDemandsRPCServiceAsync homeDemandsService = null;

    @Inject
    void setHomeDemandsService(HomeDemandsRPCServiceAsync service) {
        homeDemandsService = service;
    }

    /**************************************************************************/
    /* Get Suppliers data                                                     */
    /**************************************************************************/
    public void onGetDemand(long demandID) {
        homeDemandsService.getDemand(demandID, new SecuredAsyncCallback<FullDemandDetail>(eventBus) {
            @Override
            public void onSuccess(FullDemandDetail result) {
                eventBus.selectDemand(result);
            }
        });
    }

    public void onGetDataCount(final UniversalAsyncGrid grid, final SearchDefinition searchDefinition) {
        homeDemandsService.getDemandsCount(searchDefinition, new SecuredAsyncCallback<Long>(eventBus) {
            @Override
            public void onSuccess(Long result) {
//                grid.createAsyncDataProvider(result.intValue());
                grid.getDataProvider().updateRowCount(result.intValue(), true);
            }
        });
    }

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
    public void onGetCategoryAndSetModuleByHistory(final SearchModuleDataHolder searchDataHolder,
            final LinkedList<TreeItem> tree, final long categoryID, final int page, final long supplierID) {
        homeDemandsService.getCategory(categoryID, new SecuredAsyncCallback<CategoryDetail>(eventBus) {
            @Override
            public void onSuccess(CategoryDetail result) {
                eventBus.setModuleByHistory(searchDataHolder, tree, result, page, supplierID);
            }
        });
    }
}