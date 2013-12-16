/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.root.header.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import com.github.gwtbootstrap.client.ui.base.IconAnchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Menu element includes hideable menu widget for all screens and icon anchor for tiny-small screens.
 * @author Martin Slavkovsky
 */
public class MenuElement extends Composite {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static MenuElementUiBinder uiBinder = GWT.create(MenuElementUiBinder.class);

    interface MenuElementUiBinder extends UiBinder<Widget, MenuElement> {
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder Attributes. **/
    @UiField SimplePanel menuPanel;
    @UiField IconAnchor menuAnchor;

    /**************************************************************************/
    /* Constructors                                                           */
    /**************************************************************************/
    /**
     * Creates menu element view's components.
     */
    public MenuElement() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    public SimplePanel getMenuPanel() {
        return menuPanel;

    }

    public IconAnchor getMenuAnchor() {
        return menuAnchor;
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    public void setMenu(IsWidget menu) {
        menuPanel.setWidget(menu);

    }
}