package com.eprovement.poptavka.client.root.header.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import com.github.gwtbootstrap.client.ui.base.IconAnchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;

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
    @UiField IconAnchor searchAnchor;

    /**************************************************************************/
    /* Constructors                                                           */
    /**************************************************************************/
    public SearchElement() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    public SimplePanel getSearchPanel() {
        return searchPanel;
    }

    public IconAnchor getSearchAnchor() {
        return searchAnchor;
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    public void setSearchBar(IsWidget searchBar) {
        searchPanel.setWidget(searchBar);
    }
}
