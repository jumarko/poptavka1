package com.eprovement.poptavka.client.service.login;

import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.eprovement.poptavka.shared.domain.root.UserActivationResult;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath(LoginRPCService.URL)
public interface LoginRPCService extends RemoteService {

    String URL = "service/login";

    BusinessUserDetail getBusinessUserById(Long userId) throws RPCException;

    BusinessUserDetail getBusinessUserByEmail(String email) throws RPCException;

    UserDetail getLoggedUser() throws RPCException;

    BusinessUserDetail getLoggedBusinessUser() throws RPCException;

    String resetPassword(long userId) throws RPCException;

    /**************************************************************************/
    /* Activation methods                                                     */
    /**************************************************************************/
    UserActivationResult activateUser(BusinessUserDetail user, String activationCode) throws RPCException;

    boolean sendActivationCodeAgain(BusinessUserDetail client) throws RPCException;

    void hasActivationEmail(BusinessUserDetail user) throws RPCException;
}
