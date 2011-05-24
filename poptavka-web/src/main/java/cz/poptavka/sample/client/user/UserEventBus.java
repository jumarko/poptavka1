package cz.poptavka.sample.client.user;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.client.common.messages.MessagesPresenter;
import cz.poptavka.sample.client.common.messages.message.MessagePresenter;
import cz.poptavka.sample.client.user.admin.AdministrationPresenter;
import cz.poptavka.sample.client.user.demands.DemandsLayoutPresenter;
import cz.poptavka.sample.client.user.demands.tab.MyDemandsOperatorPresenter;
import cz.poptavka.sample.client.user.demands.tab.MyDemandsPresenter;
import cz.poptavka.sample.client.user.demands.tab.NewDemandPresenter;
import cz.poptavka.sample.client.user.demands.tab.OffersPresenter;
import cz.poptavka.sample.client.user.problems.Problem;
import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.UserDetail;

@Events(startView = UserView.class, module = UserModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface UserEventBus extends EventBus {

    /** init method. **/
    @Event(handlers = {UserPresenter.class, DemandsLayoutPresenter.class },
    historyConverter = UserHistoryConverter.class)
    String atAccount(UserDetail user);

    @Event(handlers = DemandsLayoutPresenter.class)
    void setClientDemands(ArrayList<DemandDetail> demands);

    @Event(handlers = DemandsLayoutPresenter.class)
    void showDemandDetail(long demandId);

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

    /** DEVEL **/
    /** **/
    @Event(handlers = DemandsLayoutPresenter.class)
    void requestDemands();

    @Event(handlers = {MyDemandsPresenter.class, OffersPresenter.class })
    void responseDemands(ArrayList<DemandDetail> demands);

    @Event(handlers = OffersPresenter.class)
    void responseOffers(ArrayList<ArrayList<OfferDetail>> offers);

    /** DEVEL **/
    /** DEMAND tab methods. **/
    @Event(handlers = MyDemandsPresenter.class,
    activate = MyDemandsPresenter.class,
    deactivate = {OffersPresenter.class, NewDemandPresenter.class, MyDemandsOperatorPresenter.class },
    historyConverter = UserHistoryConverter.class)
    String invokeMyDemands();

    @Event(handlers = OffersPresenter.class,
    activate = OffersPresenter.class,
    deactivate = {MyDemandsPresenter.class, NewDemandPresenter.class, MyDemandsOperatorPresenter.class },
    historyConverter = UserHistoryConverter.class)
    String invokeOffers();

    @Event(handlers = NewDemandPresenter.class,
    activate = NewDemandPresenter.class,
    deactivate = {OffersPresenter.class, MyDemandsPresenter.class, MyDemandsOperatorPresenter.class },
    historyConverter = UserHistoryConverter.class)
    String invokeNewDemand();

    @Event(handlers = MyDemandsOperatorPresenter.class,
    activate = MyDemandsOperatorPresenter.class,
    deactivate = {OffersPresenter.class, MyDemandsPresenter.class, NewDemandPresenter.class },
    historyConverter = UserHistoryConverter.class)
    String invokeMyDemandsOperator();

    @Event(handlers = AdministrationPresenter.class,
    activate = AdministrationPresenter.class,
    deactivate = {OffersPresenter.class, MyDemandsPresenter.class,
            NewDemandPresenter.class, MyDemandsOperatorPresenter.class },
    historyConverter = UserHistoryConverter.class)
    String invokeAdministration();

    //for operator only
    @Event
    void invokeProblems();

    @Event(handlers = DemandsLayoutPresenter.class)
    void displayContent(Widget contentWidget);

    @Event(handlers = MessagesPresenter.class)
    void reply();

    @Event(handlers = MessagePresenter.class)
    void send();

    @Event(handlers = MessagesPresenter.class)
    void discard();

    @Event(handlers = MessagesPresenter.class)
    void displayMessages(Problem problem);

    /** handler method area. **/
    @Event(handlers = UserHandler.class)
    void getClientsDemands(long id);

    @Event(handlers = UserHandler.class)
    void requestOffers(ArrayList<Long> idList);

    @Event(handlers = OffersPresenter.class)
    void setDetailSection(Widget widget);

    @Event(handlers = MyDemandsPresenter.class)
    void getDemandDetail(String name);
}
