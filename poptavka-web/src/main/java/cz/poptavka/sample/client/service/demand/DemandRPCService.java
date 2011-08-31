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
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.demand.BaseDemandDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import java.util.Map;

@RemoteServiceRelativePath("service/demands")
public interface DemandRPCService extends RemoteService {

    FullDemandDetail updateDemand(FullDemandDetail newDemand, String updateWhat);

    FullDemandDetail createNewDemand(FullDemandDetail newDemand, Long clientId);

    List<FullDemandDetail> getAllDemands();

    Long getAllDemandsCount();

    Long getDemandsCount(Category[] categories);

    Long getDemandsCountByCategory(long id);

    Long getDemandsCount(Locality[] localities);

    Long getDemandsCountByLocality(String code);

    List<FullDemandDetail> getDemands(ResultCriteria resultCriteria);

    List<FullDemandDetail> getDemands(Locality[] localities);

    List<FullDemandDetail> getDemands(Category[] categories);

    List<FullDemandDetail> getDemands(ResultCriteria resultCriteria, Locality[] localities);

    List<FullDemandDetail> getDemands(ResultCriteria resultCriteria, Category[] categories);

    List<FullDemandDetail> getDemandsByCategory(int fromResult, int toResult, long id);

    List<FullDemandDetail> getDemandsByLocality(int fromResult, int toResult, String code);

    ArrayList<FullDemandDetail> getClientDemands(long id);

    ArrayList<ArrayList<OfferDetail>> getDemandOffers(ArrayList<Long> idList);

    /**
     * Gets DemandDetail from DB.
     *
     * @param demandId id of demand
     * @param typeOfDetail type of Detail that should be returned
     * @return DemandDetail of selected DemandType
     */
    FullDemandDetail getFullDemandDetail(Long demandId);

    BaseDemandDetail getBaseDemandDetail(Long demandId);

    Demand getWholeDemand(Long demandId);

    List<FullDemandDetail> getDemands(int fromResult, int toResult);

    List<FullDemandDetail> getSortedDemands(int start, int count, Map<String, OrderType> orderColumns);
}
