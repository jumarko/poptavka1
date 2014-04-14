/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.resources.header;

import com.google.gwt.resources.client.CssResource;

/**
 * Defines styles for header.
 *
 * @author Jaro
 */
public interface HeaderStyles extends CssResource {

    /** Header Views. **/
    //Menu
    @ClassName("menu-icon")
    String menuIcon();
    @ClassName("menu-panel")
    String menuPanel();
    @ClassName("menu")
    String menu();
    @ClassName("user-responsive-menu")
    String userResponsiveMenu();
    @ClassName("user-label")
    String userLabel();
    @ClassName("user-settings")
    String userSettings();
    @ClassName("user-logout")
    String userLogout();

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
