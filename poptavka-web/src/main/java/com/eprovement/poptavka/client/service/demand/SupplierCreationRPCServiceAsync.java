/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.service.demand;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;

/**
 *
 * @author Praso
 */
public interface SupplierCreationRPCServiceAsync {

    void createNewSupplier(BusinessUserDetail supplier, AsyncCallback<BusinessUserDetail> callback);

    /** @see UserRPCService#checkFreeEmail(String) */
    void checkFreeEmail(String email, AsyncCallback<Boolean> callback);
}
