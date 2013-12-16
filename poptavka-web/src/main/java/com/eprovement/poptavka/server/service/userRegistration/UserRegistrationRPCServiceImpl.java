/*
 * Copyright (C) 2013, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.service.userRegistration;

import com.eprovement.poptavka.client.service.demand.UserRegistrationRPCService;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.user.ClientService;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

/**
 * This RPC handles all requests from UserRegistration module.
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
    /**
     * Checks email availability.
     * @param email to be checked
     * @return true if availabale, false otherwise
     * @throws RPCException
     */
    @Override
    public boolean checkFreeEmail(String email) throws RPCException {
        return clientService.checkFreeEmail(email);
    }
}