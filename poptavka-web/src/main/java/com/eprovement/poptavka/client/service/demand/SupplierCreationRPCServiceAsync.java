/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.service.demand;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;
import java.util.ArrayList;

/**
 *
 * @author Praso
 */
public interface SupplierCreationRPCServiceAsync {

    void createNewSupplier(UserDetail supplier, AsyncCallback<UserDetail> callback);

    void getSupplierServices(AsyncCallback<ArrayList<ServiceDetail>> callback);
}
