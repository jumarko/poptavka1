/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("rpc")
public interface SimpleService extends RemoteService {

    String getData() throws RPCException;
}
