package com.eprovement.poptavka.client.service.demand;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.eprovement.poptavka.domain.enums.LocalityType;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.LocalitySuggestionDetail;

public interface LocalityRPCServiceAsync {

    void getLocalities(LocalityType type, AsyncCallback<List<LocalityDetail>> callback);

    void getSubLocalities(Long id, AsyncCallback<List<LocalityDetail>> callback);

    void getLocality(long id, AsyncCallback<LocalityDetail> callback);

    void getAllRootLocalities(AsyncCallback<List<LocalityDetail>> callback);

    void getCityWithStateSuggestions(String cityLike, AsyncCallback<List<LocalitySuggestionDetail>> callback);

    void getShortCityWithStateSuggestions(String cityLike, AsyncCallback<List<LocalitySuggestionDetail>> callback);
}
