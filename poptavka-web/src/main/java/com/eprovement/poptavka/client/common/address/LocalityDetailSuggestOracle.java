/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.common.address;

import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Martin Slavkovsky
 */
public class LocalityDetailSuggestOracle extends MultiWordSuggestOracle {

    private static final int MIN_CHARS_TO_SEARCH = 2;
    private List<LocalityDetailMultiWordSuggestion> localitySuggestions = null;

    @Override
    public void requestSuggestions(Request request, Callback callback) {
        Response response = new Response(matchingPeople(request.getQuery(), request.getLimit()));
        callback.onSuggestionsReady(request, response);
    }

    /**
     *
     * @param query The current text being entered into the suggest box
     * @param limit The maximum number of results to return
     * @return A collection of people suggestions that match.
     */
    public Collection<LocalityDetailMultiWordSuggestion> matchingPeople(String query, int limit) {
        List<LocalityDetailMultiWordSuggestion> matchingPeople =
                new ArrayList<LocalityDetailMultiWordSuggestion>(limit);

        //if user type in before data are loaded
        if (localitySuggestions == null) {
            return matchingPeople;
        }

        // only begin to search after the user has type two characters
        if (query.length() >= MIN_CHARS_TO_SEARCH) {
            String prefixToMatch = query.toLowerCase();

            int i = 0;
            int s = localitySuggestions.size();

            // Skip forward over all the names that don't match at the beginning of the array.
            while (i < s && !localitySuggestions.get(i).getDisplayString().toLowerCase().startsWith(prefixToMatch)) {
                i++;
            }

            // Now we are at the start of the block of matching names. Add matching names till we
            // run out of names, stop finding matches, or have enough matches.
            int count = 0;

            while (i < s && localitySuggestions.get(i).getDisplayString().toLowerCase().startsWith(prefixToMatch)
                    && count < limit) {
                matchingPeople.add(localitySuggestions.get(i));
                i++;
                count++;
            }

        }

        return matchingPeople;
    }

    /**
     * @param o
     * @return
     * @see java.util.List#add(java.lang.Object)
     */
    public boolean add(LocalityDetailMultiWordSuggestion o) {
        if (localitySuggestions == null) {
            localitySuggestions = new ArrayList<LocalityDetailMultiWordSuggestion>();
        }

        return localitySuggestions.add(o);
    }

    /**
     * @param o
     * @return
     * @see java.util.List#remove(java.lang.Object)
     */
    public boolean remove(Object o) {
        if (localitySuggestions != null) {
            return localitySuggestions.remove(o);
        }

        return false;
    }
}
