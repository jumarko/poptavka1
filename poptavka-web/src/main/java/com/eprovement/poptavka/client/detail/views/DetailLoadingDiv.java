/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.detail.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;

/**
 * Loading widget displays loading indicator on parent panel.
 * @author Martin Slavkovsky
 */
public class DetailLoadingDiv extends UIObject {

    /**************************************************************************/
    /* View interface                                                         */
    /**************************************************************************/
    private static LoadingDivUiBinder uiBinder = GWT.create(LoadingDivUiBinder.class);

    interface LoadingDivUiBinder extends UiBinder<Element, DetailLoadingDiv> {
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    @UiField DivElement loading;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates loading widget view's compontents.
     */
    public DetailLoadingDiv() {
        setElement(uiBinder.createAndBindUi(this));
    }
}