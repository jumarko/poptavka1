package cz.poptavka.sample.client.user;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.client.user.demands.DemandsLayoutPresenter;
import cz.poptavka.sample.client.user.demands.tab.MyDemandsPresenter;
import cz.poptavka.sample.client.user.demands.tab.NewDemandPresenter;
import cz.poptavka.sample.client.user.demands.tab.OffersPresenter;


@Events(startView = UserView.class, module = UserModule.class)
public interface UserEventBus extends EventBus {

    /** init method. **/
    @Event(handlers = {UserPresenter.class, DemandsLayoutPresenter.class },
            historyConverter = UserHistoryConverter.class)
    String atAccount();

    @Event(forwardToParent = true)
    void setUserLayout();

    /**
     * Display User View - parent Widget for client-logged user section.
     *
     * @param body
     */
    @Event(forwardToParent = true)
    void setBodyHolderWidget(Widget body);

    /**
     * For switching between main tabs like Demands | Messages | Settings | etc.
     *
     * @param tabBody current widget
     */
    @Event(handlers = UserPresenter.class)
    void setTabWidget(Widget tabBody);

    /** DEMAND tab methods **/
    @Event(handlers = MyDemandsPresenter.class, historyConverter = UserHistoryConverter.class)
    String invokeMyDemands();

    @Event(handlers = OffersPresenter.class, historyConverter = UserHistoryConverter.class)
    String invokeOffers();

    //do not forget about common package
    @Event(handlers = NewDemandPresenter.class, historyConverter = UserHistoryConverter.class)
    String invokeNewDemand();

    //for operator only
    @Event
    void invokeProblems();

    @Event(handlers = DemandsLayoutPresenter.class)
    void displayContent(Widget contentWidget);
}
