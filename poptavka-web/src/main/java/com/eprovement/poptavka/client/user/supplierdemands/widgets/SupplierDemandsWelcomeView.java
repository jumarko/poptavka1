/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.supplierdemands.widgets;

import com.github.gwtbootstrap.client.ui.FluidRow;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * View consists of forms displaying recent news and activity.
 *
 * @author Martin Slavkovsky
 */
public class SupplierDemandsWelcomeView extends Composite
        implements SupplierDemandsWelcomePresenter.SupplierDemandsWelcomeViewInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static SupplierDemandsWelcomeViewUiBinder uiBinder = GWT.create(SupplierDemandsWelcomeViewUiBinder.class);

    interface SupplierDemandsWelcomeViewUiBinder extends UiBinder<Widget, SupplierDemandsWelcomeView> {
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    @UiField HTML potentialDemandsUnreadMessages, offeredDemandsUnreadMessages;
    @UiField HTML assignedDemandsUnreadMessages, closedDemandsUnreadMessages;
    @UiField FluidRow potentialDemandsRow, offeredDemandsRow, assignedDemandsRow, closedDemandsRow;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates SupplierDemandsWelcome view's compontents.
     */
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /** HTML. **/
    /**
     * @return the potential demands unread messages HTML
     */
    @Override
    public HTML getPotentialDemandsUnreadMessages() {
        return potentialDemandsUnreadMessages;
    }

    /**
     * @return the offered demands unread messages HTML
     */
    @Override
    public HTML getOfferedDemandsUnreadMessages() {
        return offeredDemandsUnreadMessages;
    }

    /**
     * @return the assigned demands unread messages HTML
     */
    @Override
    public HTML getAssignedDemandsUnreadMessages() {
        return assignedDemandsUnreadMessages;
    }

    /**
     * @return the closed demands unread messages HTML
     */
    @Override
    public HTML getClosedDemandsUnreadMessages() {
        return closedDemandsUnreadMessages;
    }

    /** FLUIDROW. **/
    /**
     * @return the potential demands row
     */
    @Override
    public FluidRow getPotentialDemandsRow() {
        return potentialDemandsRow;
    }

    /**
     * @return the offered demands row
     */
    @Override
    public FluidRow getOfferedDemandsRow() {
        return offeredDemandsRow;
    }

    /**
     * @return the assigend demands row
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

    /** OTHERS. **/
    /**
     * @return the widget view
     */
    @Override
    public Widget getWidgetView() {
        return this;
    }
}