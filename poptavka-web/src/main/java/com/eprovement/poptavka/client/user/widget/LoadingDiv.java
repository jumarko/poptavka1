/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

/**
 * Creates loading indicator on given parent panel.
 *
 * @author Martin Slavkovsky
 */
public class LoadingDiv extends UIObject {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static LoadingDivUiBinder uiBinder = GWT.create(LoadingDivUiBinder.class);

    interface LoadingDivUiBinder extends UiBinder<Element, LoadingDiv> {
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    @UiField DivElement loading;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates Loading div.
     */
    public LoadingDiv() {
        setElement(uiBinder.createAndBindUi(this));
    }

    /**
     * Creates Loading div in give panel.
     * @param holderWidget where loading div will be added
     */
    public LoadingDiv(Widget holderWidget) {
        setElement(uiBinder.createAndBindUi(this));
        holderWidget.getElement().appendChild(this.getElement());
    }

}
