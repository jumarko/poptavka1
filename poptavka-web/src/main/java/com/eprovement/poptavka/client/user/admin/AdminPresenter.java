package com.eprovement.poptavka.client.user.admin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;

import com.mvp4g.client.presenter.LazyPresenter;

import com.mvp4g.client.view.LazyView;
import com.eprovement.poptavka.client.main.Constants;
import com.eprovement.poptavka.client.main.Storage;
import com.eprovement.poptavka.client.main.common.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.user.admin.tab.AdminModuleWelcomeView;
import com.eprovement.poptavka.client.user.widget.LoadingDiv;

@Presenter(view = AdminView.class)//, multiple = true)
public class AdminPresenter
        extends LazyPresenter<AdminPresenter.AdminModuleInterface, AdminEventBus> {

    public interface AdminModuleInterface extends LazyView {

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

    @Override
    public void bindView() {
        view.getDemandsButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToAdminModule(null, Constants.ADMIN_DEMANDS);
            }
        });
        view.getClientsButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToAdminModule(null, Constants.ADMIN_CLIENTS);
            }
        });
        view.getSuppliersButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToAdminModule(null, Constants.ADMIN_SUPPLIERS);
            }
        });
        view.getOffersButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToAdminModule(null, Constants.ADMIN_OFFERS);
            }
        });
        view.getAccessRoleButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToAdminModule(null, Constants.ADMIN_ACCESS_ROLE);
            }
        });
        view.getEmailActivationButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToAdminModule(null, Constants.ADMIN_EMAILS_ACTIVATION);
            }
        });
        view.getInvoiceButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToAdminModule(null, Constants.ADMIN_INVOICES);
            }
        });
        view.getMessageButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToAdminModule(null, Constants.ADMIN_MESSAGES);
            }
        });
        view.getPaymentMethodButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToAdminModule(null, Constants.ADMIN_PAYMENT_METHODS);
            }
        });
        view.getPermissionButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToAdminModule(null, Constants.ADMIN_PERMISSIONS);
            }
        });
        view.getPreferenceButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToAdminModule(null, Constants.ADMIN_PREFERENCES);
            }
        });
        view.getProblemButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToAdminModule(null, Constants.ADMIN_PROBLEMS);
            }
        });
    }

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        // nothing
    }

    public void onForward() {
        // nothing
    }

    /**************************************************************************/
    /* Navigation events.                                                     */
    /**************************************************************************/
    public void onGoToAdminModule(SearchModuleDataHolder filter, int loadWidget) {
        GWT.log("onGoToAdminModule - som tu");

        Storage.showLoading(Storage.MSGS.progressAdminLayoutInit());
        view.getWidgetView().setStyleName(Storage.RSCS.common().user());

        Storage.hideLoading();
        switch (loadWidget) {
            case Constants.ADMIN_ACCESS_ROLE:
                eventBus.initAccessRoles(filter);
                break;
            case Constants.ADMIN_CLIENTS:
                eventBus.initClients(filter);
                break;
            case Constants.ADMIN_DEMANDS:
                eventBus.initDemands(filter);
                break;
            case Constants.ADMIN_EMAILS_ACTIVATION:
                eventBus.initEmailsActivation(filter);
                break;
            case Constants.ADMIN_INVOICES:
                eventBus.initInvoices(filter);
                break;
            case Constants.ADMIN_MESSAGES:
                eventBus.initMessages(filter);
                break;
            case Constants.ADMIN_OFFERS:
                eventBus.initOffers(filter);
                break;
            case Constants.ADMIN_PAYMENT_METHODS:
                eventBus.initPaymentMethods(filter);
                break;
            case Constants.ADMIN_PERMISSIONS:
                eventBus.initPermissions(filter);
                break;
            case Constants.ADMIN_PREFERENCES:
                eventBus.initPreferences(filter);
                break;
            case Constants.ADMIN_PROBLEMS:
                eventBus.initProblems(filter);
                break;
            case Constants.ADMIN_SUPPLIERS:
                eventBus.initSuppliers(filter);
                break;
            default: //welcome
                Storage.setCurrentlyLoadedView(Constants.NONE);
                view.setContent(new AdminModuleWelcomeView());
                break;
        }
    }

    /**************************************************************************/
    /* Business events handled by presenter                                   */
    /**************************************************************************/
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