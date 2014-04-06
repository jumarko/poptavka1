package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.RatingDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.eprovement.poptavka.shared.domain.offer.SupplierOffersDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.domain.supplierdemands.SupplierDashboardDetail;
import com.eprovement.poptavka.shared.domain.supplierdemands.SupplierPotentialDemandDetail;
import com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;

/**
 *
 * @author Martin Slavkovsky
 */
@RemoteServiceRelativePath(SupplierDemandsModuleRPCService.URL)
public interface SupplierDemandsModuleRPCService extends RemoteService {

    String URL = "service/supplierdemandsmodule";

    //************************ SUPPLIER - Welcome Page **************************/
    /**
     * Load all data to construct SupplierDashboardDetail. Data such as number of unread messages for particular
     * sections will be retrieved.
     *
     * @param userId of Supplier for which dashboard object will be created
     * @param supplierId of this Supplier
     * @return supplierDashboardDetail
     * @throws RPCException
     * @throws ApplicationSecurityException
     */
    SupplierDashboardDetail getSupplierDashboardDetail(long userId, long supplierId) throws RPCException,
            ApplicationSecurityException;


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
    List<SupplierPotentialDemandDetail> getSupplierPotentialDemands(long supplierId,
        SearchDefinition searchDefinition) throws RPCException;

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
    List<SupplierOffersDetail> getSupplierOffers(long supplierID,
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

    //******************* SUPPLIER - My Closed Demands ************************/
    /**
     * Get supplier's closed demands.
     * When supplier's work has been accepted, demand changed his status to Closed.
     *
     * @param supplierID
     * @param filter
     * @return
     */
    int getSupplierClosedDemandsCount(long supplierID,
            SearchDefinition searchDefinition) throws RPCException;

    /**
     * Get supplier's closed demands.
     * When supplier's work has been accepted, demand changed his status to Closed.
     *
     * @param supplierID
     * @param searchDefinition
     * @return
     */
    List<SupplierOffersDetail> getSupplierClosedDemands(long supplierID,
            SearchDefinition searchDefinition) throws RPCException;

    //******************** SUPPLIER - My Ratings ******************************/
    /**
     * Get ratings of my closed demands.
     *
     * @param filter
     * @return
     */
    int getSupplierRatingsCount(long supplierID,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException;

    /**
     * Get ratings of my all closed demands.
     *
     * @param searchDefinition
     * @return
     */
    List<RatingDetail> getSupplierRatings(long supplierID,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException;

    /**************************************************************************/
    /* Other getter methods                                                   */
    /**************************************************************************/
    FullDemandDetail getFullDemandDetail(long demandId) throws RPCException;

    FullSupplierDetail getFullSupplierDetail(long supplierId) throws RPCException;

    /**************************************************************************/
    /* Setter methods                                                         */
    /**************************************************************************/
    UnreadMessagesDetail updateUnreadMessagesCount() throws RPCException;

    /**
     * Supplier enters a new feedback for Client with respect to given demand and finnish that demand.
     *
     * @param demandID of Demand to which this feedback is connected
     * @param rating integer number that will be assigned to client
     * @param message comment that will be assigned to client
     * @throws RPCException
     * @throws ApplicationSecurityException
     */
    void finishOfferAndEnterFeedbackForClient(long demandID, long offerID, Integer rating,
            String message) throws RPCException;
}
