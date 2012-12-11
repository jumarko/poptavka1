package com.eprovement.poptavka.client.user.settings;

import com.eprovement.poptavka.client.root.BaseChildEventBus;
import com.eprovement.poptavka.client.user.settings.widget.UserSettingsPresenter;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Debug.LogLevel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.annotation.Start;

import com.eprovement.poptavka.shared.domain.settings.SettingsDetail;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.event.EventBusWithLookup;

@Debug(logLevel = LogLevel.DETAILED)
@Events(startPresenter = SettingsPresenter.class, module = SettingsModule.class)
public interface SettingsEventBus extends EventBusWithLookup, BaseChildEventBus {

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
    @Event(handlers = SettingsPresenter.class)
    void forward();

    /**************************************************************************/
    /* Navigation events.                                                     */
    /**************************************************************************/
    /**
     * The only entry point to this module due to code-spliting feature.
     */
    @Event(handlers = SettingsPresenter.class, historyConverter = SettingsHistoryConverter.class)
    String goToSettingsModule();

    /**************************************************************************/
    /* Parent events                                                          */
    /**************************************************************************/
    @Event(forwardToParent = true)
    void loadingShow(String loadingMessage);

    @Event(forwardToParent = true)
    void loadingHide();

    @Event(forwardToParent = true)
    void userMenuStyleChange(int loadedModule);

    @Event(forwardToParent = true)
    void initAddressWidget(SimplePanel embedToWidget);

    @Event(forwardToParent = true)
    void initCategoryWidget(SimplePanel holderWidget, int checkboxes, int displayCountsOfWhat);

    @Event(forwardToParent = true)
    void initLocalityWidget(SimplePanel holderWidget, int checkboxes, int displayCountsOfWhat);

    // TODO ivlcek - method loginFromSession() should be available for every module that can be accessed
    // just by entering URL into browser
//    @Event(forwardToParent = true)
//    void loginFromSession();
    @Event(forwardToParent = true)
    void setUpdatedUnreadMessagesCount(int numberOfMessages);

    @Event(forwardToParent = true)
    void loginFromSession();

    /**************************************************************************/
    /* Business events handled by Presenters.                                 */
    /**************************************************************************/
    @Event(handlers = SettingsPresenter.class)
    void setSettings(SettingsDetail detail);

    /**************************************************************************/
    /* Business events handled by UserSettingsPresenter.                      */
    /**************************************************************************/
    @Event(handlers = UserSettingsPresenter.class)
    void setUserSettings(SettingsDetail detail);

    /**************************************************************************/
    /* Business events handled by Handlers.                                   */
    /**************************************************************************/
    @Event(handlers = SettingsHandler.class)
    void getLoggedUser(long userId);

    @Event(handlers = SettingsHandler.class)
    void updateUnreadMessagesCount();
}
