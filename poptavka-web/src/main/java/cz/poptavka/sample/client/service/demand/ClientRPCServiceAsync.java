package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.adminModule.ClientDetail;
import cz.poptavka.sample.shared.domain.UserDetail;
import java.util.Map;

public interface ClientRPCServiceAsync {

    void getAllClients(AsyncCallback<ArrayList<UserDetail>> callback);

    void getClients(int start, int count,
            AsyncCallback<ArrayList<ClientDetail>> callback);

    void createNewClient(UserDetail clientDetail, AsyncCallback<UserDetail> callback);

    void verifyClient(UserDetail client, AsyncCallback<UserDetail> callback);

    void checkFreeEmail(String email, AsyncCallback<Boolean> callback);

    void getClientsCount(AsyncCallback<Integer> callback);

    void updateClient(ClientDetail supplierDetail,
            AsyncCallback<ClientDetail> callback);

    void getSortedClients(int start, int count, Map<String, OrderType> orderColumns,
            AsyncCallback<ArrayList<ClientDetail>> callback);
}
