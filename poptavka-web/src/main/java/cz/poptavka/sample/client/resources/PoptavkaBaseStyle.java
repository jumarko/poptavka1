package cz.poptavka.sample.client.resources;

import com.google.gwt.resources.client.CssResource;

public interface PoptavkaBaseStyle extends CssResource {

    @ClassName("layout-container")
    String layoutContainer();

    @ClassName("layout-container-user")
    String layoutContainerUser();

    @ClassName("header-container")
    String headerContainer();

    @ClassName("footer-container")
    String footerContainer();

    @ClassName("main-menu-container")
    String mainMenuContainer();

    @ClassName("side-menu-container")
    String sideMenuContainer();

    @ClassName("body-container")
    String bodyContainer();

    @ClassName("closed")
    String closedWidget();

    @ClassName("login-area")
    String loginArea();

    @ClassName("creation-hidden")
    String creationHidden();

    @ClassName("creation-second")
    String displaySecondPart();

    @ClassName("creation-third")
    String displayThirdPart();

    @ClassName("creation-fourth")
    String displayFourthPart();

    @ClassName("solid-back")
    String solidBackground();

    @ClassName("common-list-loader")
    String commonListLoader();

    @ClassName("elem-hidden")
    String elemHiddenOn();

    @ClassName("elem-visible")
    String elemHiddenOff();


}
