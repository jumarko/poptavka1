package com.eprovement.poptavka.client.user.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class SettingsView extends Composite implements
        SettingsPresenter.HomeSettingsViewInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static HomeSettingsViewUiBinder uiBinder = GWT
            .create(HomeSettingsViewUiBinder.class);

    interface HomeSettingsViewUiBinder extends
            UiBinder<Widget, SettingsView> {
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    @UiField
    StackLayoutPanel stackPanel;
    @UiField
    SimplePanel userSettingsPanel, clientSettingsPanel, supplierSettingsPanel;

    /**************************************************************************/
    /* Initialization */
    /**************************************************************************/
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /*  Getters                                                               */
    /**************************************************************************/
    /** PANELS. **/
    @Override
    public StackLayoutPanel getStackPanel() {
        return stackPanel;
    }

    @Override
    public SimplePanel getUserSettingsPanel() {
        return userSettingsPanel;
    }

    @Override
    public SimplePanel getClientSettingsPanel() {
        return clientSettingsPanel;
    }

    @Override
    public SimplePanel getSupplierSettingsPanel() {
        return supplierSettingsPanel;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}
