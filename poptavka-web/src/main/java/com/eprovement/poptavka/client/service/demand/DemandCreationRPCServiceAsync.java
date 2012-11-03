/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.service.demand;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;

/**
 *
 * @author praso
 */
public interface DemandCreationRPCServiceAsync {

    void createNewDemand(FullDemandDetail newDemand, Long clientId,
            AsyncCallback<FullDemandDetail> callback);

    void createNewClient(BusinessUserDetail clientDetail, AsyncCallback<BusinessUserDetail> callback);

    /** @see UserRPCService#checkFreeEmail(String) */
    void checkFreeEmail(String email, AsyncCallback<Boolean> callback);
}
