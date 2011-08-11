package cz.poptavka.sample.client.user;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBusWithLookup;

import cz.poptavka.sample.client.user.admin.AdminLayoutPresenter;
import cz.poptavka.sample.client.user.admin.tab.AdminDemandInfoPresenter;
import cz.poptavka.sample.client.user.admin.tab.AdminOfferInfoPresenter;
import cz.poptavka.sample.client.user.admin.tab.AdminOffersPresenter;
import cz.poptavka.sample.client.user.admin.tab.AdministrationPresenter;
import cz.poptavka.sample.client.user.admin.tab.DemandsOperatorPresenter;
import cz.poptavka.sample.client.user.demands.DemandsHistoryConverter;
import cz.poptavka.sample.client.user.demands.DemandsLayoutPresenter;
import cz.poptavka.sample.client.user.demands.tab.MyDemandsPresenter;
import cz.poptavka.sample.client.user.demands.tab.NewDemandPresenter;
import cz.poptavka.sample.client.user.demands.tab.OffersPresenter;
import cz.poptavka.sample.client.user.demands.tab.PotentialDemandsPresenter;
import cz.poptavka.sample.client.user.demands.widget.DetailWrapperPresenter;
import cz.poptavka.sample.client.user.handler.MessageHandler;
import cz.poptavka.sample.client.user.handler.UserHandler;
import cz.poptavka.sample.client.user.problems.MyProblemsHistoryConverter;
import cz.poptavka.sample.client.user.problems.MyProblemsPresenter;
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.domain.demand.BaseDemandDetail;
import cz.poptavka.sample.shared.domain.demand.DemandDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import cz.poptavka.sample.shared.domain.message.ClientDemandMessageDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.message.OfferDemandMessage;
import cz.poptavka.sample.shared.domain.message.OfferMessageDetail;
import cz.poptavka.sample.shared.domain.message.PotentialDemandMessage;
import cz.poptavka.sample.shared.domain.offer.FullOfferDetail;
import cz.poptavka.sample.shared.domain.type.ViewType;

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
     * setter of userDetail for whole application. This user represents logged user
     *
     * @param user
     */
    @Event(handlers = UserPresenter.class)
    void setUser(UserDetail user);

    /** Client demands list GETTER/SETTER **/
    @Event(handlers = UserPresenter.class)
    void requestClientDemands();

    /** Potential demands GETTER/SETTER. **/
    @Event(handlers = UserPresenter.class)
    void requestPotentialDemands();
    @Event(handlers = MessageHandler.class)
    void getPotentialDemands(long businessUserId);
    @Event(handlers = PotentialDemandsPresenter.class)
    void responsePotentialDemands(ArrayList<PotentialDemandMessage> potentialDemandsList);

    /** Offer demands GETTER/SETTER. **/
    // this same method could be called to MyDemandsPresenter
    // depends on it's demandDetailType and what will be difference in data, probably different
    // method will be used
    @Event(handlers = UserPresenter.class)
    void requestClientOfferDemands();
    @Event(handlers = MessageHandler.class)
    void getClientDemandsWithOffers(Long businessUserId);
    @Event(handlers = OffersPresenter.class)
    void responseClientDemandsWithOffers(ArrayList<OfferDemandMessage> result);

    /** Demand Offers GETTER/SETTER. **/
    @Event(handlers = UserHandler.class)
    void getDemandOffers(long demandId, long threadRootId);
    @Event(handlers = OffersPresenter.class)
    void setDemandOffers(ArrayList<OfferDetail> offers);

    /**
     * For switching between main tabs like Demands | Messages | Settings | etc.
     *
     * @param tabBody
     *            current widget
     */
    @Event(handlers = UserPresenter.class)
    void setTabWidget(Widget tabBody);

    @Event(handlers = UserPresenter.class)
    void setTabAdminWidget(Widget tabBody);

    // for operator only
//    @Event
//    void invokeMyProblems();

    /**
     * Handlers for widget MyProblems.
     */
    @Event(handlers = MyProblemsPresenter.class)
    void requestMyProblems();

    /** handler method area. **/

    /** REQUESTs && RESPONSEs **/
    // Always in pairs
    /** get client id. **/
    @Event(handlers = UserPresenter.class)
    void requestClientId(FullDemandDetail newDemand);
