/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.message.OfferDemandMessage;
import cz.poptavka.sample.shared.domain.message.PotentialDemandMessage;

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

    void getClientDemands(long businessUserId, int fakeParam, AsyncCallback<ArrayList<MessageDetail>> callback);

    void getPotentialDemands(long businessUserId, AsyncCallback<ArrayList<PotentialDemandMessage>> asyncCallback);

    void getOfferDemands(long businessUserId, AsyncCallback<ArrayList<OfferDemandMessage>> callback);
}
