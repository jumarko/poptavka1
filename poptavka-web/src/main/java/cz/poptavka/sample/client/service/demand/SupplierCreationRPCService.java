/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.service.demand;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import cz.poptavka.sample.shared.domain.ServiceDetail;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.exceptions.RPCException;

import java.util.ArrayList;

/**
 *
 * @author Praso
 */
@RemoteServiceRelativePath("service/suppliercreation")
public interface SupplierCreationRPCService extends RemoteService {

    UserDetail createNewSupplier(UserDetail supplier) throws RPCException;

    ArrayList<ServiceDetail> getSupplierServices() throws RPCException;
}
