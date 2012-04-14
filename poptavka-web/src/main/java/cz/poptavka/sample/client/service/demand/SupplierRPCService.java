package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.domain.common.OrderType;

import cz.poptavka.sample.shared.domain.ServiceDetail;
import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.exceptions.CommonException;

import java.util.List;
import java.util.Map;

@RemoteServiceRelativePath("service/supplier")
public interface SupplierRPCService extends RemoteService {

    UserDetail createNewSupplier(UserDetail supplier) throws CommonException;

    ArrayList<ServiceDetail> getSupplierServices() throws CommonException;

    ArrayList<FullSupplierDetail> getSuppliers(int start, int count, Long categoryID, String localityCode)
        throws CommonException;

    ArrayList<FullSupplierDetail> getSuppliers(int start, int count, Long categoryID) throws CommonException;

    ArrayList<FullSupplierDetail> getSuppliers(int start, int count) throws CommonException;

    ArrayList<FullSupplierDetail> getSortedSuppliers(int start, int count, Map<String, OrderType> orderColumns)
        throws CommonException;

    Integer getSuppliersCount() throws CommonException;

    Long getSuppliersCount(Long categoryID) throws CommonException;

    Long getSuppliersCount(Long categoryID, String localityCode) throws CommonException;

    FullSupplierDetail updateSupplier(FullSupplierDetail supplierDetail) throws CommonException;

    long filterSuppliersCount(SearchModuleDataHolder supplier) throws CommonException;

    List<FullSupplierDetail> filterSuppliers(int start, int count,
            SearchModuleDataHolder supplier, Map<String, OrderType> orderColumns) throws CommonException;
}