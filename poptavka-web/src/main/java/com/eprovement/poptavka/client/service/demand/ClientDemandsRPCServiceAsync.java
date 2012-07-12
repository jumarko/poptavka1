package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectConversationDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectDetail;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Martin Slavkovsky
 */
public interface ClientDemandsRPCServiceAsync {

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

    void getClientOffersCount(long clientID, SearchModuleDataHolder filter,
            AsyncCallback<Long> callback);

    void getClientOffersCount(long clientID, long demandID, int start,
            int maxResult, SearchModuleDataHolder filter, Map<String, OrderType> orderColumns,
            AsyncCallback<List<FullDemandDetail>> callback);

    void getClientDemandOffersCount(long clientID, long demandID, SearchModuleDataHolder filter,
            AsyncCallback<Long> callback);

    void getClientDemandOffers(long clientID, long demandID, int start,
            int maxResult, SearchModuleDataHolder filter, Map<String, OrderType> orderColumns,
            AsyncCallback<List<OfferDetail>> callback);

    void getClientAssignedDemandsCount(long clientID, SearchModuleDataHolder filter,
            AsyncCallback<Long> callback);

    void getClientAssignedDemands(long clientID, int start, int maxResult,
            SearchModuleDataHolder filter, Map<String, OrderType> orderColumns,
            AsyncCallback<List<OfferDetail>> callback);
}
