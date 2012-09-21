package com.eprovement.poptavka.client.user.handler;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import com.eprovement.poptavka.client.service.demand.MessageRPCServiceAsync;
import com.eprovement.poptavka.client.service.demand.OfferRPCServiceAsync;
import com.eprovement.poptavka.client.user.UserEventBus;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.OfferMessageDetail;
import com.eprovement.poptavka.shared.domain.type.ViewType;

@EventHandler
public class MessageHandler extends BaseEventHandler<UserEventBus> {

    @Inject
    private MessageRPCServiceAsync messageService;
    @Inject
    private OfferRPCServiceAsync offerService;

    // Beho: ??? needed ???
    public void onGetClientDemands(Long userId, int fakeParameter) {
//        messageService.getClientDemands(userId, fakeParameter,
// new SecuredAsyncCallback<ArrayList<ClientDemandDetail>>() {
//            @Override
//            public void onSuccess(ArrayList<ClientDemandDetail> result) {
////                eventBus.responseClientDemands(result);
//            }
//        });
    }

    // TODO Praso - tato metoda sa pouziva v starom UserEventBus. Mozeme ju vyuzit znovu pre novy DemandsModule
    public void onGetClientDemandWithConversations(Long userId, Long clientId) {
//        messageService.getListOfClientDemandMessages(userId, clientId,
//                new SecuredAsyncCallback<ArrayList<ClientDemandMessageDetail>>() {
//                @Override
//                public void onSuccess(ArrayList<ClientDemandMessageDetail> result) {
//                    eventBus.setClientDemandWithConversations(result);
//                }
//            });
    }

    // TODO Praso - tato metoda sa pouziva v starom UserEventBus. Mozeme ju vyuzit znovu pre novy DemandsModule
    public void onRequestDemandConversations(long messageId) {
//        messageService.getClientDemandConversations(messageId, new SecuredAsyncCallback<ArrayList<MessageDetail>>() {
//            @Override
//            public void onSuccess(ArrayList<MessageDetail> conversations) {
//                eventBus.setDemandConversations(conversations);
//            }
//        });
    }

    // TODO Praso - tato metoda sa pouziva v starom UserEventBus. Mozeme ju vyuzit znovu pre novy DemandsModule
    public void onRequestSingleConversation(long threadRootId, long messageId) {
        messageService.getConversationMessages(threadRootId, messageId,
                new SecuredAsyncCallback<ArrayList<MessageDetail>>(eventBus) {
                    @Override
                    public void onSuccess(ArrayList<MessageDetail> result) {
                        eventBus.setSingleDemandConversation(result);
                    }
                });
    }

    // TODO Praso - tato metoda sa pouziva v starom UserEventBus. Mozeme ju vyuzit znovu pre novy DemandsModule
    public void onGetPotentialDemandConversation(long messageId, long businessUserId, long userMessageId) {
        messageService.loadSuppliersPotentialDemandConversation(messageId, businessUserId, userMessageId,
                new SecuredAsyncCallback<ArrayList<MessageDetail>>(eventBus) {
                    @Override
                    public void onSuccess(ArrayList<MessageDetail> messageList) {
                        GWT.log("Conversation size: " + messageList.size());
                        eventBus.setPotentialDemandConversation(messageList, ViewType.POTENTIAL);
                        // TODO delete
                        /** DEBUG INFO **/
                        for (MessageDetail m : messageList) {
                            GWT.log(m.toString());
                        }
                    }
                });
    }

    // TODO Praso - tato metoda sa pouziva v starom UserEventBus. Mozeme ju vyuzit znovu pre novy DemandsModule
    public void onSendMessageToPotentialDemand(MessageDetail messageToSend, final ViewType viewType) {
        messageService.sendQueryToPotentialDemand(messageToSend, new SecuredAsyncCallback<MessageDetail>(eventBus) {
            @Override
            public void onSuccess(MessageDetail result) {
                eventBus.addMessageToPotentailDemandConversation(result, viewType);
            }
        });
    }
    // TODO Praso - tato metoda sa pouziva v starom UserEventBus. Mozeme ju vyuzit znovu pre novy DemandsModule
    public void onSendDemandOffer(OfferMessageDetail offerToSend) {
        GWT.log(" ** Offer demand ID: " + offerToSend.getDemandId());
        Window.alert("* * * DEMAND OFFER CREATED * * *\n\n" + offerToSend.toString());
//        messageService.sendOffer(offerToSend, new SecuredAsyncCallback<OfferMessageDetail>() {
//            @Override
//            public void onSuccess(OfferMessageDetail result) {
//                Window.alert("Offer Success");
//            }
//        });
    }
    // TODO Praso - tato metoda sa pouziva v starom UserEventBus. Mozeme ju vyuzit znovu pre novy DemandsModule
    public void onRequestPotentialDemandReadStatusChange(ArrayList<Long> messagesId, boolean isRead) {
        messageService.setMessageReadStatus(messagesId, isRead, new SecuredAsyncCallback<Void>(eventBus) {
            @Override
            public void onSuccess(Void result) {
                // there is nothing to do
            }
        });
    }
    // TODO Praso - tato metoda sa pouziva v starom UserEventBus. Mozeme ju vyuzit znovu pre novy DemandsModule
    public void onGetOfferStatusChange(OfferDetail offerDetail) {
//        GWT.log("STATE: " + offerDetail.getState());
//        offerService.changeOfferState(offerDetail, new SecuredAsyncCallback<OfferDetail>() {
//            @Override
//            public void onSuccess(OfferDetail result) {
//                eventBus.setOfferDetailChange(result);
//            }
//        });
    }

    /**
     * Get Supplier's potential demands list.
     *
     * @param businessUserId
     */
    // TODO Praso - tato metoda sa pouziva v starom UserEventBus. Mozeme ju vyuzit znovu pre novy DemandsModule
    public void onGetPotentialDemands(long businessUserId) {
//        messageService.getPotentialDemands(businessUserId,
//                new SecuredAsyncCallback<ArrayList<PotentialDemandMessage>>() {
//                    @Override
//                    public void onSuccess(
//                            ArrayList<PotentialDemandMessage> result) {
//                        GWT.log("Result size: " + result.size());
//                        eventBus.responsePotentialDemands(result);
//                    }
//                });
    }

    /**
     * Get Client's demands for offers.
     *
     * @param clientId
     */
    // TODO Praso - tato metoda sa pouziva v starom UserEventBus. Mozeme ju vyuzit znovu pre novy DemandsModule
    public void onGetClientDemandsWithOffers(Long businessUserId) {
//        messageService.getOfferDemands(businessUserId, new SecuredAsyncCallback<ArrayList<OfferDemandMessage>>() {
//            @Override
//            public void onSuccess(ArrayList<OfferDemandMessage> result) {
//                eventBus.responseClientDemandsWithOffers(result);
//            }
//        });
    }
}
