package cz.poptavka.sample.client.user;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.client.user.listOfDemands.ListOfDemandsPresenter;

@Events(startView = UserView.class, module = UserModule.class)
public interface UserEventBus extends EventBus {

    /** init method. **/
    @Event(handlers = UserPresenter.class)
    void initUser();

    /**
     * Display User View - parent Widget for client-logged user section.
     *
     * @param body
     */
    @Event(forwardToParent = true)
    void setBodyHolderWidget(Widget body);

    @Event(handlers = UserPresenter.class)
    void setTabWidget(Widget tabBody);

    @Event(handlers = ListOfDemandsPresenter.class)
    void setListOfDemandsWidget();

    @Event(forwardToParent = true)
    void listOfDemandsWidget(Widget widget);
}
