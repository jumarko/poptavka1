/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.root;

import com.eprovement.poptavka.client.common.session.CssInjector;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.client.root.interfaces.IRoot;
import com.eprovement.poptavka.resources.StyleResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Represents fundamental page separation to containers: Header, Toolbar, Body.
 * In order to have footer scrollable with body's content, footer is injected in each
 * body's content widget.
 * <b><i>Note:</i></b>
 * Vies should not holds business logic. But they can have independent UI logic.
 * They serves only as presentation layout to provide access for presenter to UI elements.
 *
 * @author Beho, Martin Slavkovsky
 */
public class RootView extends Composite implements IRoot.View {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static RootViewUiBinder uiBinder = GWT.create(RootViewUiBinder.class);

    interface RootViewUiBinder extends UiBinder<Widget, RootView> {
    }

    /**************************************************************************/
    /* CSS                                                                    */
    /**************************************************************************/
    /**
     * Inject all recquired styles for this view.
     * <b><i>Note:</i></b>
     * No need to use static definition.
     * This have one advantage and that is more nicer code.
     * Take it as onStart but for Views.
     */
    static {
        StyleResource.INSTANCE.initialStandartStyles().ensureInjected();
        CssInjector.INSTANCE.ensureInitialStylesInjected();
        CssInjector.INSTANCE.ensureLayoutStylesInjected();
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField ResizeLayoutPanel page;
    @UiField SimplePanel header, toolbar, body;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Initialize RootView.
     */
    public RootView() {
        initWidget(uiBinder.createAndBindUi(this));
        //ResizeLayoutPanel uses strange styles, that interfere with ours. Therefore remove them.
        body.getElement().removeAttribute("style");
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    @Override
    public SimplePanel getHeader() {
        return header;
    }

    @Override
    public SimplePanel getToolbar() {
        return toolbar;
    }

    @Override
    public SimplePanel getBody() {
        return body;
    }

    /**
     * Gets body resizable container.
     * @return body's resizable layout container
     */
    @Override
    public ResizeLayoutPanel getPage() {
        return page;
    }
}