/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.settings.widget;

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

    private static ClientSettingsView.ClientSettingsViewUiBinder uiBinder = GWT
            .create(ClientSettingsView.ClientSettingsViewUiBinder.class);

    interface ClientSettingsViewUiBinder extends UiBinder<Widget, ClientSettingsView> {
    }
    @UiField
    TextBox clientRating;

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}
