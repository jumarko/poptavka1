package cz.poptavka.sample.client.service.demand;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.shared.domain.SupplierDetail;

public interface SupplierRPCServiceAsync {

    void createNewSupplier(SupplierDetail supplier, AsyncCallback<Long> callback);

}
