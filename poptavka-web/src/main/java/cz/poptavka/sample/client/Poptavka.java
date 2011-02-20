/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.Mvp4gModule;

/**
 * Main entry point.
 *
 * @author Beho
 */
public class Poptavka implements EntryPoint {

    @Override
    public void onModuleLoad() {
        Mvp4gModule module =  GWT.create(Mvp4gModule.class);
        module.createAndStartModule();
        RootPanel.get("gwt-container").add((Widget) module.getStartView());
    }
}
