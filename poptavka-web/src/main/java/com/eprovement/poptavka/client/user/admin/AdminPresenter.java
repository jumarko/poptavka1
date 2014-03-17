/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.admin;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.admin.interfaces.IAdmin;
import com.eprovement.poptavka.client.user.admin.interfaces.IAdmin.AdminWidget;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.user.admin.tab.AdminModuleWelcomeView;
import com.eprovement.poptavka.client.user.admin.toolbar.AdminToolbarView;
import com.eprovement.poptavka.client.user.widget.LoadingDiv;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.history.NavigationEventCommand;
import com.mvp4g.client.presenter.LazyPresenter;

/**
 * Admin presenter.
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = AdminView.class)
public class AdminPresenter extends LazyPresenter<IAdmin.View, AdminEventBus> implements IAdmin.Presenter {

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
        view.getNewDemandsBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.closeSubMenu();
                commonSubMenuHandler(AdminWidget.NEW_DEMANDS);
            }
        });
        view.getAssignedDemandsBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.closeSubMenu();
                commonSubMenuHandler(AdminWidget.ASSIGNED_DEMANDS);
            }
        });
        view.getActiveDemandsBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.closeSubMenu();
                commonSubMenuHandler(AdminWidget.ACTIVE_DEMANDS);
            }
        });
//        view.getDemandsButton().addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                commonSubMenuHandler(Constants.ADMIN_DEMANDS);
//            }
//        });
//        view.getClientsButton().addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                commonSubMenuHandler(Constants.ADMIN_CLIENTS);
//            }
//        });
//        view.getSuppliersButton().addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                commonSubMenuHandler(Constants.ADMIN_SUPPLIERS);
//            }
//        });
//        view.getOffersButton().addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                commonSubMenuHandler(Constants.ADMIN_OFFERS);
//            }
//        });
//        view.getAccessRoleButton().addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                commonSubMenuHandler(Constants.ADMIN_ACCESS_ROLE);
//            }
//        });
//        view.getEmailActivationButton().addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                commonSubMenuHandler(Constants.ADMIN_EMAILS_ACTIVATION);
//            }
//        });
//        view.getInvoiceButton().addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                commonSubMenuHandler(Constants.ADMIN_INVOICES);
//            }
//        });
//        view.getMessageButton().addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                commonSubMenuHandler(Constants.ADMIN_MESSAGES);
//            }
//        });
//        view.getPaymentMethodButton().addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                commonSubMenuHandler(Constants.ADMIN_PAYMENT_METHODS);
//            }
//        });
//        view.getPermissionButton().addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                commonSubMenuHandler(Constants.ADMIN_PERMISSIONS);
//            }
//        });
//        view.getPreferenceButton().addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                commonSubMenuHandler(Constants.ADMIN_PREFERENCES);
//            }
//        });
//        view.getProblemButton().addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                commonSubMenuHandler(Constants.ADMIN_PROBLEMS);
//            }
//        });
    }

    /**
     * Sets common functionality for submenu.
     * @param widgetId of widget to be loaded
     */
    private void commonSubMenuHandler(AdminWidget widgetId) {
        eventBus.closeSubMenu();
        eventBus.goToAdminModule(null, widgetId);
        eventBus.toolbarRefresh();
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
        eventBus.setToolbarContent("Admin Menu", view.getToolbarContent());
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
    public void onGoToAdminModule(SearchModuleDataHolder filter, AdminWidget loadWidget) {
        GWT.log("onGoToAdminModule - som tu");

        view.getWidgetView().setStyleName(Storage.RSCS.common().user());
        view.setClientMenuActStyle(loadWidget);

        ((AdminToolbarView) view.getToolbarContent()).resetBasic();
        switch (loadWidget) {
            case NEW_DEMANDS:
                Storage.setCurrentlyLoadedView(Constants.ADMIN_NEW_DEMANDS);
                eventBus.initNewDemands(filter);
                break;
            case ASSIGNED_DEMANDS:
                Storage.setCurrentlyLoadedView(Constants.ADMIN_ASSIGEND_DEMANDS);
                eventBus.initAssignedDemands(filter);
                break;
            case ACTIVE_DEMANDS:
                Storage.setCurrentlyLoadedView(Constants.ADMIN_ACTIVE_DEMANDS);
                eventBus.initActiveDemands(filter);
                break;
            default: //welcome
                Storage.setCurrentlyLoadedView(Constants.NONE);
                ((AdminToolbarView) view.getToolbarContent()).resetFull();
                view.setContent(new AdminModuleWelcomeView());
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetClientMenuActStyle(AdminWidget widget) {
        view.setClientMenuActStyle(widget);
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
