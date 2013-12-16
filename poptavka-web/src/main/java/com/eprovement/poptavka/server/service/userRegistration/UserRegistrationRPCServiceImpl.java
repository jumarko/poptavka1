/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.server.service.userRegistration;

import com.eprovement.poptavka.client.service.demand.UserRegistrationRPCService;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.user.ClientService;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

/**
 *
 * @author Martin Slavkovsky
 */
@Configurable
public class UserRegistrationRPCServiceImpl extends AutoinjectingRemoteService
        implements UserRegistrationRPCService {

    private ClientService clientService;

    /**************************************************************************/
    /* Autowired methods                                                      */
    /**************************************************************************/
    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    /**************************************************************************/
    /* Business methods                                                       */
    /**************************************************************************/
    @Override
    public boolean checkFreeEmail(String email) throws RPCException {
        return clientService.checkFreeEmail(email);
    }
}