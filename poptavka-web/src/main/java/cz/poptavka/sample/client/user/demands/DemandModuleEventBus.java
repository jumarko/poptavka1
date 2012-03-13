package cz.poptavka.sample.client.user.demands;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Debug.LogLevel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.user.demands.handler.DemandModuleContentHandler;
import cz.poptavka.sample.client.user.demands.handler.DemandModuleMessageHandler;
import cz.poptavka.sample.client.user.demands.tab.ClientListPresenter;
import cz.poptavka.sample.client.user.demands.tab.SupplierListPresenter;
import cz.poptavka.sample.client.user.widget.DevelDetailWrapperPresenter;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import cz.poptavka.sample.shared.domain.message.ClientDemandMessageDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.message.PotentialDemandMessage;
import cz.poptavka.sample.shared.domain.type.ViewType;

@Debug(logLevel = LogLevel.DETAILED)
@Events(startView = DemandModuleView.class, module = DemandModule.class)
public interface DemandModuleEventBus extends EventBus {

    /**
     * Start event is called only when module is instantiated first time.
     * We can use it for history initialization.
     */
    @Start
    @Event(handlers = DemandModulePresenter.class)
    void start();

    /**
     * Forward event is called only if it is configured here. It there is nothing to carry out
     * in this method we should remove forward event to save the number of method invocations.
     * We can use forward event to switch css style for selected menu button.
     */
    @Forward
    @Event(handlers = DemandModulePresenter.class)
    void forward();

    /**************************************************************************/
    /* Navigation events.                                                     */
    /**************************************************************************/
    /**
     * The only entry point to this module due to code-spliting feature.
     * init demands module - left user_type menu and initial content
     *
     * @param filter - defines data holder to be displayed in advanced search bar
     * @param loadWidget - doplnit prosim ???
     */
    @Event(handlers = DemandModulePresenter.class, historyConverter = DemandModuleHistoryConverter.class)
    String goToDemandModule(SearchModuleDataHolder filter, String loadWidget);

    @Event(forwardToParent = true)
    void goToHomeDemandsModule(SearchModuleDataHolder filter, String location);

    @Event(forwardToParent = true)
    void goToHomeSuppliersModule(SearchModuleDataHolder filter, String location);

    @Event(forwardToParent = true)
    void goToCreateDemandModule(String location);

    @Event(forwardToParent = true)
    void goToCreateSupplierModule(String location);

    /**************************************************************************/
    /* Parent events                                                          */
    /**************************************************************************/
//    /**************************************************************************/
//    @Event(forwardToParent = true)
//    void setHomeBodyHolderWidget(IsWidget body);
//
//    @Event(handlers = DemandModulePresenter.class)
//    void setUserBodyHolderWidget(IsWidget body);
    //production init method
    //during development used multiple instancing
    @Event(handlers = ClientListPresenter.class)//, historyConverter = DemandModuleHistoryConverter.class)
    void initClientList(SearchModuleDataHolder filter);

    @Event(handlers = SupplierListPresenter.class)//, historyConverter = DemandModuleHistoryConverter.class)
    void initSupplierList(SearchModuleDataHolder filter);

    /**************************************************************************/
    /* Business events handled by DemandModulePresenter.                      */
    /**************************************************************************/
    // Forward methods don't need history converter because they have its owns
    //display widget in content area
    @Event(handlers = DemandModulePresenter.class)
    void displayView(Widget content);

    /**************************************************************************/
    /* Business events handled by ALL VIEW presenters. */
    /**
     * Send/Response method pair
     * Sends message and receive the answer in a form of the same message to be displayed on UI.
     * @param messageToSend
     * @param type type of handling view
     */
    @Event(handlers = DemandModuleMessageHandler.class)
    void sendMessage(MessageDetail messageToSend, ViewType type);
    //IMPORTANT: all view-resenters have to handle this method, if view handles conversation displaying

    @Event(handlers = SupplierListPresenter.class, passive = true)
    void sendMessageResponse(MessageDetail sentMessage, ViewType type);

    /**************************************************************************/
    /* Business events handled by SupplierListPresenter. */

    /*
     * Request/Response Method pair
     * Demands for CLIENT - his demands
     */
    @Event(handlers = DemandModuleContentHandler.class)
    void requestClientsDemands(SearchModuleDataHolder searchModuleDataHolder);

    @Event(handlers = ClientListPresenter.class)
    void responseClientsDemands(ArrayList<ClientDemandMessageDetail> result);

    /* Business events handled by SupplierListPresenter. */

    /*
     * Request/Response Method pair
     * NEW demands for SUPPLIER
     */
    @Event(handlers = DemandModuleContentHandler.class)
    void requestSupplierNewDemands(SearchModuleDataHolder searchModuleDataHolder);

    @Event(handlers = SupplierListPresenter.class)
    void responseSupplierNewDemands(ArrayList<PotentialDemandMessage> result);

    /**************************************************************************/
    @Event(handlers = DemandModuleContentHandler.class)
    void requestReadStatusUpdate(List<Long> selectedIdList, boolean newStatus);

    @Event(handlers = DemandModuleContentHandler.class)
    void requestStarStatusUpdate(List<Long> userMessageIdList, boolean newStatus);

    /**************************************************************************/
    /* Business events handled by DevelDetailWrapperPresenter. */

    /*
     * Request/Response Method pair
     * DemandDetail for detail section
     * @param demandId
     * @param type
     */
    @Event(handlers = DemandModuleContentHandler.class)
    void requestDemandDetail(Long demandId, ViewType type);

    @Event(handlers = DevelDetailWrapperPresenter.class, passive = true)
    void responseDemandDetail(FullDemandDetail demandDetail, ViewType type);

    /*
     * Request/Response method pair
     * Fetch and display chat(conversation) for supplier new demands list
     * @param messageId
     * @param userMessageId
     * @param userId
     */
    @Event(handlers = DemandModuleMessageHandler.class)
    void requestChatForSupplierList(long messageId, Long userMessageId, Long userId);

    @Event(handlers = DevelDetailWrapperPresenter.class)
    void responseChatForSupplierList(ArrayList<MessageDetail> chatMessages, ViewType supplierListType);
}
