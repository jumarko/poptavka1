/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.clientdemands;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;


/**
 *
 * @author Martin Slavkovsky
 */
public class ClientDemandsModuleWelcomeView extends Composite {

    private static ClientDemandsModuleWelcomeViewUiBinder uiBinder =
            GWT.create(ClientDemandsModuleWelcomeViewUiBinder.class);

    interface ClientDemandsModuleWelcomeViewUiBinder extends UiBinder<Widget, ClientDemandsModuleWelcomeView> {
    }

    /**
     * creates WIDGET view
     */
    public ClientDemandsModuleWelcomeView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * @return this widget as it is
     */
    public Widget getWidgetView() {
        return this;
    }
}