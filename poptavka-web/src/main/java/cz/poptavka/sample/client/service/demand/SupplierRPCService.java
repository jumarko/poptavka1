package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.domain.common.OrderType;

import cz.poptavka.sample.shared.domain.ServiceDetail;
import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.exceptions.RPCException;

import java.util.List;
import java.util.Map;

@RemoteServiceRelativePath(SupplierRPCService.URL)
public interface SupplierRPCService extends RemoteService {

    String URL = "service/supplier";

    UserDetail createNewSupplier(UserDetail supplier) throws RPCException;

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