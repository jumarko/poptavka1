/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 *
 * @author Praso
 */
public interface SupplierCreationRPCServiceAsync {

    void createNewSupplier(FullSupplierDetail supplier, AsyncCallback<FullSupplierDetail> callback);

    /** @see UserRPCService#checkFreeEmail(String) */
    void checkFreeEmail(String email, AsyncCallback<Boolean> callback);
}
