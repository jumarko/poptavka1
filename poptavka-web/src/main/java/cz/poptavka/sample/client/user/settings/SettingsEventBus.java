package cz.poptavka.sample.client.user.settings;

import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Debug.LogLevel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.shared.domain.settings.SettingsDetail;

@Debug(logLevel = LogLevel.DETAILED)
@Events(startView = SettingsView.class, module = SettingsModule.class)
public interface SettingsEventBus extends EventBus {

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
    void clickSettingsUserMenuStyleChange();

    /**************************************************************************/
    /* Business events handled by Presenters.                                 */
    /**************************************************************************/
    @Event(handlers = SettingsPresenter.class)
    void setSettings(SettingsDetail detail);

    /**************************************************************************/
    /* Business events handled by Handlers.                                   */
    /**************************************************************************/
    @Event(handlers = SettingsHandler.class)
    void getLoggedUser(long userId);
}
