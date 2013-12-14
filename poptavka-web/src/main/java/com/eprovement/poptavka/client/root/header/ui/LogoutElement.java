/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.root.header.ui;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.root.header.ui.LogoutElement.LogoutElementUiBinder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import com.github.gwtbootstrap.client.ui.base.IconAnchor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * Login element includes button for middle-large screens and icon anchor for tiny-small screens.
 * @author Martin Slakvovsky
 */
public class LogoutElement extends Composite {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static LogoutElementUiBinder uiBinder = GWT.create(LogoutElementUiBinder.class);

    interface LogoutElementUiBinder extends UiBinder<Widget, LogoutElement> {
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder Attributes. **/
    @UiField MenuItem username, menuLogOut, menuMyProfile;
    @UiField MyMenuItem logoutMenuItemBtn;
    @UiField MenuBar logoutMenuBarBtn;
    @UiField IconAnchor logoutAnchor;

    /**************************************************************************/
    /* Constructors                                                           */
    /**************************************************************************/
    /**
     * Creates logout element view's compotents.
     */
    public LogoutElement() {
        initWidget(uiBinder.createAndBindUi(this));
        bindHandlers();
    }

    /**************************************************************************/
    /* Bind events                                                            */
    /**************************************************************************/
    /**
     * Bind menu dropdown choices handlers.
     */
    private void bindHandlers() {
        logoutMenuBarBtn.addDomHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                logoutMenuItemBtn.setStyleName(Storage.RSCS.header().logoutButtonAct());
            }
        }, ClickEvent.getType());
        logoutMenuBarBtn.addCloseHandler(new CloseHandler<PopupPanel>() {
            @Override
            public void onClose(CloseEvent<PopupPanel> event) {
                logoutMenuItemBtn.setStyleName(Storage.RSCS.header().logoutButton());
            }
        });
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    public MenuItem getUsername() {
        return username;
    }

    public MenuItem getMenuLogOut() {
        return menuLogOut;
    }

    public MenuItem getMenuMyProfile() {
        return menuMyProfile;
    }

    public MyMenuItem getLogoutMenuItemBtn() {
        return logoutMenuItemBtn;
    }

    public MenuBar getLogoutMenuBarBtn() {
        return logoutMenuBarBtn;
    }

    public IconAnchor getLogoutAnchor() {
        return logoutAnchor;
    }
}