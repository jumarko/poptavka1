/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.admin;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.root.toolbar.ProvidesToolbar;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.user.admin.tab.AdminModuleWelcomeView;
import com.eprovement.poptavka.client.user.admin.toolbar.AdminToolbarView;
import com.eprovement.poptavka.client.user.widget.LoadingDiv;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.history.NavigationConfirmationInterface;
import com.mvp4g.client.history.NavigationEventCommand;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

/**
 * Admin presenter.
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = AdminView.class)
public class AdminPresenter
    extends LazyPresenter<AdminPresenter.AdminModuleInterface, AdminEventBus>
    implements NavigationConfirmationInterface {

    /**************************************************************************/
    /* View interface                                                         */
    /**************************************************************************/
    public interface AdminModuleInterface extends LazyView, IsWidget, ProvidesToolbar {

        void setContent(Widget contentWidget);

        Button getDemandsButton();

        Button getActiveDemandsBtn();

        Button getClientsButton();

        Button getOffersButton();

        Button getSuppliersButton();

        Button getAccessRoleButton();

        Button getEmailActivationButton();

        Button getInvoiceButton();

        Button getMessageButton();

        Button getNewDemandsBtn();

        Button getPaymentMethodButton();

        Button getPermissionButton();

        Button getPreferenceButton();

        Button getProblemButton();

        SimplePanel getContentContainer();

        Widget getWidgetView();
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private LoadingDiv loading;

    /**************************************************************************/
    /* Bind handlers                                                          */
    /**************************************************************************/
    /**
     * Binds menu buttons handlers
     */
    @Override
    public void bindView() {
        view.getActiveDemandsBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.closeSubMenu();
                eventBus.goToAdminModule(null, Constants.ADMIN_ACTIVE_DEMANDS);
            }
        });
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
        view.getNewDemandsBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.closeSubMenu();
                eventBus.goToAdminModule(null, Constants.ADMIN_NEW_DEMANDS);
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
        // nothing by default
    }

    /**
     * Every call of onForward method invokes updateUnreadMessagesCount event that is secured thus user without
     * particular access role can't access it and loginPopupView will be displayed.
     * Sets body, toolbar
     */
    public void onForward() {
        //Must be set before any widget start initialize because of autoDisplay feature
        if (!(Storage.getUser() == null && Storage.isAppCalledByURL() != null && Storage.isAppCalledByURL())) {
            eventBus.updateUnreadMessagesCount();
        }
        eventBus.setBody(view.getWidgetView());
        //TODO Martin - add i18n string
        eventBus.setToolbarContent("Admin Menu", view.getToolbarContent(), true);
        eventBus.menuStyleChange(Constants.USER_ADMININSTRATION_MODULE);
    }

    @Override
    public void confirm(NavigationEventCommand event) {
        // nothing by default
    }

    /**************************************************************************/
    /* Navigation events.                                                     */
    /**************************************************************************/
    /**
     * Creates admin module.
     * @param filter - search criteria
     * @param loadWidget - admin widget id
     */
    public void onGoToAdminModule(SearchModuleDataHolder filter, int loadWidget) {
        GWT.log("onGoToAdminModule - som tu");

        eventBus.loadingShow(Storage.MSGS.progressAdminLayoutInit());
        view.getWidgetView().setStyleName(Storage.RSCS.common().user());

        eventBus.loadingHide();
        ((AdminToolbarView) view.getToolbarContent()).resetBasic();
        switch (loadWidget) {
            case Constants.ADMIN_ACCESS_ROLE:
                eventBus.initAccessRoles(filter);
                break;
            case Constants.ADMIN_ACTIVE_DEMANDS:
                initActiveDemands(filter);
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
            case Constants.ADMIN_NEW_DEMANDS:
                initNewDemands(filter);
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
                ((AdminToolbarView) view.getToolbarContent()).resetFull();
                view.setContent(new AdminModuleWelcomeView());
                break;
        }
    }

    /**************************************************************************/
    /* Business events handled by presenter                                   */
    /**************************************************************************/
    /**
     * Toogle body loading.
     */
    public void onToggleLoading() {
        if (loading == null) {
            GWT.log("  - loading created");
            loading = new LoadingDiv(view.getContentContainer().getParent());
        } else {
            GWT.log("  - loading removed");
            loading.getElement().removeFromParent();
            loading = null;
        }
    }

    /**
     * Displays body.
     * @param content widget
     */
    public void onDisplayView(Widget content) {
        view.setContent(content);
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Inits active demands widget.
     * @param filter - search criteria
     */
    private void initActiveDemands(SearchModuleDataHolder filter) {
        eventBus.initActiveDemands(filter);
    }

    /**
     * Inits new demands widget.
     * @param filter - search criteria
     */
    private void initNewDemands(SearchModuleDataHolder filter) {
        eventBus.initNewDemands(filter);
    }
}