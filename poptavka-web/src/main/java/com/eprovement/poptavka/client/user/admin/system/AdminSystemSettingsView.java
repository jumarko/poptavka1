/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.admin.system;

import com.eprovement.poptavka.client.user.admin.interfaces.IAdminSystemSettings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * View consists of table of notifications, each represented by NotificationItem widget.
 *
 * @author Martin Slavkovsky
 */
public class AdminSystemSettingsView extends Composite implements IAdminSystemSettings.View {

    /**************************************************************************/
    /* UIBINDER                                                               */
    /**************************************************************************/
    private static AdminSystemSettingsViewUiBinder uiBinder = GWT.create(AdminSystemSettingsViewUiBinder.class);

    interface AdminSystemSettingsViewUiBinder extends UiBinder<Widget, AdminSystemSettingsView> {
    }

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField FlowPanel properties;
    @UiField Button calcDemandCountsBtn, calcSupplierCountsBtn;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    /**
     * Creates SystemSettings view's compontents.
     */
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public FlowPanel getPropertiesPanel() {
        return properties;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Button getCalcDemandCountsBtn() {
        return calcDemandCountsBtn;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Button getCalcSupplierCountsBtn() {
        return calcSupplierCountsBtn;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean isValid() {
        return true;
    }
}
