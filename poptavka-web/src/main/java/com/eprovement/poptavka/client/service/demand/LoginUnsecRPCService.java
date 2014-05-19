package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.root.UserActivationResult;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath(LoginUnsecRPCService.URL)
public interface LoginUnsecRPCService extends RemoteService {

    String URL = "service/loginUnsec";

    BusinessUserDetail getBusinessUserByEmail(String email) throws RPCException;

    String resetPassword(long userId) throws RPCException;

    /**************************************************************************/
    /* Activation methods                                                     */
    /**************************************************************************/
    UserActivationResult activateUser(BusinessUserDetail user, String activationCode) throws RPCException;

    boolean sendActivationCodeAgain(BusinessUserDetail client) throws RPCException;

    void hasActivationEmail(BusinessUserDetail user) throws RPCException;
}
