/**
 *
 * @author Excalibur
 *
 */
package cz.poptavka.sample.client.service.demand;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.PotentialDemandDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RemoteServiceRelativePath("service/demands")
public interface DemandRPCService extends RemoteService {

    List<DemandDetail> getAllDemands();

    DemandDetail updateDemand(DemandDetail newDemand);

    DemandDetail createNewDemand(DemandDetail newDemand, Long clientId);

    Set<Demand> getDemands(Locality[] localities);

    Set<Demand> getDemands(Category[] categories);

    List<Demand> getDemands(ResultCriteria resultCriteria);

    Set<Demand> getDemands(ResultCriteria resultCriteria, Locality[] localities);

    Set<Demand> getDemands(ResultCriteria resultCriteria, Category[] categories);

    ArrayList<DemandDetail> getClientDemands(long id);

    ArrayList<ArrayList<OfferDetail>> getDemandOffers(ArrayList<Long> idList);

    ArrayList<PotentialDemandDetail> getPotentialDemandsForSupplier(long businessUserId);

    DemandDetail getDemand(long demandId);
}
