package com.eprovement.poptavka.client.user.messages;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.google.gwt.core.client.GWT;
import java.util.ArrayList;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
//import com.eprovement.poptavka.client.service.demand.GeneralRPCServiceAsync;
import com.eprovement.poptavka.client.service.demand.MessagesRPCServiceAsync;
import com.eprovement.poptavka.client.service.demand.UserRPCServiceAsync;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.UserMessageDetail;
import com.eprovement.poptavka.shared.domain.type.ViewType;

import java.util.List;

@EventHandler
public class MessagesHandler extends BaseEventHandler<MessagesEventBus> {

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
        messagesService.sendInternalMessage(messageToSend, new SecuredAsyncCallback<MessageDetail>(eventBus) {
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
        messagesService.getInboxMessages(recipientId, searchDataHolder,
                new SecuredAsyncCallback<List<UserMessageDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<UserMessageDetail> result) {
                        eventBus.displayMessages(result);
                    }
                });
    }

    public void onGetSentMessages(Long senderId, SearchModuleDataHolder searchDataHolder) {
        messagesService.getSentMessages(senderId, searchDataHolder,
                new SecuredAsyncCallback<List<UserMessageDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<UserMessageDetail> result) {
                        eventBus.displayMessages(result);
                    }
                });
    }

    public void onGetDeletedMessages(Long userId, SearchModuleDataHolder searchDataHolder) {
        messagesService.getDeletedMessages(userId, searchDataHolder,
                new SecuredAsyncCallback<List<UserMessageDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<UserMessageDetail> result) {
                        eventBus.displayMessages(result);
                    }
                });
    }

    public void onRequestConversation(Long threadRootId, Long subRootId) {
        messagesService.getConversationMessages(threadRootId, subRootId,
                new SecuredAsyncCallback<ArrayList<MessageDetail>>(eventBus) {
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
        messagesService.setMessageReadStatus(selectedIdList, newStatus, new SecuredAsyncCallback<Void>(eventBus) {
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
        messagesService.setMessageStarStatus(userMessageIdList, newStatus, new SecuredAsyncCallback<Void>(eventBus) {
            @Override
            public void onSuccess(Void result) {
                //Empty by default
            }
        });
    }

    public void onDeleteMessages(List<Long> messagesIds) {
        messagesService.deleteMessages(messagesIds, new SecuredAsyncCallback<List<UserMessageDetail>>(eventBus) {
            @Override
            public void onSuccess(List<UserMessageDetail> result) {
                GWT.log("Messages deleted.");
            }
        });
    }

    public void onRequestUserInfo(Long recipientId) {
        userService.getUserById(recipientId, new SecuredAsyncCallback<BusinessUserDetail>(eventBus) {
            @Override
            public void onSuccess(BusinessUserDetail result) {
                eventBus.responseUserInfo(result);
            }
        });
    }
}
