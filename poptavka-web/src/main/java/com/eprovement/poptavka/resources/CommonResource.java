package com.eprovement.poptavka.resources;

import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.ClassName;

public interface CommonResource extends CssResource {

    @ClassName("small-loader")
    String smallLoader();

    @ClassName("elem-visible")
    String elemHiddenOff();

    @ClassName("elem-hidden")
    String elemHiddenOn();

    @ClassName("error-field")
    String errorField();

    @ClassName("info-message")
    String infoMessage();

    @ClassName("error-message")
    String errorMessage();

    @ClassName("disabled-text")
    String disabledText();

    @ClassName("loading-popup")
    String loadingPopup();

    @ClassName("hyperlink")
    String hyperlinkInline();

    @ClassName("user_content")
    String userContent();

    @ClassName("user")
    String user();

    @ClassName("empty_style")
    String emptyStyle();

    @ClassName("changed")
    String changed();

    @ClassName("textBoxAsLabel")
    String textBoxAsLabel();
}
