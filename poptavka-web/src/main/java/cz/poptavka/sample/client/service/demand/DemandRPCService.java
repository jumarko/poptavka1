
/**
 *
 * @author Excalibur
 *
 */
package cz.poptavka.sample.client.service.demand;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import cz.poptavka.sample.domain.demand.Demand;

import java.util.List;

@RemoteServiceRelativePath("service/demandservice")
public interface DemandRPCService extends RemoteService {

    List<Demand> getAllDemands();
}
