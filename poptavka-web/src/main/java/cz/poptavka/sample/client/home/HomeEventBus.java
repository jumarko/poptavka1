package cz.poptavka.sample.client.home;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.client.home.HomePresenter.AnchorEnum;
import cz.poptavka.sample.client.home.widget.locality.LocalitySelectorPresenter;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.address.LocalityType;

@Events(startView = HomeView.class, module = HomeModule.class)
public interface HomeEventBus extends EventBus {

    @Event(handlers = HomePresenter.class)
    void initHome();

    @Event(forwardToParent = true)
    void setBodyHolderWidget(Widget body);

    @Event(handlers = HomePresenter.class)
    void setAnchorWidget(AnchorEnum anchor, Widget content);

    @Event(handlers = HomeHandler.class)
    void getLocalities(LocalityType type);

    @Event(handlers = LocalitySelectorPresenter.class)
    void displayLocalityList(LocalityType type, List<Locality> list);

    @Event(handlers = LocalitySelectorPresenter.class)
    void initLocalitySelector(AnchorEnum anchor);
}
