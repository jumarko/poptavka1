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
import cz.poptavka.sample.shared.exceptions.CommonException;

/**
 *
 * @author ivan.vlcek
 */
@RemoteServiceRelativePath("service/messages")
public interface MessageRPCService extends RemoteService {

    ArrayList<MessageDetail> getClientDemands(long businessUserId, int fakeParam) throws CommonException;

    ArrayList<ClientDemandMessageDetail> getListOfClientDemandMessages(long businessUserId, long clientId)
        throws CommonException;

    ArrayList<MessageDetail> getClientDemandConversations(long threadRootId) throws CommonException;

    ArrayList<MessageDetail> getConversationMessages(long threadRootId, long subRootId) throws CommonException;

    ArrayList<PotentialDemandMessage> getPotentialDemands(long businessUserId) throws CommonException;

    ArrayList<PotentialDemandMessage> getPotentialDemandsBySearch(long businessUserId,
            SearchModuleDataHolder searchDataHolder) throws CommonException;

    ArrayList<MessageDetail> loadSuppliersPotentialDemandConversation(long threadId, long userId,
            long userMessageId) throws CommonException;

    MessageDetail sendQueryToPotentialDemand(MessageDetail messageToSend) throws CommonException;

    MessageDetail sendInternalMessage(MessageDetail messageDetailImpl) throws CommonException;

    OfferMessageDetail sendOffer(OfferMessageDetail demandOffer) throws CommonException;

    void setMessageReadStatus(List<Long> userMessageIds, boolean isRead) throws CommonException;

    ArrayList<OfferDemandMessage> getOfferDemands(long businessUserId) throws CommonException;

    void setMessageStarStatus(List<Long> list, boolean newStatus) throws CommonException;

    List<UserMessageDetail> getInboxMessages(Long recipientId, SearchModuleDataHolder searchDataHolder)
        throws CommonException;

    List<UserMessageDetail> getSentMessages(Long senderId, SearchModuleDataHolder searchDataHolder)
        throws CommonException;

    List<UserMessageDetail> getDeletedMessages(Long userId, SearchModuleDataHolder searchDataHolder)
        throws CommonException;

    void deleteMessages(List<Long> messagesIds) throws CommonException;
}