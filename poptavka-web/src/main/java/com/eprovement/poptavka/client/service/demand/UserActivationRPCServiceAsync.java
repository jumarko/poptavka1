package com.eprovement.poptavka.client.service.demand;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.eprovement.poptavka.domain.user.BusinessUser;

public interface UserActivationRPCServiceAsync {

    BusinessUser activateUser(String activationString, AsyncCallback<BusinessUser> callback);
}
