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
import com.google.gwt.user.client.ui.Widget;

/**
 * Class needed to access SuggestBox's popup.
 *
 * @author Martin Slavkovsky
 */
public class MySuggestDisplay extends SuggestBox.DefaultSuggestionDisplay {

    private static final int OFFSET_LEFT = 0;
    private static final int OFFSET_TOP = 20;
    private Widget originalContent;
    private SimpleIconLabel loader = new SimpleIconLabel();
    private Label message = new Label("Type at least 3 chars to begin search.");

    public MySuggestDisplay() {
        originalContent = super.getPopupPanel().getWidget();
        loader.setImageResource(Storage.RSCS.images().loadIcon32());
    }

    @Override
    public PopupPanel getPopupPanel() {
        return super.getPopupPanel();
    }

    public void showOriginalContent() {
        super.getPopupPanel().setWidget(originalContent);
        //show will fire callback.onSuggestionsReady
    }

    public void showInfoLabelContent() {
        super.getPopupPanel().setWidget(message);
        super.getPopupPanel().show();
    }

    public void showLoadingContent() {
        super.getPopupPanel().setWidget(loader);
        super.getPopupPanel().show();
    }

    public void setPopupPosition(SuggestBox suggestBox) {
        super.getPopupPanel().setPopupPosition(
                DOM.getAbsoluteLeft(suggestBox.getElement()) + OFFSET_LEFT,
                DOM.getAbsoluteTop(suggestBox.getElement()) + OFFSET_TOP);
    }
}
