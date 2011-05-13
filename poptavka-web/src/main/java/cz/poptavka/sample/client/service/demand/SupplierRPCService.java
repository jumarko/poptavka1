package cz.poptavka.sample.client.service.demand;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.shared.domain.SupplierDetail;

@RemoteServiceRelativePath("service/supplier")
public interface SupplierRPCService extends RemoteService {

    long createNewSupplier(SupplierDetail supplier);
}
