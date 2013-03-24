package com.eprovement.poptavka.client.user.messages;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.google.gwt.core.client.GWT;
import java.util.ArrayList;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import com.eprovement.poptavka.client.service.demand.MessagesRPCServiceAsync;
import com.eprovement.poptavka.client.service.demand.UserRPCServiceAsync;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.eprovement.poptavka.shared.domain.message.UserMessageDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;

import java.util.List;

@EventHandler
public class MessagesHandler extends BaseEventHandler<MessagesEventBus> {

    @Inject
    private MessagesRPCServiceAsync messagesService;
    @Inject
    private UserRPCServiceAsync userService;

    /**************************************************************************/
    /* Overriden methods of IEventBusData interface.                          */
    /**************************************************************************/
    public void onGetDataCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        switch (Storage.getCurrentlyLoadedView()) {
            case Constants.MESSAGES_INBOX:
                getInboxMessagesCount(grid, searchDefinition);
                break;
            default:
                break;
        }
    }

    public void onGetData(SearchDefinition searchDefinition) {
        switch (Storage.getCurrentlyLoadedView()) {
            case Constants.MESSAGES_INBOX:
                getInboxMessages(searchDefinition);
                break;
            default:
                break;
        }
    }

    /**************************************************************************/
    /* INBOX                                                                  */
    /**************************************************************************/
    public void getInboxMessagesCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        messagesService.getInboxMessagesCount(Storage.getUser().getUserId(), searchDefinition,
                new SecuredAsyncCallback<Integer>(eventBus) {
                @Override
                public void onSuccess(Integer result) {
                    grid.getDataProvider().updateRowCount(result, true);
                }
            });
    }

    public void getInboxMessages(SearchDefinition searchDefinition) {
        messagesService.getInboxMessages(Storage.getUser().getUserId(), searchDefinition,
                new SecuredAsyncCallback<ArrayList<MessageDetail>>(eventBus) {
                @Override
                public void onSuccess(ArrayList<MessageDetail> result) {
                    eventBus.displayInboxMessages(result);
                }
            });
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

    public void onUpdateUnreadMessagesCount() {
        messagesService.updateUnreadMessagesCount(new SecuredAsyncCallback<UnreadMessagesDetail>(eventBus) {
            @Override
            public void onSuccess(UnreadMessagesDetail result) {
                // empty i.e number of new messages could be retrieved
                GWT.log("UpdateUnreadMessagesCount retrieved, number=" + result.getUnreadMessagesCount());
                eventBus.setUpdatedUnreadMessagesCount(result.getUnreadMessagesCount());
            }
        });
    }
}
