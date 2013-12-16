/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.root.header.ui;

import com.eprovement.poptavka.client.root.header.ui.LoginElement.LoginElementUiBinder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import com.github.gwtbootstrap.client.ui.base.IconAnchor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;

/**
 * Login element includes button for middle-large screens and icon anchor for tiny-small screens.
 * @author Martin Slavkovsky
 */
public class LoginElement extends Composite {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static LoginElementUiBinder uiBinder = GWT.create(LoginElementUiBinder.class);

    interface LoginElementUiBinder extends UiBinder<Widget, LoginElement> {
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder Attributes. **/
    @UiField Anchor loginButton;
    @UiField IconAnchor loginAnchor;

    /**************************************************************************/
    /* Constructors                                                           */
    /**************************************************************************/
    /**
     * Creates login elements view's components.
     */
    public LoginElement() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    public Anchor getLoginButton() {
        return loginButton;
    }

    public IconAnchor getLoginAnchor() {
        return loginAnchor;
    }
}