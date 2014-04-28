/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.service.root;

import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author Martin Slavkovsky
 * @since 9.4.2014
 */
@RemoteServiceRelativePath(RootRPCService.URL)
public interface RootRPCService extends RemoteService {

    String URL = "service/root";

    /**************************************************************************/
    /* Message methods                                                        */
    /**************************************************************************/
    Boolean unsubscribe(String password) throws RPCException;

}
