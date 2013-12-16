/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.catLocSelector.others;

import com.eprovement.poptavka.client.catLocSelector.CatLocSelectorEventBus;
import com.eprovement.poptavka.client.common.SimpleIconLabel;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.resources.StyleResource;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Class needed to access SuggestBox's popup.
 * Class also contain another popup to display loading information.
 *
 * @author Martin Slavkovsky
 */
public class CatLocSuggestionDisplay extends SuggestBox.DefaultSuggestionDisplay {

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    private static final int OFFSET_LEFT = 0;
    private static final int OFFSET_TOP = 45;
    private SimpleIconLabel loader = new SimpleIconLabel();
    private CatLocSuggestOracle oracle = null;
    private PopupPanel loadingPopup = new PopupPanel(true);

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    public CatLocSuggestionDisplay() {
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
    public void setOracle(CatLocSuggestOracle oracle) {
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
    public void showItemsFound(final CatLocSelectorEventBus eventbus) {
        VerticalPanel vp = new VerticalPanel();
        vp.addStyleName(StyleResource.INSTANCE.modal().suggestModal());
        vp.add(new Label("No such item found."));
        loadingPopup.setWidget(vp);
        loadingPopup.removeStyleName(StyleResource.INSTANCE.modal().smallLoaderModal());
        loadingPopup.show();
    }

    public void showShortItemsInfo(int minCityChars) {
        VerticalPanel vp = new VerticalPanel();
        vp.addStyleName(StyleResource.INSTANCE.modal().suggestModal());
        vp.add(new Label("Type at least " + Constants.MIN_CHARS_TO_SEARCH + " characters to begin search"));
        Anchor anchor = new Anchor("Search less then " + Constants.MIN_CHARS_TO_SEARCH);
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

    public void showItemAlreadyAdded() {
        VerticalPanel vp = new VerticalPanel();
        vp.addStyleName(StyleResource.INSTANCE.modal().suggestModal());
        vp.add(new Label("Item already added.\n See table below."));
        loadingPopup.setWidget(vp);
        loadingPopup.removeStyleName(StyleResource.INSTANCE.modal().smallLoaderModal());
        loadingPopup.show();
        Timer timer = new Timer() {

            @Override
            public void run() {
                loadingPopup.hide();
            }
        };
        //TODO constant?
        timer.schedule(1000);
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
