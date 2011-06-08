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
import cz.poptavka.sample.shared.domain.demand.DemandDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import cz.poptavka.sample.shared.domain.type.ViewType;

@RemoteServiceRelativePath("service/demands")
public interface DemandRPCService extends RemoteService {

    FullDemandDetail updateDemand(FullDemandDetail newDemand);

    FullDemandDetail createNewDemand(FullDemandDetail newDemand, Long clientId);

    List<DemandDetail> getAllDemands();

    Long getAllDemandsCount();

    Long getDemandsCount(Category[] categories);

    Long getDemandsCount(Locality[] localities);

    List<DemandDetail> getDemands(ResultCriteria resultCriteria);

    List<DemandDetail> getDemands(Locality[] localities);

    List<DemandDetail> getDemands(Category[] categories);

    List<DemandDetail> getDemands(ResultCriteria resultCriteria, Locality[] localities);

    List<DemandDetail> getDemands(ResultCriteria resultCriteria, Category[] categories);

    List<DemandDetail> getDemandsByCategory(int fromResult, int toResult, long id);

    List<DemandDetail> getDemandsByLocality(int fromResult, int toResult, String code);

    ArrayList<DemandDetail> getClientDemands(long id);

    ArrayList<ArrayList<OfferDetail>> getDemandOffers(ArrayList<Long> idList);

    /**
     * Gets DemandDetail from DB.
     *
     * @param demandId id of demand
     * @param typeOfDetail type of Detail that should be returned
     * @return DemandDetail of selected DemandType
     */
    DemandDetail getDemandDetail(Long demandId, ViewType typeOfDetail);

    Demand getWholeDemand(Long demandId);

    List<DemandDetail> getDemands(int fromResult, int toResult);

}
