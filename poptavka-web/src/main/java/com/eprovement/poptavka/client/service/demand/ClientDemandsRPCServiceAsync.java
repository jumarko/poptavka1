package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectConversationDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.offer.FullOfferDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Martin Slavkovsky
 */
public interface ClientDemandsRPCServiceAsync {

    /**************************************************************************/
    /* Table getter methods                                                   */
    /**************************************************************************/
    void getClientProjectsCount(long clientID, SearchModuleDataHolder filter,
            AsyncCallback<Long> callback);

    void getClientProjects(long clientID, SearchDefinition searchDefinition,
            AsyncCallback<List<ClientProjectDetail>> callback);

    void getClientProjectConversationsCount(long clientID, long demandID, SearchModuleDataHolder filter,
            AsyncCallback<Long> callback);

    void getClientProjectConversations(long clientID, long demandID, SearchDefinition searchDefinition,
            AsyncCallback<List<ClientProjectConversationDetail>> callback);

    //
    void getClientOfferedProjectsCount(long clientID, SearchModuleDataHolder filter,
            AsyncCallback<Long> callback);

    void getClientOfferedProjects(long clientID, long demandID, SearchDefinition searchDefinition,
            AsyncCallback<List<ClientProjectDetail>> callback);

    void getClientProjectContestantsCount(long clientID, long demandID, SearchModuleDataHolder filter,
            AsyncCallback<Long> callback);

    void getClientProjectContestants(long clientID, long demandID, SearchDefinition searchDefinition,
            AsyncCallback<List<FullOfferDetail>> callback);

    //
    void getClientAssignedProjectsCount(long clientID, SearchModuleDataHolder filter,
            AsyncCallback<Long> callback);

    void getClientAssignedProjects(long clientID, SearchDefinition searchDefinition,
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

    /**************************************************************************/
    /* Messages methods                                                       */
    /**************************************************************************/
    void sendQueryToPotentialDemand(MessageDetail messageToSend, AsyncCallback<MessageDetail> callback);

    void changeOfferState(OfferDetail offerDetail, AsyncCallback<OfferDetail> callback);
}
