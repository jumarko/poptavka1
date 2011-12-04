package cz.poptavka.sample.client.user.demands;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Debug.LogLevel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.client.user.demands.handler.DemandModuleContentHandler;
import cz.poptavka.sample.client.user.demands.handler.DemandModuleMessageHandler;
import cz.poptavka.sample.client.user.demands.tab.SupplierListPresenter;
import cz.poptavka.sample.client.user.widget.DevelDetailWrapperPresenter;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.message.PotentialDemandMessage;
import cz.poptavka.sample.shared.domain.type.ViewType;

@Debug(logLevel = LogLevel.DETAILED)
@Events(startView = DemandModuleView.class, module = DemandModule.class)
public interface DemandModuleEventBus extends EventBus {


    /**************************************************************************/
    /* Navigation | Initialization events. */

    //production init method
    //during development used multiple instancing
    @Event(handlers = SupplierListPresenter.class, historyConverter = DemandModuleHistory.class)
    void initSupplierList();

    /**************************************************************************/
    /* Business events. */
    /* Business events handled by DemandModulePresenter. */

    //init demands module - left user_type menu and initial content
    @Event(handlers = DemandModulePresenter.class)
    void initDemandModule(SimplePanel panel);

    //display widget in content area
    @Event(handlers = DemandModulePresenter.class)
    void displayView(Widget content);

    /**************************************************************************/
    /* Business events. */
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
    @Event(handlers = {SupplierListPresenter.class }, passive = true)
    void sendMessageResponse(MessageDetail sentMessage, ViewType type);


    /**************************************************************************/
    /* Business events handled by SupplierListPresenter. */

    /*
     * Request/Response Method pair
     * NEW demands for SUPPLIER
     */
    @Event(handlers = DemandModuleContentHandler.class)
    void requestSupplierNewDemands();
    @Event(handlers = SupplierListPresenter.class)
    void responseSupplierNewDemands(ArrayList<PotentialDemandMessage> result);


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
