package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectConversationDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.offer.FullOfferDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Martin Slavkovsky
 */
@RemoteServiceRelativePath(ClientDemandsRPCService.URL)
public interface ClientDemandsRPCService extends RemoteService {

    String URL = "service/clientdemandsmodule";

    /**************************************************************************/
    /* Table getter methods                                                   */
    /**************************************************************************/
    //************************* CLIENT - My Demands ***************************/
    /**
     * Get all demand's count that has been created by client.
     * When new demand is created by client, will be involved here.
     * As Client: "All demands created by me."
     *
     * @param clientID - client's ID
     * @param filter - define searching criteria if any
     * @return count
     */
    long getClientProjectsCount(long clientID, SearchModuleDataHolder filter)
        throws RPCException, ApplicationSecurityException;

    /**
     * Get all demands that has been created by client.
     * When new demand is created by client, will be involved here.
     * As Client: "All demands created by me."
     *
     * @param clientID
     * @param searchDefinition
     * @return list of demand's detail objects
     */
    List<ClientProjectDetail> getClientProjects(long clientID, SearchDefinition searchDefinition)
        throws RPCException, ApplicationSecurityException;

    /**
     * When supplier asks something about a demand of some client.
     * The conversation has more messages of course but I want count of threads.
     * As Client: "Questions made by suppliers to demands made by me." "How many suppliers
     * are asing something about a certain demand."
     *
     * @param clientID - client's ID
     * @param demandID - demand's ID
     * @param filter - define searching criteria if any
     * @return count
     */
    long getClientProjectConversationsCount(long clientID, long demandID, SearchModuleDataHolder filter)
        throws RPCException, ApplicationSecurityException;

    /**
     * When supplier asks something about a demand of some client.
     * The conversation has more messages of course but I want count of threads. As
     * Client: "Questions made by suppliers to demands made by me."
     * "How many suppliers are asing something about a certain demand."
     *
     * @param clientID - client's
     * @param demandID - demand's
     * @param searchDefinition
     * @return
     */
    List<ClientProjectConversationDetail> getClientProjectConversations(long clientID, long demandID,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException;

    //************************* CLIENT - My Offers ****************************/
    /**
     * Get all demands where have been placed an offer by some supplier.
     * When supplier place an offer to client's demand, the demand will be involved here.
     * As Client: "Demands that have already an offer."
     *
     *
     * @param clientID
     * @param filter
     * @return
     */
    long getClientOfferedProjectsCount(long clientID, SearchModuleDataHolder filter)
        throws RPCException, ApplicationSecurityException;

    /**
     * Get all demands where have been placed an offer by some supplier.
     * When supplier place an offer to client's demand, the demand will be involved here.
     * As Client: "Demands that have already an offer."
     *
     * @param clientID - client's ID
     * @param demandID - demands's ID
     * @param searchDefinition
     * @return
     */
    List<ClientProjectDetail> getClientOfferedProjects(long clientID, long demandID, SearchDefinition searchDefinition)
        throws RPCException, ApplicationSecurityException;

    /**
     * Get all contestants and their offers of given demand.
     * When supplier place an offer to client's demand, the offer will be involved here.
     * As Client: "How many suppliers placed an offers to a certain demand."
     *
     * @return offers count of given demand
     */
    long getClientProjectContestantsCount(long clientID, long demandID, SearchModuleDataHolder filter)
        throws RPCException, ApplicationSecurityException;

    /**
     * Get all contestants and their offers of given demand.
     * When supplier place an offer to client's demand, the offer will be involved here.
     * As Client: "How many suppliers placed an offers to a certain demand."
     *
     * @param clientID
     * @param demandID
     * @param searchDefinition
     * @return
     */
    List<FullOfferDetail> getClientProjectContestants(long clientID, long demandID, SearchDefinition searchDefinition)
        throws RPCException, ApplicationSecurityException;

    //******************** CLIENT - My Assigned Demands ***********************/
    /**
     * Get all offers that were accepted by client to solve a demand.
     * When client accept an offer, will be involved here.
     * As Client: "All offers that were accepted by me to solve my demand."
     *
     * @param clientID
     * @param filter
     * @return
     */
    long getClientAssignedProjectsCount(long clientID, SearchModuleDataHolder filter)
        throws RPCException, ApplicationSecurityException;

    /**
     * Get all offers that were accepted by client to solve a demand.
     * When client accept an offer, will be involved here.
     * As Client: "All offers that were accepted by me to solve my demand."
     *
     * @param clientID
     * @param searchDefinition
     * @return
     */
    List<FullOfferDetail> getClientAssignedProjects(long clientID, SearchDefinition searchDefinition)
        throws RPCException, ApplicationSecurityException;

    /**************************************************************************/
    /* Other getter methods                                                   */
    /**************************************************************************/
    FullDemandDetail getFullDemandDetail(long demandId) throws RPCException, ApplicationSecurityException;

    FullSupplierDetail getFullSupplierDetail(long supplierId) throws RPCException, ApplicationSecurityException;

    ArrayList<MessageDetail> getSuppliersPotentialDemandConversation(long threadId, long userId, long userMessageId)
        throws RPCException, ApplicationSecurityException;

    /**************************************************************************/
    /* Setter methods                                                         */
    /**************************************************************************/
    void setMessageReadStatus(List<Long> userMessageIds, boolean isRead)
        throws RPCException, ApplicationSecurityException;

    void setMessageStarStatus(List<Long> list, boolean newStatus) throws RPCException, ApplicationSecurityException;

    /**************************************************************************/
    /* Messages methods                                                       */
    /**************************************************************************/
    MessageDetail sendQueryToPotentialDemand(MessageDetail messageToSend)
        throws RPCException, ApplicationSecurityException;

    OfferDetail changeOfferState(OfferDetail offerDetail) throws RPCException, ApplicationSecurityException;
}
