package com.eprovement.poptavka.client.root;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.client.common.search.SearchModuleView;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.client.root.interfaces.IRootView;
import com.eprovement.poptavka.client.root.interfaces.IRootView.IRootPresenter;
import com.google.gwt.user.client.ui.Button;

public class RootView extends ReverseCompositeView<IRootPresenter> implements
        IRootView {

    private static RootViewUiBinder uiBinder = GWT.create(RootViewUiBinder.class);

    interface RootViewUiBinder extends UiBinder<Widget, RootView> {
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField SimplePanel header, body, menu, searchBar;
    @UiField Button logo;

    /**************************************************************************/
    /* Initialization                                                                */
    /**************************************************************************/
    public RootView() {
        initWidget(uiBinder.createAndBindUi(this));
        /* Tato metoda, zaisti, ze sa nacíta CSS styl. Bez nej by sa styl nahral az pri prepnuti do
         * dalsieho modulu.
         */
        StyleResource.INSTANCE.standartStyles().ensureInjected();
        StyleResource.INSTANCE.layout().ensureInjected();

    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    @Override
    public Button getLogo() {
        return logo;
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    @Override
    public void setMenu(IsWidget menu) {
        GWT.log("Menu widget view set");
        this.menu.setWidget(menu);

    }

    @Override
    public void setSearchBar(IsWidget searchBar) {
        GWT.log("Search bar widget view set");
        this.searchBar.setWidget(searchBar);

    }

    @Override
    public void setBody(IsWidget body) {
        GWT.log("Body widget view set");
        this.body.setWidget(body);

    }

    @Override
    public void setHeader(IsWidget header) {
        GWT.log("Header widget view set");
        this.header.setWidget(header);

    }

    /**
     * Sets given advance search view to popup window and set search bar enables
     * (categories, localities, advance button).
     * @param loadedWidget
     */
    @Override
    public void setUpSearchBar(IsWidget advanceSearchWidget) {
        SearchModuleView searchBarView = (SearchModuleView) searchBar.getWidget();
        searchBarView.getSearchContent().setText(null);
        searchBarView.setAttributeSelectorWidget(advanceSearchWidget);
        searchBarView.getAdvanceSearchContentView().resetWidgets();
    }
}
