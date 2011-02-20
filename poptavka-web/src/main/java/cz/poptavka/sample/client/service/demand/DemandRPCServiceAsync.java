package cz.poptavka.sample.client.service.demand;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.domain.demand.Demand;

public interface DemandRPCServiceAsync {

    void getAllDemands(AsyncCallback<List<Demand>> callback);

}
