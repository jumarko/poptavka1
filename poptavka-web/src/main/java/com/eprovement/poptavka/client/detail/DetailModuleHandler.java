/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.detail;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.service.demand.DetailRPCServiceAsync;
import com.eprovement.poptavka.shared.domain.FullClientDetail;
import com.eprovement.poptavka.shared.domain.FullRatingDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.OfferMessageDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import java.util.List;

/**
 * Handler for Detail module.
 *
 * @author Martin Slavkovsky
 */
@EventHandler
public class DetailModuleHandler extends BaseEventHandler<DetailModuleEventBus> {

    /**************************************************************************/
    /* Inject RPC services                                                    */
    /**************************************************************************/
    @Inject
    private DetailRPCServiceAsync service;

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * Request for demand detail.
     * @param demandId
     */
    public void onRequestDemandDetail(Long demandId) {
        service.getFullDemandDetail(demandId, new SecuredAsyncCallback<FullDemandDetail>(eventBus) {
            @Override
            public void onSuccess(FullDemandDetail result) {
                eventBus.responseDemandDetail(result);
            }
        });
    }

    /**
     * Request for client detail.
     * @param clientId
     */
    public void onRequestClientDetail(Long clientId) {
        service.getFullClientDetail(clientId, new SecuredAsyncCallback<FullClientDetail>(eventBus) {
            @Override
            public void onSuccess(FullClientDetail result) {
                eventBus.responseClientDetail(result);
            }
        });
    }

    /**
     * Request for supplier detail.
     * @param supplierId
     */
    public void onRequestSupplierDetail(Long supplierId) {
        service.getFullSupplierDetail(supplierId, new SecuredAsyncCallback<FullSupplierDetail>(eventBus) {
            @Override
            public void onSuccess(FullSupplierDetail result) {
                eventBus.responseSupplierDetail(result);
            }
        });
    }

    /**
     * Request for rating detail.
     * @param demandId
     */
    public void onRequestRatingDetail(Long demandId) {
        service.getFullRatingDetail(demandId, new SecuredAsyncCallback<FullRatingDetail>(eventBus) {
            @Override
            public void onSuccess(FullRatingDetail result) {
                eventBus.responseRatingDetail(result);
            }
        });
    }

    /**
     * Load conversation between client and supplier related to particular demand / threadRoot
     *
     * @param threadId
     * @param userId
     */
    public void onRequestConversation(long threadRootId, long loggedUserId, long counterPartyUserId) {
        service.getConversation(threadRootId, loggedUserId, counterPartyUserId,
                new SecuredAsyncCallback<List<MessageDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<MessageDetail> result) {
                        eventBus.responseConversation(result);
                    }
                });
    }

    /**
     * Update isRead status of all messages for given User.
     *
     * @param userId user whose UserMessages will be udpated
     * @param messages messages to be updated as read
     */
    public void onUpdateUserMessagesReadStatus(Long userId, List<MessageDetail> messages) {
        service.updateUserMessagesReadStatus(userId, messages,
                new SecuredAsyncCallback<Void>(eventBus) {
                    @Override
                    public void onSuccess(Void result) {
                        // userMessages in DB are updated thus there is no need to invoke other methods.
                    }
                });
    }

    /**
     * Send question message.
     * @param messageToSend
     */
    public void onSendQuestionMessage(MessageDetail messageToSend) {
        service.sendQuestionMessage(messageToSend, new SecuredAsyncCallback<MessageDetail>(eventBus) {
            @Override
            public void onSuccess(MessageDetail sentMessage) {
                eventBus.addConversationMessage(sentMessage);
            }
        });
    }

    /**
     * Send offer message.
     * @param offerMessageToSend
     */
    public void onSendOfferMessage(OfferMessageDetail offerMessageToSend) {
        service.sendOfferMessage(offerMessageToSend, new SecuredAsyncCallback<MessageDetail>(eventBus) {
            @Override
            public void onSuccess(MessageDetail sentMessage) {
                GWT.log("Offer message [messageId=" + sentMessage.getMessageId()
                        + "]  has been successfully sent to client");
                eventBus.addConversationMessage(sentMessage);
                eventBus.responseSendOfferMessage();
            }
        });
    }
}
