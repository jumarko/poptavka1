package cz.poptavka.sample.client.common.creation;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.client.home.HomePresenter.AnchorEnum;

@Events(startView = CreationView.class, module = CreationModule.class)
public interface CreationEventBus extends EventBus {

    @Start
    @Event(handlers = CreationPresenter.class)
    void initDemandCreation();

    @Event(forwardToParent = true)
    void setAnchorWidget(AnchorEnum anchor, Widget content);
}
