package cz.poptavka.sample.client.user.admin.tab;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import cz.poptavka.sample.client.service.demand.ClientRPCServiceAsync;
import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.ClientDetail;
import java.util.Map;

@EventHandler
public class AdminClientsHandler extends BaseEventHandler<UserEventBus> {

    @Inject
    private ClientRPCServiceAsync clientService = null;

    /**********************************************************************************************
     ***********************  CLIENT SECTION. *****************************************************
     **********************************************************************************************/
    public void onGetAdminClientsCount() {
        clientService.getClientsCount(new AsyncCallback<Integer>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(Integer result) {
                eventBus.createAdminClientsAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onGetAdminClients(int start, int count) {
        clientService.getClients(start, count, new AsyncCallback<ArrayList<ClientDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(ArrayList<ClientDetail> result) {
                eventBus.displayAdminTabClients(result);
            }
        });

    }

    public void onGetSortedClients(int start, int count, Map<String, OrderType> orderColumns) {
        clientService.getSortedClients(start, count, orderColumns,
                new AsyncCallback<ArrayList<ClientDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    @Override
                    public void onSuccess(ArrayList<ClientDetail> result) {
                        eventBus.displayAdminTabClients(result);
                    }
                });
    }

    public void onUpdateDemand(ClientDetail client, String updateWhat) {
        clientService.updateClient(client, updateWhat, new AsyncCallback<ClientDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(ClientDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }
}
