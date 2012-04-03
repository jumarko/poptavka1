/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.service.demand;

import com.google.gwt.user.client.rpc.AsyncCallback;
import cz.poptavka.sample.shared.domain.ServiceDetail;
import cz.poptavka.sample.shared.domain.UserDetail;
import java.util.ArrayList;

/**
 *
 * @author Praso
 */
public interface SupplierCreationRPCServiceAsync {

    void createNewSupplier(UserDetail supplier, AsyncCallback<UserDetail> callback);

    void checkFreeEmail(String email, AsyncCallback<Boolean> callback);

    void getSupplierServices(AsyncCallback<ArrayList<ServiceDetail>> callback);
}
