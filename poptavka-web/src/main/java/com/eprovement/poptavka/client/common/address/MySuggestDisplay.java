/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.common.address;

import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SuggestBox;

/**
 * Class needed to access SuggestBox's popup.
 *
 * @author Martin Slavkovsky
 */
public class MySuggestDisplay extends SuggestBox.DefaultSuggestionDisplay {

    @Override
    public PopupPanel getPopupPanel() {
        return super.getPopupPanel();
    }
}
