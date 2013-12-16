package com.eprovement.poptavka.client.service.demand;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.selectors.SuggestionResponse;
import com.eprovement.poptavka.shared.selectors.addressSelector.AddressSuggestionDetail;

@RemoteServiceRelativePath(AddressSelectorRPCService.URL)
public interface AddressSelectorRPCService extends RemoteService {

    String URL = "service/addressSelector";

    SuggestionResponse<AddressSuggestionDetail> getCityWithStateSuggestions(
            int requestId, String cityLike, int wordLength) throws RPCException;

    SuggestionResponse<AddressSuggestionDetail> getShortCityWithStateSuggestions(
            int requestId, String cityLike, int wordLength) throws RPCException;
}
