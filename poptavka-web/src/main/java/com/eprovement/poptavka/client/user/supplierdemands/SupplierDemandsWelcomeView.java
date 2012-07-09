/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.supplierdemands;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author Martin Slavkovsky
 */
public class SupplierDemandsWelcomeView extends Composite {

    private static SupplierDemandsModuleWelcomeViewUiBinder uiBinder =
            GWT.create(SupplierDemandsModuleWelcomeViewUiBinder.class);

    interface SupplierDemandsModuleWelcomeViewUiBinder extends UiBinder<Widget, SupplierDemandsWelcomeView> {
    }

    /**
     * creates WIDGET view
     */
    public SupplierDemandsWelcomeView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * @return this widget as it is
     */
    public Widget getWidgetView() {
        return this;
    }
}