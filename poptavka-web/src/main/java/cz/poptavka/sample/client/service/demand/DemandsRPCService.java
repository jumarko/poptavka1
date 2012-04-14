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
import cz.poptavka.sample.shared.domain.demand.BaseDemandDetail;
import cz.poptavka.sample.shared.domain.message.ClientDemandMessageDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.message.PotentialDemandMessage;
import cz.poptavka.sample.shared.exceptions.RPCException;

/**
 *
 * @author Drobcek
 */
@RemoteServiceRelativePath("service/demandsmodule")
public interface DemandsRPCService extends RemoteService {

    ArrayList<ClientDemandMessageDetail> getListOfClientDemandMessages(long businessUserId, long clientId)
        throws RPCException;

    ArrayList<PotentialDemandMessage> getPotentialDemands(long businessUserId) throws RPCException;

    void setMessageReadStatus(List<Long> userMessageIds, boolean isRead) throws RPCException;

    void setMessageStarStatus(List<Long> list, boolean newStatus) throws RPCException;

    ArrayList<MessageDetail> loadSuppliersPotentialDemandConversation(long threadId, long userId,
            long userMessageId) throws RPCException;

    MessageDetail sendQueryToPotentialDemand(MessageDetail messageToSend) throws RPCException;

    ArrayList<ArrayList<OfferDetail>> getDemandOffers(ArrayList<Long> idList) throws RPCException;

        /**
     * Gets DemandDetail from DB.
     *
     * @param demandId id of demand
     * @param typeOfDetail type of Detail that should be returned
     * @return DemandDetail of selected DemandType
     */
    FullDemandDetail getFullDemandDetail(Long demandId) throws RPCException;

    BaseDemandDetail getBaseDemandDetail(Long demandId) throws RPCException;

}
