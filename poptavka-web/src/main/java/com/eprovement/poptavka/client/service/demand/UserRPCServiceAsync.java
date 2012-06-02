package com.eprovement.poptavka.client.service.demand;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.eprovement.poptavka.shared.domain.LoggedUserDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;

public interface UserRPCServiceAsync {

    void loginUser(UserDetail user, AsyncCallback<LoggedUserDetail> callback);

    void getSignedUser(String sessionId, AsyncCallback<UserDetail> callback);

    void getUserById(Long userId, AsyncCallback<UserDetail> callback);

    /** @see UserRPCService#checkFreeEmail(String) */
    void checkFreeEmail(String email, AsyncCallback<Boolean> callback);
}
