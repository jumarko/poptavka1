/**
 *
 * @author Excalibur
 *
 */
package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.demand.PotentialDemandDetail;

@RemoteServiceRelativePath("service/demands")
public interface DemandRPCService extends RemoteService {

    DemandDetail updateDemand(DemandDetail newDemand);

    DemandDetail createNewDemand(DemandDetail newDemand, Long clientId);

    List<DemandDetail> getAllDemands();

    List<DemandDetail> getDemands(ResultCriteria resultCriteria);

    List<DemandDetail> getDemands(Locality[] localities);

    List<DemandDetail> getDemands(Category[] categories);

    List<DemandDetail> getDemands(ResultCriteria resultCriteria, Locality[] localities);

    List<DemandDetail> getDemands(ResultCriteria resultCriteria, Category[] categories);

    ArrayList<DemandDetail> getClientDemands(long id);

    ArrayList<ArrayList<OfferDetail>> getDemandOffers(ArrayList<Long> idList);

    ArrayList<PotentialDemandDetail> getPotentialDemandsForSupplier(long businessUserId);

    /**
     * Gets DemandDetail from DB.
     *
     * @param demandId id of demand
     * @param typeOfDetail type of Detail that should be returned
     * @return DemandDetail of selected DemandType
     */
    DemandDetail getDemand(Long demandId);

    Demand getWholeDemand(Long demandId);

    List<DemandDetail> getDemands(int fromResult, int toResult);
}
