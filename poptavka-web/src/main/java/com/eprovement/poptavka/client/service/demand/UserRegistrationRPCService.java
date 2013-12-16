package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 *
 * @author Martin Slavkovsky
 */
@RemoteServiceRelativePath(UserRegistrationRPCService.URL)
public interface UserRegistrationRPCService extends RemoteService {

    String URL = "service/userRegistration";

    boolean checkFreeEmail(String email) throws RPCException;
}
