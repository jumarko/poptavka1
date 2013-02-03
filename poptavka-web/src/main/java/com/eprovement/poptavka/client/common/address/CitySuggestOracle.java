/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.common.address;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.shared.domain.LocalitySuggestionDetail;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Callback;
import com.google.gwt.user.client.ui.SuggestOracle.Request;
import java.util.ArrayList;
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

    private static final String WHITESPACE_STRING = " ";
    private static final String NOTHING = "";
    private static final String LOCALITY_SEPARATOR = ", ";
    //Need to provide RPC and EventBus
    private AddressSelectorPresenter addressSelectorPresenter = null;
    private Request suggestRequest = null;
    private Callback callback = null;

    public CitySuggestOracle(AddressSelectorPresenter presenter) {
        this.addressSelectorPresenter = presenter;
    }

    @Override
    public void requestSuggestions(final Request suggestRequest, final Callback callback) {
        this.suggestRequest = suggestRequest;
        this.callback = callback;
        if (suggestRequest.getQuery().length() < Constants.MIN_CHARS_TO_SEARCH) {
            addressSelectorPresenter.getCitySuggestionPopup().setOracle(this);
            addressSelectorPresenter.getCitySuggestionPopup().showShortCitiesInfo(Constants.MIN_CHARS_TO_SEARCH);
            addressSelectorPresenter.getCitySuggestionPopup().hideSuggestions();
        } else {
            addressSelectorPresenter.getCitySuggestionPopup().showLoading();
            addressSelectorPresenter.getLocalityService().getCityWithStateSuggestions(
                    getCityFromQuery(suggestRequest.getQuery()),
                    new SecuredAsyncCallback<List<LocalitySuggestionDetail>>(addressSelectorPresenter.getEventBus()) {
                        @Override
                        public void onSuccess(List<LocalitySuggestionDetail> result) {
                            responseSuggestions(suggestRequest, callback, result);
                        }
                    });
        }
    }

    public void requestShortCitySuggestions() {
        addressSelectorPresenter.getCitySuggestionPopup().showLoading();
        addressSelectorPresenter.getLocalityService().getShortCityWithStateSuggestions(suggestRequest.getQuery(),
                new SecuredAsyncCallback<List<LocalitySuggestionDetail>>(addressSelectorPresenter.getEventBus()) {
                    @Override
                    public void onSuccess(List<LocalitySuggestionDetail> result) {
                        responseSuggestions(suggestRequest, callback, result);
                    }
                });
    }

    private void responseSuggestions(final Request suggestRequest, final Callback callback,
            List<LocalitySuggestionDetail> result) {
        if (result.isEmpty()) {
            addressSelectorPresenter.getCitySuggestionPopup().showNoCitiesFound(
                    addressSelectorPresenter.getEventBus());
        } else {
            CitySuggestOracle.Response response = new CitySuggestOracle.Response();
            response.setSuggestions(convertToFormattedSuggestions(suggestRequest.getQuery(), result));
            addressSelectorPresenter.getCitySuggestionPopup().hideLoadingPopup();
            callback.onSuggestionsReady(suggestRequest, response);
        }
    }

    private void responseEmptySuggestions(final Request suggestRequest, final Callback callback) {
        CitySuggestOracle.Response response = new CitySuggestOracle.Response();
        response.setSuggestions(new ArrayList<LocalitySuggestionDetail>());
        addressSelectorPresenter.getCitySuggestionPopup().hideLoadingPopup();
        callback.onSuggestionsReady(suggestRequest, response);
    }

    private List<LocalitySuggestionDetail> convertToFormattedSuggestions(String query,
            List<LocalitySuggestionDetail> candidates) {
        List<LocalitySuggestionDetail> suggestions = new ArrayList<LocalitySuggestionDetail>();

        for (int i = 0; i < candidates.size(); i++) {
            String city = candidates.get(i).getCityName(); //apply only on city name

            // Create strong search string.
            SafeHtmlBuilder formatedCity = new SafeHtmlBuilder();

            String[] words = splitWord(city, query);
            for (int idx = 0; idx < words.length; idx++) {
                if (words[idx].toLowerCase().contains(query.toLowerCase())) {
                    formatWord(formatedCity, words[idx], query);
                    if (idx != words.length - 1) {
                        formatedCity.appendEscaped(WHITESPACE_STRING);
                    }
                } else {
                    formatedCity.appendEscaped(words[idx]);
                    if (idx != words.length - 1) {
                        formatedCity.appendEscaped(WHITESPACE_STRING);
                    }
                }
            }

            formatedCity.appendEscaped(LOCALITY_SEPARATOR);
            formatedCity.appendEscaped(candidates.get(i).getStateName());
            //Set formated suggestions
            candidates.get(i).setSuggestion(
                    candidates.get(i).toString(),
                    formatedCity.toSafeHtml().asString());
            suggestions.add(candidates.get(i));
        }
        return suggestions;
    }

    private String[] splitWord(String city, String query) {
        /** Formating only one word. **/
        if (!query.contains(WHITESPACE_STRING)) {
            return city.split(WHITESPACE_STRING);
            /** Formating more words. **/
        } else {
            String queryTmp = query.replace(WHITESPACE_STRING, NOTHING); //link query words
            String cityTmp = city.replace(query, queryTmp); //link city words
            //if cityTmp still contains " " -> consisting of more words
            if (cityTmp.contains(WHITESPACE_STRING)) {
                return new String[]{city};
            } else {
                String[] words = cityTmp.split(WHITESPACE_STRING);
                for (String word : words) {
                    if (word.contains(queryTmp)) {
                        word = word.replace(queryTmp, query);
                    }
                }
                return words;
            }
        }
    }

    private void formatWord(SafeHtmlBuilder formatedCity, String city, String query) {
        int start = 0; //should be always 0, only localities that starts with query are returned
        int length = query.length();

        String part1 = city.substring(start, length);
        String part2 = city.substring(length);
        formatedCity.appendHtmlConstant("<strong>");
        formatedCity.appendEscaped(part1);
        formatedCity.appendHtmlConstant("</strong>");
        formatedCity.appendEscaped(part2);

    }

    private String getCityFromQuery(String query) {
        if (query.indexOf(LOCALITY_SEPARATOR) == -1) {
            return query;
        } else {
            return query.substring(0, query.indexOf(LOCALITY_SEPARATOR));
        }
    }
}
