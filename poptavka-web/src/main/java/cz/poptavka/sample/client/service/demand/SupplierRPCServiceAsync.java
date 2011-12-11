package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.domain.common.OrderType;

import cz.poptavka.sample.shared.domain.ServiceDetail;
import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;
import cz.poptavka.sample.shared.domain.UserDetail;
import java.util.List;
import java.util.Map;

public interface SupplierRPCServiceAsync {

    void createNewSupplier(UserDetail supplier, AsyncCallback<UserDetail> callback);

    void getSupplierServices(AsyncCallback<ArrayList<ServiceDetail>> callback);

    void getSuppliers(int start, int count, Long categoryID, String localityCode,
            AsyncCallback<ArrayList<FullSupplierDetail>> callback);

    void getSuppliers(int start, int count, Long categoryID,
            AsyncCallback<ArrayList<FullSupplierDetail>> callback);

    void getSuppliers(int start, int count,
            AsyncCallback<ArrayList<FullSupplierDetail>> callback);

    void getSortedSuppliers(int start, int count, Map<String, OrderType> orderColumns,
            AsyncCallback<ArrayList<FullSupplierDetail>> callback);

    void getSuppliersCount(AsyncCallback<Integer> callback);

    void getSuppliersCount(Long categoryID, AsyncCallback<Long> callback);

    void getSuppliersCount(Long categoryID, String localityCode, AsyncCallback<Long> callback);

    void updateSupplier(FullSupplierDetail supplierDetail,
            AsyncCallback<FullSupplierDetail> callback);

    void filterSuppliers(int start, int count, SearchModuleDataHolder supplier, Map<String, OrderType> orderColumns,
            AsyncCallback<List<FullSupplierDetail>> callback);

    void filterSuppliersCount(SearchModuleDataHolder supplier, AsyncCallback<Long> callback);
}