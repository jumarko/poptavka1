/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.common.address;

import com.eprovement.poptavka.client.common.SimpleIconLabel;
import com.eprovement.poptavka.client.common.session.Storage;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SuggestBox;

/**
 * Class needed to access SuggestBox's popup.
 * Class also contain another popup to display loading information.
 *
 * @author Martin Slavkovsky
 */
public class MySuggestDisplay extends SuggestBox.DefaultSuggestionDisplay {

    private static final int OFFSET_LEFT = 0;
    private static final int OFFSET_TOP = 20;
    private SimpleIconLabel loader = new SimpleIconLabel();

    private PopupPanel loadingPopup = new PopupPanel(true);

    public MySuggestDisplay() {
        loader.setImageResource(Storage.RSCS.images().loadIcon32());
    }

    @Override
    public PopupPanel getPopupPanel() {
        return super.getPopupPanel();
    }

    public void hideLoadingPopup() {
        loadingPopup.hide();
    }

    public void showLoadingInfoLabel(String message) {
        loadingPopup.setWidget(new Label(message));
        loadingPopup.show();
    }

    public void showLoading() {
        loadingPopup.setWidget(loader);
        loadingPopup.show();
    }

    public void setLoadingPopupPosition(SuggestBox suggestBox) {
        loadingPopup.setPopupPosition(
                DOM.getAbsoluteLeft(suggestBox.getElement()) + OFFSET_LEFT,
                DOM.getAbsoluteTop(suggestBox.getElement()) + OFFSET_TOP);
    }
}
