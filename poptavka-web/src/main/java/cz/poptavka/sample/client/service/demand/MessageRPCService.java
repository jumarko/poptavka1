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
import cz.poptavka.sample.shared.domain.message.MessageDetailImpl;
import cz.poptavka.sample.shared.domain.message.OfferDemandMessageImpl;
import cz.poptavka.sample.shared.domain.message.OfferMessageDetailImpl;
import cz.poptavka.sample.shared.domain.message.PotentialDemandMessage;

/**
 *
 * @author ivan.vlcek
 */
@RemoteServiceRelativePath("service/messages")
public interface MessageRPCService extends RemoteService {

    ArrayList<MessageDetailImpl> getClientDemands(long businessUserId, int fakeParam);

    ArrayList<ClientDemandMessageDetail> getListOfClientDemandMessages(long businessUserId, long clientId);

    ArrayList<MessageDetailImpl> getClientDemandConversations(long threadRootId);

    ArrayList<MessageDetailImpl> getConversationMessages(long threadRootId, long subRootId);

    ArrayList<PotentialDemandMessage> getPotentialDemands(long businessUserId);

    ArrayList<MessageDetailImpl> loadSuppliersPotentialDemandConversation(long threadId, long userId,
            long userMessageId);

    MessageDetailImpl sendQueryToPotentialDemand(MessageDetailImpl messageToSend);

    OfferMessageDetailImpl sendOffer(OfferMessageDetailImpl demandOffer);

    void setMessageReadStatus(List<Long> userMessageIds, boolean isRead);

    ArrayList<OfferDemandMessageImpl> getOfferDemands(long businessUserId);
}
