package com.eprovement.poptavka.client.resources;

import com.github.gwtbootstrap.client.ui.resources.Resources;
import com.google.gwt.resources.client.TextResource;

public interface CustomBootstrap extends Resources {

    @Override
    @Source("css/bootstrap.min.css")
    TextResource bootstrapCss();
}
