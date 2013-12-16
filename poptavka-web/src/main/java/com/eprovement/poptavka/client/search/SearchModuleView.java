/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.search;

import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * Search module view consists of search textbox for full text search and seach and advance search buttons.
 * @author Martin Slavkovsky
 */
public class SearchModuleView extends Composite implements SearchModulePresenter.SearchModuleInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, SearchModuleView> {
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField Button searchBtn, advSearchBtn;
    @UiField TextBox searchContent;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates Search view's compontents.
     */
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /**
     * @return the search textbox
     */
    @Override
    public TextBox getSearchContent() {
        return searchContent;
    }

    /**
     * @return the search button
     */
    @Override
    public Button getSearchBtn() {
        return searchBtn;
    }

    /**
     * @return the advncae search button
     */
    @Override
    public Button getAdvSearchBtn() {
        return advSearchBtn;
    }

    /**
     * @return the widget view
     */
    @Override
    public Widget getWidgetView() {
        return this;
    }
}