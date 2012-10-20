/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 *
 * @author ivlcek
 */
public interface SecuredUserRPCServiceAsync {

    void getLoggedBusinessUser(AsyncCallback<BusinessUserDetail> callback);

    void getLoggedUser(AsyncCallback<UserDetail> callback);

}
