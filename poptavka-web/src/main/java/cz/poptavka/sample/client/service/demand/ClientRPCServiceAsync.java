package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.shared.domain.UserDetail;

public interface ClientRPCServiceAsync {

    void getAllClients(AsyncCallback<ArrayList<UserDetail>> callback);

    void createNewClient(UserDetail clientDetail, AsyncCallback<UserDetail> callback);

    void verifyClient(UserDetail client, AsyncCallback<UserDetail> callback);

    void checkFreeEmail(String email, AsyncCallback<Boolean> callback);

}
