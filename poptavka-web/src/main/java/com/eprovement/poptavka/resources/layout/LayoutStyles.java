package com.eprovement.poptavka.resources.layout;

import com.google.gwt.resources.client.CssResource;

/**
 * GWT Wrapper for base app layout.
 *
 * @author Beho, Jaro
 */
public interface LayoutStyles extends CssResource {

    /** RootView.class **/
    @ClassName("header-container")
    String headerContainer();

    @ClassName("toolbar-container")
    String toolbarContainer();

    @ClassName("body-container")
    String bodyContainer();

    @ClassName("layout-user-main")
    String layoutUser();

    @ClassName("full-size-panel")
    String fullSize();

    @ClassName("fluid-main-grid")
    String fluidMainGrid();

    /** Search Bar Styles **/
    @ClassName("search-view")
    String searchView();

    @ClassName("advanced-search-modal")
    String advancedSearchModal();

    @ClassName("advanced-search-small-modal")
    String advancedSearchSmallModal();

    /** Toolbar Module styles **/
    @ClassName("submenu-icon")
    String submenuIcon();

    @ClassName("submenu-header")
    String submenuHeader();

    @ClassName("content-container")
    String contentContainer();

    @ClassName("left-container")
    String leftContainer();

    @ClassName("detail-container")
    String detailContainer();

    @ClassName("submenu-list")
    String submenuList();

    @ClassName("pager")
    String pager();

    @ClassName("filter-label")
    String filterLabel();

    /** Other Styles **/
    @ClassName("data-grid-container")
    String dataGridContainer();

    @ClassName("detail-info")
    String detailInfo();

    @ClassName("popup-dropdown-container")
    String popupDropdownContainer();

    @ClassName("tab-layout-container")
    String tabLayoutContainer();

    @ClassName("tab-layout-panel")
    String tabLayoutPanel();

    @ClassName("stack-layout")
    String stackLayout();

    @ClassName("stack-layout-panel-header")
    String stackLayoutPanelHeader();

    @ClassName("dashboard")
    String dashboard();

    @ClassName("notification-panel")
    String notificationPanel();
}
