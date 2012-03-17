package cz.poptavka.sample.client.root;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.client.root.interfaces.IRootView;
import cz.poptavka.sample.client.root.interfaces.IRootView.IRootPresenter;

public class RootView extends ReverseCompositeView<IRootPresenter> implements
        IRootView {

    private static RootViewUiBinder uiBinder = GWT.create(RootViewUiBinder.class);
    @UiField
    SimplePanel header, body, menu, searchBar, footer;
    private PopupPanel wait = new PopupPanel();

    interface RootViewUiBinder extends UiBinder<Widget, RootView> {
    }

    public RootView() {

        // TODO praso - otestovat na online poptavke ci sa zobrazuje tato loading show/hide hlaska
        wait.add(new Label("Wait until requested module code is downloaded from server."));
        initWidget(uiBinder.createAndBindUi(this));
        /* Táto metóda, zaistí, že sa načíta CSS štýl. Bez nej by sa štýl nahral až pri prepnutí do
         * ďalšieho modulu.
         */
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
    public void setWaitVisible(boolean visible) {
        if (visible) {
            GWT.log("Show loading popup");
            wait.setPopupPosition(body.getAbsoluteLeft(), body.getAbsoluteTop());
            wait.setPixelSize(body.getOffsetWidth(), body.getOffsetHeight());
            wait.show();
        } else {
            GWT.log("Hide loading popup");
            wait.hide();
        }
    }

}
