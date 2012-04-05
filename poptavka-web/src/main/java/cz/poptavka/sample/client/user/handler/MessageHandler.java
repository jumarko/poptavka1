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
import cz.poptavka.sample.shared.domain.adminModule.OfferDetail;
import cz.poptavka.sample.shared.domain.message.ClientDemandMessageDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.message.OfferDemandMessage;
import cz.poptavka.sample.shared.domain.message.OfferMessageDetail;
import cz.poptavka.sample.shared.domain.message.PotentialDemandMessage;
import cz.poptavka.sample.shared.domain.type.ViewType;

@EventHandler
public class MessageHandler extends BaseEventHandler<UserEventBus> {

    @Inject
    private MessageRPCServiceAsync messageService;
    @Inject
    private OfferRPCServiceAsync offerService;

    // Beho: ??? needed ???
    public void onGetClientDemands(Long userId, int fakeParameter) {
//        messageService.getClientDemands(userId, fakeParameter, new AsyncCallback<ArrayList<ClientDemandDetail>>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                Window.alert("MessageHandler: onGetClientDemands:\n\n" + caught.getMessage());
//            }
//
//            @Override
//            public void onSuccess(ArrayList<ClientDemandDetail> result) {
////                eventBus.responseClientDemands(result);
//            }
//        });
    }

    // TODO Praso - tato metoda sa pouziva v starom UserEventBus. Mozeme ju vyuzit znovu pre novy DemandsModule
    public void onGetClientDemandWithConversations(Long userId, Long clientId) {
        messageService.getListOfClientDemandMessages(userId, clientId,
                new AsyncCallback<ArrayList<ClientDemandMessageDetail>>() {

                @Override
                public void onFailure(Throwable caught) {
                    Window.alert("MessageHandler: onGetClientDemandCOnversations:\n\n" + caught.getMessage());
                }

                @Override
                public void onSuccess(ArrayList<ClientDemandMessageDetail> result) {
                    eventBus.setClientDemandWithConversations(result);
                }
            });
    }

    // TODO Praso - tato metoda sa pouziva v starom UserEventBus. Mozeme ju vyuzit znovu pre novy DemandsModule
    public void onRequestDemandConversations(long messageId) {
        messageService.getClientDemandConversations(messageId, new AsyncCallback<ArrayList<MessageDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("MessageHandler: onRequestDemandConversations:\n\n" + caught.getMessage());
            }

            @Override
            public void onSuccess(ArrayList<MessageDetail> conversations) {
                eventBus.setDemandConversations(conversations);
            }
        });
    }

    // TODO Praso - tato metoda sa pouziva v starom UserEventBus. Mozeme ju vyuzit znovu pre novy DemandsModule
    public void onRequestSingleConversation(long threadRootId, long messageId) {
        messageService.getConversationMessages(threadRootId, messageId,
                new AsyncCallback<ArrayList<MessageDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("MessageHandler: onRequestSingleConversation:\n\n" + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(ArrayList<MessageDetail> result) {
                        eventBus.setSingleDemandConversation(result);
                    }
                });
    }

    // TODO Praso - tato metoda sa pouziva v starom UserEventBus. Mozeme ju vyuzit znovu pre novy DemandsModule
    public void onGetPotentialDemandConversation(long messageId, long businessUserId, long userMessageId) {
        messageService.loadSuppliersPotentialDemandConversation(messageId, businessUserId, userMessageId,
                new AsyncCallback<ArrayList<MessageDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("MessageHandler: onGetPotentialDemandConversation:\n\n" + caught.getMessage());
                    }

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
        messageService.sendQueryToPotentialDemand(messageToSend, new AsyncCallback<MessageDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                // TODO Auto-generated method stub
                Window.alert(MessageHandler.class.getName() + " at onSendQueryToPotentialDemand\n\n"
                        + caught.getMessage());
            }

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
//        messageService.sendOffer(offerToSend, new AsyncCallback<OfferMessageDetail>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                // TODO Auto-generated method stub
//                Window.alert(MessageHandler.class.getName() + " at onSendDemandOffer\n\n" + caught.getMessage());
//            }
//
//            @Override
//            public void onSuccess(OfferMessageDetail result) {
//                Window.alert("Offer Success");
//            }
//        });
    }
    // TODO Praso - tato metoda sa pouziva v starom UserEventBus. Mozeme ju vyuzit znovu pre novy DemandsModule
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
    // TODO Praso - tato metoda sa pouziva v starom UserEventBus. Mozeme ju vyuzit znovu pre novy DemandsModule
    public void onGetOfferStatusChange(OfferDetail offerDetail) {
        GWT.log("STATE: " + offerDetail.getState());
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

    /**
     * Get Supplier's potential demands list.
     *
     * @param businessUserId
     */
    // TODO Praso - tato metoda sa pouziva v starom UserEventBus. Mozeme ju vyuzit znovu pre novy DemandsModule
    public void onGetPotentialDemands(long businessUserId) {
        messageService.getPotentialDemands(businessUserId,
                new AsyncCallback<ArrayList<PotentialDemandMessage>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("Error in MessageHandler in method: onGetPotentialDemandsList"
                                + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(
                            ArrayList<PotentialDemandMessage> result) {
                        GWT.log("Result size: " + result.size());
                        eventBus.responsePotentialDemands(result);
                    }
                });
    }

    /**
     * Get Client's demands for offers.
     *
     * @param clientId
     */
    // TODO Praso - tato metoda sa pouziva v starom UserEventBus. Mozeme ju vyuzit znovu pre novy DemandsModule
    public void onGetClientDemandsWithOffers(Long businessUserId) {
        messageService.getOfferDemands(businessUserId, new AsyncCallback<ArrayList<OfferDemandMessage>>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("MessageHandler at onGetClientDemandsWithOffers exception:\n\n" + caught.getMessage());
            }

            @Override
            public void onSuccess(ArrayList<OfferDemandMessage> result) {
                eventBus.responseClientDemandsWithOffers(result);
            }
        });
    }
}
