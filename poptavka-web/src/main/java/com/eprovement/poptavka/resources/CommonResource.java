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

    @ClassName("button-right-small")
    String buttonRightSmall();

    @ClassName("button-right-medium")
    String buttonRightMedium();

    @ClassName("button-right-large")
    String buttonRightLarge();

    @ClassName("button-left-small")
    String buttonLeftSmall();

    @ClassName("button-left-medium")
    String buttonLeftMedium();

    @ClassName("button-left-large")
    String buttonLeftLarge();

    @ClassName("button-grey")
    String buttonGrey();

    @ClassName("button-long-grey")
    String buttonLongGrey();

    @ClassName("button-green")
    String buttonGreen();

    @ClassName("button-ok-green")
    String buttonOkGreen();

    @ClassName("toolbar-button-green")
    String toolbarButtonGreen();

    @ClassName("toolbar-button-grey")
    String toolbarButtonGrey();

    @ClassName("toolbar-button-back")
    String toolbarButtonBack();

    @ClassName("toolbar-menubar")
    String toolbarMenubar();

    @ClassName("universal-pager")
    String universalPager();

    @ClassName("selected-items-widget")
    String selectedItemsWidget();

    @ClassName("switch-left")
    String switchLeft();

    @ClassName("switch-right")
    String switchRight();

    @ClassName("changeMonitorStyle")
    String changeMonitorStyle();

    @ClassName("validation-container")
    String validationContainer();

    /** How it works **/
    @ClassName("how-it-works-container")
    String howItWorksContainer();

    @ClassName("myListBox")
    String myListBox();

    /** Alert Box styles **/
    @ClassName("alert-container")
    String alertContainer();
}
