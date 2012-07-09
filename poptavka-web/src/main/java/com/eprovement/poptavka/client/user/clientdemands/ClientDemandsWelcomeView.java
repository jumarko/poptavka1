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
public class ClientDemandsWelcomeView extends Composite {

    private static DemandsModuleWelcomeViewUiBinder uiBinder = GWT.create(DemandsModuleWelcomeViewUiBinder.class);

    interface DemandsModuleWelcomeViewUiBinder extends UiBinder<Widget, ClientDemandsWelcomeView> {
    }

    /**
     * creates WIDGET view
     */
    public ClientDemandsWelcomeView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * @return this widget as it is
     */
    public Widget getWidgetView() {
        return this;
    }
}