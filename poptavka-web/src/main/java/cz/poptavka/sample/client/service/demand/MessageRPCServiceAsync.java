/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.service.demand;

import com.google.gwt.user.client.rpc.AsyncCallback;
import cz.poptavka.sample.shared.domain.MessageDetail;
import cz.poptavka.sample.shared.domain.OfferDetail;

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

    void sendOffer(OfferDetail demandOffer, AsyncCallback<OfferDetail> callback);

    void setMessageReadStatus(List<Long> userMessageIds, boolean isRead, AsyncCallback<Void> callback);
}
