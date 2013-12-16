/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.addressSelector.others;

import com.eprovement.poptavka.client.addressSelector.AddressSelectorEventBus;
import com.eprovement.poptavka.client.common.SimpleIconLabel;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.resources.StyleResource;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import java.util.Date;

/**
 * Class needed to access SuggestBox's popup.
 * Class also contain another popup to display loading information.
 *
 * @author Martin Slavkovsky
 */
public class AddressSelectorSuggestDisplay extends SuggestBox.DefaultSuggestionDisplay {

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    private static final int OFFSET_LEFT = 0;
    private static final int OFFSET_TOP = 50;
    private SimpleIconLabel loader = new SimpleIconLabel();
    private AddressSelectorSuggestOracle oracle = null;
    private PopupPanel loadingPopup = new PopupPanel(true);

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    public AddressSelectorSuggestDisplay() {
        loader.setImageResource(Storage.RSCS.images().loaderIcon33());
        loader.addStyleName(StyleResource.INSTANCE.modal().commonModalStyle());
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    @Override
    public PopupPanel getPopupPanel() {
        return super.getPopupPanel();
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    public void setOracle(AddressSelectorSuggestOracle oracle) {
        this.oracle = oracle;
    }

    public void setLoadingPopupPosition(SuggestBox suggestBox) {
        loadingPopup.setPopupPosition(
                DOM.getAbsoluteLeft(suggestBox.getElement()) + OFFSET_LEFT,
                DOM.getAbsoluteTop(suggestBox.getElement()) + OFFSET_TOP);
    }

    /**************************************************************************/
    /* SHOW                                                                   */
    /**************************************************************************/
    public void showNoCitiesFound(final AddressSelectorEventBus eventbus) {
        VerticalPanel vp = new VerticalPanel();
        vp.addStyleName(StyleResource.INSTANCE.modal().suggestModal());
        vp.add(new Label(Storage.MSGS.addressNoCityFound()));
        Anchor anchor = new Anchor(Storage.MSGS.commonBtnReport());
        anchor.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventbus.sendUsEmail(Constants.SUBJECT_REPORT_ISSUE, (new Date()).toString());
            }
        });
        vp.add(anchor);
        loadingPopup.setWidget(vp);
        loadingPopup.removeStyleName(StyleResource.INSTANCE.modal().smallLoaderModal());
        loadingPopup.show();
    }

    public void showShortCitiesInfo(int minCityChars) {
        VerticalPanel vp = new VerticalPanel();
        vp.addStyleName(StyleResource.INSTANCE.modal().suggestModal());
        vp.add(new Label(Storage.MSGS.addressLoadingInfoLabel(Constants.MIN_CHARS_TO_SEARCH)));
        Anchor anchor = new Anchor(Storage.MSGS.addressMyCityIsLessInfoLabel(Constants.MIN_CHARS_TO_SEARCH));
        anchor.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                oracle.requestShortCitySuggestions();
            }
        });
        vp.add(anchor);
        loadingPopup.setWidget(vp);
        loadingPopup.removeStyleName(StyleResource.INSTANCE.modal().smallLoaderModal());
        loadingPopup.show();
    }

    public void showLoading() {
        hideSuggestions();
        loadingPopup.setWidget(loader);
        loadingPopup.addStyleName(StyleResource.INSTANCE.modal().smallLoaderModal());
        loadingPopup.show();
    }

    /**************************************************************************/
    /* HIDE                                                                   */
    /**************************************************************************/
    @Override
    public void hideSuggestions() {
        super.hideSuggestions();
    }

    public void hideLoadingPopup() {
        loadingPopup.hide();
    }
}
