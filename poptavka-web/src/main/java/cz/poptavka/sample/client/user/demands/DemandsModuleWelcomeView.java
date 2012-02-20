/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.demands;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;


/**
 *
 * @author Martin Slavkovsky
 */
public class DemandsModuleWelcomeView extends Composite {

    private static DemandsModuleWelcomeViewUiBinder uiBinder = GWT.create(DemandsModuleWelcomeViewUiBinder.class);

    interface DemandsModuleWelcomeViewUiBinder extends UiBinder<Widget, DemandsModuleWelcomeView> {
    }

    /**
     * creates WIDGET view
     */
    public DemandsModuleWelcomeView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * @return this widget as it is
     */
    public Widget getWidgetView() {
        return this;
    }
}