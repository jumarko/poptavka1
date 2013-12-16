package com.eprovement.poptavka.client.search;

import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

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
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    // Search Bar items
    @Override
    public TextBox getSearchContent() {
        return searchContent;
    }

    @Override
    public Button getSearchBtn() {
        return searchBtn;
    }

    @Override
    public Button getAdvSearchBtn() {
        return advSearchBtn;
    }

    // Layouts & Panels
    @Override
    public Widget getWidgetView() {
        return this;
    }
}