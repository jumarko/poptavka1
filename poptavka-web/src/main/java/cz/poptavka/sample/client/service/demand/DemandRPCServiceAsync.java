package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.demand.PotentialDemandDetail;

public interface DemandRPCServiceAsync {

    void getAllDemands(AsyncCallback<List<DemandDetail>> callback);

    void createNewDemand(DemandDetail newDemand, Long clientId,
            AsyncCallback<DemandDetail> callback);

    void getDemands(Category[] categories, AsyncCallback<List<DemandDetail>> callback);

    void getDemands(Locality[] localities, AsyncCallback<List<DemandDetail>> callback);

    void getDemands(ResultCriteria resultCriteria, AsyncCallback<List<DemandDetail>> callback);

    void getDemands(ResultCriteria resultCriteria, Category[] categories, AsyncCallback<List<DemandDetail>> callback);

    void getDemands(ResultCriteria resultCriteria, Locality[] localities, AsyncCallback<List<DemandDetail>> callback);

    void getClientDemands(long id, AsyncCallback<ArrayList<DemandDetail>> callback);

    void getDemandOffers(ArrayList<Long> idList, AsyncCallback<ArrayList<ArrayList<OfferDetail>>> callback);

    void updateDemand(DemandDetail demand, AsyncCallback<DemandDetail> asyncCallback);

    void getPotentialDemandsForSupplier(long businessUserId, AsyncCallback<ArrayList<PotentialDemandDetail>> callback);

    void getDemand(Long demandId, AsyncCallback<DemandDetail> callback);

    void getWholeDemand(Long demandId, AsyncCallback<Demand> callback);
}
