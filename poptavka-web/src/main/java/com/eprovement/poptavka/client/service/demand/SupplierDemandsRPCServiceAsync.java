package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.UserMessageDetail;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Martin Slavkovsky
 */
public interface SupplierDemandsRPCServiceAsync {

    void getSupplierDemandsCount(long supplierID, SearchModuleDataHolder filter,
            AsyncCallback<Long> callback);

    void getSupplierDemands(long supplierID, int start, int maxResult,
            SearchModuleDataHolder filter, Map<String, OrderType> orderColumns,
            AsyncCallback<List<FullDemandDetail>> callback);

    void getSupplierDemandConversationsCount(long supplierID, long demandID,
            SearchModuleDataHolder search, AsyncCallback<Long> callback);

    void getSupplierDemandConversations(long supplierID, long demnadID,
            int start, int maxResult, SearchModuleDataHolder filter, Map<String, OrderType> orderColumns,
            AsyncCallback<List<UserMessageDetail>> callback);

    void getSupplierOffersCount(long supplierID, SearchModuleDataHolder filter,
            AsyncCallback<Long> callback);

    void getSupplierOffers(long supplierID, int start, int maxResult,
            SearchModuleDataHolder filter, Map<String, OrderType> orderColumns,
            AsyncCallback<List<OfferDetail>> callback);

    void getSupplierAssignedDemandsCount(long supplierID, SearchModuleDataHolder filter,
            AsyncCallback<Long> callback);

    void getSupplierAssignedDemands(long supplierID, int start, int maxResult,
            SearchModuleDataHolder filter, Map<String, OrderType> orderColumns,
            AsyncCallback<List<OfferDetail>> callback);
}
