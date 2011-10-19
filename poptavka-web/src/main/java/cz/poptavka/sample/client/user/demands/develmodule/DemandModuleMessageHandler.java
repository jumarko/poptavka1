package cz.poptavka.sample.client.user.demands.develmodule;

import java.util.ArrayList;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import cz.poptavka.sample.client.service.demand.MessageRPCServiceAsync;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.type.ViewType;

@EventHandler
public class DemandModuleMessageHandler extends BaseEventHandler<DemandModuleEventBus> {

    @Inject
    private MessageRPCServiceAsync messageService;

    /**
     * Load demand/related conversation from DB.
     *
     * @param messageId
     * @param userMessageId
     * @param userId
     */
    public void onRequestChatForSupplierList(long messageId, Long userMessageId, Long userId) {
        messageService.loadSuppliersPotentialDemandConversation(messageId, userId, userMessageId,
                new AsyncCallback<ArrayList<MessageDetail>>() {

                @Override
                public void onFailure(Throwable caught) {
                    Window.alert("DemandModuleMessageHandler: onRequestConversationForSupplierList:\n\n"
                            + caught.getMessage());
                }

                @Override
                public void onSuccess(ArrayList<MessageDetail> result) {
                    eventBus.responseChatForSupplierList(result, ViewType.POTENTIAL);
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
}
