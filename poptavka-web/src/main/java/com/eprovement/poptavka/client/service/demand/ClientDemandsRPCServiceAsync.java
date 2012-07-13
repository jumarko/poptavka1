package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectContestantDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectConversationDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    void getClientProjects(int start, int maxResult,
            SearchModuleDataHolder filter, Map<String, OrderType> orderColumns,
            AsyncCallback<List<ClientProjectDetail>> callback);

    void getClientProjectConversationsCount(long clientID, long demandID, SearchModuleDataHolder filter,
            AsyncCallback<Long> callback);

    void getClientProjectConversations(long clientID, long demandID, int start,
            int maxResult, SearchModuleDataHolder filter, Map<String, OrderType> orderColumns,
            AsyncCallback<List<ClientProjectConversationDetail>> callback);

    //
    void getClientOfferedProjectsCount(long clientID, SearchModuleDataHolder filter,
            AsyncCallback<Long> callback);

    void getClientOfferedProjects(long clientID, long demandID, int start,
            int maxResult, SearchModuleDataHolder filter, Map<String, OrderType> orderColumns,
            AsyncCallback<List<ClientProjectDetail>> callback);

    void getClientProjectContestantsCount(long clientID, long demandID, SearchModuleDataHolder filter,
            AsyncCallback<Long> callback);

    void getClientProjectContestants(long clientID, long demandID, int start,
            int maxResult, SearchModuleDataHolder filter, Map<String, OrderType> orderColumns,
            AsyncCallback<List<ClientProjectContestantDetail>> callback);

    //
    void getClientAssignedDemandsCount(long clientID, SearchModuleDataHolder filter,
            AsyncCallback<Long> callback);

    void getClientAssignedDemands(long clientID, int start, int maxResult,
            SearchModuleDataHolder filter, Map<String, OrderType> orderColumns,
            AsyncCallback<List<OfferDetail>> callback);

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
}
