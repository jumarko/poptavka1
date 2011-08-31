package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.demand.BaseDemandDetail;
import cz.poptavka.sample.shared.domain.demand.DemandDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import java.util.Map;

public interface DemandRPCServiceAsync {

    void getAllDemands(AsyncCallback<List<DemandDetail>> callback);

    void getAllDemandsCount(AsyncCallback<Long> callback);

    void getDemandsCount(Category[] categories, AsyncCallback<Long> callback);

    void getDemandsCountByCategory(long id, AsyncCallback<Long> callback);

    void getDemandsCount(Locality[] localities, AsyncCallback<Long> callback);

    void getDemandsCountByLocality(String code, AsyncCallback<Long> callback);

    void createNewDemand(FullDemandDetail newDemand, Long clientId,
            AsyncCallback<FullDemandDetail> callback);

    void getDemands(Category[] categories, AsyncCallback<List<FullDemandDetail>> callback);

    void getDemands(Locality[] localities, AsyncCallback<List<FullDemandDetail>> callback);

    void getDemands(ResultCriteria resultCriteria, AsyncCallback<List<FullDemandDetail>> callback);

    void getDemands(ResultCriteria resultCriteria, Category[] categories,
            AsyncCallback<List<FullDemandDetail>> callback);

    void getDemandsByCategory(int fromResult, int toResult, long id,
            AsyncCallback<List<FullDemandDetail>> callback);

    void getDemandsByLocality(int fromResult, int toResult, String code,
            AsyncCallback<List<FullDemandDetail>> callback);

    void getDemands(ResultCriteria resultCriteria, Locality[] localities,
            AsyncCallback<List<FullDemandDetail>> callback);

    void getClientDemands(long id, AsyncCallback<ArrayList<FullDemandDetail>> callback);

    void getDemandOffers(ArrayList<Long> idList, AsyncCallback<ArrayList<ArrayList<OfferDetail>>> callback);

    void updateDemand(FullDemandDetail demand, String updateWhat, AsyncCallback<FullDemandDetail> asyncCallback);

    void getWholeDemand(Long demandId, AsyncCallback<Demand> callback);

    void getDemands(int fromResult, int toResult, AsyncCallback<List<FullDemandDetail>> callback);

    void getFullDemandDetail(Long demandId, AsyncCallback<FullDemandDetail> callback);

    void getBaseDemandDetail(Long demandId, AsyncCallback<BaseDemandDetail> callback);

    void getSortedDemands(int start, int count, Map<String, OrderType> orderColumns,
            AsyncCallback<List<FullDemandDetail>> callback);
}
