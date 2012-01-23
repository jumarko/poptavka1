package cz.poptavka.sample.client.homesettings;

import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Debug.LogLevel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.shared.domain.settings.SettingsDetail;

@Debug(logLevel = LogLevel.DETAILED)
@Events(startView = HomeSettingsView.class, module = HomeSettingsModule.class)
public interface HomeSettingsEventBus extends EventBus {

    @Event(handlers = HomeSettingsPresenter.class)
    void initSettings();

    @Event(handlers = HomeSettingsPresenter.class)
    void setSettings(SettingsDetail detail);

    @Event(handlers = HomeSettingsHandler.class)
    void getLoggedUser(long userId);

}
