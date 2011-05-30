package cz.poptavka.sample.client.user.handler;

import java.util.ArrayList;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import cz.poptavka.sample.client.service.demand.MessageRPCServiceAsync;
import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.shared.domain.MessageDetail;
import cz.poptavka.sample.shared.domain.demand.DetailType;

@EventHandler
public class MessageHandler extends BaseEventHandler<UserEventBus> {

    @Inject
    private MessageRPCServiceAsync messageService;

    public void onGetPotentialDemandConversation(long messageId, long businessUserId) {
        messageService.loadSuppliersPotentialDemandConversation(messageId, businessUserId,
                new AsyncCallback<ArrayList<MessageDetail>>() {

                @Override
                public void onFailure(Throwable caught) {
                    Window.alert("MessageHandler: onGetPotentialDemandConversation:\n\n" + caught.getMessage());
                }

                @Override
                public void onSuccess(ArrayList<MessageDetail> messageList) {
                    eventBus.setPotentialDemandConversation(messageList, DetailType.POTENTIAL);
                }
            });
    }

}
