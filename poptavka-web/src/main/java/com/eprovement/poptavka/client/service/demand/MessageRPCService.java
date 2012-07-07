/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.service.demand;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.shared.domain.message.ClientDemandMessageDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.OfferDemandMessage;
import com.eprovement.poptavka.shared.domain.message.OfferMessageDetail;
import com.eprovement.poptavka.shared.domain.message.PotentialDemandMessage;
import com.eprovement.poptavka.shared.domain.message.UserMessageDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;

/**
 *
 * @author ivan.vlcek
 */
@RemoteServiceRelativePath(MessageRPCService.URL)
public interface MessageRPCService extends RemoteService {

    String URL = "service/messages";

    ArrayList<MessageDetail> getClientDemands(long businessUserId, int fakeParam) throws RPCException;

    ArrayList<ClientDemandMessageDetail> getListOfClientDemandMessages(long businessUserId, long clientId)
        throws RPCException;

    ArrayList<MessageDetail> getClientDemandConversations(long threadRootId) throws RPCException;

    ArrayList<MessageDetail> getConversationMessages(long threadRootId, long subRootId) throws RPCException;

    ArrayList<PotentialDemandMessage> getPotentialDemands(long businessUserId) throws RPCException;

    ArrayList<PotentialDemandMessage> getPotentialDemandsBySearch(long businessUserId,
            SearchModuleDataHolder searchDataHolder) throws RPCException;

    ArrayList<MessageDetail> loadSuppliersPotentialDemandConversation(long threadId, long userId,
            long userMessageId) throws RPCException;

    MessageDetail sendQueryToPotentialDemand(MessageDetail messageToSend) throws RPCException;

    MessageDetail sendInternalMessage(MessageDetail messageDetailImpl) throws RPCException;

    OfferMessageDetail sendOffer(OfferMessageDetail demandOffer) throws RPCException;

    void setMessageReadStatus(List<Long> userMessageIds, boolean isRead) throws RPCException;

    ArrayList<OfferDemandMessage> getOfferDemands(long businessUserId) throws RPCException;

    void setMessageStarStatus(List<Long> list, boolean newStatus) throws RPCException;

    List<UserMessageDetail> getInboxMessages(Long recipientId, SearchModuleDataHolder searchDataHolder)
        throws RPCException;

    List<UserMessageDetail> getSentMessages(Long senderId, SearchModuleDataHolder searchDataHolder)
        throws RPCException;

    List<UserMessageDetail> getDeletedMessages(Long userId, SearchModuleDataHolder searchDataHolder)
        throws RPCException;

    void deleteMessages(List<Long> messagesIds) throws RPCException;
}