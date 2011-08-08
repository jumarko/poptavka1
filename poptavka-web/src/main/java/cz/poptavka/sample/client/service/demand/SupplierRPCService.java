package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.shared.domain.ServiceDetail;
import cz.poptavka.sample.shared.domain.UserDetail;

@RemoteServiceRelativePath("service/supplier")
public interface SupplierRPCService extends RemoteService {

    UserDetail createNewSupplier(UserDetail supplier);

    ArrayList<ServiceDetail> getSupplierServices();

    ArrayList<UserDetail> getSuppliers(int start, int count, Long categoryID, String localityCode);

    ArrayList<UserDetail> getSuppliers(int start, int count, Long categoryID);

    Long getSuppliersCount(Long categoryID);

    Long getSuppliersCount(Long categoryID, String localityCode);
}