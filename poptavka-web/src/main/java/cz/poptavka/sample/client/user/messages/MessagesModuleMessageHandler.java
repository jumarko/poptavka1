package cz.poptavka.sample.client.user.messages;

import com.google.gwt.core.client.GWT;
import java.util.ArrayList;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
//import cz.poptavka.sample.client.service.demand.GeneralRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.MessagesRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.UserRPCServiceAsync;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.message.UserMessageDetail;
import cz.poptavka.sample.shared.domain.type.ViewType;
import java.util.List;

@EventHandler
public class MessagesModuleMessageHandler extends BaseEventHandler<MessagesModuleEventBus> {

//    @Inject
//    private MessageRPCServiceAsync messageService;
    @Inject
    private MessagesRPCServiceAsync messagesService;
//    @Inject
//    private GeneralRPCServiceAsync generalService;
    @Inject
    private UserRPCServiceAsync userService;

    /**
     * Send message.
     * IMPORTANT: further implementation of other parts will show, if we need more than this method
     * for chat related stuff
     * @param messageToSend
     * @param type
     */
    public void onSendMessage(MessageDetail messageToSend, final String action) {
        messagesService.sendInternalMessage(messageToSend, new AsyncCallback<MessageDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("DemandModuleMessageHandler: onSendMessage:\n\n"
                        + caught.getMessage());
            }

            @Override
            public void onSuccess(MessageDetail sentMessage) {
                if (action.equals("composeNewForwarded")) {
                    // TODO forward back where I was when forwarded to Compose Message widget
                } else if (action.equals("composeNew")) {
                    //TODO forward to inbox
                    eventBus.initInbox(null);
                } else if (action.equals("composeReply")) {
                    //TODO forward to inbox && display relevant conversation with new reply
                    eventBus.initInbox(null);
                    eventBus.displayConversation(
                        sentMessage.getThreadRootId(), sentMessage.getMessageId());
                }
//                eventBus.sendMessageResponse(sentMessage, type);
            }
        });
    }

    public void onGetInboxMessages(Long recipientId, SearchModuleDataHolder searchDataHolder) {
        messagesService.getInboxMessages(recipientId, searchDataHolder, new AsyncCallback<List<UserMessageDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(List<UserMessageDetail> result) {
                eventBus.displayMessages(result);
            }
        });
    }

    public void onGetSentMessages(Long senderId, SearchModuleDataHolder searchDataHolder) {
        messagesService.getSentMessages(senderId, searchDataHolder, new AsyncCallback<List<UserMessageDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(List<UserMessageDetail> result) {
                eventBus.displayMessages(result);
            }
        });
    }

    public void onGetDeletedMessages(Long userId, SearchModuleDataHolder searchDataHolder) {
        messagesService.getDeletedMessages(userId, searchDataHolder, new AsyncCallback<List<UserMessageDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(List<UserMessageDetail> result) {
                eventBus.displayMessages(result);
            }
        });
    }

    public void onRequestConversation(Long threadRootId, Long subRootId) {
        messagesService.getConversationMessages(threadRootId, subRootId,
                new AsyncCallback<ArrayList<MessageDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    @Override
                    public void onSuccess(ArrayList<MessageDetail> result) {
                        eventBus.responseConversation(result, ViewType.EDITABLE);
                    }
                });
    }

    /**
     * Changes demands Read status. Changes are displayed immediately on frontend. No onSuccess code is needed.
     *
     * @param list list of demands which read status should be changed
     * @param newStatus of demandList
     */
    public void onRequestReadStatusUpdate(List<Long> selectedIdList, boolean newStatus) {
        messagesService.setMessageReadStatus(selectedIdList, newStatus, new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error in MessageHandler in method: onRequestReadStatusUpdate"
                        + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                //Empty by default
            }
        });
    }

    /**
     * Changes demands star status. Changes are displayed immediately on frontend. No onSuccess code is needed.
     *
     * @param list list od demands which star status should be changed
     * @param newStatus of demandList
     */
    public void onRequestStarStatusUpdate(List<Long> userMessageIdList, boolean newStatus) {
        messagesService.setMessageStarStatus(userMessageIdList, newStatus, new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error in MessageHandler in method: onRequestStarStatusUpdate"
                        + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                //Empty by default
            }
        });
    }

    public void onDeleteMessages(List<Long> messagesIds) {
        messagesService.deleteMessages(messagesIds, new AsyncCallback<List<UserMessageDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(List<UserMessageDetail> result) {
                GWT.log("Messages deleted.");
            }
        });
    }

    public void onRequestUserInfo(Long recipientId) {
        userService.getUserById(recipientId, new AsyncCallback<UserDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(UserDetail result) {
                eventBus.responseUserInfo(result);
            }
        });
    }
}
