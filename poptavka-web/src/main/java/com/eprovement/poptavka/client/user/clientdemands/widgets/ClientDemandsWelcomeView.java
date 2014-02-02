/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.github.gwtbootstrap.client.ui.FluidRow;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Client's Dasboard.
 *
 * @author Martin Slavkovsky
 */
public class ClientDemandsWelcomeView extends Composite
        implements ClientDemandsWelcomePresenter.ClientDemandsWelcomeViewInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static ClientDemandsWelcomeViewUiBinder uiBinder = GWT.create(ClientDemandsWelcomeViewUiBinder.class);

    interface ClientDemandsWelcomeViewUiBinder extends UiBinder<Widget, ClientDemandsWelcomeView> {
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    @UiField HTML myDemandsUnreadMessages, offeredDemandsUnreadMessages;
    @UiField HTML assignedDemandsUnreadMessages, closedDemandsUnreadMessages;
    @UiField FluidRow myDemandsRow, offeredDemandsRow, assignedDemandsRow, closedDemandsRow;
    @UiField SimplePanel footerContainer;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates ClientDemandsWelcome view's compontents.
     */
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /** FLUIDROW. **/
    /**
     * @return the my demands html
     */
    @Override
    public HTML getMyDemandsUnreadMessages() {
        return myDemandsUnreadMessages;
    }

    /**
     * @return the offered demands html
     */
    @Override
    public HTML getOfferedDemandsUnreadMessages() {
        return offeredDemandsUnreadMessages;
    }

    /**
     * @return the assigned demands html
     */
    @Override
    public HTML getAssignedDemandsUnreadMessages() {
        return assignedDemandsUnreadMessages;
    }

    /**
     * @return the closed demands html
     */
    @Override
    public HTML getClosedDemandsUnreadMessages() {
        return closedDemandsUnreadMessages;
    }

    /** FLUIDROW. **/
    /**
     * @return the my demands row
     */
    @Override
    public FluidRow getMyDemandsRow() {
        return myDemandsRow;
    }

    /**
     * @return the offered demands row
     */
    @Override
    public FluidRow getOfferedDemandsRow() {
        return offeredDemandsRow;
    }

    /**
     * @return the assigened demands row
     */
    @Override
    public FluidRow getAssignedDemandsRow() {
        return assignedDemandsRow;
    }

    /**
     * @return the closed demands row
     */
    @Override
    public FluidRow getClosedDemandsRow() {
        return closedDemandsRow;
    }

    /**
     * @return footer container
     */
    @Override
    public SimplePanel getFooterContainer() {
        return footerContainer;
    }

    /** OTHERS. **/
    /**
     * @return the widget view
     */
    @Override
    public Widget getWidgetView() {
        return this;
    }
}