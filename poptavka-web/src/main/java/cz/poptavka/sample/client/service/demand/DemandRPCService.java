/**
 *
 * @author Excalibur
 *
 */
package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.client.main.common.search.SearchDataHolder;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.demand.BaseDemandDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;

@RemoteServiceRelativePath("service/demands")
public interface DemandRPCService extends RemoteService {

    FullDemandDetail updateDemand(FullDemandDetail newDemand);

    FullDemandDetail createNewDemand(FullDemandDetail newDemand, Long clientId);

    List<FullDemandDetail> getAllDemands();

    Long getAllDemandsCount();

    Long getDemandsCount(Category[] categories);

    Long getDemandsCount(Locality[] localities);

    Long getDemandsCountByCategory(long id);

    Long getDemandsCountByLocality(String code);

    Long getDemandsCountByCategoryLocality(long id, String code);

    List<FullDemandDetail> getDemands(ResultCriteria resultCriteria);

    List<FullDemandDetail> getDemands(Locality[] localities);

    List<FullDemandDetail> getDemands(Category[] categories);

    List<FullDemandDetail> getDemands(ResultCriteria resultCriteria, Locality[] localities);

    List<FullDemandDetail> getDemands(ResultCriteria resultCriteria, Category[] categories);

    List<FullDemandDetail> getDemandsByCategory(int fromResult, int toResult, long id);

    List<FullDemandDetail> getDemandsByLocality(int fromResult, int toResult, String code);

    List<FullDemandDetail> getDemandsByCategoryLocality(int fromResult, int toResult, long id, String code);

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

    long filterDemandsCount(SearchDataHolder detail, Map<String, OrderType> orderColumns);

    List<FullDemandDetail> filterDemands(int start, int count, SearchDataHolder detail);
}
