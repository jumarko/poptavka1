/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.home.createSupplier;

import com.eprovement.poptavka.client.common.BaseChildEventBus;
import com.eprovement.poptavka.client.home.createSupplier.interfaces.ISupplierCreationModule;
import com.eprovement.poptavka.client.root.gateways.CatLocSelectorGateway;
import com.eprovement.poptavka.client.root.gateways.UserRegistrationGateway;
import com.eprovement.poptavka.client.serviceSelector.interfaces.IServiceSelectorModule;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBusWithLookup;

/**
 * SupplierCreationEventBus servers all events for module SupplierCreationModule.
 * Provides supplier creation process.
 *
 * @author ivan.vlcek
 */
@Events(startPresenter = SupplierCreationPresenter.class, module = SupplierCreationModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface SupplierCreationEventBus extends EventBusWithLookup, BaseChildEventBus,
    ISupplierCreationModule.Gateway,
    CatLocSelectorGateway, UserRegistrationGateway, IServiceSelectorModule.Gateway {

    /**
     * Start event is called only when module is instantiated first time.
     * We can use it for history initialization.
     */
    @Start
    @Event(handlers = SupplierCreationPresenter.class)
    void start();

    /**
     * Forward event is called only if it is configured here. It there is nothing to carry out
     * in this method we should remove forward event to save the number of method invocations.
     * We can use forward event to switch css style for selected menu button.
     */
    @Forward
    @Event(handlers = SupplierCreationPresenter.class, navigationEvent = true)
    void forward();

    /**************************************************************************/
    /* Navigation events.                                                     */
    /**************************************************************************/
    /**
     * The only entry point to this module due to code-spliting feature.
     */
    @Override
    @Event(handlers = SupplierCreationPresenter.class, navigationEvent = true,
    historyConverter = SupplierCreationHistoryConverter.class, name = ISupplierCreationModule.NAME)
    void goToCreateSupplierModule();

    /**************************************************************************/
    /* Parent events                                                          */
    /**************************************************************************/
    @Event(forwardToParent = true)
    void logout(int widgetToLoad);

    @Event(forwardToParent = true)
    void checkCompanySelected();

    /**************************************************************************/
    /* Business events handled by Handlers.                                   */
    /**************************************************************************/
    @Event(handlers = SupplierCreationHandler.class)
    void requestRegisterSupplier(FullSupplierDetail newSupplier);

    @Event(handlers = SupplierCreationPresenter.class)
    void responseRegisterSupplier(FullSupplierDetail newSupplier);

    /**************************************************************************/
    /* Business events handled by Presenter.                                  */
    /**************************************************************************/
    @Event(handlers = SupplierCreationPresenter.class)
    void setUserRegistrationHeight(boolean company);
}