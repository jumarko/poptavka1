/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.root.header.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.root.interfaces.IMenuView;
import com.eprovement.poptavka.client.root.interfaces.IRootSelectors;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Menu view includes buttons for: home, client, supplier, demands,
 * createDemand, suppliers, createSupplier, inbox, administration.
 *
 * @author Martin Slavkovsky
 */
public class MenuView extends Composite implements IMenuView {

    private static UserMenuViewUiBinder uiBinder = GWT.create(UserMenuViewUiBinder.class);

    interface UserMenuViewUiBinder extends UiBinder<Widget, MenuView> {
    }
    /**
     * ***********************************************************************
     */
    /* Attributes                                                             */
    /**
     * ***********************************************************************
     */
    private IRootSelectors animation = GWT.create(IRootSelectors.class);
    private static final String SLIDE_PX = "280px";
    private static final int SLIDE_DURATION = 500;
    /**
     * UiBinder attributes. *
     */
    @UiField
    Button home, client, supplier, demands, createDemand, suppliers;
    @UiField
    Button createSupplier, inbox, administration, menuOpenButton;
    @UiField
    HTMLPanel menuPanel;
    private boolean isMenuPanelVisible = false;

    /**
     * ***********************************************************************
     */
    /* Initialization                                                         */
    /**
     * ***********************************************************************
     */
    /**
     * Creates menu view's compontents.
     */
    public MenuView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * ***********************************************************************
     */
    /* Getters                                                                */
    /**
     * ***********************************************************************
     */
    /**
     * @return the home button
     */
    @Override
    public Button getHome() {
        return home;
    }

    /**
     * @return the client button
     */
    @Override
    public Button getClient() {
        return client;
    }

    /**
     * @return the supplier button
     */
    @Override
    public Button getSupplier() {
        return supplier;
    }

    /**
     * @return the demands button
     */
    @Override
    public Button getDemands() {
        return demands;
    }

    /**
     * @return the create demands button
     */
    @Override
    public Button getCreateDemand() {
        return createDemand;
    }

    /**
     * @return the suppliers button
     */
    @Override
    public Button getSuppliers() {
        return suppliers;
    }

    /**
     * @return the create suppliers button
     */
    @Override
    public Button getCreateSupplier() {
        return createSupplier;
    }

    /**
     * @return the inbox/messages button
     */
    @Override
    public Button getInbox() {
        return inbox;
    }

    /**
     * @return the administration button
     */
    @Override
    public Button getAdministration() {
        return administration;
    }

    /**
     * @return menu panel
     */
    @Override
    public Button getMenuOpenButton() {
        return menuOpenButton;
    }

    /**
     * @return menu panel
     */
    public HTMLPanel getMenuPanel() {
        return menuPanel;
    }

    /**
     * @return isMenuPanelVisible
     */
    public boolean getIsMenuPanelVisible() {
        return isMenuPanelVisible;
    }

    /**
     * ***********************************************************************
     */
    /* Setters                                                                */
    /**
     * ***********************************************************************
     */
    @Override
    public void resetMenuVisbilityFlag() {
        isMenuPanelVisible = false;
    }

    /**
     * ***********************************************************************
     */
    /* Style change methods.                                                  */
    /**
     * ***********************************************************************
     */
    /**
     * Opens the hidden menu or hides the open menu.
     *
     */
    @Override
    public void setMenuPanelVisibility() {
        if (getIsMenuPanelVisible()) {
            hideMenu();
        } else {
            animation.getMenuPanel().animate("right: -" + SLIDE_PX, 0);
            animation.getMenuPanel().animate("right: +=" + SLIDE_PX, SLIDE_DURATION);
            isMenuPanelVisible = true;
        }
    }

    /**
     * Hides the open menu.
     *
     */
    @Override
    public void hideMenu() {
        animation.getMenuPanel().animate("right: -=" + SLIDE_PX, SLIDE_DURATION);
        isMenuPanelVisible = false;
    }

    /**
     * Loads right styles to menu buttons.
     *
     * @param loadedModule - use module constants from class Contants.
     */
    @Override
    public void menuStyleChange(int loadedModule) {
        switch (loadedModule) {
            case Constants.USER_CLIENT_MODULE:
                clientMenuStyleChange();
                break;
            case Constants.USER_SUPPLIER_MODULE:
                supplierMenuStyleChange();
                break;
            case Constants.USER_MESSAGES_MODULE:
                messagesMenuStyleChange();
                break;
            case Constants.HOME_DEMANDS_MODULE:
                demandsMenuStyleChange();
                break;
            case Constants.CREATE_DEMAND:
                createDemandMenuStyleChange();
                break;
            case Constants.HOME_SUPPLIERS_MODULE:
                suppliersMenuStyleChange();
                break;
            case Constants.CREATE_SUPPLIER:
                createSupplierMenuStyleChange();
                break;
            case Constants.USER_ADMININSTRATION_MODULE:
                administrationMenuStyleChange();
                break;
            default:
                //default style home
                homeMenuStyleChange();
                break;
        }
    }

