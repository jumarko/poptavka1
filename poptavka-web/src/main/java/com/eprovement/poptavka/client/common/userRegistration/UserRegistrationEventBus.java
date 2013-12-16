/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.common.userRegistration;

import com.eprovement.poptavka.client.root.gateways.AddressSelectorGateway;
import com.eprovement.poptavka.client.root.gateways.DetailModuleGateway;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.event.EventBusWithLookup;

/**
 * Used for registering user.
 *
 * @author Martin Slavkovsky
 */
@Events(startPresenter = UserRegistrationPresenter.class, module = UserRegistrationModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface UserRegistrationEventBus extends EventBusWithLookup,
        DetailModuleGateway, AddressSelectorGateway {

    /**
     * Start event is called only when module is instantiated first time.
     * We can use it for history initialization.
     */
    @Start
    @Event(handlers = UserRegistrationPresenter.class)
    void start();

    /**
     * Forward event is called only if it is configured here. It there is nothing to carry out
     * in this method we should remove forward event to save the number of method invocations.
     * We can use forward event to switch css style for selected menu button.
     */
    @Forward
    @Event(handlers = UserRegistrationPresenter.class)
    void forward();

    /**************************************************************************/
    /* Parent events                                                          */
    /**************************************************************************/
    /**
     * Create professional and create project user registration tab height
     * handlers.
    */
    @Event(forwardToParent = true)
    void setUserRegistrationHeight(boolean company);

    @Event(forwardToParent = true)
    void sendUsEmail(int subject, String errorId);

    @Event(forwardToParent = true)
    void displayError(int errorResponseCode, String errorId);

    /**************************************************************************/
    /* Business events.                                                       */
    /**************************************************************************/
    @Event(generate = UserRegistrationPresenter.class)
    void initUserRegistration(SimplePanel holderWidget);

    @Event(handlers = UserRegistrationHandler.class)
    void checkFreeEmail(String value);

    @Event(handlers = UserRegistrationPresenter.class)
    void checkFreeEmailResponse(Boolean result);

    @Event(handlers = UserRegistrationPresenter.class)
    void fillBusinessUserDetail(BusinessUserDetail userDetail);

    @Event(handlers = UserRegistrationPresenter.class)
    void checkCompanySelected();
}
