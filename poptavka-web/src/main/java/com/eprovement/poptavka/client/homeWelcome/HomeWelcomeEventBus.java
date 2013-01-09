/*
 * HomeWelcomEventBus is very first home page to be loaded.
 *
 * Specification:
 * Wireframe: http://www.webgres.cz/axure/
 */
package com.eprovement.poptavka.client.homeWelcome;

import com.eprovement.poptavka.client.root.BaseChildEventBus;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBusWithLookup;
import java.util.ArrayList;

@Events(startPresenter = HomeWelcomePresenter.class, module = HomeWelcomeModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface HomeWelcomeEventBus extends EventBusWithLookup, BaseChildEventBus {

    /**
     * First event to be handled.
     */
    @Start
    @Event(handlers = HomeWelcomePresenter.class)
    void start();

    /**
     * Forward event is called only if it is configured here. If there is nothing to carry out
     * in this method we should remove forward event to save the number of method invocations.
     */
    @Forward
    @Event(handlers = HomeWelcomePresenter.class)
    void forward();

    /**************************************************************************/
    /* Navigation events.                                                     */
    /**************************************************************************/
    /**
     * The only entry point to this module. This module is not asynchronous.
     *
     * @param filter - defines data holder to be displayed in advanced search bar
     */
    @Event(handlers = HomeWelcomePresenter.class, historyConverter = HomeWelcomeHistoryConverter.class
            , navigationEvent = true)
    String goToHomeWelcomeModule();

    /**************************************************************************/
    /* Parent events                                                          */
    /**************************************************************************/
    @Event(forwardToParent = true)
    void goToHomeDemandsModule(SearchModuleDataHolder filter);

    @Event(forwardToParent = true)
    void setUpSearchBar(IsWidget searchView);

    @Event(forwardToParent = true)
    void menuStyleChange(int loadedModule);

    /**************************************************************************/
    /* Business events handled by Presenters.                                 */
    /**************************************************************************/
    @Event(handlers = HomeWelcomePresenter.class)
    void displayCategories(ArrayList<CategoryDetail> list);

    /**************************************************************************/
    /* Business events handled by Handlers.                                   */
    /**************************************************************************/
    @Event(handlers = HomeWelcomeHandler.class)
    void getRootCategories();
}
