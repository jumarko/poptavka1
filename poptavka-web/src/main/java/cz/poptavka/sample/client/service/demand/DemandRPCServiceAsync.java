package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.PotentialDemandDetail;

public interface DemandRPCServiceAsync {

    void getAllDemands(AsyncCallback<List<DemandDetail>> callback);

    void createNewDemand(DemandDetail newDemand, Long clientId,
            AsyncCallback<DemandDetail> callback);

    void getDemands(Category[] categories, AsyncCallback<Set<Demand>> callback);

    void getDemands(Locality[] localities, AsyncCallback<Set<Demand>> callback);

    void getDemands(ResultCriteria resultCriteria, AsyncCallback<List<Demand>> callback);

    void getDemands(ResultCriteria resultCriteria, Category[] categories, AsyncCallback<Set<Demand>> callback);

    void getDemands(ResultCriteria resultCriteria, Locality[] localities, AsyncCallback<Set<Demand>> callback);

    void getClientDemands(long id, AsyncCallback<ArrayList<DemandDetail>> callback);

    void getDemandOffers(ArrayList<Long> idList, AsyncCallback<ArrayList<ArrayList<OfferDetail>>> callback);

    void updateDemand(DemandDetail demand, AsyncCallback<DemandDetail> asyncCallback);

    void getPotentialDemandsForSupplier(long businessUserId, AsyncCallback<ArrayList<PotentialDemandDetail>> callback);

    void getDemand(long demandId, AsyncCallback<DemandDetail> asyncCallback);
}
