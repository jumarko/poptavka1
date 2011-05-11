package cz.poptavka.sample.client.resources;

import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.ClassName;

public interface CommonResource extends CssResource {

    @ClassName("small-loader")
    String smallLoader();

    @ClassName("elem-visible")
    String elemHiddenOff();

    @ClassName("elem-hidden")
    String elemHiddenOn();

    @ClassName("info-message")
    String infoMessage();

    @ClassName("error-message")
    String errorMessage();

    @ClassName("disabled-text")
    String disabledText();

    @ClassName("loading-popup")
    String loadingPopup();

}
