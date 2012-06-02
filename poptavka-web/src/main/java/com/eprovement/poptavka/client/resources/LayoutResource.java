package com.eprovement.poptavka.client.resources;

import com.google.gwt.resources.client.CssResource;

/**
 *
 * GWT Wrapper for base app layout.
 *
 * @author Beho
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
    @ClassName("login-area")
    String loginArea();
    @ClassName("login-button")
    String loginButton();
    @ClassName("footer-toggle")
    String footerToggle();
    @ClassName("home-menu")
    String homeMenu();
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
    @ClassName("stackLayout")
    String stackLayout();
    @ClassName("stackLayoutPanelHeader")
    String stackLayoutPanelHeader();

    /** DELETE AFTER THIS LINE **/


}
