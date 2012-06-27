package com.eprovement.poptavka.client.service.demand;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eprovement.poptavka.client.main.common.search.SearchModuleDataHolder;
import com.eprovement.poptavka.domain.common.OrderType;

import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import java.util.List;
import java.util.Map;

public interface SupplierRPCServiceAsync {

    void createNewSupplier(BusinessUserDetail supplier, AsyncCallback<BusinessUserDetail> callback);

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