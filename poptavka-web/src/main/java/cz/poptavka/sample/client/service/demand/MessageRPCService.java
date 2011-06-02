/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.service.demand;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import cz.poptavka.sample.shared.domain.MessageDetail;
import cz.poptavka.sample.shared.domain.OfferDetail;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ivan.vlcek
 */
@RemoteServiceRelativePath("service/messages")
public interface MessageRPCService extends RemoteService {

    ArrayList<MessageDetail> loadSuppliersPotentialDemandConversation(long threadId, long userId);

    MessageDetail sendQueryToPotentialDemand(MessageDetail messageToSend);

    OfferDetail sendOffer(OfferDetail demandOffer);

    void setMessageReadStatus(List<Long> messagesId, boolean isRead);
}
