/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.root.unsubscribe;

import com.eprovement.poptavka.client.common.session.CssInjector;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.client.root.interfaces.IUnsubscribe;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * @author Martin Slavkovsky
 * @since 9.4.2014
 */
public class UnsubscribeView extends Composite implements IUnsubscribe.View {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static UnsubscribeViewUiBinder uiBinder = GWT.create(UnsubscribeViewUiBinder.class);

    interface UnsubscribeViewUiBinder extends UiBinder<Widget, UnsubscribeView> {
    }

    /**************************************************************************/
    /* CSS                                                                    */
    /**************************************************************************/
    static {
        CssInjector.INSTANCE.ensureCommonStylesInjected();
        CssInjector.INSTANCE.ensureCreateTabPanelStylesInjected();
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField Button cancelBtn, unsubscribeBtn;
    @UiField SimplePanel footerContainer;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Initialize UnsubscibeView.
     */
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    @Override
    public Button getCancel() {
        return cancelBtn;
    }

    @Override
    public Button getUnsubscribe() {
        return unsubscribeBtn;
    }

    @Override
    public SimplePanel getFooterContainer() {
        return footerContainer;
    }
}
