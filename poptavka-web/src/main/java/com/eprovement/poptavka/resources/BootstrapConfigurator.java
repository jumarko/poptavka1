package com.eprovement.poptavka.resources;

import com.github.gwtbootstrap.client.ui.config.Configurator;
import com.github.gwtbootstrap.client.ui.resources.Resources;
import com.google.gwt.core.client.GWT;

public class BootstrapConfigurator implements Configurator {

    @Override
    public Resources getResources() {
        return GWT.create(CustomBootstrap.class);
    }

    @Override
    public boolean hasResponsiveDesign() {
        return false;
    }
}
