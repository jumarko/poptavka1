package cz.poptavka.sample.client.resources;

import com.google.gwt.resources.client.CssResource;

public interface PoptavkaBaseStyle extends CssResource {

    @ClassName("layout-container")
    String layoutContainer();

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
}
