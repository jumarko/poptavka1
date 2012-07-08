/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.server.service.clientdemands;

import com.eprovement.poptavka.client.service.demand.ClientDemandsRPCService;
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
@Component(ClientDemandsRPCService.URL)
public class ClientDemandsRPCServiceImpl extends AutoinjectingRemoteService
        implements ClientDemandsRPCService {

    //************************* CLIENT - My Demands ***************************/
    /**
     * Get all demand's count that has been created by client.
     * When new demand is created by client, will be involved here.
     * As Client: "All demands created by me."
     *
     * @param clientID - client's ID
     * @param filter - define searching criteria if any
     * @return count
     */
    @Override
    public long getClientDemandsCount(long clientID, SearchModuleDataHolder filter) {
        //TODO Martin - implement when implemented on backend
        return -1L;
    }

    /**
     * Get all demands that has been created by client.
     * When new demand is created by client, will be involved here.
     * As Client: "All demands created by me."
     *
     * @param start
     * @param maxResult
     * @param filter
     * @param orderColumns
     * @return list of demand's detail objects
     */
    @Override
    public List<FullDemandDetail> getClientDemands(int start, int maxResult,
            SearchModuleDataHolder filter, Map<String, OrderType> orderColumns) {
        //TODO Martin - implement when implemented on backend
        return new ArrayList<FullDemandDetail>();
    }

    /**
     * When supplier asks something about a demand of some client.
     * The conversation has more messages of course but I want count of threads.
     * As Client: "Questions made by suppliers to demands made by me." "How many suppliers
     * are asing something about a certain demand."
     *
     * @param clientID - client's ID
     * @param demandID - demand's ID
     * @param filter - define searching criteria if any
     * @return count
     */
    @Override
    public long getClientDemandConversationsCount(long clientID, long demandID, SearchModuleDataHolder filter) {
        //TODO Martin - implement when implemented on backend
        return -1L;
    }

    /**
     * When supplier asks something about a demand of some client.
     * The conversation has more messages of course but I want count of threads. As
     * Client: "Questions made by suppliers to demands made by me."
     * "How many suppliers are asing something about a certain demand."
     *
     * @param clientID - client's
     * @param demandID - demand's
     * @param start
     * @param maxResult
     * @param filter
     * @param orderColumns
     * @return
     */
    @Override
    public List<UserMessageDetail> getClientDemandConversations(long clientID, long demandID, int start,
            int maxResult, SearchModuleDataHolder filter, Map<String, OrderType> orderColumns) {
        //TODO Martin - implement when implemented on backend
        return new ArrayList<UserMessageDetail>();
    }

    //************************* CLIENT - My Offers ****************************/
    /**
     * Get all demands where have been placed an offer by some supplier.
     * When supplier place an offer to client's demand, the demand will be involved here.
     * As Client: "Demands that have already an offer."
     *
     * @param clientID
     * @param filter
     * @return
     */
    @Override
    public long getClientOffersCount(long clientID, SearchModuleDataHolder filter) {
        //TODO Martin - implement when implemented on backend
        return -1L;
    }

    /**
     * Get all demands where have been placed an offer by some supplier.
     * When supplier place an offer to client's demand, the demand will be involved here.
     * As Client: "Demands that have already an offer."
     *
     * @param clientID - client's ID
     * @param demandID - demands's ID
     * @param start
     * @param maxResult
     * @param filter
     * @param orderColumns
     * @return
     */
    @Override
    public List<FullDemandDetail> getClientOffersCount(long clientID, long demandID, int start,
            int maxResult, SearchModuleDataHolder filter, Map<String, OrderType> orderColumns) {
        //TODO Martin - implement when implemented on backend
        return new ArrayList<FullDemandDetail>();
    }

    /**
     * Get all offers of given demand.
     * When supplier place an offer to client's demand, the offer will be involved here.
     * As Client: "How many suppliers placed an offers to a certain demand."
     *
     * @return offers count of given demand
     */
    @Override
    public long getClientDemandOffersCount(long clientID, long demandID, SearchModuleDataHolder filter) {
        //TODO Martin - implement when implemented on backend
        return -1L;
    }

    /**
     * Get all offers of given demand.
     * When supplier place an offer to client's demand, the offer will be involved here.
     * As Client: "How many suppliers placed an offers to a certain demand."
     *
     * @param clientID
     * @param demandID
     * @param start
     * @param maxResult
     * @param filter
     * @param orderColumns
     * @return
     */
    @Override
    public List<OfferDetail> getClientDemandOffers(long clientID, long demandID, int start,
            int maxResult, SearchModuleDataHolder filter, Map<String, OrderType> orderColumns) {
        //TODO Martin - implement when implemented on backend
        return new ArrayList<OfferDetail>();
    }

    //******************** CLIENT - My Assigned Demands ***********************/
    /**
     * Get all offers that were accepted by client to solve a demand.
     * When client accept an offer, will be involved here.
     * As Client: "All offers that were accepted by me to solve my demand."
     *
     * @param clientID
     * @param filter
     * @return
     */
    @Override
    public long getClientAssignedDemandsCount(long clientID, SearchModuleDataHolder filter) {
        //TODO Martin - implement when implemented on backend
        return -1L;
    }

    /**
     * Get all offers that were accepted by client to solve a demand.
     * When client accept an offer, will be involved here.
     * As Client: "All offers that were accepted by me to solve my demand."
     *
     * @param clientID
     * @param start
     * @param maxResult
     * @param filter
     * @param orderColumns
     * @return
     */
    @Override
    public List<OfferDetail> getClientAssignedDemands(long clientID, int start, int maxResult,
            SearchModuleDataHolder filter, Map<String, OrderType> orderColumns) {
        //TODO Martin - implement when implemented on backend
        return new ArrayList<OfferDetail>();
    }
}
