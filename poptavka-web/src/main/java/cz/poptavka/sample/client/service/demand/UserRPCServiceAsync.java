package cz.poptavka.sample.client.service.demand;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.shared.domain.LoggedUserDetail;
import cz.poptavka.sample.shared.domain.UserDetail;

public interface UserRPCServiceAsync {

    void loginUser(UserDetail user, AsyncCallback<LoggedUserDetail> callback);

    void getSignedUser(String sessionId, AsyncCallback<UserDetail> callback);

    void getUserById(Long userId, AsyncCallback<UserDetail> callback);

    /** @see UserRPCService#checkFreeEmail(String) */
    void checkFreeEmail(String email, AsyncCallback<Boolean> callback);
}
