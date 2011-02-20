package cz.poptavka.sample.client.main;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.annotation.InjectService;
import com.mvp4g.client.event.BaseEventHandler;

import cz.poptavka.sample.client.service.demand.LocalityRPCServiceAsync;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.address.LocalityType;

@EventHandler
public class MainHandler extends BaseEventHandler<MainEventBus> {

    @Inject
    public MainHandler() {
    }

    private LocalityRPCServiceAsync service = null;

    public void onStart() {
        service.getLocalities(LocalityType.DISTRICT, new AsyncCallback<ArrayList<Locality>>() {

            @Override
            public void onSuccess(ArrayList<Locality> result) {
                // TODO Auto-generated method stub
                eventBus.setData(result);
            }

            @Override
            public void onFailure(Throwable caught) {
                eventBus.setData(null);
            }
        });

    }

    @InjectService
    public void setService(LocalityRPCServiceAsync service) {
        this.service = service;
    }

}
