/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.search;

import com.eprovement.poptavka.client.search.advanced.AdvanceSearchPresenter;
import com.eprovement.poptavka.client.common.BaseChildEventBus;
import com.eprovement.poptavka.client.root.gateways.CatLocSelectorGateway;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Debug.LogLevel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBus;

/**
 * Search module allows user to fill serach criteria that are used as filters when
 * retieving data. Doesn't perform search functionality itselft.
 *
 * @author Martin Slavkovsky
 */
@Debug(logLevel = LogLevel.DETAILED)
@Events(startPresenter = SearchModulePresenter.class, module = SearchModule.class)
public interface SearchModuleEventBus extends EventBus, BaseChildEventBus, CatLocSelectorGateway {

    /**************************************************************************/
    /* Parent events - navigation events                                      */
    /**************************************************************************/
    @Event(forwardToParent = true)
    void goToHomeDemandsModule(SearchModuleDataHolder filter);

    @Event(forwardToParent = true)
    void goToHomeSuppliersModule(SearchModuleDataHolder filter);

    @Event(forwardToParent = true)
    void goToClientDemandsModule(SearchModuleDataHolder filter, int loadWidget);

    @Event(forwardToParent = true)
    void goToSupplierDemandsModule(SearchModuleDataHolder filter, int loadWidget);

    @Event(forwardToParent = true)
    void goToAdminModule(SearchModuleDataHolder filter, int loadWidget);

    @Event(forwardToParent = true)
    void goToMessagesModule(SearchModuleDataHolder filter, int loadWidget);

    /**************************************************************************/
    /* Business events handled by SearchPresenter                             */
    /**************************************************************************/
    @Event(handlers = SearchModulePresenter.class)
    void goToSearchModule();

    @Override
    @Event(handlers = SearchModulePresenter.class)
    void resetSearchBar(Widget newAttributeSearchWidget);

    @Event(handlers = SearchModulePresenter.class)
    void showPopupNoSearchCriteria();

    @Event(handlers = SearchModulePresenter.class)
    void showAdvancedSearchPopup();

    /**************************************************************************/
    /* Business events handled by AdvanceSearchPresenter                      */
    /**************************************************************************/
    @Event(handlers = AdvanceSearchPresenter.class)
    void initAdvanceSearchPopup(Widget newAttributeSearchWidget);

    @Event(handlers = AdvanceSearchPresenter.class)
    void showAdvanceSearchPopup();
}
