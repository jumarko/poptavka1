package cz.poptavka.sample.client.user;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import cz.poptavka.sample.client.service.demand.DemandRPCServiceAsync;
import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.OfferDetail;

@EventHandler
public class UserHandler extends BaseEventHandler<UserEventBus> {

    private DemandRPCServiceAsync demandService = null;

    @Inject
    void setDemandService(DemandRPCServiceAsync service) {
        demandService = service;
    }

    public void onGetClientsDemands(long id) {
        demandService.getClientDemands(id, new AsyncCallback<ArrayList<DemandDetail>>() {
            @Override
            public void onSuccess(ArrayList<DemandDetail> list) {
                eventBus.setClientDemands(list);
            }

            @Override
            public void onFailure(Throwable arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void onRequestOffers(ArrayList<Long> idList) {
        demandService.getDemandOffers(idList, new AsyncCallback<ArrayList<ArrayList<OfferDetail>>>() {
            @Override
            public void onFailure(Throwable arg0) {
            }

            @Override
            public void onSuccess(ArrayList<ArrayList<OfferDetail>> result) {
                eventBus.responseOffers(result);
            }
        });
    }

}
