package cz.poptavka.sample.client.home.demands;

import java.util.List;

import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.event.EventBus;
import com.mvp4g.client.annotation.Events;

import cz.poptavka.sample.domain.demand.Demand;

@Events(startView = DemandsView.class)
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
    void displayDemands(List<Demand> result);

}
