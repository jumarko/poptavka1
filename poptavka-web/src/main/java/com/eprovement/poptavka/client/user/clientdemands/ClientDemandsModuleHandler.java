package com.eprovement.poptavka.client.user.clientdemands;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.service.demand.ClientDemandsModuleRPCServiceAsync;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.ChangeDetail;
import com.eprovement.poptavka.shared.domain.DemandRatingsDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandConversationDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandDetail;
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.eprovement.poptavka.shared.domain.offer.ClientOfferedDemandOffersDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import java.util.ArrayList;
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
    // Retrieving methods - CLIENT PROJECTS                                   */
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
    // Retrieving methods - CLIENT PROJECT CONVERSATIONS                      */
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
    // Retrieving methods - CLIENT OFFERED PROJECTS                           */
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
    /**
     * Changes demands Read status. Changes are displayed immediately on frontend. No onSuccess code is needed.
     *
     * @param selectedIdList list of demands which read status should be changed
     * @param newStatus of demandList
     */
    public void onRequestReadStatusUpdate(List<Long> selectedIdList, boolean newStatus) {
        clientDemandsService.setMessageReadStatus(selectedIdList, newStatus, new SecuredAsyncCallback<Void>(eventBus) {
            @Override
            public void onSuccess(Void result) {
                //Empty by default
            }
        });
    }

    /**
     * Changes demands star status. Changes are displayed immediately on frontend. No onSuccess code is needed.
     *
     * @param userMessageIdList list od demands which star status should be changed
     * @param newStatus of demandList
     */
    public void onRequestStarStatusUpdate(List<Long> userMessageIdList, boolean newStatus) {
        clientDemandsService.setMessageStarStatus(userMessageIdList, newStatus,
                new SecuredAsyncCallback<Void>(eventBus) {
                    @Override
                    public void onSuccess(Void result) {
                        //Empty by default
                    }
                });
    }

    public void onRequestCloseAndRateSupplier(final long demandID, final Integer supplierRating,
            final String supplierMessage) {
        clientDemandsService.closeDemandAndEnterFeedbackForSupplier(demandID, supplierRating, supplierMessage,
                new SecuredAsyncCallback<Void>(eventBus) {
                    @Override
                    public void onSuccess(Void result) {
                        eventBus.responseFeedback();
                        eventBus.goToClientDemandsModule(null, Constants.CLIENT_CLOSED_DEMANDS);
                    }
                });

    }

    public void onRequestAcceptOffer(long offerId, long latestUserMessageId) {
        GWT.log("onRequestAcceptOffer, params: offerId=" + offerId);
        clientDemandsService.acceptOffer(offerId, latestUserMessageId, Storage.MSGS.acceptedOfferMessage(),
                new SecuredAsyncCallback<Void>(eventBus) {
                    @Override
                    public void onSuccess(Void result) {
                        GWT.log("onRequestAcceptOffer finished");
                        eventBus.goToClientDemandsModule(null, Constants.CLIENT_ASSIGNED_DEMANDS);
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
                // empty i.e number of new messages could be retrieved
                GWT.log("UpdateUnreadMessagesCount retrieved, number=" + result.getUnreadMessagesCount());
                eventBus.setUpdatedUnreadMessagesCount(result.getUnreadMessagesCount());
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
    /* Get Detail object for restoring history state                          */
    /**************************************************************************/
    public void onGetClientDemandAndInitClientDemandConversationByHistory(
            long parentId, final int childTablePage, final long childId, final SearchModuleDataHolder filterHolder) {
        clientDemandsService.getClientDemand(parentId, new SecuredAsyncCallback<ClientDemandDetail>(eventBus) {
            @Override
            public void onSuccess(ClientDemandDetail result) {
                eventBus.initClientDemandConversationByHistory(result, childTablePage, childId, filterHolder);
            }
        });
    }

    public void onGetClientOfferedDemandAndInitClientOfferedDemandOffersByHistory(
            long parentId, final int childTablePage, final long childId, final SearchModuleDataHolder filterHolder) {
        clientDemandsService.getClientOfferedDemand(parentId,
                new SecuredAsyncCallback<ClientDemandDetail>(eventBus) {
                    @Override
                    public void onSuccess(ClientDemandDetail result) {
                        eventBus.initClientOfferedDemandOffersByHistory(result, childTablePage, childId, filterHolder);
                    }
                });
    }

    /**************************************************************************/
    /* CRUD operation of demand                                               */
    /**************************************************************************/
    public void onRequestDeleteDemand(long demandId) {
        clientDemandsService.deleteDemand(demandId, new SecuredAsyncCallback<Boolean>(eventBus) {
            @Override
            public void onSuccess(Boolean result) {
                eventBus.responseDeleteDemand(result);
            }
        });
    }

    public void onRequestUpdateDemand(long demandId, ArrayList<ChangeDetail> updatedFields) {
        clientDemandsService.updateDemand(demandId, updatedFields, new SecuredAsyncCallback<Boolean>(eventBus) {
            @Override
            public void onSuccess(Boolean result) {
                eventBus.responseUpdateDemand(result);
            }
        });
    }
}
