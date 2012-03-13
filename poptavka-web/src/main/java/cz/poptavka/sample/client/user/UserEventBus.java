package cz.poptavka.sample.client.user;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBusWithLookup;

import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.user.demands.tab.old.MyDemandsPresenter;
import cz.poptavka.sample.client.user.demands.tab.old.OffersPresenter;
import cz.poptavka.sample.client.user.demands.tab.old.PotentialDemandsPresenter;
import cz.poptavka.sample.client.user.handler.MessageHandler;
import cz.poptavka.sample.client.user.handler.UserHandler;
import cz.poptavka.sample.client.user.problems.MyProblemsPresenter;
import cz.poptavka.sample.client.user.widget.unused.OldDetailWrapperPresenter;
import cz.poptavka.sample.shared.domain.adminModule.OfferDetail;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.domain.demand.BaseDemandDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import cz.poptavka.sample.shared.domain.message.ClientDemandMessageDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.message.OfferDemandMessage;
import cz.poptavka.sample.shared.domain.message.OfferMessageDetail;
import cz.poptavka.sample.shared.domain.message.PotentialDemandMessage;
import cz.poptavka.sample.shared.domain.offer.FullOfferDetail;
import cz.poptavka.sample.shared.domain.type.ViewType;

/**
 * !!!! NOT USED ANYMORE !!!!!
 * Module loaded after user's log in. Default view is summary of his new
 * messages, new demands for him.
 *
 * @author beho
 */
