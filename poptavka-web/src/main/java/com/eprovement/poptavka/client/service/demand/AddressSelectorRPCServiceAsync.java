package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.selectors.SuggestionResponse;
import com.eprovement.poptavka.shared.selectors.addressSelector.AddressSuggestionDetail;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AddressSelectorRPCServiceAsync {

    void getCityWithStateSuggestions(int requestId, String cityLike, int wordLength,
            AsyncCallback<SuggestionResponse<AddressSuggestionDetail>> callback);

    void getShortCityWithStateSuggestions(int requestId, String cityLike, int wordLength,
            AsyncCallback<SuggestionResponse<AddressSuggestionDetail>> callback);
}
