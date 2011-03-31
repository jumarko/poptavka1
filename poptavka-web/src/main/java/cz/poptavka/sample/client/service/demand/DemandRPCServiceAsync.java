package cz.poptavka.sample.client.service.demand;

import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;

public interface DemandRPCServiceAsync {

    void getAllDemands(AsyncCallback<List<Demand>> callback);

    void getDemands(Category[] categories, AsyncCallback<Set<Demand>> callback);

    void getDemands(Locality[] localities, AsyncCallback<Set<Demand>> callback);

}
