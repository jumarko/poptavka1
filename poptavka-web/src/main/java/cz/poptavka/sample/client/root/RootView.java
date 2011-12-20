package cz.poptavka.sample.client.root;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.root.interfaces.IRootView;
import cz.poptavka.sample.client.root.interfaces.IRootView.IRootPresenter;

public class RootView extends ReverseCompositeView<IRootPresenter> implements
        IRootView {

    private static RootViewUiBinder uiBinder = GWT
            .create(RootViewUiBinder.class);

    @UiField
    SimplePanel header, body, menu, searchBar, footer;

    interface RootViewUiBinder extends UiBinder<Widget, RootView> {
    }

    public RootView() {
        initWidget(uiBinder.createAndBindUi(this));
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
    public IsWidget getHeader() {
        return this.header.getWidget();
    }

    @Override
    public SimplePanel getSearchBar() {
        return this.searchBar;
    }

    @Override
    public Widget getBody() {
        return this.body;
    }

    @Override
    public Widget getMenu() {
        return this.menu;
    }

}
