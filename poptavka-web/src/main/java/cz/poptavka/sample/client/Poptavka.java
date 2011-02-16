/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.gwtplatform.mvp.client.DelayedBindRegistry;
import cz.poptavka.sample.client.gin.PoptavkaGinjector;

import java.util.logging.Logger;

/**
 * Main entry point.
 *
 * @author Dusan
 */
public class Poptavka implements EntryPoint {

    private static final Logger LOGGER = Logger.getLogger(Poptavka.class.getName());


    private final PoptavkaGinjector ginjector = GWT.create(PoptavkaGinjector.class);

    /**
     * Creates a new instance of MainEntryPoint.
     */
    public Poptavka() {
    }

    private void initialize() {

        DelayedBindRegistry.bind(ginjector);

        ginjector.getPlaceManager().revealCurrentPlace();

    }


    /**
     * The entry point method, called automatically by loading a module
     * that declares an implementing class as an entry-point.
     */
    @Override
    public void onModuleLoad() {
        initialize();
    }
}
