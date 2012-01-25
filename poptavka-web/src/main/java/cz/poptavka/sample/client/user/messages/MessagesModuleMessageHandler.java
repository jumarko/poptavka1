package cz.poptavka.sample.client.user.messages;

import java.util.ArrayList;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.service.demand.GeneralRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.MessageRPCServiceAsync;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.message.UserMessageDetail;
import cz.poptavka.sample.shared.domain.type.ViewType;
import java.util.List;

@EventHandler
public class MessagesModuleMessageHandler extends BaseEventHandler<MessagesModuleEventBus> {

    @Inject
    private MessageRPCServiceAsync messageService;
    @Inject
    private GeneralRPCServiceAsync generalService;

    /**
     * Send message.
     * IMPORTANT: further implementation of other parts will show, if we need more than this method
     * for chat related stuff
     * @param messageToSend
     * @param type
     */
    public void onSendMessage(MessageDetail messageToSend, final ViewType type) {
        messageService.sendQueryToPotentialDemand(messageToSend, new AsyncCallback<MessageDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("DemandModuleMessageHandler: onSendMessage:\n\n"
                        + caught.getMessage());
            }

            @Override
            public void onSuccess(MessageDetail sentMessage) {
                eventBus.sendMessageResponse(sentMessage, type);
            }
        });
    }

    public void onGetInboxMessages(Long recipientId, SearchModuleDataHolder searchDataHolder) {
        messageService.getInboxMessages(recipientId, searchDataHolder, new AsyncCallback<List<UserMessageDetail>>() {

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
        messageService.getSentMessages(senderId, searchDataHolder, new AsyncCallback<List<UserMessageDetail>>() {

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
        messageService.getDeletedMessages(userId, searchDataHolder, new AsyncCallback<List<UserMessageDetail>>() {

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
        messageService.getConversationMessages(threadRootId, subRootId,
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
        messageService.setMessageReadStatus(selectedIdList, newStatus, new AsyncCallback<Void>() {

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
        messageService.setMessageStarStatus(userMessageIdList, newStatus, new AsyncCallback<Void>() {

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
}
