/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.shared.domain.message.ClientDemandMessageDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetailImpl;
import cz.poptavka.sample.shared.domain.message.OfferDemandMessageImpl;
import cz.poptavka.sample.shared.domain.message.OfferMessageDetailImpl;
import cz.poptavka.sample.shared.domain.message.PotentialDemandMessageImpl;

/**
 *
 * @author ivan.vlcek
 */
public interface MessageRPCServiceAsync {

    void loadSuppliersPotentialDemandConversation(
            long threadId, long userId, long userMessageId, AsyncCallback<ArrayList<MessageDetailImpl>> callback);

    void sendQueryToPotentialDemand(MessageDetailImpl messageToSend, AsyncCallback<MessageDetailImpl> callback);

    void sendOffer(OfferMessageDetailImpl demandOffer, AsyncCallback<OfferMessageDetailImpl> callback);

    void setMessageReadStatus(List<Long> userMessageIds, boolean isRead, AsyncCallback<Void> callback);

    void getClientDemands(long businessUserId, int fakeParam, AsyncCallback<ArrayList<MessageDetailImpl>> callback);

    void getListOfClientDemandMessages(long businessUserId, long clientId,
            AsyncCallback<ArrayList<ClientDemandMessageDetail>> callback);

    void getClientDemandConversations(long threadRootId, AsyncCallback<ArrayList<MessageDetailImpl>> callback);

    void getConversationMessages(long threadRootId, long subRootId,
            AsyncCallback<ArrayList<MessageDetailImpl>> callback);

    void getPotentialDemands(long businessUserId, AsyncCallback<ArrayList<PotentialDemandMessageImpl>> asyncCallback);

    void getOfferDemands(long businessUserId, AsyncCallback<ArrayList<OfferDemandMessageImpl>> callback);
}
