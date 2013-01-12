package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.domain.enums.CommonAccessRoles;
import com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import com.eprovement.poptavka.shared.domain.UserDetail;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import org.springframework.security.access.annotation.Secured;

@RemoteServiceRelativePath(UserRPCService.URL)
public interface UserRPCService extends RemoteService {

    String URL = "service/user";

    UserDetail loginUser(String email, String password) throws RPCException;

    BusinessUserDetail getBusinessUserById(Long userId) throws RPCException;

    UserDetail getUserById(Long userId) throws RPCException;

    BusinessUserDetail getLoggedBusinessUser() throws RPCException;

    UserDetail getLoggedUser() throws RPCException;

    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    BusinessUserDetail getBusinessUserByEmail(String email) throws RPCException, ApplicationSecurityException;
}
