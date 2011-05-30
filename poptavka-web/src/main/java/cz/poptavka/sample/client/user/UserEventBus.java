package cz.poptavka.sample.client.user;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBusWithLookup;

import cz.poptavka.sample.client.common.messages.MessagesPresenter;
import cz.poptavka.sample.client.common.messages.message.MessagePresenter;
import cz.poptavka.sample.client.user.admin.AdminDemandInfoPresenter;
import cz.poptavka.sample.client.user.admin.AdministrationPresenter;
import cz.poptavka.sample.client.user.demands.DemandsHistoryConverter;
import cz.poptavka.sample.client.user.demands.DemandsLayoutPresenter;
import cz.poptavka.sample.client.user.demands.tab.MyDemandsOperatorPresenter;
import cz.poptavka.sample.client.user.demands.tab.MyDemandsPresenter;
import cz.poptavka.sample.client.user.demands.tab.NewDemandPresenter;
import cz.poptavka.sample.client.user.demands.tab.OffersPresenter;
import cz.poptavka.sample.client.user.demands.tab.PotentialDemandsPresenter;
import cz.poptavka.sample.client.user.demands.widgets.DetailWrapperPresenter;
import cz.poptavka.sample.client.user.problems.Problem;
import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.domain.demand.DetailType;

@Events(startView = UserView.class, module = UserModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface UserEventBus extends EventBusWithLookup {

    /** init method. **/
    @Event(handlers = { UserPresenter.class }, historyConverter = UserHistoryConverter.class)
    String atAccount();

    /** getter for UserDetail. **/
    @Event(handlers = UserHandler.class)
    void getUser();

    /**
     * setter of userDetail for whole application. This user represents logged
     * user
     */
    @Event(handlers = UserPresenter.class)
    void setUser(UserDetail user);

    /** init call for DemandsLayoutPresenter, if one of user's roles is CLIENT **/
    @Event(handlers = UserHandler.class)
    void getClientsDemands(Long id);

    @Event(handlers = DemandsLayoutPresenter.class)
    void setClientDemands(ArrayList<DemandDetail> demands);

    @Event(handlers = DemandsLayoutPresenter.class)
    void addNewDemand(DemandDetail result);

    /**
     * For switching between main tabs like Demands | Messages | Settings | etc.
     *
     * @param tabBody
     *            current widget
     */
    @Event(handlers = UserPresenter.class)
    void setTabWidget(Widget tabBody);

    /** DEVEL **/
    @Event(handlers = OffersPresenter.class)
    void responseOffers(ArrayList<ArrayList<OfferDetail>> offers);

    // for operator only
    @Event
    void invokeProblems();

    /** Messages secition **/
    @Event(handlers = MessagesPresenter.class)
    void reply();

    @Event(handlers = MessagePresenter.class)
    void send();

    @Event(handlers = MessagesPresenter.class)
    void discard();

    @Event(handlers = MessagesPresenter.class)
    void displayMessages(Problem problem);

    /** handler method area. **/

    /** REQUESTs && RESPONSEs **/
    // Always in pairs
    /** get client id. **/
    @Event(handlers = UserPresenter.class)
    void requestClientId();

    @Event(handlers = NewDemandPresenter.class)
    void responseClientId(Long clientId);

    @Event(handlers = DemandsLayoutPresenter.class)
    void requestClientDemands();

    @Event(handlers = { MyDemandsPresenter.class,
            MyDemandsOperatorPresenter.class,
            // TODO Beho - create own RPC call for supplier
            // this is just while development
            PotentialDemandsPresenter.class }, passive = true)
    void responseClientDemands(ArrayList<DemandDetail> demands);

    /**
     * common method for displaying Demand Detail in the window.
     * **/
    // TODO implements demandDetail section loading for Wrapper
    @Event(handlers = {UserHandler.class, DetailWrapperPresenter.class })
    void getDemandDetail(Long demandId, DetailType typeOfDetail);

    @Event(handlers = DetailWrapperPresenter.class, passive = true)
    void setDemandDetail(DemandDetail detail, DetailType typeOfDetail);

    /**
     * method for displaying conversation to selected demand
     **/
    // TODO
    // @Event(handlers = UserHandler.class)
    // void getMessageTree(long demandId);
    //
    // @Event(handlers = DetailWrapperPresenter.class)
    // void setMessageTree(param);

    // TODO delete
    // @Event(handlers = {MyDemandsPresenter.class, OffersPresenter.class,
    // MyDemandsOperatorPresenter.class, PotentialDemandsPresenter.class },
    // passive = true)
    // void responseDemandDetail(Widget widget);

    // TODO delete
//    @Event
//    void responseDemandDetail(DemandDetail detail);

    /** get supplier Id. **/
    @Event(handlers = UserPresenter.class)
    void requestSupplierId();

    @Event
    void responseSupplierId(Long supplierId);

    /** TODO - ivlcek this could be replaced to new EventBus for Admin **/
    @Event(handlers = UserHandler.class)
    void getAllDemands();

    @Event(handlers = UserHandler.class)
    void updateDemand(DemandDetail demand);

    @Event(handlers = AdministrationPresenter.class)
    void refreshUpdatedDemand(DemandDetail demand);

    @Event(handlers = AdministrationPresenter.class)
    void setAllDemands(List<DemandDetail> demands);

    @Event(handlers = AdminDemandInfoPresenter.class)
    void showAdminDemandDetail(DemandDetail selectedObject);

    @Event(handlers = AdministrationPresenter.class)
    void responseAdminDemandDetail(Widget widget);

    @Event(handlers = UserHandler.class)
    void requestOffers(ArrayList<Long> idList);

    /** Call to UserPresenter **/
    /**
     * Display User View - parent Widget for client-logged user section.
     *
     * @param body
     */
    @Event(forwardToParent = true)
    void setBodyHolderWidget(Widget body);

    /** display child widget **/
    @Event(handlers = DemandsLayoutPresenter.class)
    void displayContent(Widget contentWidget);

    /**
     * Navigation events section. /* Presenters do NOT listen to events when
     * deactivated
     **/
    @Event(handlers = MyDemandsPresenter.class, activate = MyDemandsPresenter.class, deactivate = {
            OffersPresenter.class, NewDemandPresenter.class,
            PotentialDemandsPresenter.class, MyDemandsOperatorPresenter.class,
            AdministrationPresenter.class }, historyConverter = DemandsHistoryConverter.class)
    String invokeMyDemands();

    @Event(handlers = OffersPresenter.class, activate = OffersPresenter.class, deactivate = {
            MyDemandsPresenter.class, NewDemandPresenter.class,
            PotentialDemandsPresenter.class, MyDemandsOperatorPresenter.class,
            AdministrationPresenter.class }, historyConverter = DemandsHistoryConverter.class, name = "lambada")
    String invokeOffers();

    @Event(handlers = NewDemandPresenter.class, activate = NewDemandPresenter.class, deactivate = {
            OffersPresenter.class, MyDemandsPresenter.class,
            PotentialDemandsPresenter.class, MyDemandsOperatorPresenter.class,
            AdministrationPresenter.class }, historyConverter = DemandsHistoryConverter.class)
    String invokeNewDemand();

    @Event(handlers = PotentialDemandsPresenter.class, activate = PotentialDemandsPresenter.class, deactivate = {
            OffersPresenter.class, MyDemandsPresenter.class,
            NewDemandPresenter.class, MyDemandsOperatorPresenter.class,
            AdministrationPresenter.class }, historyConverter = DemandsHistoryConverter.class)
    String invokePotentialDemands();

    @Event(handlers = MyDemandsOperatorPresenter.class, activate = MyDemandsOperatorPresenter.class, deactivate = {
            OffersPresenter.class, MyDemandsPresenter.class,
            PotentialDemandsPresenter.class, NewDemandPresenter.class,
            AdministrationPresenter.class }, historyConverter = DemandsHistoryConverter.class)
    String invokeMyDemandsOperator();

    @Event(handlers = AdministrationPresenter.class, activate = AdministrationPresenter.class, deactivate = {
            OffersPresenter.class, MyDemandsPresenter.class,
            PotentialDemandsPresenter.class, NewDemandPresenter.class,
            MyDemandsOperatorPresenter.class }, historyConverter = DemandsHistoryConverter.class)
    String invokeAdministration();

    /** Navigation Events section END **/

    /** marks event to load right after main UI is displayed. */
    @Event(handlers = UserPresenter.class)
    void markEventToLoad(String historyName);

    /** Calls to MainEventBus **/
    @Event(forwardToParent = true)
    void initDemandBasicForm(SimplePanel holderPanel);

    @Event(forwardToParent = true)
    void initCategoryWidget(SimplePanel holderPanel);

    @Event(forwardToParent = true)
    void initLocalityWidget(SimplePanel holderPanel);

    @Event(forwardToParent = true)
    void initDemandAdvForm(SimplePanel holderPanel);

    @Event(forwardToParent = true)
    void loadingShow(String progressGettingDemandData);

    @Event(forwardToParent = true)
    void loadingShowWithAnchor(String progressGettingDemandDataring, Widget anchor);

    @Event(forwardToParent = true)
    void createDemand(DemandDetail demand, Long id);

    @Event(forwardToParent = true)
    void setUserLayout();

    @Event(forwardToParent = true)
    void loadingHide();

    @Event(handlers = UserPresenter.class)
    void setUserInteface(StyleInterface widgetView);

    @Event(handlers = UserPresenter.class)
    void clearUserOnUnload();

    /**
     * hacky later fire event Needed when refreshing in User Section - refresh
     * not neededi in prod
     **/
    @Event(handlers = UserPresenter.class)
    void fireMarkedEvent();

}
