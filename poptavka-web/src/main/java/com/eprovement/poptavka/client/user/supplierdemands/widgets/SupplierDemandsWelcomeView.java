/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /** HTML. **/
    @Override
    public HTML getPotentialDemandsUnreadMessages() {
        return potentialDemandsUnreadMessages;
    }

    @Override
    public HTML getOfferedDemandsUnreadMessages() {
        return offeredDemandsUnreadMessages;
    }

    @Override
    public HTML getAssignedDemandsUnreadMessages() {
        return assignedDemandsUnreadMessages;
    }

    @Override
    public HTML getClosedDemandsUnreadMessages() {
        return closedDemandsUnreadMessages;
    }

    /** FLUIDROW. **/
    @Override
    public FluidRow getPotentialDemandsRow() {
        return potentialDemandsRow;
    }

    @Override
    public FluidRow getOfferedDemandsRow() {
        return offeredDemandsRow;
    }

    @Override
    public FluidRow getAssignedDemandsRow() {
        return assignedDemandsRow;
    }

    @Override
    public FluidRow getClosedDemandsRow() {
        return closedDemandsRow;
    }

    /** OTHERS. **/
    @Override
    public Widget getWidgetView() {
        return this;
    }
}