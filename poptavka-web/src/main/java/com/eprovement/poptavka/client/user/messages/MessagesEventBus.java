package com.eprovement.poptavka.client.user.messages;

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

import com.eprovement.poptavka.client.main.common.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.user.messages.tab.ComposeMessagePresenter;
import com.eprovement.poptavka.client.user.messages.tab.ConversationWrapperPresenter;
import com.eprovement.poptavka.client.user.messages.tab.MessageListPresenter;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.UserMessageDetail;
import com.eprovement.poptavka.shared.domain.type.ViewType;

@Debug(logLevel = LogLevel.DETAILED)
@Events(startView = MessagesView.class, module = MessagesModule.class)
public interface MessagesEventBus extends EventBus {

    /**
     * Start event is called only when module is instantiated first time.
     * We can use it for history initialization.
     */
    @Start
    @Event(handlers = MessagesPresenter.class)
    void start();

    /**
     * Forward event is called only if it is configured here. It there is nothing to carry out
     * in this method we should remove forward event to save the number of method invocations.
     * We can use forward event to switch css style for selected menu button.
     */
    @Forward
    @Event(handlers = MessagesPresenter.class)
    void forward();

    /**************************************************************************/
    /* Navigation events.                                                     */
    /**************************************************************************/
    /**
     * The only entry point to this module due to code-spliting feature.
     *
     * @param filter - defines data holder to be displayed in advanced search bar
     * @param loadWidget - prosim doplnit ???
     */
    @Event(handlers = MessagesPresenter.class, historyConverter = MessagesHistoryConverter.class)
    String goToMessagesModule(SearchModuleDataHolder searchDataHolder, int loadWidget);

    /**************************************************************************/
    /* Parent events                                                          */
    /**************************************************************************/
    @Event(forwardToParent = true)
    void userMenuStyleChange(int loadedModule);

    /**************************************************************************/
    /* Business Initialization events                                         */
    /**************************************************************************/
    @Event(handlers = ComposeMessagePresenter.class)//, historyConverter = MessagesModuleHistoryConverter.class)
    void initComposeNew();

    @Event(handlers = ComposeMessagePresenter.class)//, historyConverter = MessagesModuleHistoryConverter.class)
    void initComposeReply(MessageDetail msgDetail);

    @Event(handlers = MessageListPresenter.class)//, historyConverter = MessagesModuleHistoryConverter.class)
    void initInbox(SearchModuleDataHolder searchDataHolder);

    @Event(handlers = MessageListPresenter.class)//, historyConverter = MessagesModuleHistoryConverter.class)
    void initSent(SearchModuleDataHolder searchDataHolder);

    @Event(handlers = MessageListPresenter.class)//, historyConverter = MessagesModuleHistoryConverter.class)
    void initTrash(SearchModuleDataHolder searchDataHolder);

    @Event(handlers = MessageListPresenter.class)//, historyConverter = MessagesModuleHistoryConverter.class)
    void initDraft(SearchModuleDataHolder searchDataHolder);

    /**************************************************************************/
    /* Business events handled by presenters                                  */
    /**************************************************************************/
    //display widget in content area
    @Event(handlers = MessagesPresenter.class)
    void displayMain(Widget content);

    @Event(handlers = MessagesPresenter.class)
    void displayDetail(Widget content);

//    @Event(handlers = MessageListPresenter.class, passive = true)
//    void sendMessageResponse(MessageDetail sentMessage, ViewType type);
    @Event(handlers = MessageListPresenter.class)
    void displayMessages(List<UserMessageDetail> messages);

    @Event(handlers = ComposeMessagePresenter.class)
    void responseUserInfo(UserDetail userDetail);

    /**************************************************************************/
    /* Business events handled by ConversationWrapperPresenter.               */
    /**************************************************************************/
    /*
     * Request/Response method pair
     * Fetch and display chat(conversation) for supplier new demands list
     * @param messageId
     * @param userMessageId
     * @param userId
     */
    @Event(handlers = ConversationWrapperPresenter.class)
    void displayConversation(Long threadRootId, Long messageId);

    @Event(handlers = ConversationWrapperPresenter.class)
    void responseConversation(ArrayList<MessageDetail> chatMessages, ViewType supplierListType);

    /**************************************************************************/
    /* Business events handled by MessagesModuleMessageHandler                      */
    /**************************************************************************/
    /**
     * Send/Response method pair
     * Sends message and receive the answer in a form of the same message to be displayed on UI.
     * @param messageToSend
     * @param type type of handling view
     */
    @Event(handlers = MessagesHandler.class)
    void sendMessage(MessageDetail messageToSend, String action);
    //IMPORTANT: all view-resenters have to handle this method, if view handles conversation displaying

    @Event(handlers = MessagesHandler.class)
    void getInboxMessages(Long recipientId, SearchModuleDataHolder searchDataHolder);

    @Event(handlers = MessagesHandler.class)
    void getSentMessages(Long senderId, SearchModuleDataHolder searchDataHolder);

    @Event(handlers = MessagesHandler.class)
    void getDeletedMessages(Long userId, SearchModuleDataHolder searchDataHolder);

    @Event(handlers = MessagesHandler.class)
    void requestReadStatusUpdate(List<Long> selectedIdList, boolean newStatus);

    @Event(handlers = MessagesHandler.class)
    void requestStarStatusUpdate(List<Long> userMessageIdList, boolean newStatus);

    @Event(handlers = MessagesHandler.class)
    void deleteMessages(List<Long> messagesIds);

    @Event(handlers = MessagesHandler.class)
    void requestUserInfo(Long receiverId);

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
    @Event(handlers = MessagesHandler.class)
    void requestConversation(Long threadRootId, Long subRootId);
}
