package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.eprovement.poptavka.shared.domain.offer.SupplierOffersDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.domain.supplierdemands.SupplierPotentialDemandDetail;
import com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Martin Slavkovsky
 */
@RemoteServiceRelativePath(SupplierDemandsModuleRPCService.URL)
public interface SupplierDemandsModuleRPCService extends RemoteService {

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
    int getSupplierPotentialDemandsCount(long supplierID,
            SearchDefinition searchDefinition) throws RPCException;

    /**
     * Get demands of categories that I am interested in.
     * When a demand is created it is assigned to certain categories. Than the system sends
     * demands to suppliers what have registered those categories and are
     * interested in receiving this kind of demand.
     * As Supplier: "All potential demands that I am interested in."
     *
     * @param supplierID
     * @param searchDefinition
     * @return
     */
    List<SupplierPotentialDemandDetail> getSupplierPotentialDemands(long userId,
            long supplierId, SearchDefinition searchDefinition) throws RPCException;

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
    int getSupplierOffersCount(long supplierID,
            SearchDefinition searchDefinition) throws RPCException;

    /**
     * Get offers sent by supplier.
     * When supplier sends an offer, it will be involved here.
     * As Supplier: "Offers I sent"
     *
     * @param supplierID
     * @param searchDefinition
     * @return
     */
    List<SupplierOffersDetail> getSupplierOffers(long supplierID, long userId,
            SearchDefinition searchDefinition) throws RPCException;

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
    int getSupplierAssignedDemandsCount(long supplierID,
            SearchDefinition searchDefinition) throws RPCException;

    /**
     * Get supplier's offers that have been accepted.
     * When client accept an supplier's offer, the offer will be implemented here.
     * As Supplier: "Offers that I 'won'."
     *
     * @param supplierID
     * @param searchDefinition
     * @return
     */
    List<SupplierOffersDetail> getSupplierAssignedDemands(long supplierID,
            SearchDefinition searchDefinition) throws RPCException;

    /**************************************************************************/
    /* Other getter methods                                                   */
    /**************************************************************************/
    FullDemandDetail getFullDemandDetail(long demandId) throws RPCException;

    FullSupplierDetail getFullSupplierDetail(long supplierId) throws RPCException;

    ArrayList<MessageDetail> getSuppliersPotentialDemandConversation(long threadId, long userId,
            long userMessageId) throws RPCException;

    /**************************************************************************/
    /* Setter methods                                                         */
    /**************************************************************************/
    void setMessageReadStatus(List<Long> userMessageIds, boolean isRead) throws RPCException;

    void setMessageStarStatus(List<Long> list, boolean newStatus) throws RPCException;

    MessageDetail finishOffer(long offerId, long userMessageId, long userId) throws RPCException;

    UnreadMessagesDetail updateUnreadMessagesCount() throws RPCException;

    /**************************************************************************/
    /* Get Detail object for selecting in selection models                    */
    /**************************************************************************/
    SupplierPotentialDemandDetail getSupplierDemand(long supplierDemandID)
        throws RPCException, ApplicationSecurityException;

    SupplierOffersDetail getSupplierOffer(long supplierDemandID) throws RPCException, ApplicationSecurityException;

    SupplierOffersDetail getSupplierAssignedDemand(long demandID) throws RPCException, ApplicationSecurityException;

    /**
     * Suppier enters a new feedback for Client with respect to given demand.
     *
     * @param demandID of Demand to which this feedback is connected
     * @param clientRating integer number that will be assigned to client
     * @param clientMessage comment that will be assigned to client
     * @throws RPCException
     * @throws ApplicationSecurityException
     */
    void enterFeedbackForClient(long demandID, Integer clientRating, String clientMessage) throws RPCException;
}
