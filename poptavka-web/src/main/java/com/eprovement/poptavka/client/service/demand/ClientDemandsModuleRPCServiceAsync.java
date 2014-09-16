package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.RatingDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDashboardDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandConversationDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.eprovement.poptavka.shared.domain.offer.ClientOfferedDemandOffersDetail;
import com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;

/**
 *
 * @author Martin Slavkovsky
 */
public interface ClientDemandsModuleRPCServiceAsync {

    /**************************************************************************/
    /* Table getter methods                                                   */
    /**************************************************************************/
    //ClientDemands widget
    void getClientDemandsCount(long userID, SearchDefinition searchDefinition,
            AsyncCallback<Integer> callback);

    void getClientDemands(long userID, SearchDefinition searchDefinition,
            AsyncCallback<List<ClientDemandDetail>> callback);

    void getClientDemandConversationsCount(long userID, long demandID, SearchDefinition searchDefinition,
            AsyncCallback<Integer> callback);

    void getClientDemandConversations(long userID, long demandID, SearchDefinition searchDefinition,
            AsyncCallback<List<ClientDemandConversationDetail>> callback);

    //ClientOffers widget
    void getClientOfferedDemandsCount(long userID, SearchDefinition searchDefinition,
            AsyncCallback<Integer> callback);

    void getClientOfferedDemands(long userID, SearchDefinition searchDefinition,
            AsyncCallback<List<ClientDemandDetail>> callback);

    void getClientOfferedDemandOffersCount(long userID, long demandID, SearchDefinition searchDefinition,
            AsyncCallback<Integer> callback);

    void getClientOfferedDemandOffers(long userID, long demandID, long threadRootId, SearchDefinition searchDefinition,
            AsyncCallback<List<ClientOfferedDemandOffersDetail>> callback);

    //ClientAssignedDemands widget
    void getClientAssignedDemandsCount(long userID, SearchDefinition searchDefinition,
            AsyncCallback<Integer> callback);

    void getClientAssignedDemands(long userID, SearchDefinition searchDefinition,
            AsyncCallback<List<ClientOfferedDemandOffersDetail>> callback);

    //ClientClosedDemands widget
    void getClientClosedDemandsCount(long userID, SearchDefinition searchDefinition,
            AsyncCallback<Integer> callback);

    void getClientClosedDemands(long userID, SearchDefinition searchDefinition,
            AsyncCallback<List<ClientOfferedDemandOffersDetail>> callback);

    //ClientRatings widget
    void getClientRatingsCount(long userID, SearchDefinition searchDefinition,
            AsyncCallback<Integer> callback);

    void getClientRatings(long userID, SearchDefinition searchDefinition,
            AsyncCallback<List<RatingDetail>> callback);

    /**************************************************************************/
    /* Setter methods                                                         */
    /**************************************************************************/
    void acceptOffer(long offerId, AsyncCallback<Void> callback);

    /**************************************************************************/
    /* Messages methods                                                       */
    /**************************************************************************/
    void updateUnreadMessagesCount(AsyncCallback<UnreadMessagesDetail> callback);

    /**************************************************************************/
    /* CRUD operation of demand                                               */
    /**************************************************************************/
    void updateDemand(long demandId, FullDemandDetail updatedDemand, AsyncCallback<FullDemandDetail> calback) throws
            RPCException, ApplicationSecurityException;

    void requestDeleteDemand(long demandId, AsyncCallback<FullDemandDetail> calback) throws
            RPCException, ApplicationSecurityException;

    void closeDemandAndEnterFeedbackForSupplier(long demandID, long offerID, Integer supplierRating,
            String supplierMessage, AsyncCallback<Void> calback);

    /**************************************************************************/
    /* Other getter methods                                                   */
    /**************************************************************************/
    void getClientDashboardDetail(long userId, AsyncCallback<ClientDashboardDetail> calback);

    /**
     * @see com.eprovement.poptavka.client.service.demand.ClientDemandsModuleRPCService#substractCredit(long, int)
     */
    void substractCredit(long offerId, int credits, AsyncCallback<Boolean> callback);
}
