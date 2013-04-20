/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.clientdemands.widgets;

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
    /** FLUIDROW. **/
    @Override
    public HTML getMyDemandsUnreadMessages() {
        return myDemandsUnreadMessages;
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
    public FluidRow getMyDemandsRow() {
        return myDemandsRow;
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