//
//    @Event(handlers = { MyDemandsPresenter.class, DemandsOperatorPresenter.class }, passive = true)
//    void responseClientDemands(ArrayList<FullDemandDetail> demands);

    /**
     * common method for displaying Demand Detail in the window.
     * **/
    // TODO implements demandDetail section loading for Wrapper
    @Event(handlers = { UserHandler.class,
            // serves for visual sing, that content is loading
            DetailWrapperPresenter.class })
    void getDemandDetail(Long demandId, ViewType typeOfDetail);

    @Event(handlers = DetailWrapperPresenter.class, passive = true)
    void setFullDemandDetail(FullDemandDetail detail);

    @Event(handlers = DetailWrapperPresenter.class, passive = true)
    void setBaseDemandDetail(BaseDemandDetail detail);

    /** method for displaying conversation to selected demand. **/
    @Event(handlers = {UserPresenter.class, DetailWrapperPresenter.class })
    void requestPotentialDemandConversation(long messageId, long userMessageId);

    @Event(handlers = MessageHandler.class)
    void getPotentialDemandConversation(long messageId, long userId, long userMessageId);

    @Event(handlers = DetailWrapperPresenter.class, passive = true)
    void setPotentialDemandConversation(ArrayList<MessageDetail> messageList, ViewType wrapperhandlerType);

    @Event(handlers = DetailWrapperPresenter.class, passive = true)
    void setSingleDemandConversation(ArrayList<MessageDetail> messageList);

    /**
     * Bubbling message to send to UserPresenter to get the user ID.
     *
     * @param messageToSend
     *            message to be sent
     */
    @Event(handlers = UserPresenter.class)
    void bubbleMessageSending(MessageDetail messageToSend, ViewType viewType);

    @Event(handlers = MessageHandler.class)
    void sendMessageToPotentialDemand(MessageDetail messageToSend, ViewType viewType);

    @Event(handlers = DetailWrapperPresenter.class)
    void addMessageToPotentailDemandConversation(MessageDetail result, ViewType wrapperhandlerType);

    /** Offers message display & state change. **/
    @Event(handlers = DetailWrapperPresenter.class)
    void setOfferMessage(OfferDetail offerDetail);

    @Event(handlers = MessageHandler.class)
    void getOfferStatusChange(OfferDetail offerDetail);

    @Event(handlers = OffersPresenter.class)
    void setOfferDetailChange(OfferDetail offerDetail);

    /**
     * Bubbling offer to send to UserPresenter to get the user ID and supplier ID.
     *
     * @param messageToSend
     *            message to be sent
     */
    @Event(handlers = UserPresenter.class)
    void bubbleOfferSending(OfferMessageDetail offerToSend);

    @Event(handlers = MessageHandler.class)
    void sendDemandOffer(OfferMessageDetail offerToSend);

    @Event(handlers = MessageHandler.class)
    void requestPotentialDemandReadStatusChange(ArrayList<Long> messages, boolean isRead);

    /** TODO - ivlcek this could be replaced to new EventBus for Admin. **/
    @Event(handlers = UserHandler.class)
    void getAllDemands();

    @Event(handlers = UserHandler.class)
    void updateDemand(FullDemandDetail demand);

    @Event(handlers = UserHandler.class)
    void updateOffer(FullOfferDetail offer);

    @Event(handlers = AdministrationPresenter.class)
    void refreshUpdatedDemand(FullDemandDetail demand);

    @Event(handlers = AdministrationPresenter.class)
    void refreshUpdatedOffer(FullOfferDetail demand);

    @Event(handlers = AdministrationPresenter.class)
    void setAllDemands(List<DemandDetail> demands);

    @Event(handlers = AdminDemandInfoPresenter.class)
    void showAdminDemandDetail(FullDemandDetail selectedObject);

    @Event(handlers = AdminOfferInfoPresenter.class)
    void showAdminOfferDetail(FullOfferDetail selectedObject);

    @Event(handlers = AdministrationPresenter.class)
    void responseAdminDemandDetail(Widget widget);

    @Event(handlers = AdministrationPresenter.class)
    void responseAdminOfferDetail(Widget widget);

    /** Call to UserPresenter **/
    /**
     * Display User View - parent Widget for client-logged user section.
     *
     * @param body
     */
    @Event(forwardToParent = true)
    void setBodyHolderWidget(Widget body);

    /** display child widget. **/
    @Event(handlers = DemandsLayoutPresenter.class)
    void displayContent(Widget contentWidget);

    /**
     * Navigation events section. /* Presenters do NOT listen to events when deactivated
     **/
    @Event(handlers = MyDemandsPresenter.class, activate = MyDemandsPresenter.class, deactivate = {
            OffersPresenter.class, NewDemandPresenter.class, PotentialDemandsPresenter.class,
            DemandsOperatorPresenter.class, AdministrationPresenter.class },
            historyConverter = DemandsHistoryConverter.class)
    String invokeMyDemands();

