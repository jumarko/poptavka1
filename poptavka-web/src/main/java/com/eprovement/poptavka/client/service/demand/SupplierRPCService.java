package com.eprovement.poptavka.client.service.demand;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.eprovement.poptavka.client.main.common.search.SearchModuleDataHolder;
import com.eprovement.poptavka.domain.common.OrderType;

import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;

import java.util.List;
import java.util.Map;

@RemoteServiceRelativePath(SupplierRPCService.URL)
public interface SupplierRPCService extends RemoteService {

    String URL = "service/supplier";

    BusinessUserDetail createNewSupplier(BusinessUserDetail supplier) throws RPCException;

    ArrayList<ServiceDetail> getSupplierServices() throws RPCException;

    ArrayList<FullSupplierDetail> getSuppliers(int start, int count, Long categoryID, String localityCode)
        throws RPCException;

    ArrayList<FullSupplierDetail> getSuppliers(int start, int count, Long categoryID) throws RPCException;

    ArrayList<FullSupplierDetail> getSuppliers(int start, int count) throws RPCException;

    ArrayList<FullSupplierDetail> getSortedSuppliers(int start, int count, Map<String, OrderType> orderColumns)
        throws RPCException;

    Integer getSuppliersCount() throws RPCException;

    Long getSuppliersCount(Long categoryID) throws RPCException;

    Long getSuppliersCount(Long categoryID, String localityCode) throws RPCException;

    FullSupplierDetail updateSupplier(FullSupplierDetail supplierDetail) throws RPCException;

    long filterSuppliersCount(SearchModuleDataHolder supplier) throws RPCException;

    List<FullSupplierDetail> filterSuppliers(int start, int count,
            SearchModuleDataHolder supplier, Map<String, OrderType> orderColumns) throws RPCException;
}