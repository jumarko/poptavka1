package cz.poptavka.sample.client.resources;

import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.ClassName;

public interface CommonResource extends CssResource {

    @ClassName("small-loader")
    String smallLoader();

    @ClassName("elem-hidden")
    String elemHiddenOn();

    @ClassName("elem-visible")
    String elemHiddenOff();
}
