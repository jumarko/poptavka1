package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import cz.poptavka.sample.domain.common.OrderType;

import cz.poptavka.sample.shared.domain.ServiceDetail;
import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;
import cz.poptavka.sample.shared.domain.UserDetail;
import java.util.Map;

@RemoteServiceRelativePath("service/supplier")
public interface SupplierRPCService extends RemoteService {

    UserDetail createNewSupplier(UserDetail supplier);

    ArrayList<ServiceDetail> getSupplierServices();

    ArrayList<FullSupplierDetail> getSuppliers(int start, int count, Long categoryID, String localityCode);

    ArrayList<FullSupplierDetail> getSuppliers(int start, int count, Long categoryID);

    ArrayList<FullSupplierDetail> getSuppliers(int start, int count);

    ArrayList<FullSupplierDetail> getSortedSuppliers(int start, int count, Map<String, OrderType> orderColumns);

    Integer getSuppliersCount();

    Long getSuppliersCount(Long categoryID);

    Long getSuppliersCount(Long categoryID, String localityCode);

    FullSupplierDetail updateSupplier(FullSupplierDetail supplierDetail, String updateWhat);
}