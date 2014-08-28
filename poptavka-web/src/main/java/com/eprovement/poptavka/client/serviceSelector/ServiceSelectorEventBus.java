/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.serviceSelector;

import com.eprovement.poptavka.client.common.BaseChildEventBus;
import com.eprovement.poptavka.domain.enums.ServiceType;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.eprovement.poptavka.shared.domain.UserServiceDetail;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.event.EventBusWithLookup;
import java.util.ArrayList;
import java.util.List;

/**
 * Used for selecting services during registration process.
 *
 * @author Martin Slavkovsky
 */
@Events(startPresenter = ServiceSelectorPresenter.class, module = ServiceSelectorModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface ServiceSelectorEventBus extends EventBusWithLookup, BaseChildEventBus {

    /**
     * Start event is called only when module is instantiated first time.
     * We can use it for history initialization.
     */
    @Start
    @Event(handlers = ServiceSelectorPresenter.class)
    void start();

    /**
     * Forward event is called only if it is configured here. It there is nothing to carry out
     * in this method we should remove forward event to save the number of method invocations.
     * We can use forward event to switch css style for selected menu button.
     */
    @Forward
    @Event(handlers = ServiceSelectorPresenter.class, navigationEvent = true)
    void forward();

    /**************************************************************************/
    /* Business events handled by Presenter.                                  */
    /**************************************************************************/
    @Event(handlers = ServiceSelectorPresenter.class)
    void initServicesWidget(SimplePanel embedToWidget);

    @Event(handlers = ServiceSelectorPresenter.class)
    void initServicesWidget2(SimplePanel embedToWidget, String infoLabel);

    @Event(handlers = ServiceSelectorPresenter.class)
    void displayServices(ArrayList<ServiceDetail> services);

    @Event(handlers = ServiceSelectorPresenter.class)
    void fillServices(List<ServiceDetail> services);

    @Event(handlers = ServiceSelectorPresenter.class)
    void selectService(ServiceDetail service);

    /**************************************************************************/
    /* Business events handled by Handler.                                    */
    /**************************************************************************/
    @Event(handlers = ServiceSelectorHandler.class)
    void requestServices(ServiceType... serviceTypes);

    /**
     * Request/Response pair for creating user services.
     * @param userId
     * @param serviceDetail
     */
    @Event(handlers = ServiceSelectorHandler.class)
    void requestCreateUserService(long userId, ServiceDetail serviceDetail);

    @Event(handlers = ServiceSelectorPresenter.class)
    void responseCreateUserService(UserServiceDetail userServiceDetail);
}
