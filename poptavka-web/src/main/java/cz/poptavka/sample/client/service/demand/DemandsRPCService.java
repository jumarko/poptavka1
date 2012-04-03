/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.service.demand;

import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.shared.domain.adminModule.OfferDetail;
import cz.poptavka.sample.shared.domain.message.ClientDemandMessageDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.message.PotentialDemandMessage;

/**
 *
 * @author Drobcek
 */
@RemoteServiceRelativePath("service/demandsmodule")
public interface DemandsRPCService extends RemoteService {

    /**
     * Gets DemandDetail from DB.
     *
     * @param demandId id of demand
     * @param typeOfDetail type of Detail that should be returned
     * @return DemandDetail of selected DemandType
     */
    FullDemandDetail getFullDemandDetail(Long demandId);

    ArrayList<ClientDemandMessageDetail> getListOfClientDemandMessages(long businessUserId, long clientId);

    ArrayList<PotentialDemandMessage> getPotentialDemands(long businessUserId);

    void setMessageReadStatus(List<Long> userMessageIds, boolean isRead);

    void setMessageStarStatus(List<Long> list, boolean newStatus);

    ArrayList<MessageDetail> loadSuppliersPotentialDemandConversation(long threadId, long userId,
            long userMessageId);

    MessageDetail sendQueryToPotentialDemand(MessageDetail messageToSend);

    ArrayList<ArrayList<OfferDetail>> getDemandOffers(ArrayList<Long> idList);
}
