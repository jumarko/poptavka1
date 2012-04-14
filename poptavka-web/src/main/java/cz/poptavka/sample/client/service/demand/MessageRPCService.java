/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.shared.domain.message.ClientDemandMessageDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.message.OfferDemandMessage;
import cz.poptavka.sample.shared.domain.message.OfferMessageDetail;
import cz.poptavka.sample.shared.domain.message.PotentialDemandMessage;
import cz.poptavka.sample.shared.domain.message.UserMessageDetail;
import cz.poptavka.sample.shared.exceptions.RPCException;

/**
 *
 * @author ivan.vlcek
 */
@RemoteServiceRelativePath("service/messages")
public interface MessageRPCService extends RemoteService {

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