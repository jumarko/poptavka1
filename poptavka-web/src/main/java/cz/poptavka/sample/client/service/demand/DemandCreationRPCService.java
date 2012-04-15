/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.service.demand;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import cz.poptavka.sample.shared.exceptions.RPCException;

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
