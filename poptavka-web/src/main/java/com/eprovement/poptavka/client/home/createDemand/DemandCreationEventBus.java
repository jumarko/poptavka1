/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.home.createDemand;

import com.eprovement.poptavka.client.common.BaseChildEventBus;
import com.eprovement.poptavka.client.home.createDemand.widget.FormDemandAdvPresenter;
import com.eprovement.poptavka.client.home.createDemand.widget.FormDemandBasicPresenter;
import com.eprovement.poptavka.client.root.gateways.CatLocSelectorGateway;
import com.eprovement.poptavka.client.root.gateways.LoginGateway;
import com.eprovement.poptavka.client.root.gateways.UserRegistrationGateway;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.annotation.Start;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.mvp4g.client.event.EventBusWithLookup;

/**
 * DemandCreationEventBus servers all events for module DemandCreationModule.
 * Provides demand creation process.
 *
 * @author ivan.vlcek
 */
@Events(startPresenter = DemandCreationPresenter.class, module = DemandCreationModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface DemandCreationEventBus extends EventBusWithLookup, BaseChildEventBus,
    CatLocSelectorGateway, UserRegistrationGateway, LoginGateway {

    /**
     * Start event is called only when module is instantiated first time.
     * We can use it for history initialization.
     */
    @Start
    @Event(handlers = DemandCreationPresenter.class)
    void start();

    /**
     * Forward event is called only if it is configured here. It there is nothing to carry out
     * in this method we should remove forward event to save the number of method invocations.
     * We can use forward event to switch css style for selected menu button.
     */
    @Forward
    @Event(handlers = DemandCreationPresenter.class, navigationEvent = true)
    void forward();

    /**************************************************************************/
    /* Navigation events.                                                     */
    /**************************************************************************/
    /**
     * The only entry point to this module due to code-spliting feature.
     */
    @Event(handlers = DemandCreationPresenter.class, navigationEvent = true,
    historyConverter = DemandCreationHistoryConverter.class, name = "createDemand")
    String goToCreateDemandModule();

    /**************************************************************************/
    /* Business events handled by FormDemandBasicPresenter.                   */
    /**************************************************************************/
    @Event(generate = FormDemandBasicPresenter.class)
    void initDemandBasicForm(SimplePanel holderWidget);

    /**************************************************************************/
    /* Business events handled by FormDemandAdvPresenter.                     */
    /**************************************************************************/
    @Event(generate = FormDemandAdvPresenter.class)
    void initDemandAdvForm(SimplePanel holderWidget);

    /**************************************************************************/
    /* Parent events                                                          */
    /**************************************************************************/
    @Event(forwardToParent = true)
    void atAccount();

    @Event(forwardToParent = true)
    void logout(int widgetToLoad);

    @Event(forwardToParent = true, navigationEvent = true)
    void goToClientDemandsModule(SearchModuleDataHolder filterm, int loadWidget);

    /**************************************************************************/
    /* Business events handled by DemandCreationPresenter.                    */
    /**************************************************************************/
    @Event(handlers = DemandCreationPresenter.class)
    void restoreDefaultFirstTab();

    @Event(handlers = DemandCreationPresenter.class)
    void setUserRegistrationHeight(boolean company);

    /**************************************************************************/
    /* Business events handled by Handlers.                                   */
    /**************************************************************************/
    @Event(handlers = DemandCreationHandler.class)
    void registerNewClient(BusinessUserDetail newClient);

    @Event(handlers = DemandCreationHandler.class)
    void createDemand(FullDemandDetail detail, Long clientId);
}