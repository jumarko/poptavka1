/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.common.address;

import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;

/**
 *
 * @author Martin Slavkovsky
 */
public class LocalityDetailMultiWordSuggestion extends MultiWordSuggestOracle.MultiWordSuggestion {

    private LocalityDetail localityDetail = null;

    public LocalityDetailMultiWordSuggestion() {
        super();
    }

    public LocalityDetailMultiWordSuggestion(LocalityDetail localityDetail) {
        super(localityDetail.getName(), localityDetail.getName());

        this.localityDetail = localityDetail;
    }

    public LocalityDetail getLocalityDetail() {
        return localityDetail;
    }
}
