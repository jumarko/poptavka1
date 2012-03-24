/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.service.demand;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import cz.poptavka.sample.shared.domain.UserDetail;

/**
 *
 * @author Praso
 */
@RemoteServiceRelativePath("service/suppliercreation")
public interface SupplierCreationRPCService extends RemoteService {

    UserDetail createNewSupplier(UserDetail supplier);

    boolean checkFreeEmail(String email);
}
