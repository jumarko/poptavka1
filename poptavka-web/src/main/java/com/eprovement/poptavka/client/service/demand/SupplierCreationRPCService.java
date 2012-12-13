/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.service.demand;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;


/**
 *
 * @author Praso
 */
@RemoteServiceRelativePath(SupplierCreationRPCService.URL)
public interface SupplierCreationRPCService extends RemoteService {

    String URL = "service/suppliercreation";

    BusinessUserDetail createNewSupplier(BusinessUserDetail supplier) throws RPCException;

    /**
     * Checks wheter given {@code email} is available.
     * @param email ęmail address to be checked
     */
    boolean checkFreeEmail(String email) throws RPCException;
}
