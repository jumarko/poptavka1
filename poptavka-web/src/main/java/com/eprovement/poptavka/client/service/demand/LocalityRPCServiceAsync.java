package com.eprovement.poptavka.client.service.demand;


import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.eprovement.poptavka.domain.address.LocalityType;
import com.eprovement.poptavka.shared.domain.LocalityDetail;

public interface LocalityRPCServiceAsync {

    void getLocalities(LocalityType type, AsyncCallback<ArrayList<LocalityDetail>> callback);

    void getLocalities(String locCode, AsyncCallback<ArrayList<LocalityDetail>> callback);

    void getLocality(long id, AsyncCallback<LocalityDetail> callback);

    void getAllRootLocalities(AsyncCallback<ArrayList<LocalityDetail>> callback);

    void getSubLocalities(String locCode, AsyncCallback<ArrayList<LocalityDetail>> callback);
}
