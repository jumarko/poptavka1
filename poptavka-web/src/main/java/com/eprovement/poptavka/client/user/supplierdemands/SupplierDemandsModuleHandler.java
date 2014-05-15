/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.supplierdemands;

import com.eprovement.poptavka.client.common.security.GetDataCallback;
import com.eprovement.poptavka.client.common.security.GetDataCountCallback;
import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.service.demand.SupplierDemandsModuleRPCServiceAsync;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.RatingDetail;
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.eprovement.poptavka.shared.domain.offer.SupplierOffersDetail;
import com.eprovement.poptavka.shared.domain.supplierdemands.SupplierDashboardDetail;
import com.eprovement.poptavka.shared.domain.supplierdemands.SupplierPotentialDemandDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

/**
 * Handle RPC calls for SupplierDemands module.
 *
 * @author Martin Slavkovsky
 */
@EventHandler
public class SupplierDemandsModuleHandler extends BaseEventHandler<SupplierDemandsModuleEventBus> {

    /**************************************************************************/
    /* Inject RPC services                                                    */
    /**************************************************************************/
    @Inject
    private SupplierDemandsModuleRPCServiceAsync supplierDemandsService = null;

    /**************************************************************************/
    /* Overriden methods of IEventBusData interface.                          */
    /**************************************************************************/
    /**
     * Request needed table data count.
     * @param grid - table
     * @param searchDefinition - search criteria
     */
    public void onGetDataCount(UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        switch (Storage.getCurrentlyLoadedView()) {
            case Constants.SUPPLIER_POTENTIAL_DEMANDS:
                getSupplierPotentialDemandsCount(grid, searchDefinition);
                break;
            case Constants.SUPPLIER_OFFERS:
                getSupplierOffersCount(grid, searchDefinition);
                break;
            case Constants.SUPPLIER_ASSIGNED_DEMANDS:
                getSupplierAssignedDemandsCount(grid, searchDefinition);
                break;
            case Constants.SUPPLIER_CLOSED_DEMANDS:
                getSupplierClosedDemandsCount(grid, searchDefinition);
                break;
            case Constants.SUPPLIER_RATINGS:
                getSupplierRatingsCount(grid, searchDefinition);
                break;
            default:
                break;
        }
    }

    /**
     * Request needed table data.
     * @param searchDefinition - search criteria
     */
    public void onGetData(UniversalAsyncGrid grid, SearchDefinition searchDefinition, int requestId) {
        switch (Storage.getCurrentlyLoadedView()) {
            case Constants.SUPPLIER_POTENTIAL_DEMANDS:
                getSupplierPotentialDemands(grid, searchDefinition, requestId);
                break;
            case Constants.SUPPLIER_OFFERS:
                getSupplierOffers(grid, searchDefinition, requestId);
                break;
            case Constants.SUPPLIER_ASSIGNED_DEMANDS:
                getSupplierAssignedDemands(grid, searchDefinition, requestId);
                break;
            case Constants.SUPPLIER_CLOSED_DEMANDS:
                getSupplierClosedDemands(grid, searchDefinition, requestId);
                break;
            case Constants.SUPPLIER_RATINGS:
                getSupplierRatings(grid, searchDefinition, requestId);
                break;
            default:
                break;
        }
    }

    /**************************************************************************/
    /* Retrieving methods - SUPPLIER WELCOME DASHBOARD                        */
    /**************************************************************************/
    /**
     * Requests supplier dashboard data.
     */
    public void onGetSupplierDashboardDetail() {
        supplierDemandsService.getSupplierDashboardDetail(Storage.getUser().getUserId(), Storage.getUser().getUserId(),
            new SecuredAsyncCallback<SupplierDashboardDetail>(eventBus) {

                @Override
                public void onSuccess(SupplierDashboardDetail result) {
                    eventBus.loadSupplierDashboardDetail(result);
                }
            });
    }

    /**************************************************************************/
    /* Retrieving methods                                                     */
    /**************************************************************************/
    /**
     * Request potential demands table data count.
     * @param grid - table
     * @param searchDefinition - search criteria
     */
    private void getSupplierPotentialDemandsCount(UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        supplierDemandsService.getSupplierPotentialDemandsCount(Storage.getUser().getUserId(), searchDefinition,
            new GetDataCountCallback(eventBus, grid));
    }

