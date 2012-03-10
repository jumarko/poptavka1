/*
 * HomeWelcomEventBus is very first home page to be loaded.
 *
 * Specification:
 * Wireframe: http://www.webgres.cz/axure/
 */
package cz.poptavka.sample.client.homeWelcome;

import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;

@Events(startView = HomeWelcomeView.class, module = HomeWelcomeModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface HomeWelcomeEventBus extends EventBus {

    /**
     * First event to be handled.
     */
    @Start
    @Event(handlers = HomeWelcomePresenter.class)
    void start();

    /**
     * Forward event is called only if it is configured here. If there is nothing to carry out
     * in this method we should remove forward event to save the number of method invocations.
     */
    @Forward
    @Event(handlers = HomeWelcomePresenter.class)
    void forward();

    /**************************************************************************/
    /* Navigation events. */
    /**************************************************************************/
    /* Parent events */
    @Event(forwardToParent = true)
    void setHomeBodyHolderWidget(IsWidget view);

    /**************************************************************************/
    /* Business events handled by Presenters. */
    @Event(handlers = HomeWelcomePresenter.class, historyConverter = HomeWelcomeHistoryConverter.class)
    String initHomeWelcomeModule(SearchModuleDataHolder filter);
    /**************************************************************************/
    /* Business events handled by Handlers. */
}
