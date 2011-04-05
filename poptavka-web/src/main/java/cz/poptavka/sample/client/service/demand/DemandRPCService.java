
/**
 *
 * @author Excalibur
 *
 */
package cz.poptavka.sample.client.service.demand;

import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.shared.domain.DemandDetail;

@RemoteServiceRelativePath("service/demands")
public interface DemandRPCService extends RemoteService {

    List<Demand> getAllDemands();

    String  createNewDemand(DemandDetail newDemand, Long clientId);

    Set<Demand> getDemands(Locality... localities);
    Set<Demand> getDemands(Category... categories);

}
