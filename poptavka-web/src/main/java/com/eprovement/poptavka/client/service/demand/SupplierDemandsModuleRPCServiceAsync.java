package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.DemandRatingsDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.eprovement.poptavka.shared.domain.offer.SupplierOffersDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.domain.supplierdemands.SupplierDashboardDetail;
import com.eprovement.poptavka.shared.domain.supplierdemands.SupplierPotentialDemandDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;

/**
 *
 * @author Martin Slavkovsky
 */
public interface SupplierDemandsModuleRPCServiceAsync {

    //SupplierWelcome widget
    void getSupplierDashboardDetail(long userId, long supplierId, AsyncCallback<SupplierDashboardDetail> callback);

    //SupplierDemands widget
    void getSupplierPotentialDemandsCount(long supplierID, SearchDefinition searchDefinition,
            AsyncCallback<Integer> callback);

    void getSupplierPotentialDemands(long userId, long supplierId, SearchDefinition searchDefinition,
            AsyncCallback<List<SupplierPotentialDemandDetail>> callback);

    //SupplierOffers widget
    void getSupplierOffersCount(long supplierID, SearchDefinition searchDefinition,
            AsyncCallback<Integer> callback);

    void getSupplierOffers(long supplierID, long userId, SearchDefinition searchDefinition,
            AsyncCallback<List<SupplierOffersDetail>> callback);

    //SupplierAssignedDemands widget
    void getSupplierAssignedDemandsCount(long supplierID, SearchDefinition searchDefinition,
            AsyncCallback<Integer> callback);

    void getSupplierAssignedDemands(long supplierID, SearchDefinition searchDefinition,
            AsyncCallback<List<SupplierOffersDetail>> callback);

    //SupplierClosedDemands widget
    void getSupplierClosedDemandsCount(long supplierID, SearchDefinition searchDefinition,
            AsyncCallback<Integer> callback);

    void getSupplierClosedDemands(long supplierID, SearchDefinition searchDefinition,
            AsyncCallback<List<SupplierOffersDetail>> callback);

    //SupplierRatings widget
    void getSupplierRatingsCount(long supplierID, SearchDefinition searchDefinition,
            AsyncCallback<Integer> callback);

    void getSupplierRatings(long supplierID, SearchDefinition searchDefinition,
            AsyncCallback<List<DemandRatingsDetail>> callback);

    /**************************************************************************/
    /* Other getter methods                                                   */
    /**************************************************************************/
    void getFullDemandDetail(long demandId, AsyncCallback<FullDemandDetail> callback);

    void getFullSupplierDetail(long supplierId, AsyncCallback<FullSupplierDetail> callback);

    /**************************************************************************/
    /* Setter methods                                                         */
    /**************************************************************************/
    void updateUnreadMessagesCount(AsyncCallback<UnreadMessagesDetail> callback);

    /**************************************************************************/
    /* Get Detail object for selecting in selection models                    */
    /**************************************************************************/
    void getSupplierDemand(long id, AsyncCallback<SupplierPotentialDemandDetail> calback);

    void getSupplierOffer(long id, AsyncCallback<SupplierOffersDetail> calback);

    void getSupplierAssignedDemand(long id, AsyncCallback<SupplierOffersDetail> calback);

    void finishOfferAndEnterFeedbackForClient(long demandID, long offerID, Integer rating,
            String message, AsyncCallback<Void> calback);

}
