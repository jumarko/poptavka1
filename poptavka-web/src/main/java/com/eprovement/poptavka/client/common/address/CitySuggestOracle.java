/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.common.address;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.shared.domain.LocalityDetailSuggestion;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Callback;
import com.google.gwt.user.client.ui.SuggestOracle.Request;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Provide data for City suggest box in AddressSelector widget.
 * According to user selection starts searching for data where each word of city name
 * starts with user input. Searching starts when user's input meets min chars required
 * represented by MIN_CHARS_TO_SEARCH constant in this class.
 *
 * @author Martin Slavkovsky
 */
public class CitySuggestOracle extends MultiWordSuggestOracle {

    private static final int MIN_CHARS_TO_SEARCH = 3;
    //Need to provide RPC and EventBus
    private AddressSelectorPresenter addressSelectorPresenter = null;

    public CitySuggestOracle(AddressSelectorPresenter presenter) {
        this.addressSelectorPresenter = presenter;
    }

    @Override
    public void requestSuggestions(final Request suggestRequest, final Callback callback) {
        addressSelectorPresenter.getCitySuggestionPopup().setPopupPosition(
                addressSelectorPresenter.getView().getCitySuggestBox());
        if (suggestRequest.getQuery().length() >= MIN_CHARS_TO_SEARCH) {
            addressSelectorPresenter.getCitySuggestionPopup().showLoadingContent();
            addressSelectorPresenter.getLocalityService().getCityWithStateSuggestions(suggestRequest.getQuery(),
                    new SecuredAsyncCallback<List<LocalityDetailSuggestion>>(addressSelectorPresenter.getEventBus()) {
                        @Override
                        public void onSuccess(List<LocalityDetailSuggestion> result) {
                            CitySuggestOracle.Response response = new CitySuggestOracle.Response();
                            Collection<LocalityDetailSuggestion> list =
                                    new ArrayList<LocalityDetailSuggestion>();
                            for (LocalityDetailSuggestion loc : result) {
                                list.add(new LocalityDetailSuggestion(loc.toString(), loc.toString()));
                            }
                            response.setSuggestions(list);
                            addressSelectorPresenter.getCitySuggestionPopup().showOriginalContent();
                            callback.onSuggestionsReady(suggestRequest, response);
                        }
                    });
        } else {
            addressSelectorPresenter.getCitySuggestionPopup().showInfoLabelContent();
        }
    }
}
