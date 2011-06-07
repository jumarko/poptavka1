package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.demand.ClientDemandDetail;
import cz.poptavka.sample.shared.domain.demand.BaseDemandDetail;

public interface DemandRPCServiceAsync {

    void getAllDemands(AsyncCallback<List<ClientDemandDetail>> callback);

    void getAllDemandsCount(AsyncCallback<Long> callback);

    void getDemandsCount(Category[] categories, AsyncCallback<Long> callback);

    void getDemandsCount(Locality[] localities, AsyncCallback<Long> callback);

    void createNewDemand(ClientDemandDetail newDemand, Long clientId,
            AsyncCallback<ClientDemandDetail> callback);

    void getDemands(Category[] categories, AsyncCallback<List<ClientDemandDetail>> callback);

    void getDemands(Locality[] localities, AsyncCallback<List<ClientDemandDetail>> callback);

    void getDemands(ResultCriteria resultCriteria, AsyncCallback<List<ClientDemandDetail>> callback);

    void getDemands(ResultCriteria resultCriteria, Category[] categories,
            AsyncCallback<List<ClientDemandDetail>> callback);

    void getDemandsByCategory(int fromResult, int toResult, long id, AsyncCallback<List<ClientDemandDetail>> callback);

    void getDemandsByLocality(int fromResult, int toResult, String code,
            AsyncCallback<List<ClientDemandDetail>> callback);

    void getDemands(ResultCriteria resultCriteria, Locality[] localities,
            AsyncCallback<List<ClientDemandDetail>> callback);

    void getClientDemands(long id, AsyncCallback<ArrayList<ClientDemandDetail>> callback);

    void getDemandOffers(ArrayList<Long> idList, AsyncCallback<ArrayList<ArrayList<OfferDetail>>> callback);

    void updateDemand(ClientDemandDetail demand, AsyncCallback<ClientDemandDetail> asyncCallback);

    void getDemand(Long demandId, AsyncCallback<ClientDemandDetail> callback);

    void getWholeDemand(Long demandId, AsyncCallback<Demand> callback);

    void getDemands(int fromResult, int toResult, AsyncCallback<List<ClientDemandDetail>> callback);

    void getPotentialDemandsForSupplier(long businessUserId, AsyncCallback<ArrayList<BaseDemandDetail>> callback);
}
