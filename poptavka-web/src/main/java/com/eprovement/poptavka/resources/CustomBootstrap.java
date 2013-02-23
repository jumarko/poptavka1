package com.eprovement.poptavka.resources;

import com.github.gwtbootstrap.client.ui.resources.Resources;
import com.google.gwt.resources.client.TextResource;

public interface CustomBootstrap extends Resources {

    @Override
    @Source("css/bootstrap.min.css")
    TextResource bootstrapCss();
}
