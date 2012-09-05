/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.service.demand;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SimpleServiceAsync {

    /**
     * GWT-RPC service  asynchronous (client-side) interface.
     * @see com.gwtsecurity.sample.client.services.SimpleService
     */
    void getData(AsyncCallback<String> callback);

    /**
     * GWT-RPC service  asynchronous (client-side) interface.
     * @see com.gwtsecurity.sample.client.services.SimpleService
     */
    void getSecuredData(AsyncCallback<String> callback);

}
