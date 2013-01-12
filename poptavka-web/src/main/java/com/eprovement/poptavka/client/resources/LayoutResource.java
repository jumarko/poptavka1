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

    @ClassName("detail-info")
    String detailInfo();

    @ClassName("home-logo")
    String homeLogo();

    @ClassName("user-logo")
    String userLogo();

    /* Tool Bar Styles */
    @ClassName("toolbar")
    String bodyNavbar();

    @ClassName("toolbar-header")
    String toolbarHeader();

    @ClassName("toolbar-items-block")
    String toolbarItemsBlock();

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

    /** DELETE AFTER THIS LINE **/

}
