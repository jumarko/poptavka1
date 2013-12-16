package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginRPCServiceAsync {

    /**
     * Finds user by specified id.
     * @param userId - user id to find
     */
    void getBusinessUserById(Long userId, AsyncCallback<BusinessUserDetail> callback);

    void getLoggedUser(AsyncCallback<UserDetail> callback);

    void getLoggedBusinessUser(AsyncCallback<BusinessUserDetail> callback);
}
