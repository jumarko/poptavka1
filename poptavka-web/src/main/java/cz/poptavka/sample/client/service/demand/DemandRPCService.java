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
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.demand.ClientDemandDetail;
import cz.poptavka.sample.shared.domain.demand.BaseDemandDetail;

@RemoteServiceRelativePath("service/demands")
public interface DemandRPCService extends RemoteService {

    ClientDemandDetail updateDemand(ClientDemandDetail newDemand);

    ClientDemandDetail createNewDemand(ClientDemandDetail newDemand, Long clientId);

    List<ClientDemandDetail> getAllDemands();

    Long getAllDemandsCount();

    Long getDemandsCount(Category[] categories);

    Long getDemandsCount(Locality[] localities);

    List<ClientDemandDetail> getDemands(ResultCriteria resultCriteria);

    List<ClientDemandDetail> getDemands(Locality[] localities);

    List<ClientDemandDetail> getDemands(Category[] categories);

    List<ClientDemandDetail> getDemands(ResultCriteria resultCriteria, Locality[] localities);

    List<ClientDemandDetail> getDemands(ResultCriteria resultCriteria, Category[] categories);

    List<ClientDemandDetail> getDemandsByCategory(int fromResult, int toResult, long id);

    List<ClientDemandDetail> getDemandsByLocality(int fromResult, int toResult, String code);

    ArrayList<ClientDemandDetail> getClientDemands(long id);

    ArrayList<ArrayList<OfferDetail>> getDemandOffers(ArrayList<Long> idList);

    ArrayList<BaseDemandDetail> getPotentialDemandsForSupplier(long businessUserId);

    /**
     * Gets ClientDemandDetail from DB.
     *
     * @param demandId id of demand
     * @param typeOfDetail type of Detail that should be returned
     * @return ClientDemandDetail of selected DemandType
     */
    ClientDemandDetail getDemand(Long demandId);

    Demand getWholeDemand(Long demandId);

    List<ClientDemandDetail> getDemands(int fromResult, int toResult);
}
