/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.common.address;

import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Callback;
import com.google.gwt.user.client.ui.SuggestOracle.Request;
import java.util.Collections;

/**
 *
 * @author Martin Slavkovsky
 */
public class RegionSuggestOracle extends MultiWordSuggestOracle {

    private static final int MIN_CHARS_TO_SEARCH = 2;
    //Need to provide RPC and EventBus
    private AddressSelectorPresenter addressSelectorPresenter = null;
    private static String localityCode = null;

    public RegionSuggestOracle(AddressSelectorPresenter presenter) {
        this.addressSelectorPresenter = presenter;
    }

    @Override
    public void requestSuggestions(final Request suggestRequest, final Callback callback) {
        if (suggestRequest.getQuery().length() >= MIN_CHARS_TO_SEARCH) {

//            addressSelectorPresenter.getLocalityService().getLocalitySuggests(localityCode, suggestRequest.getQuery(),
//                    new SecuredAsyncCallback<List<LocalityDetail>>(addressSelectorPresenter.getEventBus()) {
//                        @Override
//                        public void onSuccess(List<LocalityDetail> result) {
//                            RegionSuggestOracle.Response response =
//                                    new RegionSuggestOracle.Response();
//
//                            Collection<LocalityDetailSuggestion> list =
//                                    new ArrayList<LocalityDetailSuggestion>();
//                            for (LocalityDetail loc : result) {
//                                list.add(new LocalityDetailSuggestion(loc));
//                            }
//                            response.setSuggestions(list);
//                            callback.onSuggestionsReady(suggestRequest, response);
//                        }
//                    });
        } else {
            callback.onSuggestionsReady(suggestRequest,
                    new RegionSuggestOracle.Response(
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
