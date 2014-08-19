package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.domain.enums.ServiceType;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.ArrayList;

/**
 *
 * @author Martin Slavkovsky
 */
@RemoteServiceRelativePath(ServiceSelectorRPCService.URL)
public interface ServiceSelectorRPCService extends RemoteService {

    String URL = "service/serviceSelector";

    ArrayList<ServiceDetail> getSupplierServices(ServiceType...serviceTypes) throws RPCException;
}
