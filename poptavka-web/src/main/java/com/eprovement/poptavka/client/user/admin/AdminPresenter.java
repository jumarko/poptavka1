/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.admin;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.admin.interfaces.IAdminModule;
import com.eprovement.poptavka.client.user.admin.interfaces.IAdminModule.AdminWidget;
import com.eprovement.poptavka.client.user.admin.toolbar.AdminModuleWelcomeView;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.user.admin.toolbar.AdminToolbarView;
import com.eprovement.poptavka.client.user.widget.LoadingDiv;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.history.NavigationEventCommand;
import com.mvp4g.client.presenter.LazyPresenter;

/**
 * Admin presenter.
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = AdminView.class)
public class AdminPresenter extends LazyPresenter<IAdminModule.View, AdminEventBus> implements IAdminModule.Presenter {

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
        view.getClientsBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.closeSubMenu();
                commonSubMenuHandler(AdminWidget.CLIENTS);
            }
        });
        view.getSystemBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.closeSubMenu();
                commonSubMenuHandler(AdminWidget.SYSTEM);
            }
        });
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
        eventBus.setBody(view);
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

        view.asWidget().setStyleName(Storage.RSCS.common().user());
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
            case CLIENTS:
                Storage.setCurrentlyLoadedView(Constants.ADMIN_CLIENTS);
                eventBus.initClients(filter);
                break;
            case SYSTEM:
                Storage.setCurrentlyLoadedView(Constants.ADMIN_SUPPLIERS);
                eventBus.initAdminSystemSettings();
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
    public void onDisplayView(IsWidget content) {
        view.setContent(content);
    }
}
