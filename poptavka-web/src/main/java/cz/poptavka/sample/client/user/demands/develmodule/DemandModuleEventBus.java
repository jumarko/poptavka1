package cz.poptavka.sample.client.user.demands.develmodule;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Debug.LogLevel;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.client.user.demands.develmodule.s.list.SupplierListPresenter;
import cz.poptavka.sample.shared.domain.message.PotentialDemandMessage;

@Debug(logLevel = LogLevel.DETAILED)
@Events(startView = DemandModuleView.class, module = DemandModule.class)
public interface DemandModuleEventBus extends EventBus {

    //init demands module
    @Event(handlers = DemandModulePresenter.class)
    void initDemandModule(SimplePanel panel);

    //display widget in content area
    @Event(handlers = DemandModulePresenter.class)
    void displayView(Widget content);

    //production init method
    //during development used multiple instancing
    @Event(handlers = SupplierListPresenter.class)
    void initSupplierList();

    @Event(handlers = DemandModuleHandler.class)
    void requestSupplierNewDemands();

    @Event(handlers = SupplierListPresenter.class)
    void responseSupplierNewDemands(ArrayList<PotentialDemandMessage> result);
}
