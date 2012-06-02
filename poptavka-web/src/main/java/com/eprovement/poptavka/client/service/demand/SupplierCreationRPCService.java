/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.service.demand;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;

import java.util.ArrayList;

/**
 *
 * @author Praso
 */
@RemoteServiceRelativePath(SupplierCreationRPCService.URL)
public interface SupplierCreationRPCService extends RemoteService {

    String URL = "service/suppliercreation";

    UserDetail createNewSupplier(UserDetail supplier) throws RPCException;

    ArrayList<ServiceDetail> getSupplierServices() throws RPCException;
}
