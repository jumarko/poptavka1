package com.eprovement.poptavka.client.service.demand;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.eprovement.poptavka.domain.common.OrderType;
import com.eprovement.poptavka.shared.domain.adminModule.ClientDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;
import java.util.Map;

public interface ClientRPCServiceAsync {

    void getAllClients(AsyncCallback<ArrayList<UserDetail>> callback);

    void getClients(int start, int count,
            AsyncCallback<ArrayList<ClientDetail>> callback);

    void createNewClient(UserDetail clientDetail, AsyncCallback<UserDetail> callback);

    void getClientsCount(AsyncCallback<Integer> callback);

    void updateClient(ClientDetail supplierDetail,
            AsyncCallback<ClientDetail> callback);

    void getSortedClients(int start, int count, Map<String, OrderType> orderColumns,
            AsyncCallback<ArrayList<ClientDetail>> callback);
}
