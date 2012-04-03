/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.service.demand;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.shared.domain.adminModule.OfferDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import cz.poptavka.sample.shared.domain.message.ClientDemandMessageDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.message.PotentialDemandMessage;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Drobcek
 */
public interface DemandsRPCServiceAsync {

    void getFullDemandDetail(Long demandId, AsyncCallback<FullDemandDetail> callback);

    void getListOfClientDemandMessages(long businessUserId, long clientId,
            AsyncCallback<ArrayList<ClientDemandMessageDetail>> callback);

    void getPotentialDemands(long businessUserId,
            AsyncCallback<ArrayList<PotentialDemandMessage>> asyncCallback);

    void setMessageReadStatus(List<Long> userMessageIds, boolean isRead, AsyncCallback<Void> callback);

    void setMessageStarStatus(List<Long> list, boolean newStatus, AsyncCallback<Void> callback);

    void loadSuppliersPotentialDemandConversation(
            long threadId, long userId, long userMessageId, AsyncCallback<ArrayList<MessageDetail>> callback);

    void sendQueryToPotentialDemand(MessageDetail messageToSend, AsyncCallback<MessageDetail> callback);

    // TODO Praso - zatial sa nepouziva
    void getDemandOffers(ArrayList<Long> idList, AsyncCallback<ArrayList<ArrayList<OfferDetail>>> callback);
}
