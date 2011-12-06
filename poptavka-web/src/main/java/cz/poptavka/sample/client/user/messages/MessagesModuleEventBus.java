package cz.poptavka.sample.client.user.messages;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Debug.LogLevel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.client.user.messages.tab.ConversationWrapperPresenter;
import cz.poptavka.sample.client.user.messages.tab.MessageListPresenter;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.message.UserMessageDetail;
import cz.poptavka.sample.shared.domain.type.ViewType;

@Debug(logLevel = LogLevel.DETAILED)
@Events(startView = MessagesModuleView.class, module = MessagesModule.class)
public interface MessagesModuleEventBus extends EventBus {

    /**************************************************************************/
    /* Navigation | Initialization events. */
    //production init method
    //during development used multiple instancing
    @Event(handlers = MessageListPresenter.class, historyConverter = MessagesModuleHistory.class)
    void initInbox();

    @Event(handlers = MessageListPresenter.class, historyConverter = MessagesModuleHistory.class)
    void initSent();

    @Event(handlers = MessageListPresenter.class, historyConverter = MessagesModuleHistory.class)
    void initTrash();

//    @Event(handlers = MessageListPresenter.class)
//    void initInbox();
//
//    @Event(handlers = MessageListPresenter.class)
//    void initSent();
//
//    @Event(handlers = MessageListPresenter.class)
//    void initTrash();
    /**************************************************************************/
    /* Business events. */
    /* Business events handled by DemandModulePresenter. */
    //init demands module - left user_type menu and initial content
    @Event(handlers = MessagesModulePresenter.class)
    void initMessagesModule(SimplePanel panel);

    //display widget in content area
    @Event(handlers = MessagesModulePresenter.class)
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
    @Event(handlers = MessagesModuleMessageHandler.class)
    void sendMessage(MessageDetail messageToSend, ViewType type);
    //IMPORTANT: all view-resenters have to handle this method, if view handles conversation displaying

    @Event(handlers = MessageListPresenter.class, passive = true)
    void sendMessageResponse(MessageDetail sentMessage, ViewType type);

    @Event(handlers = MessageListPresenter.class)
    void displayMessages(List<UserMessageDetail> messages);

    /**************************************************************************/
    /* Business events handled by SupplierListPresenter. */
    @Event(handlers = MessagesModuleMessageHandler.class)
    void getInboxMessages(Long recipientId);

    @Event(handlers = MessagesModuleMessageHandler.class)
    void getSentMessages(Long senderId);

    @Event(handlers = MessagesModuleMessageHandler.class)
    void getDeletedMessages(Long userId);

    @Event(handlers = MessagesModuleMessageHandler.class)
    void requestReadStatusUpdate(List<Long> selectedIdList, boolean newStatus);

    @Event(handlers = MessagesModuleMessageHandler.class)
    void requestStarStatusUpdate(List<Long> userMessageIdList, boolean newStatus);

    /**************************************************************************/
    /* Business events handled by DevelDetailWrapperPresenter. */

    /*
     * Request/Response Method pair
     * DemandDetail for detail section
     * @param demandId
     * @param type
     */
//    @Event(handlers = MessagesModuleContentHandler.class)
//    void requestDemandDetail(Long demandId, ViewType type);
//    @Event(handlers = DevelDetailWrapperPresenter.class, passive = true)
//    void responseDemandDetail(FullDemandDetail demandDetail, ViewType type);

    /*
     * Request/Response method pair
     * Fetch and display chat(conversation) for supplier new demands list
     * @param messageId
     * @param userMessageId
     * @param userId
     */
    @Event(handlers = MessagesModuleMessageHandler.class)
    void requestConversation(Long threadRootId, Long subRootId);

    @Event(handlers = ConversationWrapperPresenter.class)
    void responseConversation(ArrayList<MessageDetail> chatMessages, ViewType supplierListType);
}
