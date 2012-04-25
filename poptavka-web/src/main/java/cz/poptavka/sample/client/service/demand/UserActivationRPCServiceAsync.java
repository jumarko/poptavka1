package cz.poptavka.sample.client.service.demand;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.domain.user.BusinessUser;

public interface UserActivationRPCServiceAsync {

    BusinessUser activateUser(String activationString, AsyncCallback<BusinessUser> callback);
}
