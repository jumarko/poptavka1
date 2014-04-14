/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.root.header;

import com.eprovement.poptavka.client.common.session.CssInjector;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;

import com.eprovement.poptavka.client.root.header.ui.LoginElement;
import com.eprovement.poptavka.client.root.header.ui.LogoutElement;
import com.eprovement.poptavka.client.root.header.ui.NotificationsElement;
import com.eprovement.poptavka.client.root.header.ui.SearchElement;
import com.eprovement.poptavka.client.root.interfaces.IHeaderView;
import com.github.gwtbootstrap.client.ui.base.IconAnchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Header view involves: Logo, Menu bar, Search bar, Header Toolbar (settings, notifications, login, logout).
 * Some elements, especially buttons has two states according to screen size.
 *
 * @author Ivan Vlcek, Martin Slavkovsky
 * @since 16.11.2013
 */
public class HeaderView extends Composite implements IHeaderView {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static HeaderViewUiBinder uiBinder = GWT.create(HeaderViewUiBinder.class);

    interface HeaderViewUiBinder extends UiBinder<Widget, HeaderView> {
    }

    /**************************************************************************/
    /* CSS                                                                    */
    /**************************************************************************/
    static {
        CssInjector.INSTANCE.ensureHeaderStylesInjected();
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder Attributes. **/
    @UiField Button logo;
    @UiField SimplePanel menu;
    @UiField SearchElement search;
    @UiField NotificationsElement notifications;
    @UiField LoginElement login;
    @UiField LogoutElement logout;
    @UiField IconAnchor settingsAnchor;

    /**************************************************************************/
    /* Constructors                                                           */
    /**************************************************************************/
    /**
     * Creates header view's components.
     */
    public HeaderView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /**
     * @return the logo button
     */
    @Override
    public Button getLogo() {
        return logo;
    }

    /**
     * @return the menu element
     */
    @Override
    public SimplePanel getMenu() {
        return menu;
    }

    /**
     * @return the search element
     */
    @Override
    public SearchElement getSearch() {
        return search;
    }

    /**
     * @return the nofication element
     */
    @Override
    public NotificationsElement getNotifications() {
        return notifications;
    }

    /**
     * @return the login element
     */
    @Override
    public LoginElement getLogin() {
        return login;
    }

    /**
     * @return the logout element
     */
    @Override
    public LogoutElement getLogout() {
        return logout;
    }

    /**
     * @return the settings icon anchor
     */
    @Override
    public IconAnchor getSettingsAnchor() {
        return settingsAnchor;
    }
}