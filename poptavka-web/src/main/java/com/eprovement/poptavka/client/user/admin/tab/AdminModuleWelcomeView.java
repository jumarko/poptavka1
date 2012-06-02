/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.admin.tab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;


/**
 *
 * @author Martin Slavkovsky
 */
public class AdminModuleWelcomeView extends Composite {

    private static AdminModuleWelcomeViewUiBinder uiBinder = GWT.create(AdminModuleWelcomeViewUiBinder.class);

    interface AdminModuleWelcomeViewUiBinder extends UiBinder<Widget, AdminModuleWelcomeView> {
    }

    /**
     * creates WIDGET view
     */
    public AdminModuleWelcomeView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * @return this widget as it is
     */
    public Widget getWidgetView() {
        return this;
    }
}