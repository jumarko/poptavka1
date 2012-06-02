/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.service.demand;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;

/**
 *
 * @author praso
 */
@RemoteServiceRelativePath(DemandCreationRPCService.URL)
public interface DemandCreationRPCService extends RemoteService {

    String URL = "service/demandcreation";

    FullDemandDetail createNewDemand(FullDemandDetail newDemand, Long clientId) throws RPCException;

    UserDetail createNewClient(UserDetail clientDetail) throws RPCException;
}
