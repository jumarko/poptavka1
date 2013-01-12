package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandConversationDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.eprovement.poptavka.shared.domain.offer.ClientOfferedDemandOffersDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
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
            AsyncCallback<Long> callback);

    void getClientDemands(long userID, SearchDefinition searchDefinition,
            AsyncCallback<List<ClientDemandDetail>> callback);

    void getClientDemandConversationsCount(long userID, long demandID, SearchDefinition searchDefinition,
            AsyncCallback<Long> callback);

    void getClientDemandConversations(long userID, long demandID, SearchDefinition searchDefinition,
            AsyncCallback<List<ClientDemandConversationDetail>> callback);

    //ClientOffers widget
    void getClientOfferedDemandsCount(long userID, SearchDefinition searchDefinition,
            AsyncCallback<Long> callback);

    void getClientOfferedDemands(long userID, SearchDefinition searchDefinition,
            AsyncCallback<List<ClientDemandDetail>> callback);

    void getClientOfferedDemandOffersCount(long userID, long demandID, SearchDefinition searchDefinition,
            AsyncCallback<Long> callback);

    void getClientOfferedDemandOffers(long userID, long demandID, long threadRootId, SearchDefinition searchDefinition,
            AsyncCallback<List<ClientOfferedDemandOffersDetail>> callback);

    //ClientAssignedDemands widget
    void getClientAssignedDemandsCount(long userID, SearchDefinition searchDefinition,
            AsyncCallback<Long> callback);

    void getClientAssignedDemands(long userID, SearchDefinition searchDefinition,
            AsyncCallback<List<ClientOfferedDemandOffersDetail>> callback);

    /**************************************************************************/
    /* Other getter methods                                                   */
    /**************************************************************************/
    void getFullDemandDetail(long demandId,
            AsyncCallback<ClientOfferedDemandOffersDetail> callback);

    void getFullSupplierDetail(long supplierId,
            AsyncCallback<FullSupplierDetail> callback);

    void getSuppliersPotentialDemandConversation(long threadId, long userId, long userMessageId,
            AsyncCallback<ArrayList<MessageDetail>> callback);

    /**************************************************************************/
    /* Setter methods                                                         */
    /**************************************************************************/
    void setMessageReadStatus(List<Long> userMessageIds, boolean isRead,
            AsyncCallback<Void> callback);

    void setMessageStarStatus(List<Long> list, boolean newStatus,
            AsyncCallback<Void> callback);

    void closeDemand(long id,
            AsyncCallback<ArrayList<Void>> callback);

    void acceptOffer(long offerId,
            AsyncCallback<ArrayList<Void>> callback);

    /**************************************************************************/
    /* Messages methods                                                       */
    /**************************************************************************/
    void sendQueryToPotentialDemand(MessageDetail messageToSend,
            AsyncCallback<MessageDetail> callback);

    void updateUnreadMessagesCount(AsyncCallback<UnreadMessagesDetail> callback);

    /**************************************************************************/
    /* Get Detail object for selecting in selection models                    */
    /**************************************************************************/
    void getClientDemand(long id, AsyncCallback<ClientDemandDetail> calback);

    void getClientDemandConversation(long id, AsyncCallback<ClientDemandConversationDetail> calback);

    void getClientOfferedDemand(long id, AsyncCallback<ClientDemandDetail> calback);

    void getClientOfferedDemandOffer(long id, AsyncCallback<ClientOfferedDemandOffersDetail> calback);

    void getClientAssignedDemand(long id, AsyncCallback<ClientOfferedDemandOffersDetail> calback);
}
