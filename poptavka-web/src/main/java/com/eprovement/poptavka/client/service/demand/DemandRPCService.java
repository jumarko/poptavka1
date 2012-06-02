/**
 *
 * @author Excalibur
 *
 */
package com.eprovement.poptavka.client.service.demand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import com.eprovement.poptavka.client.main.common.search.SearchModuleDataHolder;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.common.OrderType;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.demand.BaseDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;

@RemoteServiceRelativePath(DemandRPCService.URL)
public interface DemandRPCService extends RemoteService {

    String URL = "service/demands";

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

    List<FullDemandDetail> getDemands(Locality... localities);

    List<FullDemandDetail> getDemands(Category... categories);

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

    Long getSortedDemandsCount(Map<String, OrderType> orderColumns);

    List<FullDemandDetail> getSortedDemands(int start, int count, Map<String, OrderType> orderColumns);

    long filterDemandsCount(SearchModuleDataHolder holder);

    List<FullDemandDetail> filterDemands(int start, int count,
            SearchModuleDataHolder holder, Map<String, OrderType> orderColumns);
}
