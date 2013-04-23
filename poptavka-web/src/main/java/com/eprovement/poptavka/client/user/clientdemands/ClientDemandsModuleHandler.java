package com.eprovement.poptavka.client.user.clientdemands;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.service.demand.ClientDemandsModuleRPCServiceAsync;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.shared.domain.DemandRatingsDetail;
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

@EventHandler
public class ClientDemandsModuleHandler extends BaseEventHandler<ClientDemandsModuleEventBus> {

    @Inject
    private ClientDemandsModuleRPCServiceAsync clientDemandsService;

    //*************************************************************************/
    // Overriden methods of IEventBusData interface.                          */
    //*************************************************************************/
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

    private void getClientRatings(SearchDefinition searchDefinition) {
        clientDemandsService.getClientRatings(
                Storage.getUser().getUserId(), searchDefinition,
                new SecuredAsyncCallback<List<DemandRatingsDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<DemandRatingsDetail> result) {
                        eventBus.displayClientRatings(result);
                    }
                });
    }

    /**************************************************************************/
    /* Other                                                                  */
    /**************************************************************************/
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
    public void onUpdateUnreadMessagesCount() {
        clientDemandsService.updateUnreadMessagesCount(new SecuredAsyncCallback<UnreadMessagesDetail>(eventBus) {
            @Override
            public void onSuccess(UnreadMessagesDetail result) {
                eventBus.setUpdatedUnreadMessagesCount(result);
            }
        });
    }

    /**************************************************************************/
    /* Get Detail object for selecting in selection models                    */
    /**************************************************************************/
    public void onGetClientDemand(long clientDemandID) {
        clientDemandsService.getClientDemand(clientDemandID, new SecuredAsyncCallback<ClientDemandDetail>(eventBus) {
            @Override
            public void onSuccess(ClientDemandDetail result) {
                eventBus.selectClientDemand(result);
            }
        });
    }

    public void onGetClientDemandConversation(long clientDemandConversationID) {
        clientDemandsService.getClientDemandConversation(clientDemandConversationID,
                new SecuredAsyncCallback<ClientDemandConversationDetail>(eventBus) {
                    @Override
                    public void onSuccess(ClientDemandConversationDetail result) {
                        eventBus.selectClientDemandConversation(result);
                    }
                });
    }

    public void onGetClientOfferedDemand(long clientDemandID) {
        clientDemandsService.getClientOfferedDemand(clientDemandID,
                new SecuredAsyncCallback<ClientDemandDetail>(eventBus) {
                    @Override
                    public void onSuccess(ClientDemandDetail result) {
                        eventBus.selectClientOfferedDemand(result);
                    }
                });
    }

    public void onGetClientOfferedDemandOffer(long clientOfferedDemandOfferID) {
        clientDemandsService.getClientOfferedDemandOffer(clientOfferedDemandOfferID,
                new SecuredAsyncCallback<ClientOfferedDemandOffersDetail>(eventBus) {
                    @Override
                    public void onSuccess(ClientOfferedDemandOffersDetail result) {
                        eventBus.selectClientOfferedDemandOffer(result);
                    }
                });
    }

    public void onGetClientAssignedDemand(long demandID) {
        clientDemandsService.getClientAssignedDemand(demandID,
                new SecuredAsyncCallback<ClientOfferedDemandOffersDetail>(eventBus) {
                    @Override
                    public void onSuccess(ClientOfferedDemandOffersDetail result) {
                        eventBus.selectClientAssignedDemand(result);
                    }
                });
    }

    /**************************************************************************/
    /* CRUD operation of demand                                               */
    /**************************************************************************/
    public void onRequestDeleteDemand(long demandId) {
        clientDemandsService.requestDeleteDemand(demandId, new SecuredAsyncCallback<FullDemandDetail>(eventBus) {
            @Override
            public void onSuccess(FullDemandDetail result) {
                eventBus.responseDeleteDemand(result.getDemandStatus() == DemandStatus.CLOSED ? true : false);
            }
        });
    }

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
