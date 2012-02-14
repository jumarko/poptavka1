package cz.poptavka.sample.client.homeWelcome;

import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;

@Events(startView = HomeWelcomeView.class, module = HomeWelcomeModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface HomeWelcomeEventBus extends EventBus {

    @Start
    @Event(handlers = HomeWelcomePresenter.class)
    void start();

    @Event(forwardToParent = true)
    void setHomeBodyHolderWidget(IsWidget view);

    @Event(handlers = HomeWelcomePresenter.class)
    void initHomeWelcomeModule(SearchModuleDataHolder filter);

}
