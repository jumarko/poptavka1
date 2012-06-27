package com.eprovement.poptavka.client.service.demand;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.eprovement.poptavka.shared.domain.UserDetail;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;

public interface UserRPCServiceAsync {

    void loginUser(BusinessUserDetail user, AsyncCallback<UserDetail> callback);

    void getSignedUser(String sessionId, AsyncCallback<BusinessUserDetail> callback);

    void getUserById(Long userId, AsyncCallback<BusinessUserDetail> callback);

    /** @see UserRPCService#checkFreeEmail(String) */
    void checkFreeEmail(String email, AsyncCallback<Boolean> callback);
}
