package com.eprovement.poptavka.client.resources;

import com.google.gwt.resources.client.CssResource;

/**
 *
 * GWT Wrapper for base app layout.
 *
 * @author Beho, Jaro
 *
 */
public interface LayoutResource extends CssResource {

    /** MainView.class **/
    @ClassName("layout-public-main")
    String layoutPublic();

    @ClassName("layout-user-main")
    String layoutUser();

    @ClassName("header-container")
    String headerContainer();

    @ClassName("header-content")
    String headerContent();

    @ClassName("footer-container")
    String footerContainer();

    @ClassName("body-container")
    String bodyContainer();

    @ClassName("body-full-container")
    String bodyFullContainer();

    @ClassName("body-background")
    String bodyBackground();

    @ClassName("body-navbar")
    String bodyNavbar();

    @ClassName("detail-info")
    String detailInfo();

    @ClassName("home-logo")
    String homeLogo();

    @ClassName("user-logo")
    String userLogo();

    /* login styles */
    @ClassName("login")
    String login();

    @ClassName("login-bar")
    String loginBar();

    @ClassName("login-button")
    String loginButton();

    @ClassName("logout-button")
    String logoutButton();

    @ClassName("logout-menu-bar")
    String logoutMenuBar();

    @ClassName("help-button")
    String helpButton();

    @ClassName("footer-toggle")
    String footerToggle();

    @ClassName("home-menu")
    String homeMenu();

    @ClassName("search-bar")
    String searchBar();

    @ClassName("search-view")
    String searchView();

    @ClassName("selected")
    String selected();

    /** UserView.class **/
    @ClassName("full-size-panel")
    String fullSize();

    @ClassName("tab-layout-container")
    String tabLayoutContainer();

    @ClassName("tab-layout-panel")
    String tabLayoutPanel();

    @ClassName("demand-tab-content")
    String demandTabContent();

    @ClassName("demand-tab-content-header")
    String demandTabContentHeader();

    @ClassName("pager")
    String pager();

    @ClassName("submenu-header")
    String submenuHeader();

    @ClassName("submenu-list")
    String submenuList();

    /** DemandCreation.class **/
    @ClassName("stack-layout")
    String stackLayout();

    @ClassName("stack-layout-panel-header")
    String stackLayoutPanelHeader();

    @ClassName("create-tab-layout")
    String createTabLayout();

    @ClassName("create-tab-layout-header")
    String createTabLayoutHeader();

    @ClassName("create-tab-layout-selected")
    String createTabLayoutSelected();

    /** DELETE AFTER THIS LINE **/

}
