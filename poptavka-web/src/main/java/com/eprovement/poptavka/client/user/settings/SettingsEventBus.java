/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.settings;

import com.eprovement.poptavka.client.common.BaseChildEventBus;
import com.eprovement.poptavka.client.root.gateways.CatLocSelectorGateway;
import com.eprovement.poptavka.client.root.gateways.AddressSelectorGateway;
import com.eprovement.poptavka.client.root.gateways.InfoWidgetsGateway;
import com.eprovement.poptavka.client.serviceSelector.interfaces.IServiceSelectorModule;
import com.eprovement.poptavka.client.user.settings.interfaces.IClientSettings;
import com.eprovement.poptavka.client.user.settings.interfaces.ISecuritySettings;
import com.eprovement.poptavka.client.user.settings.interfaces.ISupplierSettings;
import com.eprovement.poptavka.client.user.settings.interfaces.ISystemSettings;
import com.eprovement.poptavka.client.user.settings.interfaces.IUserSettings;
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Debug.LogLevel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.annotation.Start;

import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.mvp4g.client.event.EventBusWithLookup;

/**
 * Provides settings interface for user to change his user data, settings, etc.
 * @author Martin Slavkovsky
 */
@Debug(logLevel = LogLevel.DETAILED)
@Events(startPresenter = SettingsPresenter.class, module = SettingsModule.class)
public interface SettingsEventBus extends EventBusWithLookup, BaseChildEventBus,
    CatLocSelectorGateway, IServiceSelectorModule.Gateway, AddressSelectorGateway, InfoWidgetsGateway,
    IUserSettings.Gateway, IClientSettings.Gateway, ISupplierSettings.Gateway, ISystemSettings.Gateway,
    ISecuritySettings.Gateway {

    /**
     * Start event is called only when module is instantiated first time.
     * We can use it for history initialization.
     */
    @Start
    @Event(handlers = SettingsPresenter.class)
    void start();

    /**
     * Forward event is called only if it is configured here. It there is nothing to carry out
     * in this method we should remove forward event to save the number of method invocations.
     * We can use forward event to switch css style for selected menu button.
     */
    @Forward
    @Event(handlers = SettingsPresenter.class, navigationEvent = true)
    void forward();

    /**************************************************************************/
    /* Navigation events.                                                     */
    /**************************************************************************/
    /**
     * The only entry point to this module due to code-spliting feature.
     */
    @Event(handlers = SettingsPresenter.class, historyConverter = SettingsHistoryConverter.class,
        navigationEvent = true)
    String goToSettingsModule();

    /**************************************************************************/
    /* Parent events                                                          */
    /**************************************************************************/
    @Event(forwardToParent = true)
    void setUpdatedUnreadMessagesCount(UnreadMessagesDetail numberOfMessages);

    /**************************************************************************/
    /* Business events handled by Presenters.                                 */
    /**************************************************************************/
    @Event(handlers = SettingsPresenter.class)
    void setSettings(SettingDetail detail);

    @Event(handlers = SettingsPresenter.class)
    void responseUpdateSettings(Boolean updated);

    /**************************************************************************/
    /* Business events handled by Handlers.                                   */
    /**************************************************************************/
    @Event(handlers = SettingsHandler.class)
    void getLoggedUser(long userId);

    @Event(handlers = SettingsHandler.class)
    void updateUnreadMessagesCount();

    @Event(handlers = SettingsHandler.class)
    void requestUpdateSettings(SettingDetail settingsDetail);
}
