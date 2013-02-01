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
import com.google.gwt.i18n.client.LocalizableMessages;

public class RootView extends ReverseCompositeView<IRootPresenter> implements
        IRootView {

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private static RootViewUiBinder uiBinder = GWT.create(RootViewUiBinder.class);
    @UiField
    SimplePanel logo, header, body, menu, searchBar, footer;

    interface RootViewUiBinder extends UiBinder<Widget, RootView> {
    }

    public RootView() {

        initWidget(uiBinder.createAndBindUi(this));
        /* Tato metoda, zaisti, ze sa nacíta CSS styl. Bez nej by sa styl nahral az pri prepnuti do
         * dalsieho modulu.
         */
        StyleResource.INSTANCE.standartStyles().ensureInjected();
        StyleResource.INSTANCE.layout().ensureInjected();

    }

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
    public void setFooter(IsWidget footer) {
        GWT.log("Footer widget view set");
        this.footer.setWidget(footer);

    }

    @Override
    public void setHeader(IsWidget header) {
        GWT.log("Header widget view set");
        this.header.setWidget(header);

    }

    @Override
    public void setLogoStyle(String style) {
        this.logo.setStyleName(style);
    }

    /**
     * Sets given advance search view to popup window and set search bar enables
     * (categories, localities, advance button).
     * @param loadedWidget
     */
    @Override
    public void setUpSearchBar(IsWidget advanceSearchWidget) {
        SearchModuleView searchBarView = (SearchModuleView) searchBar.getWidget();
        searchBarView.getSearchContent().setText(null); //placeholder will set default -- null, or "" ???
        searchBarView.setAttributeSelectorWidget(advanceSearchWidget);
    }
}
