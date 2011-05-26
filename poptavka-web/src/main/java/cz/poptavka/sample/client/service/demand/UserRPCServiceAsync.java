package cz.poptavka.sample.client.service.demand;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.shared.domain.UserDetail;

public interface UserRPCServiceAsync {

    void loginUser(UserDetail user, AsyncCallback<String> callback);

    void getSignedUser(String sessionId, AsyncCallback<UserDetail> callback);

}
