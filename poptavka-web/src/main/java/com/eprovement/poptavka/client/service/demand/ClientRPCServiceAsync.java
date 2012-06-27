package com.eprovement.poptavka.client.service.demand;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.eprovement.poptavka.domain.common.OrderType;
import com.eprovement.poptavka.shared.domain.adminModule.ClientDetail;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import java.util.Map;

public interface ClientRPCServiceAsync {

    void getAllClients(AsyncCallback<ArrayList<BusinessUserDetail>> callback);

    void getClients(int start, int count,
            AsyncCallback<ArrayList<ClientDetail>> callback);

    void createNewClient(BusinessUserDetail clientDetail, AsyncCallback<BusinessUserDetail> callback);

    void getClientsCount(AsyncCallback<Integer> callback);

    void updateClient(ClientDetail supplierDetail,
            AsyncCallback<ClientDetail> callback);

    void getSortedClients(int start, int count, Map<String, OrderType> orderColumns,
            AsyncCallback<ArrayList<ClientDetail>> callback);
}
