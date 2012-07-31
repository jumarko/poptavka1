package com.eprovement.poptavka.client.homedemands;

import com.eprovement.poptavka.client.common.SecuredAsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.common.errorDialog.ErrorDialogPopupView;
import com.eprovement.poptavka.client.service.demand.HomeDemandsRPCServiceAsync;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;

import java.util.List;
import java.util.Map;

@EventHandler
public class HomeDemandsHandler extends BaseEventHandler<HomeDemandsEventBus> {

    private HomeDemandsRPCServiceAsync homeDemandsService = null;
    private ErrorDialogPopupView errorDialog;

    @Inject
    void setHomeDemandsService(HomeDemandsRPCServiceAsync service) {
        homeDemandsService = service;
    }

    public void onGetDataCount(final UniversalAsyncGrid grid, final SearchModuleDataHolder detail) {
        homeDemandsService.getDemandsCount(detail, new SecuredAsyncCallback<Long>() {
            @Override
            public void onSuccess(Long result) {
                grid.createAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onGetData(int start, int count, SearchModuleDataHolder detail,
            Map<String, OrderType> orderColumns) {
        homeDemandsService.getDemands(start, count, detail, orderColumns,
                new SecuredAsyncCallback<List<FullDemandDetail>>() {
                    @Override
                    public void onSuccess(List<FullDemandDetail> result) {
                        eventBus.displayDemands(result);
                    }
                });
    }
}