package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.shared.domain.ServiceDetail;
import cz.poptavka.sample.shared.domain.UserDetail;

public interface SupplierRPCServiceAsync {

    void createNewSupplier(UserDetail supplier, AsyncCallback<UserDetail> callback);

    void getSupplierServices(AsyncCallback<ArrayList<ServiceDetail>> callback);

    void getSuppliers(int start, int count, Long categoryID, Long localityID,
            AsyncCallback<ArrayList<UserDetail>> callback);

    void getSuppliers(int start, int count, Long categoryID,
            AsyncCallback<ArrayList<UserDetail>> callback);

    void getSuppliersCount(Long categoryID, AsyncCallback<Long> callback);

    void getSuppliersCount(Long categoryID, Long localityID, AsyncCallback<Long> callback);
}
