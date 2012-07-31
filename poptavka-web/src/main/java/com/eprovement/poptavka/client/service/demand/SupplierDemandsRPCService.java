package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.offer.FullOfferDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.ArrayList;
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
    long getSupplierPotentialProjectsCount(long supplierID, SearchModuleDataHolder filter)
        throws ApplicationSecurityException;

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
    List<FullOfferDetail> getSupplierPotentialProjects(long supplierID, int start, int maxResult,
            SearchModuleDataHolder filter, Map<String, OrderType> orderColumns) throws ApplicationSecurityException;

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
    long getSupplierContestsCount(long supplierID, SearchModuleDataHolder filter) throws ApplicationSecurityException;

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
    //TODO Martin premenovat FullOfferDetail na ContestDetail alebo nieco podobne
    List<FullOfferDetail> getSupplierContests(long supplierID, int start, int maxResult,
            SearchModuleDataHolder filter, Map<String, OrderType> orderColumns) throws ApplicationSecurityException;

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
    long getSupplierAssignedProjectsCount(long supplierID, SearchModuleDataHolder filter)
        throws ApplicationSecurityException;

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
    List<FullOfferDetail> getSupplierAssignedProjects(long supplierID, int start, int maxResult,
            SearchModuleDataHolder filter, Map<String, OrderType> orderColumns) throws ApplicationSecurityException;

    /**************************************************************************/
    /* Other getter methods                                                   */
    /**************************************************************************/
    FullDemandDetail getFullDemandDetail(long demandId) throws ApplicationSecurityException;

    FullSupplierDetail getFullSupplierDetail(long supplierId) throws ApplicationSecurityException;

    ArrayList<MessageDetail> getSuppliersPotentialDemandConversation(long threadId, long userId,
            long userMessageId) throws RPCException, ApplicationSecurityException;

    /**************************************************************************/
    /* Setter methods                                                         */
    /**************************************************************************/
    void setMessageReadStatus(List<Long> userMessageIds, boolean isRead)
        throws RPCException, ApplicationSecurityException;

    void setMessageStarStatus(List<Long> list, boolean newStatus) throws RPCException, ApplicationSecurityException;
}
