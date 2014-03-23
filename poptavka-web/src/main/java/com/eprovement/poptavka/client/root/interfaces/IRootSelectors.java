package com.eprovement.poptavka.client.root.interfaces;

import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Selector;
import com.google.gwt.query.client.Selectors;

/**
 * Selectors for Login view ui elements.
 *
 * @author Martin Slavkovsky
 */
public interface IRootSelectors extends Selectors {

    @Selector("#gwt-debug-toolbarContainer")
    GQuery getToolbarContainer();

    @Selector("#gwt-debug-bodyContainer")
    GQuery getBodyContainer();

    @Selector("#gwt-debug-menuPanel")
    GQuery getMenuPanel();

    @Selector("#gwt-debug-submenuContainer")
    GQuery getSubmenuContainer();

    @Selector("#gwt-debug-detailPanel")
    GQuery getDetailPanel();

    @Selector("#gwt-debug-closeBtn")
    GQuery getCloseBtn();

    @Selector("#gwt-debug-clearBtn")
    GQuery getClearBtn();

    @Selector("#gwt-debug-searchBtn2")
    GQuery getSearchBtn2();

    @Selector("#gwt-debug-advanceSearchPopupNoCriteriaPanel")
    GQuery getAdvanceSearchPopupNoCriteriaPanel();
}
