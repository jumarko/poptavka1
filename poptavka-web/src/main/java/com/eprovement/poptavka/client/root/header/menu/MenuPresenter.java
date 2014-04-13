/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.root.header.menu;

import com.eprovement.poptavka.client.common.CommonAccessRoles;
import com.google.gwt.core.client.GWT;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.client.root.interfaces.HandleResizeEvent;
import com.eprovement.poptavka.client.root.interfaces.IMenuView;
import com.eprovement.poptavka.client.root.interfaces.IMenuView.IUserMenuPresenter;
import com.eprovement.poptavka.client.user.admin.interfaces.IAdminModule;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.mvp4g.client.SingleSplitter;

/**
 * Menu presenter provides handlers for menu buttons and correct menu layout
 * according to type of logged user.
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = MenuView.class, async = SingleSplitter.class)
public class MenuPresenter extends BasePresenter<IMenuView, RootEventBus>
        implements IUserMenuPresenter, HandleResizeEvent {

    /**
     * ***********************************************************************
     */
    /* General Module events                                                  */
    /**
     * ***********************************************************************
     */
    /**
     * Sets menu widget to page layout.
     */
    public void onStart() {
        eventBus.setMenu(view);
    }

    /**
     * ***********************************************************************
     */
    /* Bind methods.                                                          */
    /**
     * ***********************************************************************
     */
    /**
     * Bind menu buttons handlers.
     */
    @Override
    public void bind() {
        view.getHome().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToHomeWelcomeModule();
                view.hideMenu();
            }
        });
        view.getClient().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToClientDemandsModule(null, Constants.NONE);
                view.hideMenu();
            }
        });
        view.getSupplier().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                view.hideMenu();
                eventBus.goToSupplierDemandsModule(null, Constants.NONE);
            }
        });
        view.getInbox().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToMessagesModule(null, Constants.MESSAGES_INBOX);
                view.hideMenu();
            }
        });
        view.getAdministration().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToAdminModule(null, IAdminModule.AdminWidget.DASHBOARD);
                view.hideMenu();
            }
        });
        view.getDemands().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToHomeDemandsModule(null);
                view.hideMenu();
            }
        });
        view.getSuppliers().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToHomeSuppliersModule(null);
                view.hideMenu();
            }
        });
        view.getCreateDemand().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToCreateDemandModule();
                view.hideMenu();
            }
        });
        view.getCreateSupplier().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToCreateSupplierModule();
                view.hideMenu();
            }
        });
        view.getMenuOpenButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                view.setMenuPanelVisibility();
            }
        });

        view.getUserSettings().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.menuStyleChange(Constants.USER_SETTINGS_MODULE);
                eventBus.goToSettingsModule();
                view.hideMenu();
            }
        });

        view.getUserLogout().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.menuStyleChange(Constants.HOME_WELCOME_MODULE);
                eventBus.logout(Constants.HOME_WELCOME_MODULE);
                view.hideMenu();
            }
        });
    }

    /**
     * ***********************************************************************
     */
    /* Layout events                                                          */
    /**
     * ***********************************************************************
     */
    public void onResize(int actualWidth) {
        //set shorter dates on small screens
        if (actualWidth > 1200) {
            view.resetMenuVisbilityFlag();
        }
    }

    /**
     * Sets menu layout for unlogged user.
     */
    public void onAtHome() {
        view.getHome().setVisible(true);
        view.getClient().setVisible(false);
        view.getSupplier().setVisible(false);
        view.getDemands().setVisible(true);
        view.getSuppliers().setVisible(true);
        view.getCreateDemand().setVisible(true);
        view.getCreateSupplier().setVisible(true);
        view.getInbox().setVisible(false);
        view.getAdministration().setVisible(false);
        view.getUserMenuPanel().setVisible(false);
    }

    /**
     * Sets menu layout for logged user. Different layout according to user's
     * access and business role is used.
     */
    public void onAtAccount() {
        GWT.log("User menu view loaded");
        view.getHome().setVisible(false);
        view.getInbox().setVisible(true);
        view.getUserMenuPanel().setVisible(true);
        view.getUserLabel().setText(Storage.getUser().getEmail());
        //Tab visibility must be defined here, because unlike constructor in UserMenuView
        //this method is called each time user is logging in
        /* ADMIN TAB */
        if (Storage.getUser().getAccessRoles().contains(CommonAccessRoles.ADMIN)) {
            view.getClient().setVisible(false);
            view.getSupplier().setVisible(false);
            view.getDemands().setVisible(false);
            view.getSuppliers().setVisible(false);
            view.getCreateDemand().setVisible(false);
            view.getCreateSupplier().setVisible(false);
            view.getAdministration().setVisible(true);
            view.menuStyleChange(Constants.USER_ADMININSTRATION_MODULE);
        } else {
            /* SUPPLIER TAB */
            if (Storage.getBusinessUserDetail().getBusinessRoles().contains(
                    BusinessUserDetail.BusinessRole.SUPPLIER)) {
                view.getClient().setVisible(true);
                view.getSupplier().setVisible(true);
                view.getDemands().setVisible(true);
                view.getSuppliers().setVisible(true);
                view.getCreateDemand().setVisible(true);
                view.getCreateSupplier().setVisible(false);
                view.getAdministration().setVisible(false);
                view.menuStyleChange(Constants.USER_SUPPLIER_MODULE);
                view.setSupplierButtonVerticalNoLine(true);
                /* CLIENT TAB */
            } else if (Storage.getBusinessUserDetail().getBusinessRoles().contains(
                    BusinessUserDetail.BusinessRole.CLIENT)) {
                view.getClient().setVisible(true);
                view.getSupplier().setVisible(false);
                view.getDemands().setVisible(true);
                view.getSuppliers().setVisible(true);
                view.getCreateDemand().setVisible(true);
                view.getCreateSupplier().setVisible(true);
                view.getAdministration().setVisible(false);
                view.menuStyleChange(Constants.USER_CLIENT_MODULE);
                view.setSupplierButtonVerticalNoLine(false);
            }
        }
    }

    /**
     * ***********************************************************************
     */
    /* Business events                                                        */
    /**
     * ***********************************************************************
     */
    /**
     * Loads right styles to menu buttons.
     *
     * @param loadedModule - use module constants from class Contants.
     */
    public void onMenuStyleChange(int loadedModule) {
        view.menuStyleChange(loadedModule);
    }
}
