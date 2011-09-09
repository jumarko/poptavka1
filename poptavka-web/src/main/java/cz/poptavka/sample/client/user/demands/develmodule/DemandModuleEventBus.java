package cz.poptavka.sample.client.user.demands.develmodule;

import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBus;

@Events(startView = DemandModuleView.class, module = DemandModule.class)
public interface DemandModuleEventBus extends EventBus {

    //init demands module
    @Event(handlers = DemandModulePresenter.class)
    void initDemandModule();
}
