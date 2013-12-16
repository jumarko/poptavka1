/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.supplierdemands;

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
import java.util.List;

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
    public void onGetDataCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
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
    public void onGetData(SearchDefinition searchDefinition) {
        switch (Storage.getCurrentlyLoadedView()) {
            case Constants.SUPPLIER_POTENTIAL_DEMANDS:
                getSupplierPotentialDemands(searchDefinition);
                break;
            case Constants.SUPPLIER_OFFERS:
                getSupplierOffers(searchDefinition);
                break;
            case Constants.SUPPLIER_ASSIGNED_DEMANDS:
                getSupplierAssignedDemands(searchDefinition);
                break;
            case Constants.SUPPLIER_CLOSED_DEMANDS:
                getSupplierClosedDemands(searchDefinition);
                break;
            case Constants.SUPPLIER_RATINGS:
                getSupplierRatings(searchDefinition);
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
        supplierDemandsService.getSupplierDashboardDetail(Storage.getUser().getUserId(), Storage.getSupplierId(),
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
    private void getSupplierPotentialDemandsCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        supplierDemandsService.getSupplierPotentialDemandsCount(Storage.getUser().getUserId(), searchDefinition,
                new SecuredAsyncCallback<Integer>(eventBus) {

                    @Override
                    public void onSuccess(Integer result) {
                        grid.getDataProvider().updateRowCount(result, true);
                    }
                });
    }

    /**
     * Request potential demands table data.
     * @param searchDefinition - search criteria
     */
    private void getSupplierPotentialDemands(SearchDefinition searchDefinition) {
        supplierDemandsService.getSupplierPotentialDemands(
                Storage.getUser().getUserId(), Storage.getSupplierId(), searchDefinition,
                new SecuredAsyncCallback<List<SupplierPotentialDemandDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<SupplierPotentialDemandDetail> result) {
                        eventBus.displaySupplierDemands(result);
                    }
                });
    }

    /**
     * Request offers table data count.
     * @param grid - table
     * @param searchDefinition - search criteria
     */
    private void getSupplierOffersCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        supplierDemandsService.getSupplierOffersCount(
                Storage.getSupplierId(), searchDefinition,
                new SecuredAsyncCallback<Integer>(eventBus) {
                    @Override
                    public void onSuccess(Integer result) {
                        grid.getDataProvider().updateRowCount(result, true);
                    }
                });
    }

    /**
     * Request offers table data.
     * @param searchDefinition - search criteria
     */
    private void getSupplierOffers(SearchDefinition searchDefinition) {
        supplierDemandsService.getSupplierOffers(
                Storage.getSupplierId(), Storage.getUser().getUserId(), searchDefinition,
                new SecuredAsyncCallback<List<SupplierOffersDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<SupplierOffersDetail> result) {
                        eventBus.displaySupplierOffers(result);
                    }
                });
    }

    /**
     * Request assigend demands table data count.
     * @param grid - table
     * @param searchDefinition - search criteria
     */
    private void getSupplierAssignedDemandsCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        supplierDemandsService.getSupplierAssignedDemandsCount(
                Storage.getSupplierId(), searchDefinition,
                new SecuredAsyncCallback<Integer>(eventBus) {
                    @Override
                    public void onSuccess(Integer result) {
                        grid.getDataProvider().updateRowCount(result, true);
                    }
                });
    }

    /**
     * Request assinged demands table data .
     * @param searchDefinition - search criteria
     */
    private void getSupplierAssignedDemands(SearchDefinition searchDefinition) {
        supplierDemandsService.getSupplierAssignedDemands(
                Storage.getSupplierId(), searchDefinition,
                new SecuredAsyncCallback<List<SupplierOffersDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<SupplierOffersDetail> result) {
                        eventBus.displaySupplierAssignedDemands(result);
                    }
                });
    }

    /**
     * Request closed demands table data count.
     * @param grid - table
     * @param searchDefinition - search criteria
     */
    private void getSupplierClosedDemandsCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        supplierDemandsService.getSupplierClosedDemandsCount(
                Storage.getSupplierId(), searchDefinition,
                new SecuredAsyncCallback<Integer>(eventBus) {
                    @Override
                    public void onSuccess(Integer result) {
                        grid.getDataProvider().updateRowCount(result, true);
                    }
                });
    }

    /**
     * Request closed demands table data .
     * @param searchDefinition - search criteria
     */
    private void getSupplierClosedDemands(SearchDefinition searchDefinition) {
        supplierDemandsService.getSupplierClosedDemands(
                Storage.getSupplierId(), searchDefinition,
                new SecuredAsyncCallback<List<SupplierOffersDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<SupplierOffersDetail> result) {
                        eventBus.displaySupplierAssignedDemands(result);
                    }
                });
    }

    /**
     * Request ratings table data count.
     * @param grid - table
     * @param searchDefinition - search criteria
     */
    private void getSupplierRatingsCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        supplierDemandsService.getSupplierRatingsCount(
                Storage.getSupplierId(), searchDefinition,
                new SecuredAsyncCallback<Integer>(eventBus) {
                    @Override
                    public void onSuccess(Integer result) {
                        grid.getDataProvider().updateRowCount(result, true);
                    }
                });
    }

    /**
     * Request ratings table data .
     * @param searchDefinition - search criteria
     */
    private void getSupplierRatings(SearchDefinition searchDefinition) {
        supplierDemandsService.getSupplierRatings(
                Storage.getSupplierId(), searchDefinition,
                new SecuredAsyncCallback<List<RatingDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<RatingDetail> result) {
                        eventBus.displaySupplierRatings(result);
                    }
                });
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