/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client;


import com.eprovement.poptavka.client.common.Track;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.Mvp4gModule;

/**
 * Main entry point.
 *
 * @author Beho
 */
public class Poptavka implements EntryPoint, ValueChangeHandler<String> {

    private static final Logger LOGGER = Logger.getLogger("poptavka");
    @Override
    public void onModuleLoad() {
        LOGGER.info("Loading ...");
        initHistoryObservations();
        Mvp4gModule module =  GWT.create(Mvp4gModule.class);
        module.createAndStartModule();
        LOGGER.info("Loading done ... ");
//        RootPanel.get("gwt-container").add((Widget) module.getStartView());
        RootLayoutPanel.get().add((Widget) module.getStartView());
    }

    /**
     * watch the historyTokens after the querystring#historyToken or you could say
     * querystring#[historyEvent|applicationState]
     */
    private void initHistoryObservations() {

        History.addValueChangeHandler(this);

        // first load
        Track.track("home");
    }

    public void onValueChange(ValueChangeEvent<String> event) {

        // get the querystring token
        String historyToken = History.getToken();

        // send to static method that will send the __utm.gif to google's server fro tracking
        Track.track(historyToken);

    }

}
