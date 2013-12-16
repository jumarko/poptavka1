/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.clientdemands;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.service.demand.ClientDemandsModuleRPCServiceAsync;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.shared.domain.RatingDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDashboardDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandConversationDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.eprovement.poptavka.shared.domain.offer.ClientOfferedDemandOffersDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import java.util.List;

/**
 * Handle RPC calls for ClientDemands module.
 * @author Martin Slavkovsky
 */
@EventHandler
public class ClientDemandsModuleHandler extends BaseEventHandler<ClientDemandsModuleEventBus> {

    /**************************************************************************/
    /* Inject RPC services                                                    */
    /**************************************************************************/
    @Inject
    private ClientDemandsModuleRPCServiceAsync clientDemandsService;

    //*************************************************************************/
    // Overriden methods of IEventBusData interface.                          */
    //*************************************************************************/
    /**
     * Request table data count.
     * @param grid - table
     * @param searchDefinition - search criteria
     */
    public void onGetDataCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        switch (Storage.getCurrentlyLoadedView()) {
            case Constants.CLIENT_DEMANDS:
                getClientDemandsCount(grid, searchDefinition);
                break;
            case Constants.CLIENT_DEMAND_DISCUSSIONS:
                getClientDemandConversationsCount(grid, searchDefinition);
                break;
            case Constants.CLIENT_OFFERED_DEMANDS:
                getClientOfferedDemandsCount(grid, searchDefinition);
                break;
            case Constants.CLIENT_OFFERED_DEMAND_OFFERS:
                getClientOfferedDemandOffersCount(grid, searchDefinition);
                break;
            case Constants.CLIENT_ASSIGNED_DEMANDS:
                getClientAssignedDemandsCount(grid, searchDefinition);
                break;
            case Constants.CLIENT_CLOSED_DEMANDS:
                getClientClosedDemandsCount(grid, searchDefinition);
                break;
            case Constants.CLIENT_RATINGS:
                getClientRatingsCount(grid, searchDefinition);
                break;
            default:
                break;
        }
    }

    /**
     * Request table data.
     * @param searchDefinition - search criteria
     */
    public void onGetData(SearchDefinition searchDefinition) {
        switch (Storage.getCurrentlyLoadedView()) {
            case Constants.CLIENT_DEMANDS:
                getClientDemands(searchDefinition);
                break;
            case Constants.CLIENT_DEMAND_DISCUSSIONS:
                getClientDemandConversations(searchDefinition);
                break;
            case Constants.CLIENT_OFFERED_DEMANDS:
                getClientOfferedDemands(searchDefinition);
                break;
            case Constants.CLIENT_OFFERED_DEMAND_OFFERS:
                getClientOfferedDemandOffers(searchDefinition);
                break;
            case Constants.CLIENT_ASSIGNED_DEMANDS:
                getClientAssignedDemands(searchDefinition);
                break;
            case Constants.CLIENT_CLOSED_DEMANDS:
                getClientClosedDemands(searchDefinition);
                break;
            case Constants.CLIENT_RATINGS:
                getClientRatings(searchDefinition);
                break;
            default:
                break;
        }
    }

    //*************************************************************************/
    // Retrieving methods - CLIENT WELCOME DASHBOARD                          */
    //*************************************************************************/
    /**
     * Request client dashboard detail.
     */
    public void onGetClientDashboardDetail() {
        clientDemandsService.getClientDashboardDetail(Storage.getUser().getUserId(),
                new SecuredAsyncCallback<ClientDashboardDetail>(eventBus) {
                    @Override
                    public void onSuccess(ClientDashboardDetail result) {
                        eventBus.loadClientDashboardDetail(result);
                    }
                });
    }

    //*************************************************************************/
    // Retrieving methods - CLIENT DEMANDS                                   */
    //*************************************************************************/
    /**
     * Request client demands data count.
     * @param grid - table
     * @param searchDefinition - search criteria
     */
    private void getClientDemandsCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        clientDemandsService.getClientDemandsCount(Storage.getUser().getUserId(), searchDefinition,
                new SecuredAsyncCallback<Integer>(eventBus) {
                    @Override
                    public void onSuccess(Integer result) {
                        GWT.log("getClientDemandsCount: " + result);
                        grid.getDataProvider().updateRowCount(result, true);
                    }
                });
    }

    /**
     * Request client demands data.
     * @param searchDefinition - search criteria
     */
    private void getClientDemands(SearchDefinition searchDefinition) {
        clientDemandsService.getClientDemands(
                Storage.getUser().getUserId(), searchDefinition,
                new SecuredAsyncCallback<List<ClientDemandDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<ClientDemandDetail> result) {
                        eventBus.displayClientDemands(result);
                    }
                });
    }

    //*************************************************************************/
    // Retrieving methods - CLIENT DEMAND CONVERSATIONS                      */
    //*************************************************************************/
    /**
     * Request client demands conversation table data count.
     * @param grid - table
     * @param searchDefinition - search criteria
     */
    private void getClientDemandConversationsCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        clientDemandsService.getClientDemandConversationsCount(
                Storage.getUser().getUserId(), Storage.getDemandId(), searchDefinition,
                new SecuredAsyncCallback<Integer>(eventBus) {
                    @Override
                    public void onSuccess(Integer result) {
                        GWT.log("getClientDemandConversationsCount: " + result);
                        grid.getDataProvider().updateRowCount(result, true);
                        if (result == 0) {
                            eventBus.responseConversationNoData();
                        }
                    }
                });
    }

    /**
     * Request client demands conversation table data.
     * @param searchDefinition - search criteria
     */
    private void getClientDemandConversations(SearchDefinition searchDefinition) {
        clientDemandsService.getClientDemandConversations(
                Storage.getUser().getUserId(), Storage.getDemandId(), searchDefinition,
                new SecuredAsyncCallback<List<ClientDemandConversationDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<ClientDemandConversationDetail> result) {
                        eventBus.displayClientDemandConversations(result);
                    }
                });
    }

    //*************************************************************************/
    // Retrieving methods - CLIENT OFFERED DEMANDS                           */
    //*************************************************************************/
    /**
     * Request client offered demands table data count.
     * @param grid - table
     * @param searchDefinition - search criteria
     */
    private void getClientOfferedDemandsCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        clientDemandsService.getClientOfferedDemandsCount(Storage.getUser().getUserId(), searchDefinition,
                new SecuredAsyncCallback<Integer>(eventBus) {
                    @Override
                    public void onSuccess(Integer result) {
                        GWT.log("getClientOfferedDemandsCount: " + result);
                        grid.getDataProvider().updateRowCount(result, true);
                    }
                });
    }

    /**
     * Request client offered demands table data.
     * @param searchDefinition - search criteria
     */
    private void getClientOfferedDemands(SearchDefinition searchDefinition) {
        clientDemandsService.getClientOfferedDemands(
                Storage.getUser().getUserId(), searchDefinition,
                new SecuredAsyncCallback<List<ClientDemandDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<ClientDemandDetail> result) {
                        eventBus.displayClientOfferedDemands(result);
                    }
                });
    }

    //*************************************************************************/
    // Retrieving methods - CLIENT PROJECT CONTESTANTS                        */
    //*************************************************************************/
    /**
     * Request client demands offers table data count.
     * @param grid - table
     * @param searchDefinition - search criteria
     */
    private void getClientOfferedDemandOffersCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        clientDemandsService.getClientOfferedDemandOffersCount(
                Storage.getUser().getUserId(), Storage.getDemandId(), searchDefinition,
                new SecuredAsyncCallback<Integer>(eventBus) {
                    @Override
                    public void onSuccess(Integer result) {
                        GWT.log("getClientOfferedDemandOffersCount: " + result);
                        grid.getDataProvider().updateRowCount(result, true);
                    }
                });
    }

    /**
     * Request client demands offers table data.
     * @param searchDefinition - search criteria
     */
    private void getClientOfferedDemandOffers(SearchDefinition searchDefinition) {
        clientDemandsService.getClientOfferedDemandOffers(
                Storage.getUser().getUserId(), Storage.getDemandId(), Storage.getThreadRootId(), searchDefinition,
                new SecuredAsyncCallback<List<ClientOfferedDemandOffersDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<ClientOfferedDemandOffersDetail> result) {
                        eventBus.displayClientOfferedDemandOffers(result);
                    }
                });
    }

    /**************************************************************************/
    /* Retrieving methods - CLIENT ASSIGNED DEMANDS                           */
    /**************************************************************************/
    /**
     * Request client assigned demands table data count.
     * @param grid - table
     * @param searchDefinition - search criteria
     */
    private void getClientAssignedDemandsCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        clientDemandsService.getClientAssignedDemandsCount(
                Storage.getUser().getUserId(), searchDefinition,
                new SecuredAsyncCallback<Integer>(eventBus) {
                    @Override
                    public void onSuccess(Integer result) {
                        GWT.log("getClientAssignedDemandsCount: " + result);
                        grid.getDataProvider().updateRowCount(result, true);
                    }
                });
    }

    /**
     * Request client assigned demands table data.
     * @param searchDefinition - search criteria
     */
    private void getClientAssignedDemands(SearchDefinition searchDefinition) {
        clientDemandsService.getClientAssignedDemands(
                Storage.getUser().getUserId(), searchDefinition,
                new SecuredAsyncCallback<List<ClientOfferedDemandOffersDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<ClientOfferedDemandOffersDetail> result) {
                        eventBus.displayClientAssignedDemands(result);
                    }
                });
    }

    /**************************************************************************/
    /* Retrieving methods - CLIENT CLOSED DEMANDS                           */
    /**************************************************************************/
    /**
     * Request client closed demands table data count.
     * @param grid - table
     * @param searchDefinition - search criteria
     */
    private void getClientClosedDemandsCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        clientDemandsService.getClientClosedDemandsCount(
                Storage.getUser().getUserId(), searchDefinition,
                new SecuredAsyncCallback<Integer>(eventBus) {
                    @Override
                    public void onSuccess(Integer result) {
                        GWT.log("getClientAssignedDemandsCount: " + result);
                        grid.getDataProvider().updateRowCount(result, true);
                    }
                });
    }

    /**
     * Request client closed demands table data.
     * @param searchDefinition - search criteria
     */
    private void getClientClosedDemands(SearchDefinition searchDefinition) {
        clientDemandsService.getClientClosedDemands(
                Storage.getUser().getUserId(), searchDefinition,
                new SecuredAsyncCallback<List<ClientOfferedDemandOffersDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<ClientOfferedDemandOffersDetail> result) {
                        eventBus.displayClientAssignedDemands(result);
                    }
                });
    }

    /**************************************************************************/
    /* Retrieving methods - CLIENT RATINGS                                    */
    /**************************************************************************/
    /**
     * Request client ratings table data count.
     * @param grid - table
     * @param searchDefinition - ssearch criteria
     */
    private void getClientRatingsCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        clientDemandsService.getClientRatingsCount(
                Storage.getUser().getUserId(), searchDefinition,
                new SecuredAsyncCallback<Integer>(eventBus) {
                    @Override
                    public void onSuccess(Integer result) {
                        GWT.log("getClientRatingsCount: " + result);
                        grid.getDataProvider().updateRowCount(result, true);
                    }
                });
    }

    /**
     * Request client ratings table data.
     * @param searchDefinition - search criteria
     */
    private void getClientRatings(SearchDefinition searchDefinition) {
        clientDemandsService.getClientRatings(
                Storage.getUser().getUserId(), searchDefinition,
                new SecuredAsyncCallback<List<RatingDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<RatingDetail> result) {
                        eventBus.displayClientRatings(result);
                    }
                });
    }

    /**************************************************************************/
    /* Other                                                                  */
    /**************************************************************************/
    /**
     * Close demand and rate supplier.
     * @param demandID
     * @param offerID
     * @param supplierRating - supplier's rating
     * @param supplierMessage - rating's message
     */
    public void onRequestCloseAndRateSupplier(final long demandID, final long offerID, final Integer supplierRating,
            final String supplierMessage) {
        clientDemandsService.closeDemandAndEnterFeedbackForSupplier(demandID, offerID, supplierRating, supplierMessage,
                new SecuredAsyncCallback<Void>(eventBus) {
                    @Override
                    public void onSuccess(Void result) {
                        eventBus.sendStatusMessage(Storage.MSGS.closeDemandMessage());
                        eventBus.responseFeedback();
                    }
                });
    }

    /**
     * Accepts offer.
     * @param offerId
     */
    public void onRequestAcceptOffer(long offerId) {
        GWT.log("onRequestAcceptOffer, params: offerId=" + offerId);
        clientDemandsService.acceptOffer(offerId, new SecuredAsyncCallback<Void>(eventBus) {
                @Override
                public void onSuccess(Void result) {
                    GWT.log("onRequestAcceptOffer finished");
                    eventBus.sendStatusMessage(Storage.MSGS.acceptedOfferMessage());
                    eventBus.responseAcceptOffer();
                }
            });
    }

    /**************************************************************************/
    /* Button actions - messaging.                                            */
    /**************************************************************************/
    /**
     * Updates unread messages count.
     */
    public void onUpdateUnreadMessagesCount() {
        clientDemandsService.updateUnreadMessagesCount(new SecuredAsyncCallback<UnreadMessagesDetail>(eventBus) {
            @Override
            public void onSuccess(UnreadMessagesDetail result) {
                eventBus.setUpdatedUnreadMessagesCount(result);
            }
        });
    }

    /**************************************************************************/
    /* CRUD operation of demand                                               */
    /**************************************************************************/
    /**
     * Deletes demand.
     * @param demandId
     */
    public void onRequestDeleteDemand(long demandId) {
        clientDemandsService.requestDeleteDemand(demandId, new SecuredAsyncCallback<FullDemandDetail>(eventBus) {
            @Override
            public void onSuccess(FullDemandDetail result) {
                eventBus.responseDeleteDemand(result.getDemandStatus() == DemandStatus.CLOSED ? true : false);
            }
        });
    }

    /**
     * Updates demand.
     * @param demandId
     * @param updatedDemand
     */
    public void onRequestUpdateDemand(long demandId, FullDemandDetail updatedDemand) {
        clientDemandsService.updateDemand(demandId, updatedDemand,
                new SecuredAsyncCallback<FullDemandDetail>(eventBus) {
                @Override
                public void onSuccess(FullDemandDetail result) {
                    eventBus.responseUpdateDemand(result);
                }
            });
    }
}
