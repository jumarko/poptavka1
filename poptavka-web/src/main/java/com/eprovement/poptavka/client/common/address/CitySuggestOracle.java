/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.common.address;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Callback;
import com.google.gwt.user.client.ui.SuggestOracle.Request;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Martin Slavkovsky
 */
public class CitySuggestOracle extends MultiWordSuggestOracle {

    private static final int MIN_CHARS_TO_SEARCH = 2;
    //Need to provide RPC and EventBus
    private AddressSelectorPresenter addressSelectorPresenter = null;
    private static String localityCode = null;

    public CitySuggestOracle(AddressSelectorPresenter presenter) {
        this.addressSelectorPresenter = presenter;
    }

    @Override
    public void requestSuggestions(final Request suggestRequest, final Callback callback) {
        if (suggestRequest.getQuery().length() >= MIN_CHARS_TO_SEARCH) {

            addressSelectorPresenter.getLocalityService().getLocalitySuggests(localityCode, suggestRequest.getQuery(),
                    new SecuredAsyncCallback<List<LocalityDetail>>(addressSelectorPresenter.getEventBus()) {
                        @Override
                        public void onSuccess(List<LocalityDetail> result) {
                            CitySuggestOracle.Response response =
                                    new CitySuggestOracle.Response();

                            Collection<LocalityDetailMultiWordSuggestion> list =
                                    new ArrayList<LocalityDetailMultiWordSuggestion>();
                            for (LocalityDetail loc : result) {
                                list.add(new LocalityDetailMultiWordSuggestion(loc));
                            }
                            response.setSuggestions(list);
                            callback.onSuggestionsReady(suggestRequest, response);
                        }
                    });
        } else {
            callback.onSuggestionsReady(suggestRequest,
                    new CitySuggestOracle.Response(
                    Collections.<SuggestOracle.Suggestion>emptyList()));
        }
    }

    /**
     * Set locality code for next suggestions. Method sets localityCode attribute,
     * which is always passed as attribute to RPC while <b>requestSuggestions</b> method is called.
     *
     * @param locCode
     */
    public static void setLocalityCodeForNextSuggestions(String locCode) {
        localityCode = locCode;
    }

}
