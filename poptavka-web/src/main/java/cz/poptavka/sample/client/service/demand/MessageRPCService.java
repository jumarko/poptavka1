/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.shared.domain.message.ClientDemandMessageDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.message.OfferDemandMessage;
import cz.poptavka.sample.shared.domain.message.OfferMessageDetail;
import cz.poptavka.sample.shared.domain.message.PotentialDemandMessage;

/**
 *
 * @author ivan.vlcek
 */
@RemoteServiceRelativePath("service/messages")
public interface MessageRPCService extends RemoteService {

    ArrayList<MessageDetail> getClientDemands(long businessUserId, int fakeParam);

    ArrayList<ClientDemandMessageDetail> getListOfClientDemandMessages(long businessUserId, long clientId);

    ArrayList<MessageDetail> getClientDemandConversations(long threadRootId);

    ArrayList<MessageDetail> getConversationMessages(long threadRootId, long subRootId);

    ArrayList<PotentialDemandMessage> getPotentialDemands(long businessUserId);

    ArrayList<MessageDetail> loadSuppliersPotentialDemandConversation(long threadId, long userId,
            long userMessageId);

    MessageDetail sendQueryToPotentialDemand(MessageDetail messageToSend);

    OfferMessageDetail sendOffer(OfferMessageDetail demandOffer);

    void setMessageReadStatus(List<Long> userMessageIds, boolean isRead);

    ArrayList<OfferDemandMessage> getOfferDemands(long businessUserId);

    void setMessageStarStatus(List<Long> list, boolean newStatus);
}
