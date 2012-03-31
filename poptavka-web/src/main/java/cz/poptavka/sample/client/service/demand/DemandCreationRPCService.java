/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.service.demand;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;

/**
 *
 * @author praso
 */
@RemoteServiceRelativePath("service/demandcreation")
public interface DemandCreationRPCService extends RemoteService {

    FullDemandDetail createNewDemand(FullDemandDetail newDemand, Long clientId);

    UserDetail createNewClient(UserDetail clientDetail);

    boolean checkFreeEmail(String email);

}
