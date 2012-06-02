/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.demand.BaseDemandDetail;
import com.eprovement.poptavka.shared.domain.message.ClientDemandMessageDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.PotentialDemandMessage;
import com.eprovement.poptavka.shared.exceptions.RPCException;

/**
 *
 * @author Drobcek
 */
@RemoteServiceRelativePath(DemandsRPCService.URL)
public interface DemandsRPCService extends RemoteService {

    String URL = "service/demandsmodule";

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
