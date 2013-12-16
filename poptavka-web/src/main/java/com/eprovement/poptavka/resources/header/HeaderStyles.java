package com.eprovement.poptavka.resources.header;

import com.google.gwt.resources.client.CssResource;

/**
 * Styles for Headers.
 * @author Martin Slavkovsky
 */
public interface HeaderStyles extends CssResource {

    /** Header Views. **/
    /** Home Menu Styles. **/
//    @ClassName("menu-bar")
//    String menuBar();
//    @ClassName("header-bar")
//    String headerBar();

    //Menu
    @ClassName("menu-icon")
    String menuIcon();
    @ClassName("menu-panel")
    String menuPanel();
    @ClassName("menu")
    String menu();

    //Logo
    @ClassName("logo")
    String logo();

    /** Search Bar Styles. **/
    @ClassName("search-icon")
    String searchIcon(); //Root
    @ClassName("search-panel")
    String searchPanel(); //Root

    @ClassName("settings-icon")
    String settingsIcon(); //Root

    /** Login Styles. **/
    @ClassName("login-icon")
    String loginIcon();

    @ClassName("login-panel")
    String loginPanel();

    @ClassName("login")
    String login();

    @ClassName("login-bar")
    String loginBar();

    @ClassName("login-button")
    String loginButton();

    /** Logout Styles **/
    @ClassName("logout-button")
    String logoutButton();

    @ClassName("logout-button-act")
    String logoutButtonAct();

    @ClassName("logout-menu-bar")
    String logoutMenuBar();

    @ClassName("help-button")
    String helpButton();
}
