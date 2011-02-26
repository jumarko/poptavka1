package cz.poptavka.sample.client.home;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.annotation.InjectService;
import com.mvp4g.client.event.BaseEventHandler;

import cz.poptavka.sample.client.service.demand.LocalityRPCServiceAsync;
import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.shared.domain.LocalityDetail;

@EventHandler
public class HomeHandler extends BaseEventHandler<HomeEventBus> {

    private LocalityRPCServiceAsync localityService = null;

    @InjectService
    public void setLocalityService(LocalityRPCServiceAsync service) {
        localityService = service;
    }

    public void onGetLocalities(final LocalityType type) {
        localityService.getLocalities(type, new AsyncCallback<List<LocalityDetail>>() {
            @Override
            public void onSuccess(List<LocalityDetail> list) {
                eventBus.displayLocalityList(type, list);
            }

            @Override
            public void onFailure(Throwable arg0) {
                //empty
            }
        });
    }

    public void onGetChildLocalities(final LocalityType type, String locCode) {
        localityService.getLocalities(locCode, new AsyncCallback<List<LocalityDetail>>() {
            @Override
            public void onFailure(Throwable arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onSuccess(List<LocalityDetail> list) {
                eventBus.displayLocalityList(type, list);
            }
        });
    }

}
