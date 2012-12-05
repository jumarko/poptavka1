package com.eprovement.poptavka.client.service.demand;


import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.eprovement.poptavka.domain.enums.LocalityType;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetailSuggestion;

public interface LocalityRPCServiceAsync {

    void getLocalities(LocalityType type, AsyncCallback<List<LocalityDetail>> callback);

    void getLocalities(String locCode, AsyncCallback<List<LocalityDetail>> callback);

    void getLocality(long id, AsyncCallback<LocalityDetail> callback);

    void getAllRootLocalities(AsyncCallback<List<LocalityDetail>> callback);

    void getSubLocalities(String locCode, AsyncCallback<List<LocalityDetail>> callback);

    void getLocalitySuggests(String locCode, String startWith, AsyncCallback<List<LocalityDetail>> callback);

    void getCityWithStateSuggestions(String cityLike, AsyncCallback<List<LocalityDetailSuggestion>> callback);
}
