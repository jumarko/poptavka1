/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.homeWelcome;

import com.eprovement.poptavka.client.common.BaseChildEventBus;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBusWithLookup;
import java.util.ArrayList;

/**
 * HomeWelcomEventBus is very first home page to be loaded.
 *
 * Specification:
 * Wireframe: http://www.webgres.cz/axure/
 *
 * @author Beho, Martin Slavkovsky
 */
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
    @Event(handlers = HomeWelcomePresenter.class, navigationEvent = true)
    void forward();

    /**************************************************************************/
    /* Navigation events.                                                     */
    /**************************************************************************/
    /**
     * The only entry point to this module. This module is not asynchronous.
     *
     * @param filter - defines data holder to be displayed in advanced search bar
     */
    @Event(handlers = HomeWelcomePresenter.class, historyConverter = HomeWelcomeHistoryConverter.class,
            navigationEvent = true)
    void goToHomeWelcomeModule();

    /**************************************************************************/
    /* History events.                                                        */
    /**************************************************************************/
    @Event(historyConverter = HomeWelcomeHistoryConverter.class, name = "view")
    void createCustomToken(String param);

    /**************************************************************************/
    /* Navigation Parent events */
    /**************************************************************************/
    @Event(forwardToParent = true)
    void goToHomeDemandsModule(SearchModuleDataHolder filter);

    @Event(forwardToParent = true)
    void goToHomeDemandsModuleFromWelcome(int categoryIdx, ICatLocDetail category);

    @Event(forwardToParent = true)
    void goToHomeSuppliersModule(SearchModuleDataHolder filter);

    @Event(forwardToParent = true)
    void goToCreateDemandModule();

    @Event(forwardToParent = true)
    void goToCreateSupplierModule();

    /**************************************************************************/
    /* Business events handled by Presenters.                                 */
    /**************************************************************************/
    @Event(handlers = HomeWelcomePresenter.class)
    void displayCategories(ArrayList<ICatLocDetail> list);

    @Event(handlers = HomeWelcomePresenter.class)
    void displayHowItWorkdsDemands();

    @Event(handlers = HomeWelcomePresenter.class)
    void displayHowItWorkdsSuppliers();

    /**************************************************************************/
    /* Business events handled by Handlers.                                   */
    /**************************************************************************/
    @Event(handlers = HomeWelcomeHandler.class)
    void getRootCategories();
}