package cz.poptavka.sample.client.user.messages;

import com.google.gwt.user.client.ui.IsWidget;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Debug.LogLevel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.user.messages.tab.ComposeMessagePresenter;
import cz.poptavka.sample.client.user.messages.tab.ConversationWrapperPresenter;
import cz.poptavka.sample.client.user.messages.tab.MessageListPresenter;
import cz.poptavka.sample.shared.domain.UserDetail;
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
    @Event(handlers = MessageListPresenter.class) //, historyConverter = MessagesModuleHistory.class)
    void initMailBox(String type, SearchModuleDataHolder filter);

//    @Event(handlers = MessageListPresenter.class, historyConverter = MessagesModuleHistory.class)
//    void initMessagesTabModuleInbox(SearchModuleDataHolder searchDataHolder);
//
//    @Event(handlers = MessageListPresenter.class, historyConverter = MessagesModuleHistory.class)
//    void initMessagesTabModuleSent(SearchModuleDataHolder searchDataHolder);
//
//    @Event(handlers = MessageListPresenter.class, historyConverter = MessagesModuleHistory.class)
//    void initMessagesTabModuleTrash(SearchModuleDataHolder searchDataHolder);
//
//    @Event(handlers = ComposeMessagePresenter.class, historyConverter = MessagesModuleHistory.class)
//    void initMessagesTabComposeMail(MessageDetail msgDetail, String action);

    /**************************************************************************/
    /* Business events. */
    /* Business events handled by DemandModulePresenter. */
    //init demands module - left user_type menu and initial content
    @Event(handlers = MessagesModulePresenter.class)
    void initMessagesModule(SearchModuleDataHolder searchDataHolder, String action);

    //display widget in content area
    @Event(handlers = MessagesModulePresenter.class)
    void displayMain(Widget content);

    @Event(handlers = MessagesModulePresenter.class)
    void displayDetail(Widget content);

    /**************************************************************************/
    @Event(forwardToParent = true)
    void setHomeBodyHolderWidget(IsWidget body);

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
    void sendMessage(MessageDetail messageToSend, String action);
    //IMPORTANT: all view-resenters have to handle this method, if view handles conversation displaying

//    @Event(handlers = MessageListPresenter.class, passive = true)
//    void sendMessageResponse(MessageDetail sentMessage, ViewType type);
    @Event(handlers = MessageListPresenter.class)
    void displayMessages(List<UserMessageDetail> messages);

    /**************************************************************************/
    /* Business events handled by SupplierListPresenter. */
    @Event(handlers = MessagesModuleMessageHandler.class)
    void getInboxMessages(Long recipientId, SearchModuleDataHolder searchDataHolder);

    @Event(handlers = MessagesModuleMessageHandler.class)
    void getSentMessages(Long senderId, SearchModuleDataHolder searchDataHolder);

    @Event(handlers = MessagesModuleMessageHandler.class)
    void getDeletedMessages(Long userId, SearchModuleDataHolder searchDataHolder);

    @Event(handlers = MessagesModuleMessageHandler.class)
    void requestReadStatusUpdate(List<Long> selectedIdList, boolean newStatus);

    @Event(handlers = MessagesModuleMessageHandler.class)
    void requestStarStatusUpdate(List<Long> userMessageIdList, boolean newStatus);

    @Event(handlers = MessagesModuleMessageHandler.class)
    void deleteMessages(List<Long> messagesIds);

    @Event(handlers = MessagesModuleMessageHandler.class)
    void requestUserInfo(Long receiverId);

    @Event(handlers = ComposeMessagePresenter.class)
    void responseUserInfo(UserDetail userDetail);

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
    @Event(handlers = ConversationWrapperPresenter.class)
    void displayConversation(Long threadRootId, Long messageId);

    @Event(handlers = MessagesModuleMessageHandler.class)
    void requestConversation(Long threadRootId, Long subRootId);

    @Event(handlers = ConversationWrapperPresenter.class)
    void responseConversation(ArrayList<MessageDetail> chatMessages, ViewType supplierListType);
}
