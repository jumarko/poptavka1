package cz.poptavka.sample.client.home.demands;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.event.EventBus;
import com.mvp4g.client.annotation.Events;

import cz.poptavka.sample.client.home.HomePresenter.AnchorEnum;

@Events(startView = DemandsView.class, module = DemandsModule.class)
public interface DemandsEventBus extends EventBus {

    /**
     * Initialize demands presenter.
     */
    @Event(handlers = DemandsPresenter.class)
    void start();

    /**
     * Display demands on success initialization of demands presenter.
     * @param result - retrieved demands
     */
    @Event(handlers = DemandsPresenter.class)
    void displayDemands();

    @Event(forwardToParent = true)
    void setAnchorWidget(AnchorEnum anchor, Widget content);
}
