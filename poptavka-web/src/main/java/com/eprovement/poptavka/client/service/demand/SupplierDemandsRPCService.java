package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.UserMessageDetail;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Martin Slavkovsky
 */
@RemoteServiceRelativePath(SupplierDemandsRPCService.URL)
public interface SupplierDemandsRPCService extends RemoteService {

    String URL = "service/supplierdemandsmodule";

    //************************ SUPPLIER - My Demands **************************/
    /**
     * Get demands of categories that I am interested in.
     * When a demand is created it is assigned to certain categories.
     * Than the system sends demands to suppliers what have registered those categories and are
     * interested in receiving this kind of demand.
     * As Supplier: "All potential demands that I am interested in."
     *
     * @param supplierID
     * @param filter
     * @return
     */
    long getSupplierDemandsCount(long supplierID, SearchModuleDataHolder filter);

    /**
     * Get demands of categories that I am interested in.
     * When a demand is created it is assigned to certain categories. Than the system sends
     * demands to suppliers what have registered those categories and are
     * interested in receiving this kind of demand.
     * As Supplier: "All potential demands that I am interested in."
     *
     * @param supplierID
     * @param start
     * @param maxResult
     * @param filter
     * @param orderColumns
     * @return
     */
    List<FullDemandDetail> getSupplierDemands(long supplierID, int start, int maxResult,
            SearchModuleDataHolder filter, Map<String, OrderType> orderColumns);

    /**
     * When supplier asks something about a demand of some client.
     * The conversation has more messages of course but I want count of threads.
     * As Supplier: "Questions made by me to demands made by clients."
     * "How many suppliers are asing something about a certain demand."
     *
     * @param supplierID
     * @param demandID
     * @param search
     * @return
     */
    long getSupplierDemandConversationsCount(long supplierID, long demandID,
            SearchModuleDataHolder search);

    /**
     * When supplier asks something about a demand of some client.
     * The conversation has more messages of course but I want count of threads.
     * As Supplier: "Questions made by me to demands made by clients."
     * "How many suppliers are asing something about a certain demand."
     *
     * @param supplierID
     * @param demnad
     * @return
     */
    List<UserMessageDetail> getSupplierDemandConversations(long supplierID, long demnadID,
            int start, int maxResult, SearchModuleDataHolder filter, Map<String, OrderType> orderColumns);
    //************************ SUPPLIER - My Offers ***************************/

    /**
     * Get offers sent by supplier.
     * When supplier sends an offer, it will be involved here.
     * As Supplier: "Offers I sent"
     *
     * @param supplierID
     * @param filter
     * @return
     */
    long getSupplierOffersCount(long supplierID, SearchModuleDataHolder filter);

    /**
     * Get offers sent by supplier.
     * When supplier sends an offer, it will be involved here.
     * As Supplier: "Offers I sent"
     *
     * @param supplierID
     * @param start
     * @param maxResult
     * @param filter
     * @param orderColumns
     * @return
     */
    List<OfferDetail> getSupplierOffers(long supplierID, int start, int maxResult,
            SearchModuleDataHolder filter, Map<String, OrderType> orderColumns);

    //******************* SUPPLIER - My Assigned Demands **********************/
    /**
     * Get supplier's offers that have been accepted.
     * When client accept an supplier's offer, the offer will be implemented here.
     * As Supplier: "Offers that I 'won'."
     *
     * @param supplierID
     * @param filter
     * @return
     */
    long getSupplierAssignedDemandsCount(long supplierID, SearchModuleDataHolder filter);

    /**
     * Get supplier's offers that have been accepted.
     * When client accept an supplier's offer, the offer will be implemented here.
     * As Supplier: "Offers that I 'won'."
     *
     * @param supplierID
     * @param start
     * @param maxResult
     * @param filter
     * @param orderColumns
     * @return
     */
    List<OfferDetail> getSupplierAssignedDemands(long supplierID, int start, int maxResult,
            SearchModuleDataHolder filter, Map<String, OrderType> orderColumns);
}
