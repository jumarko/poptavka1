/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.root.header.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Search element includes search container for search bar widget on middle-large screens
 * and icon anchor on tiny-small screens.
 * @author Martin Slavkovsky
 */
public class SearchElement extends Composite {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static SearchElementUiBinder uiBinder = GWT.create(SearchElementUiBinder.class);

    interface SearchElementUiBinder extends UiBinder<Widget, SearchElement> {
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder Attributes. **/
    @UiField SimplePanel searchPanel;
    @UiField Button searchButton;

    /**************************************************************************/
    /* Constructors                                                           */
    /**************************************************************************/
    /**
     * Creates search element view's compontents.
     */
    public SearchElement() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /**
     * @return the search panel.
     */
    public SimplePanel getSearchPanel() {
        return searchPanel;
    }

    /**
     * @return the search anchor.
     */
    public Button getSearchButton() {
        return searchButton;
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    /**
     * Sets search container's widget.
     * @param searchBar widget
     */
    public void setSearchBar(IsWidget searchBar) {
        searchPanel.setWidget(searchBar);
    }
}
