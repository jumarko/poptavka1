package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath(LoginRPCService.URL)
public interface LoginRPCService extends RemoteService {

    String URL = "service/login";

    BusinessUserDetail getBusinessUserById(Long userId) throws RPCException;

    UserDetail getLoggedUser() throws RPCException;

    BusinessUserDetail getLoggedBusinessUser() throws RPCException;
}
