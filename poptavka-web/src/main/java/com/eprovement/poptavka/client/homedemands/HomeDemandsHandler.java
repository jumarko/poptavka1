package com.eprovement.poptavka.client.homedemands;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import com.eprovement.poptavka.client.main.common.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.main.errorDialog.ErrorDialogPopupView;
import com.eprovement.poptavka.client.service.demand.HomeDemandsRPCServiceAsync;
import com.eprovement.poptavka.domain.common.OrderType;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.exceptions.ExceptionUtils;
import com.eprovement.poptavka.shared.exceptions.RPCException;

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

    public void onGetDemandsCount(SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        homeDemandsService.getDemandsCount(detail, new AsyncCallback<Long>() {

            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
            }

            @Override
            public void onSuccess(Long result) {
//                eventBus.setResultSource("get");
//                eventBus.setResultCount(result);
                eventBus.createAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onGetDemands(int start, int count, SearchModuleDataHolder detail,
            Map<String, OrderType> orderColumns) {
        homeDemandsService.getDemands(start, count, detail, orderColumns,
                new AsyncCallback<List<FullDemandDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof RPCException) {
                            ExceptionUtils.showErrorDialog(errorDialog, caught);
                        }
                    }

                    @Override
                    public void onSuccess(List<FullDemandDetail> result) {
                        eventBus.displayDemands(result);
                    }
                });
    }
}