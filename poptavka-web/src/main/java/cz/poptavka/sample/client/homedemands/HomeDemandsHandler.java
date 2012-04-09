package cz.poptavka.sample.client.homedemands;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.main.errorDialog.ErrorDialogPopupView;
import cz.poptavka.sample.client.service.demand.HomeDemandsRPCServiceAsync;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import cz.poptavka.sample.shared.exceptions.CommonException;

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

    public void onFilterDemandsCount(SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        homeDemandsService.filterDemandsCount(detail, new AsyncCallback<Long>() {

            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof CommonException) {
                    CommonException commonException = (CommonException) caught;
                    errorDialog = new ErrorDialogPopupView();
                    errorDialog.show(commonException.getSymbol());
                }
                throw new UnsupportedOperationException("onFilterDemands (HomeDemandsHandler) - not supported yet.");
            }

            @Override
            public void onSuccess(Long result) {
//                eventBus.setResultSource("filter");
//                eventBus.setResultCount(result);
                eventBus.createAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onFilterDemands(int start, int count, SearchModuleDataHolder detail,
            Map<String, OrderType> orderColumns) {
        homeDemandsService.filterDemands(start, count, detail, orderColumns,
                new AsyncCallback<List<FullDemandDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof CommonException) {
                            CommonException commonException = (CommonException) caught;
                            errorDialog = new ErrorDialogPopupView();
                            errorDialog.show(commonException.getSymbol());
                        }
                        throw new UnsupportedOperationException(""
                                + "onFilterDemands (HomeDemandsHandler) - not supported yet.");
                    }

                    @Override
                    public void onSuccess(List<FullDemandDetail> result) {
                        eventBus.displayDemands(result);
                    }
                });
    }
}