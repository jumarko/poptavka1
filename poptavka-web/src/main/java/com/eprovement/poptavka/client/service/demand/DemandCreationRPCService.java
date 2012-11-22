/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.service.demand;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
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

    BusinessUserDetail createNewClient(BusinessUserDetail clientDetail) throws RPCException;

    /**
     * Checks wheter given {@code email} is available.
     * @param email ęmail address to be checked
     */
    boolean checkFreeEmail(String email) throws RPCException;

    boolean activateClient(String activationCode) throws RPCException;

    boolean sentActivationCodeAgain(BusinessUserDetail client) throws RPCException;
}
