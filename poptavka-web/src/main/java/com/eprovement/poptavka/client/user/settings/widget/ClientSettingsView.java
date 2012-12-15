/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author Martin Slavkovsky
 */
public class ClientSettingsView extends Composite implements ClientSettingsPresenter.ClientSettingsViewInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static ClientSettingsView.ClientSettingsViewUiBinder uiBinder = GWT
            .create(ClientSettingsView.ClientSettingsViewUiBinder.class);

    interface ClientSettingsViewUiBinder extends UiBinder<Widget, ClientSettingsView> {
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    @UiField
    TextBox clientRating;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        clientRating.setEnabled(false);
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    @Override
    public void setClientSettings(SettingDetail detail) {
        clientRating.setText(Integer.toString(detail.getClientRating()));
    }

    @Override
    public SettingDetail updateClientSettings(SettingDetail detail) {
        // nothing to set - ratings will be calculated automatically.
        return detail;
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    @Override
    public boolean isSettingChange() {
        return true;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}
