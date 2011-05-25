package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.shared.domain.ServiceDetail;
import cz.poptavka.sample.shared.domain.SupplierDetail;

public interface SupplierRPCServiceAsync {

    void createNewSupplier(SupplierDetail supplier, AsyncCallback<Long> callback);

    void getSupplierServices(AsyncCallback<ArrayList<ServiceDetail>> callback);

}
