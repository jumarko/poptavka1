/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.service.demand;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eprovement.poptavka.client.main.common.search.SearchModuleDataHolder;
import com.eprovement.poptavka.shared.domain.message.ClientDemandMessageDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.OfferDemandMessage;
import com.eprovement.poptavka.shared.domain.message.OfferMessageDetail;
import com.eprovement.poptavka.shared.domain.message.PotentialDemandMessage;

import com.eprovement.poptavka.shared.domain.message.UserMessageDetail;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ivan.vlcek
 */
public interface MessageRPCServiceAsync {

    void loadSuppliersPotentialDemandConversation(
            long threadId, long userId, long userMessageId, AsyncCallback<ArrayList<MessageDetail>> callback);

    void sendQueryToPotentialDemand(MessageDetail messageToSend, AsyncCallback<MessageDetail> callback);

    void sendInternalMessage(MessageDetail messageDetailImpl, AsyncCallback<MessageDetail> callback);

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

    void getPotentialDemandsBySearch(long businessUserId, SearchModuleDataHolder searchDataHolder,
            AsyncCallback<ArrayList<PotentialDemandMessage>> asyncCallback);

    void getOfferDemands(long businessUserId, AsyncCallback<ArrayList<OfferDemandMessage>> callback);

    void setMessageStarStatus(List<Long> list, boolean newStatus, AsyncCallback<Void> callback);

    void getInboxMessages(Long recipientId, SearchModuleDataHolder searchDataHolder,
            AsyncCallback<List<UserMessageDetail>> callback);

    void getSentMessages(Long senderId, SearchModuleDataHolder searchDataHolder,
            AsyncCallback<List<UserMessageDetail>> callback);

    void getDeletedMessages(Long userId, SearchModuleDataHolder searchDataHolder,
            AsyncCallback<List<UserMessageDetail>> callback);

    void deleteMessages(List<Long> messagesIds, AsyncCallback<List<UserMessageDetail>> callback);
}
