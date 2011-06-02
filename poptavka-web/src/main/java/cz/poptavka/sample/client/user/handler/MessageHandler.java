package cz.poptavka.sample.client.user.handler;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import cz.poptavka.sample.client.service.demand.MessageRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.OfferRPCServiceAsync;
import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.shared.domain.MessageDetail;
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.demand.DetailType;

@EventHandler
public class MessageHandler extends BaseEventHandler<UserEventBus> {

    @Inject
    private MessageRPCServiceAsync messageService;
    @Inject
    private OfferRPCServiceAsync offerService;

    public void onGetPotentialDemandConversation(long messageId, long businessUserId, long userMessageId) {

        // TODO NOT WORKING NOW
        messageService.loadSuppliersPotentialDemandConversation(messageId, businessUserId, userMessageId,
                new AsyncCallback<ArrayList<MessageDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("MessageHandler: onGetPotentialDemandConversation:\n\n" + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(ArrayList<MessageDetail> messageList) {
                        GWT.log("Conversation size: " + messageList.size());
                        eventBus.setPotentialDemandConversation(messageList, DetailType.POTENTIAL);
                        // TODO delete
                        /** DEBUG INFO **/
                        for (MessageDetail m : messageList) {
                            GWT.log(m.toString());
                        }
                    }
                });
    }

    public void onSendQueryToPotentialDemand(MessageDetail messageToSend) {
        messageService.sendQueryToPotentialDemand(messageToSend, new AsyncCallback<MessageDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                // TODO Auto-generated method stub
                Window.alert(MessageHandler.class.getName() + " at onSendQueryToPotentialDemand\n\n"
                        + caught.getMessage());
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
                Window.alert(MessageHandler.class.getName() + " at onSendDemandOffer\n\n" + caught.getMessage());
            }

            @Override
            public void onSuccess(OfferDetail result) {
                Window.alert("Offer Success");
                // eventBus.
            }
        });
    }

    public void onRequestPotentialDemandReadStatusChange(ArrayList<Long> messagesId, boolean isRead) {
        messageService.setMessageReadStatus(messagesId, isRead, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                // TODO Auto-generated method stub
                Window.alert(MessageHandler.class.getName() + " at onRequestPotentialDemandReadStatusChange\n\n"
                        + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                // there is nothing to do
            }
        });
    }

    public void onGetOfferStatusChange(OfferDetail offerDetail) {
        offerService.changeOfferState(offerDetail, new AsyncCallback<OfferDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert(MessageHandler.class.getName() + " at onGetOfferStatusChange\n\n" + caught.getMessage());
            }

            @Override
            public void onSuccess(OfferDetail result) {
                eventBus.setOfferDetailChange(result);
            }
        });
    }
}
