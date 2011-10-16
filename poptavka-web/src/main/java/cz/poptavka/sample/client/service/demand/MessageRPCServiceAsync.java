/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.shared.domain.message.ClientDemandMessageDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.message.OfferDemandMessage;
import cz.poptavka.sample.shared.domain.message.OfferMessageDetail;
import cz.poptavka.sample.shared.domain.message.PotentialDemandMessage;

/**
 *
 * @author ivan.vlcek
 */
public interface MessageRPCServiceAsync {

    void loadSuppliersPotentialDemandConversation(
            long threadId, long userId, long userMessageId, AsyncCallback<ArrayList<MessageDetail>> callback);

    void sendQueryToPotentialDemand(MessageDetail messageToSend, AsyncCallback<MessageDetail> callback);

    void sendOffer(OfferMessageDetail demandOffer, AsyncCallback<OfferMessageDetail> callback);

    void setMessageReadStatus(List<Long> userMessageIds, boolean isRead, AsyncCallback<Void> callback);

    void getClientDemands(long businessUserId, int fakeParam, AsyncCallback<ArrayList<MessageDetail>> callback);

    void getListOfClientDemandMessages(long businessUserId, long clientId,
            AsyncCallback<ArrayList<ClientDemandMessageDetail>> callback);

    void getClientDemandConversations(long threadRootId, AsyncCallback<ArrayList<MessageDetail>> callback);

    void getConversationMessages(long threadRootId, long subRootId,
            AsyncCallback<ArrayList<MessageDetail>> callback);

    void getPotentialDemands(long businessUserId,
            AsyncCallback<ArrayList<PotentialDemandMessage>> asyncCallback);

    void getOfferDemands(long businessUserId, AsyncCallback<ArrayList<OfferDemandMessage>> callback);
}
