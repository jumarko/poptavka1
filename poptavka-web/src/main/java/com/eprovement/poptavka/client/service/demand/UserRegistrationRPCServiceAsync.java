package com.eprovement.poptavka.client.service.demand;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 *
 * @author Martin Slavkovsky
 */
public interface UserRegistrationRPCServiceAsync {

    void checkFreeEmail(String email, AsyncCallback<Boolean> callback);
}
