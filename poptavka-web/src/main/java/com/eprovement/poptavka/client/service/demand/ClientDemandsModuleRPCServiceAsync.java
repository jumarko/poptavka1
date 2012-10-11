package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandConversationDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.offer.FullOfferDetail;
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
    void getClientDemandsCount(long clientID, SearchDefinition searchDefinition,
            AsyncCallback<Long> callback);

    void getClientDemands(SearchDefinition searchDefinition,
            AsyncCallback<List<ClientDemandDetail>> callback);

    void getClientDemandConversationsCount(long clientID, long demandID, SearchDefinition searchDefinition,
            AsyncCallback<Long> callback);

    void getClientDemandConversations(long clientID, long demandID, SearchDefinition searchDefinition,
            AsyncCallback<List<ClientDemandConversationDetail>> callback);

    //ClientOffers widget
    void getClientOfferedDemandsCount(long clientID, SearchDefinition searchDefinition,
            AsyncCallback<Long> callback);

    void getClientOfferedDemands(long clientID, long demandID, SearchDefinition searchDefinition,
            AsyncCallback<List<ClientDemandDetail>> callback);

    void getClientOfferedDemandOffersCount(long clientID, long demandID, SearchDefinition searchDefinition,
            AsyncCallback<Long> callback);

    void getClientOfferedDemandOffers(long clientID, long demandID, SearchDefinition searchDefinition,
            AsyncCallback<List<FullOfferDetail>> callback);

    //ClientAssignedDemands widget
    void getClientAssignedDemandsCount(long clientID, SearchDefinition searchDefinition,
            AsyncCallback<Long> callback);

    void getClientAssignedDemands(long clientID, SearchDefinition searchDefinition,
            AsyncCallback<List<FullOfferDetail>> callback);

    /**************************************************************************/
    /* Other getter methods                                                   */
    /**************************************************************************/
    void getFullDemandDetail(long demandId, AsyncCallback<FullDemandDetail> callback);

    void getFullSupplierDetail(long supplierId, AsyncCallback<FullSupplierDetail> callback);

    void getSuppliersPotentialDemandConversation(
            long threadId, long userId, long userMessageId, AsyncCallback<ArrayList<MessageDetail>> callback);

    /**************************************************************************/
    /* Setter methods                                                         */
    /**************************************************************************/
    void setMessageReadStatus(List<Long> userMessageIds, boolean isRead, AsyncCallback<Void> callback);

    void setMessageStarStatus(List<Long> list, boolean newStatus, AsyncCallback<Void> callback);

    void closeDemand(FullDemandDetail demandDetail, AsyncCallback<ArrayList<Void>> callback);

    void acceptOffer(FullOfferDetail fullOfferDetail, AsyncCallback<ArrayList<Void>> callback);

    void declineOffer(OfferDetail offerDetail, AsyncCallback<ArrayList<Void>> callback);

    /**************************************************************************/
    /* Messages methods                                                       */
    /**************************************************************************/
    void sendQueryToPotentialDemand(MessageDetail messageToSend, AsyncCallback<MessageDetail> callback);

    void changeOfferState(OfferDetail offerDetail, AsyncCallback<OfferDetail> callback);
}