//    @Event(handlers = MyProblemsPresenter.class, activate = MyProblemsPresenter.class, deactivate = {
//            OffersPresenter.class, NewDemandPresenter.class, PotentialDemandsPresenter.class,
//            DemandsOperatorPresenter.class, AdministrationPresenter.class },
//            historyConverter = DemandsHistoryConverter.class)
    @Event(handlers = MyProblemsPresenter.class, historyConverter = MyProblemsHistoryConverter.class)
    String invokeMyProblems();

    @Event(handlers = OffersPresenter.class, activate = OffersPresenter.class, deactivate = { MyDemandsPresenter.class,
            NewDemandPresenter.class, PotentialDemandsPresenter.class, DemandsOperatorPresenter.class,
            AdministrationPresenter.class }, historyConverter = DemandsHistoryConverter.class)
    String invokeOffers();

    @Event(handlers = NewDemandPresenter.class, activate = NewDemandPresenter.class, deactivate = {
            OffersPresenter.class, MyDemandsPresenter.class, PotentialDemandsPresenter.class,
            DemandsOperatorPresenter.class, AdministrationPresenter.class },
            historyConverter = DemandsHistoryConverter.class)
    String invokeNewDemand();

    @Event(handlers = PotentialDemandsPresenter.class, activate = PotentialDemandsPresenter.class, deactivate = {
            OffersPresenter.class, MyDemandsPresenter.class, NewDemandPresenter.class, DemandsOperatorPresenter.class,
            AdministrationPresenter.class, AdminOffersPresenter.class },
            historyConverter = DemandsHistoryConverter.class)
    String invokePotentialDemands();

    @Event(handlers = DemandsOperatorPresenter.class, activate = DemandsOperatorPresenter.class, deactivate = {
            OffersPresenter.class, MyDemandsPresenter.class, PotentialDemandsPresenter.class, NewDemandPresenter.class,
            AdministrationPresenter.class, AdminOffersPresenter.class },
            historyConverter = DemandsHistoryConverter.class)
    String invokeDemandsOperator();

    @Event(handlers = AdministrationPresenter.class, activate = AdministrationPresenter.class, deactivate = {
            OffersPresenter.class, MyDemandsPresenter.class, PotentialDemandsPresenter.class, NewDemandPresenter.class,
            DemandsOperatorPresenter.class, AdminOffersPresenter.class },
            historyConverter = UserHistoryConverter.class)
    String invokeAdministration();

    @Event(handlers = AdminOffersPresenter.class, activate = AdminOffersPresenter.class, deactivate = {
            OffersPresenter.class, MyDemandsPresenter.class, PotentialDemandsPresenter.class, NewDemandPresenter.class,
            DemandsOperatorPresenter.class, AdministrationPresenter.class },
            historyConverter = UserHistoryConverter.class)
    String invokeAdminOffers();

    /** Navigation Events section END **/

    /**
     * hacky later fire event Needed when refreshing in User Section - refresh not neededi in prod.
     **/
    @Event(handlers = UserPresenter.class)
    void fireMarkedEvent();

    /** marks event to load right after main UI is displayed. */
    // devel method
    @Event(handlers = UserPresenter.class)
    void markEventToLoad(String historyName);

    /** Calls to MainEventBus. **/
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
    void createDemand(FullDemandDetail demand, Long id);

    @Event(forwardToParent = true)
    void setUserLayout();

    @Event(forwardToParent = true)
    void loadingHide();

    @Event(handlers = UserPresenter.class)
    void setUserInteface(StyleInterface widgetView);

    @Event(handlers = UserPresenter.class)
    void clearUserOnUnload();

    /** TEST CLIENT DEMANDS ON MESSAGE BASED SYSTEM **/
    // START
    @Event(handlers = MessageHandler.class)
    void getClientDemands(Long userId, int fakeParameter);

    // Beho: ??? needed ???
//    @Event(handlers = MyDemandsPresenter.class)
//    void responseClientDemands(ArrayList<MessageDetail> result);

    @Event(handlers = { DemandsLayoutPresenter.class, AdminLayoutPresenter.class })
    void toggleLoading();

    @Event(handlers = UserPresenter.class)
    void requestDemandsWithConversationInfo();

    @Event(handlers = MessageHandler.class)
    void getClientDemandWithConversations(Long userId, Long clientId);

    @Event(handlers = MyDemandsPresenter.class)
    void setClientDemandWithConversations(ArrayList<ClientDemandMessageDetail> result);

    @Event(handlers = MessageHandler.class)
    void requestDemandConversations(long messageId);

    @Event(handlers = MyDemandsPresenter.class)
    void setDemandConversations(ArrayList<MessageDetail> conversations);

    @Event(handlers = {MessageHandler.class, DetailWrapperPresenter.class })
    void requestSingleConversation(long threadRootId, long messageId);
    //END

    @Event(handlers = AdminLayoutPresenter.class)
    void initAdmin();


}
