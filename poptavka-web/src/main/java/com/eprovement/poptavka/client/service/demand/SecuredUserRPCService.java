package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath(SecuredUserRPCService.URL)
public interface SecuredUserRPCService extends RemoteService {

    String URL = "service/secured/user";

    BusinessUserDetail getLoggedBusinessUser() throws RPCException, ApplicationSecurityException;

    UserDetail getLoggedUser() throws RPCException, ApplicationSecurityException;
}
