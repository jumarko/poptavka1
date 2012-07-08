package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.UserMessageDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Martin Slavkovsky
 */
public interface ClientDemandsRPCServiceAsync {

    void getClientDemandsCount(long clientID, SearchModuleDataHolder filter,
            AsyncCallback<Long> callback) throws RPCException;

    void getClientDemands(int start, int maxResult,
            SearchModuleDataHolder filter, Map<String, OrderType> orderColumns,
            AsyncCallback<List<FullDemandDetail>> callback) throws RPCException;

    void getClientDemandConversationsCount(long clientID, long demandID, SearchModuleDataHolder filter,
            AsyncCallback<Long> callback) throws RPCException;

    void getClientDemandConversations(long clientID, long demandID, int start,
            int maxResult, SearchModuleDataHolder filter, Map<String, OrderType> orderColumns,
            AsyncCallback<List<UserMessageDetail>> callback) throws RPCException;

    void getClientOffersCount(long clientID, SearchModuleDataHolder filter,
            AsyncCallback<Long> callback) throws RPCException;

    void getClientOffersCount(long clientID, long demandID, int start,
            int maxResult, SearchModuleDataHolder filter, Map<String, OrderType> orderColumns,
            AsyncCallback<List<FullDemandDetail>> callback) throws RPCException;

    void getClientDemandOffersCount(long clientID, long demandID, SearchModuleDataHolder filter,
            AsyncCallback<Long> callback) throws RPCException;

    void getClientDemandOffers(long clientID, long demandID, int start,
            int maxResult, SearchModuleDataHolder filter, Map<String, OrderType> orderColumns,
            AsyncCallback<List<OfferDetail>> callback) throws RPCException;

    void getClientAssignedDemandsCount(long clientID, SearchModuleDataHolder filter,
            AsyncCallback<Long> callback) throws RPCException;

    void getClientAssignedDemands(long clientID, int start, int maxResult,
            SearchModuleDataHolder filter, Map<String, OrderType> orderColumns,
            AsyncCallback<List<OfferDetail>> callback) throws RPCException;
}
