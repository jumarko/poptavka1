package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.demand.DemandDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import cz.poptavka.sample.shared.domain.type.ViewType;

public interface DemandRPCServiceAsync {

    void getAllDemands(AsyncCallback<List<DemandDetail>> callback);

    void getAllDemandsCount(AsyncCallback<Long> callback);

    void getDemandsCount(Category[] categories, AsyncCallback<Long> callback);

    void getDemandsCountByCategory(long id, AsyncCallback<Long> callback);

    void getDemandsCount(Locality[] localities, AsyncCallback<Long> callback);

    void getDemandsCountByLocality(String code, AsyncCallback<Long> callback);

    void createNewDemand(FullDemandDetail newDemand, Long clientId,
            AsyncCallback<FullDemandDetail> callback);

    void getDemands(Category[] categories, AsyncCallback<List<DemandDetail>> callback);

    void getDemands(Locality[] localities, AsyncCallback<List<DemandDetail>> callback);

    void getDemands(ResultCriteria resultCriteria, AsyncCallback<List<DemandDetail>> callback);

    void getDemands(ResultCriteria resultCriteria, Category[] categories,
            AsyncCallback<List<DemandDetail>> callback);

    void getDemandsByCategory(int fromResult, int toResult, long id, AsyncCallback<List<DemandDetail>> callback);

    void getDemandsByLocality(int fromResult, int toResult, String code,
            AsyncCallback<List<DemandDetail>> callback);

    void getDemands(ResultCriteria resultCriteria, Locality[] localities,
            AsyncCallback<List<DemandDetail>> callback);

    void getClientDemands(long id, AsyncCallback<ArrayList<DemandDetail>> callback);

    void getDemandOffers(ArrayList<Long> idList, AsyncCallback<ArrayList<ArrayList<OfferDetail>>> callback);

    void updateDemand(FullDemandDetail demand, AsyncCallback<FullDemandDetail> asyncCallback);

    void getWholeDemand(Long demandId, AsyncCallback<Demand> callback);

    void getDemands(int fromResult, int toResult, AsyncCallback<List<DemandDetail>> callback);

    void getDemandDetail(Long demandId, ViewType typeOfDetail, AsyncCallback<DemandDetail> callback);

}
