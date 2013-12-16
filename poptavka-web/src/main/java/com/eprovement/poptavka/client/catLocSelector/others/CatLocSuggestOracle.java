/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.catLocSelector.others;

import com.eprovement.poptavka.client.catLocSelector.manager.ManagerPresenter;
import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.shared.selectors.SuggestionResponse;
import com.eprovement.poptavka.shared.selectors.catLocSelector.CatLocSuggestionDetail;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Callback;
import com.google.gwt.user.client.ui.SuggestOracle.Request;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Provide data for City suggest box in AddressSelector widget.
 * According to user selection starts searching for data where each word of city name
 * starts with user input. Searching starts when user's input meets min chars required
 * represented by MIN_CHARS_TO_SEARCH constant in this class.
 *
 * @author Martin Slavkovsky
 */
public class CatLocSuggestOracle extends MultiWordSuggestOracle {

    private static final String SUBSTRING_FORMATTER_LEFT = "<strong>";
    private static final String SUBSTRING_FORMATTER_RIGHT = "</strong>";
    //Need to provide RPC and EventBus
    private ManagerPresenter presenter = null;
    private Request suggestRequest = null;
    private Callback callback = null;
    private Timer timer = null;
    private int requestId = 1;
    private int currentRequestId = 0;
    private boolean throwAwayAnyReponse;

    public CatLocSuggestOracle(ManagerPresenter managerPresenter) {
        this.presenter = managerPresenter;
        this.timer = new Timer() {
            @Override
            public void run() {
                presenter.getCatLocSuggestionPopup().showLoading();
                presenter.getCatLocService().getSuggestions(requestId++,
                        presenter.getBuilder().getSelectorType(),
                        suggestRequest.getQuery(), Constants.MIN_CHARS_TO_SEARCH,
                        new SecuredAsyncCallback<SuggestionResponse<CatLocSuggestionDetail>>(
                        presenter.getEventBus()) {
                            @Override
                            public void onSuccess(SuggestionResponse<CatLocSuggestionDetail> result) {
                                manageResponses(result);
                            }
                        });
            }
        };
    }

    @Override
    public void requestSuggestions(final Request suggestRequest, final Callback callback) {
        this.suggestRequest = suggestRequest;
        this.callback = callback;
        timer.cancel();
        if (suggestRequest.getQuery().length() < Constants.MIN_CHARS_TO_SEARCH) {
            throwAwayAnyReponse = true;
            presenter.getCatLocSuggestionPopup().setOracle(this);
            presenter.getCatLocSuggestionPopup().showShortItemsInfo(
                    Constants.MIN_CHARS_TO_SEARCH);
            presenter.getCatLocSuggestionPopup().hideSuggestions();
        } else {
            throwAwayAnyReponse = false;
            //If the user keeps triggering this event (e.g., keeps typing), cancel and restart the timer
            timer.schedule(Constants.SUGGESTBOX_DELAY);
        }

    }

    public void requestShortCitySuggestions() {
        throwAwayAnyReponse = false;
        presenter.getCatLocSuggestionPopup().showLoading();
        presenter.getCatLocService().getShortSuggestions(requestId++,
                presenter.getBuilder().getSelectorType(),
                suggestRequest.getQuery(), Constants.MIN_CHARS_TO_SEARCH,
                new SecuredAsyncCallback<SuggestionResponse<CatLocSuggestionDetail>>(
                presenter.getEventBus()) {
                    @Override
                    public void onSuccess(SuggestionResponse<CatLocSuggestionDetail> result) {
                        manageResponses(result);
                    }
                });
    }

    /**
     * Since server communication is asynchronous, for long requests responses can change order.
     * Each next request is usually more selective and takes less time to finnish.
     * That causes strange behaviour of replacing responses on frontend UI.
     * Therefore we need to link request and response and throw away the older one.
     * @param response containing response id and suggestions
     */
    private void manageResponses(SuggestionResponse<CatLocSuggestionDetail> response) {
        //if it's newer reponse, accept it
        if (!throwAwayAnyReponse && currentRequestId < response.getId()) {
            currentRequestId = response.getId();
            responseSuggestions(suggestRequest, callback, response.getSuggestions());
        }
        //else - forget the response - throw it away
    }

    private void responseSuggestions(final Request suggestRequest, final Callback callback,
            List<CatLocSuggestionDetail> result) {
        //Deny selecting root Categories by removing them from suggestions
        for (Iterator<CatLocSuggestionDetail> it = result.iterator(); it.hasNext();) {
            CatLocSuggestionDetail catLocSuggestionDetail = it.next();
            if (presenter.getBuilder().getSelectorType() == CatLocSelectorBuilder.SELECTOR_TYPE_CATEGORIES
                    && catLocSuggestionDetail.getParentCatLoc() == null) {
                it.remove();
            }
        }
        //If no result found, display "No Itmes Found" popup
        if (result.isEmpty()) {
            presenter.getCatLocSuggestionPopup().hideSuggestions();
            presenter.getCatLocSuggestionPopup().showItemsFound(
                    presenter.getEventBus());
        //Else format mathing substring to bold font
        } else {
            CatLocSuggestOracle.Response response = new CatLocSuggestOracle.Response();
            response.setSuggestions(convertToFormattedSuggestions(suggestRequest.getQuery(), result));
            presenter.getCatLocSuggestionPopup().hideLoadingPopup();
            callback.onSuggestionsReady(suggestRequest, response);
        }
    }

    private List<CatLocSuggestionDetail> convertToFormattedSuggestions(String query,
            List<CatLocSuggestionDetail> candidates) {
        List<CatLocSuggestionDetail> suggestions = new ArrayList<CatLocSuggestionDetail>();

        for (int i = 0; i < candidates.size(); i++) {
            String category = candidates.get(i).getCatLoc().getName();

            // Create strong search string.
            SafeHtmlBuilder formatedCity = new SafeHtmlBuilder();

            formatWord(formatedCity, category, query);

            if (candidates.get(i).getParentCatLoc() != null) { //is root item
                formatedCity.appendEscaped(" (");
                formatedCity.appendEscaped(candidates.get(i).getParentCatLoc().getName());
                formatedCity.appendEscaped(")");
            }

            //Set formated suggestions
            candidates.get(i).setSuggestion(
                    candidates.get(i).getCatLoc().getName(),
                    formatedCity.toSafeHtml().asString());
            suggestions.add(candidates.get(i));
        }
        return suggestions;
    }

    /**
     * Surround substring "query" of string "city" with html tag.
     * @param formatedCity formated city - user's input as bold font of matching item
     * @param item suggested item
     * @param query user's input for suggest box
     */
    private void formatWord(SafeHtmlBuilder formatedCity, String item, String query) {
        int start = item.toLowerCase().indexOf(query.toLowerCase());

        if (start == -1) {
            formatedCity.appendEscaped(item);
        } else {
            int length = query.length();

            String part1 = item.substring(0, start);
            String part2 = item.substring(start, start + length);
            String part3 = item.substring(start + length);

            formatedCity.appendEscaped(part1);
            formatedCity.appendHtmlConstant(SUBSTRING_FORMATTER_LEFT);
            formatedCity.appendEscaped(part2);
            formatedCity.appendHtmlConstant(SUBSTRING_FORMATTER_RIGHT);
            formatedCity.appendEscaped(part3);
        }
    }
}
