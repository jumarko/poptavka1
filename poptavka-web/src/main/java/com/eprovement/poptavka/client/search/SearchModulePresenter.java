/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.search;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.search.FilterItem;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.ArrayList;

/**
 * Search module presenter.
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = SearchModuleView.class)
public class SearchModulePresenter
        extends LazyPresenter<SearchModulePresenter.SearchModuleInterface, SearchModuleEventBus> {

    /**************************************************************************/
    /* Interfaces                                                             */
    /**************************************************************************/
    /**
     * View interface
     */
    public interface SearchModuleInterface extends LazyView, IsWidget {

        //GETTERS - search bar items
        TextBox getSearchContent();

        Button getSearchBtn();

        Button getAdvSearchBtn();
    }

    /**
     * Advance search view interface.
     * Martin 1.5.2013 - don't know why but cannot be Lazy because advaced search views are not initializing.
     */
    public interface SearchModulesViewInterface extends IsWidget, ProvidesValidate {

        ArrayList<FilterItem> getFilter();
    }

    /**************************************************************************/
    /** CSS                                                                   */
    /**************************************************************************/
    static {
        StyleResource.INSTANCE.modal().ensureInjected();
    }

    /**************************************************************************/
    /** Attributes                                                            */
    /**************************************************************************/
    private boolean resetAdvancePopup = true;
    private Widget newAttributeSearchWidget;

    /**************************************************************************/
    /** Bind events                                                           */
    /**************************************************************************/
    /**
     * Binds handlers:
     * <ul>
     *   <li>search button hadnler,</li>
     *   <li>advaced search button hadnler,</li>
     *   <li>search texbox keyUp hadnler,</li>
     * </ul>
     */
    @Override
    public void bindView() {
        this.addSearchBtnClickHandler();
        this.addAdvanceSearchBtnClickHandler();
        this.addSearchContentBoxClickHandler();
    }

    /**************************************************************************/
    /** General Module events                                                 */
    /**************************************************************************/
    public void onStart() {
        // nothing by default
    }

    public void onForward() {
        // nothing by default
    }

    /**************************************************************************/
    /** Navigation events                                                     */
    /**************************************************************************/
    /**
     * Initialize search module.
     */
    public void onGoToSearchModule() {
        GWT.log("SearchModule loaded");
        //nothing by default
    }

    /**************************************************************************/
    /** Business events                                                       */
    /**************************************************************************/
    /**
     * Shows advanced search popup.
     */
    public void onShowAdvancedSearchPopup() {
        if (resetAdvancePopup) {
            eventBus.initAdvanceSearchPopup(newAttributeSearchWidget);
            resetAdvancePopup = false;
        }
        eventBus.showAdvanceSearchPopup();
    }

    /**
     * Shows <b>No search criteria</b> popup.
     */
    public void onShowPopupNoSearchCriteria() {
        int left = view.getSearchContent().getElement().getAbsoluteLeft();
        int top = view.getSearchContent().getElement().getAbsoluteTop() + 40;
        VerticalPanel vp = new VerticalPanel();
        vp.addStyleName(StyleResource.INSTANCE.modal().suggestModal());
        vp.add(new Label(Storage.MSGS.searchNoSearchingCriteria()));
        PopupPanel popup = new PopupPanel(true);
        popup.setWidget(vp);
        popup.setPopupPosition(left, top);
        popup.show();
    }

    /**
     * Resets search bar will sets given attribute widget to advnace search popup
     * and clears search textbox.
     * @param newAttributeSearchWidget - new attribute search widget
     */
    public void onResetSearchBar(Widget newAttributeSearchWidget) {
        view.getSearchContent().setText(null);
        this.resetAdvancePopup = true;
        this.newAttributeSearchWidget = newAttributeSearchWidget;
    }

    /**************************************************************************/
    /** Additional events used in bind method                                 */
    /**************************************************************************/
    /**
     * Binds search button handler.
     */
    private void addSearchBtnClickHandler() {
        view.getSearchBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                executeSearch();
            }
        });
    }

    /**
     * Binds advace search button handler.
     */
    private void addAdvanceSearchBtnClickHandler() {
        view.getAdvSearchBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.showAdvancedSearchPopup();
            }
        });
    }

    /**
     * Binds search textbox keyUp handler.
     */
    private void addSearchContentBoxClickHandler() {
        view.getSearchContent().addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == Constants.ENTER_KEY_CODE) {
                    executeSearch();
                }
            }
        });
    }

    /**************************************************************************/
    /** Helper methods                                                        */
    /**************************************************************************/
    /**
     * Full text search.
     */
    private void executeSearch() {
        if (!view.getSearchContent().getText().isEmpty()) {
            SearchModuleDataHolder filter = SearchModuleDataHolder.getSearchModuleDataHolder();
            //set search content text for full text search
            filter.setSearchText(view.getSearchContent().getText());
            forwardToCurrentView(filter);
        } else {
            eventBus.showPopupNoSearchCriteria();
        }
    }

    /**
     * Choose right method for searching in current view.
     * Appropriate RPC is called and it forwards user to appropriate view.
     */
    private void forwardToCurrentView(SearchModuleDataHolder filter) {
        switch (Storage.getCurrentlyLoadedView()) {
            case Constants.HOME_SUPPLIERS_BY_DEFAULT:
                eventBus.goToHomeSuppliersModule(filter);
                break;
            default:
                eventBus.goToHomeDemandsModule(filter);
                break;
        }
    }
}