    /**
     * ***********************************************************************
     */
    /* Helper methods.                                                        */
    /**
     * ***********************************************************************
     */
    /**
     * Sets <b>home</b> button active state style.
     */
    private void homeMenuStyleChange() {
        home.addStyleName(Constants.ACT);
        client.removeStyleName(Constants.ACT);
        supplier.removeStyleName(Constants.ACT);
        demands.removeStyleName(Constants.ACT);
        createDemand.removeStyleName(Constants.ACT);
        suppliers.removeStyleName(Constants.ACT);
        createSupplier.removeStyleName(Constants.ACT);
        inbox.removeStyleName(Constants.ACT);
        administration.removeStyleName(Constants.ACT);
    }

    /**
     * Sets <b>client</b> button active state style.
     */
    private void clientMenuStyleChange() {
        home.removeStyleName(Constants.ACT);
        client.addStyleName(Constants.ACT);
        supplier.removeStyleName(Constants.ACT);
        demands.removeStyleName(Constants.ACT);
        createDemand.removeStyleName(Constants.ACT);
        suppliers.removeStyleName(Constants.ACT);
        createSupplier.removeStyleName(Constants.ACT);
        inbox.removeStyleName(Constants.ACT);
        administration.removeStyleName(Constants.ACT);
    }

    /*
     * Sets <b>supplier</b> button active state style.
     */
    private void supplierMenuStyleChange() {
        home.removeStyleName(Constants.ACT);
        client.removeStyleName(Constants.ACT);
        supplier.addStyleName(Constants.ACT);
        demands.removeStyleName(Constants.ACT);
        createDemand.removeStyleName(Constants.ACT);
        suppliers.removeStyleName(Constants.ACT);
        createSupplier.removeStyleName(Constants.ACT);
        inbox.removeStyleName(Constants.ACT);
        administration.removeStyleName(Constants.ACT);
    }

    /*
     * Sets <b>messages</b> button active state style.
     */
    private void messagesMenuStyleChange() {
        home.removeStyleName(Constants.ACT);
        client.removeStyleName(Constants.ACT);
        supplier.removeStyleName(Constants.ACT);
        demands.removeStyleName(Constants.ACT);
        createDemand.removeStyleName(Constants.ACT);
        suppliers.removeStyleName(Constants.ACT);
        createSupplier.removeStyleName(Constants.ACT);
        inbox.addStyleName(Constants.ACT);
        administration.removeStyleName(Constants.ACT);
    }

    /*
     * Sets <b>demands</b> button active state style.
     */
    private void demandsMenuStyleChange() {
        home.removeStyleName(Constants.ACT);
        client.removeStyleName(Constants.ACT);
        supplier.removeStyleName(Constants.ACT);
        demands.addStyleName(Constants.ACT);
        createDemand.removeStyleName(Constants.ACT);
        suppliers.removeStyleName(Constants.ACT);
        createSupplier.removeStyleName(Constants.ACT);
        inbox.removeStyleName(Constants.ACT);
        administration.removeStyleName(Constants.ACT);
    }

    /*
     * Sets <b>create demand</b> button active state style.
     */
    private void createDemandMenuStyleChange() {
        home.removeStyleName(Constants.ACT);
        client.removeStyleName(Constants.ACT);
        supplier.removeStyleName(Constants.ACT);
        demands.removeStyleName(Constants.ACT);
        createDemand.addStyleName(Constants.ACT);
        suppliers.removeStyleName(Constants.ACT);
        createSupplier.removeStyleName(Constants.ACT);
        inbox.removeStyleName(Constants.ACT);
        administration.removeStyleName(Constants.ACT);
    }

    /*
     * Sets <b>supplier</b> button active state style.
     */
    private void suppliersMenuStyleChange() {
        home.removeStyleName(Constants.ACT);
        client.removeStyleName(Constants.ACT);
        supplier.removeStyleName(Constants.ACT);
        demands.removeStyleName(Constants.ACT);
        createDemand.removeStyleName(Constants.ACT);
        suppliers.addStyleName(Constants.ACT);
        createSupplier.removeStyleName(Constants.ACT);
        inbox.removeStyleName(Constants.ACT);
        administration.removeStyleName(Constants.ACT);
    }

    /*
     * Sets <b>create supplier</b> button active state style.
     */
    private void createSupplierMenuStyleChange() {
        home.removeStyleName(Constants.ACT);
        client.removeStyleName(Constants.ACT);
        supplier.removeStyleName(Constants.ACT);
        demands.removeStyleName(Constants.ACT);
        createDemand.removeStyleName(Constants.ACT);
        suppliers.removeStyleName(Constants.ACT);
        createSupplier.addStyleName(Constants.ACT);
        inbox.removeStyleName(Constants.ACT);
        administration.removeStyleName(Constants.ACT);
    }

    /*
     * Sets <b>administrator</b> button active state style.
     */
    private void administrationMenuStyleChange() {
        home.removeStyleName(Constants.ACT);
        client.removeStyleName(Constants.ACT);
        supplier.removeStyleName(Constants.ACT);
        demands.removeStyleName(Constants.ACT);
        createDemand.removeStyleName(Constants.ACT);
        suppliers.removeStyleName(Constants.ACT);
        createSupplier.removeStyleName(Constants.ACT);
        inbox.removeStyleName(Constants.ACT);
        administration.addStyleName(Constants.ACT);
    }

    /**
     * Sets supplier button vertical line style.
     *
     * @param noLine true if no line style is used, false otherwise
     */
    @Override
    public void setSupplierButtonVerticalNoLine(boolean noLine) {
        if (noLine) {
            suppliers.setStyleName("button8");
        } else {
            suppliers.setStyleName("button4");
        }
    }
}