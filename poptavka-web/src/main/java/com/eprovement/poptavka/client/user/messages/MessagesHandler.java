/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.messages;

import com.eprovement.poptavka.client.common.security.GetDataCallback;
import com.eprovement.poptavka.client.common.security.GetDataCountCallback;
import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.google.gwt.core.client.GWT;
import java.util.ArrayList;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import com.eprovement.poptavka.client.service.demand.MessagesRPCServiceAsync;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.eprovement.poptavka.shared.domain.message.UserMessageDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;

import java.util.List;

/**
 * Handle RPC calls for Message module.
 * @author Martin Slavkovsky
 */
@EventHandler
public class MessagesHandler extends BaseEventHandler<MessagesEventBus> {

    /**************************************************************************/
    /* Inject RPC services                                                    */
    /**************************************************************************/
    @Inject
    private MessagesRPCServiceAsync messagesService;

    /**************************************************************************/
    /* Overriden methods of IEventBusData interface.                          */
    /**************************************************************************/
    /**
     * Request table data count.
     * @param grid - table
     * @param searchDefinition - search criteria
     */
    public void onGetDataCount(UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        switch (Storage.getCurrentlyLoadedView()) {
            case Constants.MESSAGES_INBOX:
                getInboxMessagesCount(grid, searchDefinition);
                break;
            default:
                break;
        }
    }

    /**
     * Request table data.
     * @param searchDefinition - search criteria
     */
    public void onGetData(UniversalAsyncGrid grid, SearchDefinition searchDefinition, int requestId) {
        switch (Storage.getCurrentlyLoadedView()) {
            case Constants.MESSAGES_INBOX:
                getInboxMessages(grid, searchDefinition, requestId);
                break;
            default:
                break;
        }
    }

    /**************************************************************************/
    /* INBOX                                                                  */
    /**************************************************************************/
    /**
     * Request inbox table data count.
     * @param grid - table
     * @param searchDefinition - search criteria
     */
    public void getInboxMessagesCount(UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        messagesService.getInboxMessagesCount(Storage.getUser().getUserId(), searchDefinition,
            new GetDataCountCallback(eventBus, grid));
    }

    /**
     * Request inbox table data.
     * @param searchDefinition - search criteria
     */
    public void getInboxMessages(UniversalAsyncGrid grid, SearchDefinition searchDefinition, int requestId) {
        messagesService.getInboxMessages(Storage.getUser().getUserId(), searchDefinition,
            new GetDataCallback<MessageDetail>(eventBus, grid, requestId));
    }

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
                    // TODO LATER forward back where I was when forwarded to Compose Message widget
                } else if (action.equals("composeNew")) {
                    //TODO LATER forward to inbox
                    eventBus.initInbox(null);
                } else if (action.equals("composeReply")) {
                    //TODO LATER forward to inbox && display relevant conversation with new reply
                    eventBus.initInbox(null);
//                    eventBus.displayConversation(
//                        sentMessage.getThreadRootId(), sentMessage.getMessageId());
                }
//                eventBus.sendMessageResponse(sentMessage, type);
            }
        });
    }

    /**
     * Request conversations
     * @param threadRootId
     * @param subRootId
     */
    public void onRequestConversation(Long threadRootId, Long subRootId) {
        messagesService.getConversationMessages(threadRootId, subRootId,
            new SecuredAsyncCallback<ArrayList<MessageDetail>>(eventBus) {
                @Override
                public void onSuccess(ArrayList<MessageDetail> result) {
//                        eventBus.responseConversation(result, ViewType.EDITABLE);
                }
            });
    }

    /**
     * Deletes messages.
     * @param messagesIds to remove
     */
    public void onDeleteMessages(List<Long> messagesIds) {
        messagesService.deleteMessages(messagesIds, new SecuredAsyncCallback<List<UserMessageDetail>>(eventBus) {
            @Override
            public void onSuccess(List<UserMessageDetail> result) {
                GWT.log("Messages deleted.");
                //TODO implement
            }
        });
    }

    /**
     * Updates unread messages count.
     */
    public void onUpdateUnreadMessagesCount() {
        messagesService.updateUnreadMessagesCount(new SecuredAsyncCallback<UnreadMessagesDetail>(eventBus) {
            @Override
            public void onSuccess(UnreadMessagesDetail result) {
                eventBus.setUpdatedUnreadMessagesCount(result);
            }
        });
    }
}
