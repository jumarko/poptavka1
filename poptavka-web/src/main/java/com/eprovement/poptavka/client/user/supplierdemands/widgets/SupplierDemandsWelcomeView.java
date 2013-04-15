/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.supplierdemands.widgets;

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

    private static SupplierDemandsWelcomeViewUiBinder uiBinder = GWT.create(SupplierDemandsWelcomeViewUiBinder.class);

    @UiField HTML potentialDemandsUnreadMessages, offeredDemandsUnreadMessages, assignedDemandsUnreadMessages,
    closedDemandsUnreadMessages;

    interface SupplierDemandsWelcomeViewUiBinder extends UiBinder<Widget, SupplierDemandsWelcomeView> {
    }

    /**
     * creates WIDGET view.
     */
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * @return this widget as it is
     */
    @Override
    public Widget getWidgetView() {
        return this;
    }

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
}