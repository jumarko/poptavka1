package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.shared.domain.ClientDetail;

public interface ClientRPCServiceAsync {

    void getAllClients(AsyncCallback<ArrayList<ClientDetail>> callback);

    void sendClientId(long id, AsyncCallback<Void> callback);

    void createNewClient(ClientDetail clientDetail, AsyncCallback<Long> callback);

    void verifyClient(ClientDetail client, AsyncCallback<Long> callback);

}
