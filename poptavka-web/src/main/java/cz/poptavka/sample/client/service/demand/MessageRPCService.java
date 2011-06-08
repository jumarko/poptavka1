/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.message.OfferDemandMessage;
import cz.poptavka.sample.shared.domain.message.PotentialDemandMessage;

/**
 *
 * @author ivan.vlcek
 */
@RemoteServiceRelativePath("service/messages")
public interface MessageRPCService extends RemoteService {

    ArrayList<MessageDetail> getClientDemands(long businessUserId, int fakeParam);

    ArrayList<PotentialDemandMessage> getPotentialDemands(long businessUserId);

    ArrayList<MessageDetail> loadSuppliersPotentialDemandConversation(long threadId, long userId, long userMessageId);

    MessageDetail sendQueryToPotentialDemand(MessageDetail messageToSend);

    OfferDetail sendOffer(OfferDetail demandOffer);

    void setMessageReadStatus(List<Long> userMessageIds, boolean isRead);

    ArrayList<OfferDemandMessage> getOfferDemands(long businessUserId);
}
