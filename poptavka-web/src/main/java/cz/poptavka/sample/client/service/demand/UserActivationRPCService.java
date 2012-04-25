package cz.poptavka.sample.client.service.demand;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.domain.user.BusinessUser;

@RemoteServiceRelativePath(UserActivationRPCService.URL)
public interface UserActivationRPCService {

    String URL = "useractivation";

    BusinessUser activateUser(String activationString);
}
