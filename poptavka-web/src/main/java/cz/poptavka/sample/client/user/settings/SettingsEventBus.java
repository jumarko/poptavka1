package cz.poptavka.sample.client.user.settings;

import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Debug.LogLevel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.shared.domain.settings.SettingsDetail;

@Debug(logLevel = LogLevel.DETAILED)
@Events(startView = SettingsView.class, module = SettingsModule.class)
public interface SettingsEventBus extends EventBus {

    @Event(handlers = SettingsPresenter.class)
    void initSettings();

    @Event(handlers = SettingsPresenter.class)
    void setSettings(SettingsDetail detail);

    @Event(handlers = SettingsHandler.class)
    void getLoggedUser(long userId);

}
