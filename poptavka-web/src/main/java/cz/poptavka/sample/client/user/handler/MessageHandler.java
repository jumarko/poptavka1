package cz.poptavka.sample.client.user.handler;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import cz.poptavka.sample.client.service.demand.MessageRPCServiceAsync;
import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.shared.domain.MessageDetail;
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.demand.DetailType;

@EventHandler
public class MessageHandler extends BaseEventHandler<UserEventBus> {

    @Inject
    private MessageRPCServiceAsync messageService;

    public void onGetPotentialDemandConversation(long messageId, long businessUserId, int test) {

        // TODO NOT WORKING NOW
//        messageService.loadSuppliersPotentialDemandConversation(messageId, businessUserId,
//                new AsyncCallback<ArrayList<MessageDetail>>() {
//
//                @Override
//                public void onFailure(Throwable caught) {
//                    Window.alert("MessageHandler: onGetPotentialDemandConversation:\n\n" + caught.getMessage());
//                }
//
//                @Override
//                public void onSuccess(ArrayList<MessageDetail> messageList) {
//                    eventBus.setPotentialDemandConversation(messageList, DetailType.POTENTIAL);
//                }
//            });

        // Devel
        ArrayList<MessageDetail> messageList = new ArrayList<MessageDetail>();
        MessageDetail message = new MessageDetail();
        message.setThreadRootId(6);
        message.setParentId(6);
        message.setId(22);
        message.setSenderId(116);

        GWT.log(message.getSenderId() + " -< ID");
        message.setBody("Testing Message");
        message.setMessageState("SENT");
        message.setSubject("Demand Subject");
        message.setSent(new Date(2011, 12, 07));

        for (int i = 0; i < test; i++) {
            messageList.add(message);
        }
        eventBus.setPotentialDemandConversation(messageList, DetailType.POTENTIAL);
    }

    public void onSendQueryToPotentialDemand(MessageDetail messageToSend) {
        messageService.sendQueryToPotentialDemand(messageToSend, new AsyncCallback<MessageDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                // TODO Auto-generated method stub
                Window.alert(MessageHandler.class.getName()
                        + " at onSendQueryToPotentialDemand\n\n" + caught.getMessage());
            }

            @Override
            public void onSuccess(MessageDetail result) {
                eventBus.addReplyToPotentailDemandConversation(result, DetailType.POTENTIAL);
            }
        });
    }

    public void onSendDemandOffer(OfferDetail offerToSend) {
        GWT.log(" ** Offer demand ID: " + offerToSend.getDemandId());
        messageService.sendOffer(offerToSend, new AsyncCallback<OfferDetail>() {
            @Override
            public void onFailure(Throwable caught) {
                // TODO Auto-generated method stub
                Window.alert(MessageHandler.class.getName()
                        + " at onSendDemandOffer\n\n" + caught.getMessage());
            }

            @Override
            public void onSuccess(OfferDetail result) {
                Window.alert("Offer Success");
//                eventBus.
            }
        });
    }
}
