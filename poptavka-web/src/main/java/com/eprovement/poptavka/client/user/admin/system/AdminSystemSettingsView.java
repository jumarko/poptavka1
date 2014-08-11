/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.admin.system;

import com.eprovement.poptavka.client.user.admin.interfaces.IAdminSystemSettings;
import com.eprovement.poptavka.shared.domain.adminModule.LogDetail;
import com.github.gwtbootstrap.client.ui.Icon;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
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
    @UiField Button demandCountsBtn, supplierCountsBtn;
    @UiField Icon demandCountsProgressSpinner, supplierCountsProgressSpinner;
    @UiField Label demandCountsProgressLabel, supplierCountsProgressLabel;

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

    /**
     * @{inheritDoc}
     */
    @Override
    public void setDemandCountsProgress(LogDetail detail) {
        demandCountsBtn.setVisible(detail == null);
        demandCountsBtn.setEnabled(detail == null);
        demandCountsProgressSpinner.setVisible(detail != null);
        demandCountsProgressLabel.setVisible(detail != null);
        if (detail != null) {
            demandCountsProgressLabel.setText(new StringBuilder()
                .append(detail.getPercentageProgress().toString())
                .append("% (")
                .append(detail.getProcessedItems() == null ? "0" : detail.getProcessedItems())
                .append("/")
                .append(detail.getTotalItems() == null ? "?" : detail.getTotalItems())
                .append(")").toString()
            );
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void setSupplierCountsProgress(LogDetail detail) {
        supplierCountsBtn.setVisible(detail == null);
        supplierCountsBtn.setEnabled(detail == null);
        supplierCountsProgressSpinner.setVisible(detail != null);
        supplierCountsProgressLabel.setVisible(detail != null);
        if (detail != null) {
            supplierCountsProgressLabel.setText(new StringBuilder()
                .append(detail.getPercentageProgress().toString())
                .append("% (")
                .append(detail.getProcessedItems() == null ? "0" : detail.getProcessedItems())
                .append("/")
                .append(detail.getTotalItems() == null ? "?" : detail.getTotalItems())
                .append(")").toString()
            );
        }
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
    public Button getDemandCountsBtn() {
        return demandCountsBtn;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Button getSupplierCountsBtn() {
        return supplierCountsBtn;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean isValid() {
        return true;
    }
}
