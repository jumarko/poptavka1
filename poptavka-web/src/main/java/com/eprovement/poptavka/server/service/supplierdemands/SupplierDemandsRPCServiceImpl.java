/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.server.service.supplierdemands;

import com.eprovement.poptavka.client.service.demand.SupplierDemandsRPCService;
import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.UserMessageDetail;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 *
 * @author Martin Slavkovsky
 */
@Component(SupplierDemandsRPCService.URL)
public class SupplierDemandsRPCServiceImpl extends AutoinjectingRemoteService
        implements SupplierDemandsRPCService {

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
    @Override
    public long getSupplierDemandsCount(long supplierID, SearchModuleDataHolder filter) {
        //TODO Martin - implement when implemented on backend
        return -1L;
    }

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
    @Override
    public List<FullDemandDetail> getSupplierDemands(long supplierID, int start, int maxResult,
            SearchModuleDataHolder filter, Map<String, OrderType> orderColumns) {
        //TODO Martin - implement when implemented on backend
        return new ArrayList<FullDemandDetail>();
    }

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
    @Override
    public long getSupplierDemandConversationsCount(long supplierID, long demandID,
            SearchModuleDataHolder search) {
        //TODO Martin - implement when implemented on backend
        return -1L;
    }

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
    @Override
    public List<UserMessageDetail> getSupplierDemandConversations(long supplierID, long demnadID,
            int start, int maxResult, SearchModuleDataHolder filter, Map<String, OrderType> orderColumns) {
        //TODO Martin - implement when implemented on backend
        return new ArrayList<UserMessageDetail>();
    }
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
    @Override
    public long getSupplierOffersCount(long supplierID, SearchModuleDataHolder filter) {
        //TODO Martin - implement when implemented on backend
        return -1L;
    }

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
    @Override
    public List<OfferDetail> getSupplierOffers(long supplierID, int start, int maxResult,
            SearchModuleDataHolder filter, Map<String, OrderType> orderColumns) {
        //TODO Martin - implement when implemented on backend
        return new ArrayList<OfferDetail>();
    }

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
    @Override
    public long getSupplierAssignedDemandsCount(long supplierID, SearchModuleDataHolder filter) {
        //TODO Martin - implement when implemented on backend
        return -1L;
    }

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
    @Override
    public List<OfferDetail> getSupplierAssignedDemands(long supplierID, int start, int maxResult,
            SearchModuleDataHolder filter, Map<String, OrderType> orderColumns) {
        //TODO Martin - implement when implemented on backend
        return new ArrayList<OfferDetail>();
    }
}
