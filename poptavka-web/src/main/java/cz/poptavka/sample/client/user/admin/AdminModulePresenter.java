package cz.poptavka.sample.client.user.admin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.user.admin.tab.AdminModuleWelcomeView;
import cz.poptavka.sample.client.user.widget.LoadingDiv;

@Presenter(view = AdminModuleView.class, multiple = true)
public class AdminModulePresenter
        extends BasePresenter<AdminModulePresenter.AdminModuleInterface, AdminModuleEventBus> {

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    public interface AdminModuleInterface {

        Widget getWidgetView();

        void setContent(Widget contentWidget);

        Button getDemandsButton();

        Button getClientsButton();

        Button getOffersButton();

        Button getSuppliersButton();

        Button getAccessRoleButton();

        Button getEmailActivationButton();

        Button getInvoiceButton();

        Button getMessageButton();

        Button getPaymentMethodButton();

        Button getPermissionButton();

        Button getPreferenceButton();

        Button getProblemButton();

        SimplePanel getContentPanel();
    }
    private LoadingDiv loading = null;

    public void bind() {
        view.getDemandsButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initAdminModule(null, "initDemands");
            }
        });
        view.getClientsButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initAdminModule(null, "initClients");
//                eventBus.initClients(null);
            }
        });
        view.getSuppliersButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initAdminModule(null, "initSuppliers");
//                eventBus.initSuppliers(null);
            }
        });
        view.getOffersButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initAdminModule(null, "initOffers");
//                eventBus.initOffers(null);
            }
        });
        view.getAccessRoleButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initAdminModule(null, "initAccessRoles");
//                eventBus.initAccessRoles(null);
            }
        });
        view.getEmailActivationButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initAdminModule(null, "initEmailsActivation");
//                eventBus.initEmailsActivation(null);
            }
        });
        view.getInvoiceButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initAdminModule(null, "initInvoices");
//                eventBus.initInvoices(null);
            }
        });
        view.getMessageButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initAdminModule(null, "initMessages");
//                eventBus.initMessages(null);
            }
        });
        view.getPaymentMethodButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initAdminModule(null, "initPaymentMethods");
//                eventBus.initPaymentMethods(null);
            }
        });
        view.getPermissionButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initAdminModule(null, "initPermissions");
//                eventBus.initPermissions(null);
            }
        });
        view.getPreferenceButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initAdminModule(null, "initPreferences");
//                eventBus.initPreferences(null);
            }
        });
        view.getProblemButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initAdminModule(null, "initProblems");
//                eventBus.initProblems(null);
            }
        });
    }

    public void onInitAdminModule(SearchModuleDataHolder filter, String loadWidget) {
        Storage.setCurrentlyLoadedModule("admin");
        GWT.log("onInitAdminModule - som tu");

        Storage.showLoading(Storage.MSGS.progressAdminLayoutInit());
        view.getWidgetView().setStyleName(Storage.RSCS.common().user());

        eventBus.setHomeBodyHolderWidget(view.getWidgetView());

        Storage.hideLoading();

        if (loadWidget.equals("initAccessRoles")) {
            eventBus.initAccessRoles(filter);
        } else if (loadWidget.equals("initClients")) {
            eventBus.initClients(filter);
        } else if (loadWidget.equals("initDemands")) {
            eventBus.initDemands(filter);
        } else if (loadWidget.equals("initEmailsActivation")) {
            eventBus.initEmailsActivation(filter);
        } else if (loadWidget.equals("initInvoices")) {
            eventBus.initInvoices(filter);
        } else if (loadWidget.equals("initMessages")) {
            eventBus.initMessages(filter);
        } else if (loadWidget.equals("initOffers")) {
            eventBus.initOffers(filter);
        } else if (loadWidget.equals("initPaymentMethods")) {
            eventBus.initPaymentMethods(filter);
        } else if (loadWidget.equals("initPermissions")) {
            eventBus.initPermissions(filter);
        } else if (loadWidget.equals("initPreferences")) {
            eventBus.initPreferences(filter);
        } else if (loadWidget.equals("initProblems")) {
            eventBus.initProblems(filter);
        } else if (loadWidget.equals("initSuppliers")) {
            eventBus.initSuppliers(filter);
        } else { //welcome
            view.setContent(new AdminModuleWelcomeView());
        }
    }

    public void onToggleLoading() {
        if (loading == null) {
            GWT.log("  - loading created");
            loading = new LoadingDiv(view.getContentPanel().getParent());
        } else {
            GWT.log("  - loading removed");
            loading.getElement().removeFromParent();
            loading = null;
        }
    }

    public void onDisplayView(Widget content) {
        view.setContent(content);
    }
}