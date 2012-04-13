package cz.poptavka.sample.client.root;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.main.Constants;
import cz.poptavka.sample.client.main.common.search.SearchModuleView;
import cz.poptavka.sample.client.main.common.search.views.AdminAccessRolesViewView;
import cz.poptavka.sample.client.main.common.search.views.AdminClientsViewView;
import cz.poptavka.sample.client.main.common.search.views.AdminDemandsViewView;
import cz.poptavka.sample.client.main.common.search.views.AdminEmailActivationViewView;
import cz.poptavka.sample.client.main.common.search.views.AdminInvoicesViewView;
import cz.poptavka.sample.client.main.common.search.views.AdminMessagesViewView;
import cz.poptavka.sample.client.main.common.search.views.AdminOffersViewView;
import cz.poptavka.sample.client.main.common.search.views.AdminPaymentMethodsViewView;
import cz.poptavka.sample.client.main.common.search.views.AdminPermissionsViewView;
import cz.poptavka.sample.client.main.common.search.views.AdminPreferencesViewView;
import cz.poptavka.sample.client.main.common.search.views.AdminProblemsViewView;
import cz.poptavka.sample.client.main.common.search.views.AdminSuppliersViewView;
import cz.poptavka.sample.client.main.common.search.views.HomeDemandViewView;
import cz.poptavka.sample.client.main.common.search.views.HomeSuppliersViewView;
import cz.poptavka.sample.client.main.common.search.views.MessagesTabViewView;
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
        /* Tato metoda, zaisti, ze sa nacíta CSS styl. Bez nej by sa styl nahral az pri prepnuti do
         * dalsieho modulu.
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

    /**
     * According to currently loaded widget methods loads appropiate advance search view
     * to popup window and set search bar enables (categories, localities, advance button).
     * @param loadedWidget
     */
    @Override
    public void setUpSearchBar(int loadedWidget) {
        SearchModuleView searchBarView = (SearchModuleView) searchBar.getWidget();
        if (searchBarView != null) {
            switch (loadedWidget) {
                case Constants.HOME_DEMANDS:
                    searchBarView.getPopupPanel().setWidget(new HomeDemandViewView());
                    searchBarView.setSearchBarEnables(true, true, true);
                    break;
                case Constants.HOME_SUPPLIERS:
                    searchBarView.getPopupPanel().setWidget(new HomeSuppliersViewView());
                    searchBarView.setSearchBarEnables(false, true, true);
                    break;
                case Constants.DEMANDS_CLIENT_MY_DEMANDS:
                    break;
                case Constants.DEMANDS_CLIENT_OFFERS:
                    break;
                case Constants.DEMANDS_CLIENT_ASSIGNED_DEMANDS:
                    break;
                case Constants.DEMANDS_SUPPLIER_MY_DEMANDS:
                    break;
                case Constants.DEMANDS_SUPPLIER_OFFERS:
                    break;
                case Constants.DEMANDS_SUPPLIER_ASSIGNED_DEMANDS:
                    break;
                case Constants.MESSAGES_INBOX:
                    searchBarView.getPopupPanel().setWidget(new MessagesTabViewView());
                    searchBarView.setSearchBarEnables(false, false, true);
                    break;
                case Constants.MESSAGES_SENT:
                    searchBarView.getPopupPanel().setWidget(new MessagesTabViewView());
                    searchBarView.setSearchBarEnables(false, false, true);
                    break;
                case Constants.MESSAGES_TRASH:
                    searchBarView.getPopupPanel().setWidget(new MessagesTabViewView());
                    searchBarView.setSearchBarEnables(false, false, true);
                    break;
                case Constants.ADMIN_ACCESS_ROLE:
                    searchBarView.getPopupPanel().setWidget(new AdminAccessRolesViewView());
                    searchBarView.setSearchBarEnables(false, false, true);
                    break;
                case Constants.ADMIN_CLIENTS:
                    searchBarView.getPopupPanel().setWidget(new AdminClientsViewView());
                    searchBarView.setSearchBarEnables(false, false, true);
                    break;
                case Constants.ADMIN_DEMANDS:
                    searchBarView.getPopupPanel().setWidget(new AdminDemandsViewView());
                    searchBarView.setSearchBarEnables(true, true, true);
                    break;
                case Constants.ADMIN_EMAILS_ACTIVATION:
                    searchBarView.getPopupPanel().setWidget(new AdminEmailActivationViewView());
                    searchBarView.setSearchBarEnables(false, false, true);
                    break;
                case Constants.ADMIN_INVOICES:
                    searchBarView.getPopupPanel().setWidget(new AdminInvoicesViewView());
                    searchBarView.setSearchBarEnables(false, false, true);
                    break;
                case Constants.ADMIN_MESSAGES:
                    searchBarView.getPopupPanel().setWidget(new AdminMessagesViewView());
                    searchBarView.setSearchBarEnables(false, false, true);
                    break;
                case Constants.ADMIN_OFFERS:
                    searchBarView.getPopupPanel().setWidget(new AdminOffersViewView());
                    searchBarView.setSearchBarEnables(false, false, true);
                    break;
                case Constants.ADMIN_PAYMENT_METHODS:
                    searchBarView.getPopupPanel().setWidget(new AdminPaymentMethodsViewView());
                    searchBarView.setSearchBarEnables(false, false, true);
                    break;
                case Constants.ADMIN_PERMISSIONS:
                    searchBarView.getPopupPanel().setWidget(new AdminPermissionsViewView());
                    searchBarView.setSearchBarEnables(false, false, true);
                    break;
                case Constants.ADMIN_PREFERENCES:
                    searchBarView.getPopupPanel().setWidget(new AdminPreferencesViewView());
                    searchBarView.setSearchBarEnables(false, false, true);
                    break;
                case Constants.ADMIN_PROBLEMS:
                    searchBarView.getPopupPanel().setWidget(new AdminProblemsViewView());
                    searchBarView.setSearchBarEnables(false, false, true);
                    break;
                case Constants.ADMIN_SUPPLIERS:
                    searchBarView.getPopupPanel().setWidget(new AdminSuppliersViewView());
                    searchBarView.setSearchBarEnables(true, true, true);
                    break;
                default:
                    searchBarView.getPopupPanel().clear();
                    searchBarView.setSearchBarEnables(false, false, false);
                    break;
            }
        }
    }
}