@Events(startView = UserView.class, module = UserModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface UserEventBus extends EventBusWithLookup {

    /** init method. **/
    @Event(handlers = UserPresenter.class, historyConverter = UserHistoryConverter.class)
    String atAccount();

    /** getter for UserDetail. **/
    @Event(handlers = UserHandler.class)
    void getUser();

    /**
     * setter of userDetail for whole application. This user represents logged
     * user
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
    void responsePotentialDemands(
            ArrayList<PotentialDemandMessage> potentialDemandsList);

    /** Offer demands GETTER/SETTER. **/
    // this same method could be called to MyDemandsPresenter
    // depends on it's demandDetailType and what will be difference in data,
    // probably different
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
    void setDemandOffers(ArrayList<FullOfferDetail> offers);

    /**
     * For switching between main tabs like Demands | Messages | Settings | etc.
     *
     * @param tabBody
     *            current widget
     */
    // @Event(handlers = UserPresenter.class)
    // void setTabWidget(Widget tabBody);
    //
    // @Event(handlers = UserPresenter.class)
    // void setTabAdminWidget(Widget tabBody);
    //
    // @Event(handlers = UserPresenter.class)
    // void setTabSettingsWidget(Widget tabBody);
    @Event(handlers = MyProblemsPresenter.class)
    void requestMyProblems();

    /** handler method area. **/
    /** REQUESTs && RESPONSEs **/
    // Always in pairs
    /** get client id. **/
    @Event(handlers = UserPresenter.class)
    void requestClientId(FullDemandDetail newDemand);

    /**
     * common method for displaying Demand Detail in the window.
     * **/
    // TODO implements demandDetail section loading for Wrapper
    // serves for visual sing, that content is loading
    @Event(handlers = { UserHandler.class, OldDetailWrapperPresenter.class })
    void getDemandDetail(Long demandId, ViewType typeOfDetail);

    @Event(handlers = OldDetailWrapperPresenter.class, passive = true)
    void setFullDemandDetail(FullDemandDetail detail);

    @Event(handlers = OldDetailWrapperPresenter.class, passive = true)
    void setBaseDemandDetail(BaseDemandDetail detail);

    /** method for displaying conversation to selected demand. **/
    @Event(handlers = { UserPresenter.class, OldDetailWrapperPresenter.class })
    void requestPotentialDemandConversation(long messageId, long userMessageId);

    @Event(handlers = MessageHandler.class)
    void getPotentialDemandConversation(long messageId, long userId,
            long userMessageId);

    @Event(handlers = OldDetailWrapperPresenter.class, passive = true)
    void setPotentialDemandConversation(ArrayList<MessageDetail> messageList,
            ViewType wrapperhandlerType);

    @Event(handlers = OldDetailWrapperPresenter.class, passive = true)
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
    void sendMessageToPotentialDemand(MessageDetail messageToSend,
            ViewType viewType);

    @Event(handlers = OldDetailWrapperPresenter.class)
    void addMessageToPotentailDemandConversation(MessageDetail result,
            ViewType wrapperhandlerType);

    /** Offers message display & state change. **/
    @Event(handlers = OldDetailWrapperPresenter.class)
    void setOfferMessage(FullOfferDetail offerDetail);

    @Event(handlers = MessageHandler.class)
    void getOfferStatusChange(OfferDetail offerDetail);

    @Event(handlers = OffersPresenter.class)
    void setOfferDetailChange(OfferDetail offerDetail);

    /**
     * Bubbling offer to send to UserPresenter to get the user ID and supplier
     * ID.
     *
     * @param messageToSend
     *            message to be sent
     */
    @Event(handlers = UserPresenter.class)
    void bubbleOfferSending(OfferMessageDetail offerToSend);

    @Event(handlers = MessageHandler.class)
    void sendDemandOffer(OfferMessageDetail offerToSend);

    @Event(handlers = MessageHandler.class)
    void requestPotentialDemandReadStatusChange(ArrayList<Long> messages,
            boolean isRead);

    /** Call to UserPresenter **/
    /**
     * Display User View - parent Widget for client-logged user section.
     *
     * @param body
     */
    // @DisplayChildModuleView({DemandModule.class,
    // AdminModule.class, MessagesModule.class })
    @Event(forwardToParent = true)
    void setHomeBodyHolderWidget(IsWidget body);

    @Event(forwardToParent = true)
    void setUserBodyHolderWidget(Widget body);

    @Event(forwardToParent = true)
    void setMenu(IsWidget menu);

    @Event(forwardToParent = true)
    void setHeader(IsWidget header);

    @Event(forwardToParent = true)
    void start();

    /**********************************************************************************************
     ************ Navigation events section. /* Presenters do NOT listen to events when
     * deactivated ----------------------- DEMANDS SECTION
     */
//    @Event(handlers = MyDemandsPresenter.class, activate = MyDemandsPresenter.class, deactivate = {
//            OffersPresenter.class, PotentialDemandsPresenter.class },
//            historyConverter = DemandsHistoryConverter.class)
//    String invokeMyDemands();
//
//    @Event(handlers = MyProblemsPresenter.class, historyConverter = MyProblemsHistoryConverter.class)
//    String invokeMyProblems();
//
//    @Event(handlers = OffersPresenter.class, activate = OffersPresenter.class, deactivate = {
//            MyDemandsPresenter.class, PotentialDemandsPresenter.class },
//            historyConverter = DemandsHistoryConverter.class)
//    String invokeOffers();
//
//    @Event(handlers = PotentialDemandsPresenter.class, activate = PotentialDemandsPresenter.class, deactivate = {
//            OffersPresenter.class, MyDemandsPresenter.class }, historyConverter = DemandsHistoryConverter.class)
//    String invokePotentialDemands();

//    @Event(forwardToParent = true)
//    void initMessagesTabModuleInbox(SearchModuleDataHolder searchDataHolder);

//    @Event(forwardToParent = true)
//    void initMessagesTabModuleSent(SearchModuleDataHolder searchDataHolder);

//    @Event(forwardToParent = true)
//    void initMessagesTabModuleTrash(SearchModuleDataHolder searchDataHolder);
    /***********************************************************************************************
     ************************* Navigation Events section END ***************************************
     **********************************************************************************************/
    /*
     * hacky later fire event Needed when refreshing in User Section - refresh
     * not neededi in prod.
     */
    /**********************************************************************************************
     *********************** USER SECTION (THE BASE). ********************************************
     **********************************************************************************************/
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
    void loadingShowWithAnchor(String progressGettingDemandDataring,
            Widget anchor);

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
    @Event(handlers = MessageHandler.class)
    void getClientDemands(Long userId, int fakeParameter);

    // Beho: ??? needed ???
    // @Event(handlers = MyDemandsPresenter.class)
    // void responseClientDemands(ArrayList<MessageDetail> result);
    // @Event(handlers = { OldDemandsLayoutPresenter.class })
    // void toggleLoading();
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

    @Event(handlers = { MessageHandler.class, OldDetailWrapperPresenter.class })
    void requestSingleConversation(long threadRootId, long messageId);

    // END

    /**********************************************************************************************
    // Category
    @Event(handlers = AllSuppliersHandler.class)
    void getSubCategories(Long category);

    @Event(handlers = { AllDemandsHandler.class, AllSuppliersHandler.class })
    void getCategories();

    // Locality
    @Event(handlers = { AllDemandsHandler.class, AllSuppliersHandler.class })
    void getLocalities();

    // Suppliers
    @Event(handlers = AllSuppliersHandler.class)
    void getSuppliersByCategoryLocality(int start, int count, Long category,
            String locality);

    @Event(handlers = AllSuppliersHandler.class)
    void getSuppliersByCategory(int start, int count, Long category);

    @Event(handlers = AllSuppliersHandler.class)
    void getSuppliersCount(Long category, String locality);

    @Event(handlers = AllSuppliersHandler.class)
    void getSuppliersCountByCategory(Long category);

    // Display
    @Event(handlers = AllSuppliersPresenter.class)
    void atDisplaySuppliers(CategoryDetail categoryDetail);

    @Event(handlers = AllSuppliersPresenter.class)
    void displayRootcategories(ArrayList<CategoryDetail> list);

    @Event(handlers = AllSuppliersPresenter.class)
    void displaySubCategories(ArrayList<CategoryDetail> list,
            Long parentCategory);

    @Event(handlers = AllSuppliersPresenter.class)
    void displayDemandTabSuppliers(ArrayList<FullSupplierDetail> list);

    @Event(handlers = AllSuppliersPresenter.class)
    void setLocalityData(ArrayList<LocalityDetail> list);

    @Event(handlers = AllSuppliersPresenter.class, historyConverter = DemandsHistoryConverter.class)
    void addToPath(CategoryDetail category);

    @Event(handlers = AllSuppliersPresenter.class)
    void removeFromPath(Long code);

    @Event(handlers = AllSuppliersPresenter.class)
    void setCategoryID(Long categoryCode);

    @Event(handlers = AllSuppliersPresenter.class)
    void createAsyncDataProviderSupplier(final long totalFound);

    /* <<<<<<<<<<-------- ALL SUPPLIERS -------------------- */
    /* ------------------ ALL DEMANDS ---------------------->>>> */


    /* <<<<<<<<<<-------- ALL DEMANDS -------------------- */
    /**********************************************************************************************
     *********************** MESSAGES SECTION ****************************************************
     **********************************************************************************************/
    /**********************************************************************************************
     *********************** SETTINGS SECTION ****************************************************
     **********************************************************************************************/
    /**********************************************************************************************
     *********************** CONTACTS SECTION ****************************************************
     **********************************************************************************************/
    /**
     * **************** BEHO development corner. **************** implements
     * methods for Supplier new demands
     */
    // init demands module
//    @Event(forwardToParent = true)
//    void initDemandModule();

    @Event(forwardToParent = true)
    void goToHomeDemandsModule(SearchModuleDataHolder filter, String location);

    @Event(forwardToParent = true)
    void goToHomeSuppliersModule(SearchModuleDataHolder filter, String location);

//    @Event(forwardToParent = true)
//    void eegoToCreateDemand(String location);

    // added by Martin
    // init demands module

//    @Event(forwardToParent = true)
//    void initMessagesModule(SearchModuleDataHolder filter, String action);

//    @Event(forwardToParent = true)
//    void initAdminModule(SearchModuleDataHolder filter);

    // @Event(forwardToParent = true)
    // void initSearchModule(SimplePanel panel);
    @Event(forwardToParent = true)
    void clearSearchContent();

    /**
     * ********************* End corner ************************
     */
//    @Event(forwardToParent = true)
//    void initSettings();
}