    /**
     * Request potential demands table data.
     * @param searchDefinition - search criteria
     */
    private void getSupplierPotentialDemands(
        UniversalAsyncGrid grid, SearchDefinition searchDefinition, int requestId) {
        supplierDemandsService.getSupplierPotentialDemands(Storage.getUser().getUserId(), searchDefinition,
            new GetDataCallback<SupplierPotentialDemandDetail>(eventBus, grid, requestId));
    }

    /**
     * Request offers table data count.
     * @param grid - table
     * @param searchDefinition - search criteria
     */
    private void getSupplierOffersCount(UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        supplierDemandsService.getSupplierOffersCount(
            Storage.getUser().getUserId(), searchDefinition,
            new GetDataCountCallback(eventBus, grid));
    }

    /**
     * Request offers table data.
     * @param searchDefinition - search criteria
     */
    private void getSupplierOffers(UniversalAsyncGrid grid, SearchDefinition searchDefinition, int requestId) {
        supplierDemandsService.getSupplierOffers(Storage.getUser().getUserId(), searchDefinition,
            new GetDataCallback<SupplierOffersDetail>(eventBus, grid, requestId));
    }

    /**
     * Request assigend demands table data count.
     * @param grid - table
     * @param searchDefinition - search criteria
     */
    private void getSupplierAssignedDemandsCount(UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        supplierDemandsService.getSupplierAssignedDemandsCount(Storage.getSupplierId(), searchDefinition,
            new GetDataCountCallback(eventBus, grid));
    }

    /**
     * Request assinged demands table data .
     * @param searchDefinition - search criteria
     */
    private void getSupplierAssignedDemands(
        UniversalAsyncGrid grid, SearchDefinition searchDefinition, int requestId) {
        supplierDemandsService.getSupplierAssignedDemands(Storage.getSupplierId(), searchDefinition,
            new GetDataCallback<SupplierOffersDetail>(eventBus, grid, requestId));
    }

    /**
     * Request closed demands table data count.
     * @param grid - table
     * @param searchDefinition - search criteria
     */
    private void getSupplierClosedDemandsCount(UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        supplierDemandsService.getSupplierClosedDemandsCount(Storage.getSupplierId(), searchDefinition,
            new GetDataCountCallback(eventBus, grid));
    }

    /**
     * Request closed demands table data .
     * @param searchDefinition - search criteria
     */
    private void getSupplierClosedDemands(UniversalAsyncGrid grid, SearchDefinition searchDefinition, int requestId) {
        supplierDemandsService.getSupplierClosedDemands(Storage.getSupplierId(), searchDefinition,
            new GetDataCallback<SupplierOffersDetail>(eventBus, grid, requestId));
    }

    /**
     * Request ratings table data count.
     * @param grid - table
     * @param searchDefinition - search criteria
     */
    private void getSupplierRatingsCount(UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        supplierDemandsService.getSupplierRatingsCount(Storage.getSupplierId(), searchDefinition,
            new GetDataCountCallback(eventBus, grid));
    }

    /**
     * Request ratings table data .
     * @param searchDefinition - search criteria
     */
    private void getSupplierRatings(UniversalAsyncGrid grid, SearchDefinition searchDefinition, int requestId) {
        supplierDemandsService.getSupplierRatings(Storage.getSupplierId(), searchDefinition,
            new GetDataCallback<RatingDetail>(eventBus, grid, requestId));
    }

    /**************************************************************************/
    /* Other                                                                  */
    /**************************************************************************/
    /**
     * Finnishes offer and rates client.
     * @param demandID
     * @param offerID
     * @param rating of client
     * @param message of rating
     */
    public void onRequestFinishAndRateClient(final long demandID, final long offerID,
        final Integer rating, final String message) {
        supplierDemandsService.finishOfferAndEnterFeedbackForClient(demandID, offerID, rating, message,
            new SecuredAsyncCallback<Void>(eventBus) {
                @Override
                public void onSuccess(Void result) {
                    eventBus.sendStatusMessage(Storage.MSGS.finishedOfferMessage());
                    eventBus.responseFeedback();
                }
            });
    }

    /**
     * Updates unread messages count.
     */
    public void onUpdateUnreadMessagesCount() {
        supplierDemandsService.updateUnreadMessagesCount(new SecuredAsyncCallback<UnreadMessagesDetail>(eventBus) {
            @Override
            public void onSuccess(UnreadMessagesDetail result) {
                eventBus.setUpdatedUnreadMessagesCount(result);
            }
        });
    }
}
