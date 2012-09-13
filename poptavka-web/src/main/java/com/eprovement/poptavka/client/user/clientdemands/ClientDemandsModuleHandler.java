package com.eprovement.poptavka.client.user.clientdemands;

import com.eprovement.poptavka.client.root.email.EmailDialogPopupView;
import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.service.demand.ClientDemandsRPCServiceAsync;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectConversationDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.offer.FullOfferDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import java.util.ArrayList;
import java.util.List;

@EventHandler
public class ClientDemandsModuleHandler extends BaseEventHandler<ClientDemandsModuleEventBus> {

    @Inject
    private ClientDemandsRPCServiceAsync clientDemandsService;
    private EmailDialogPopupView errorDialog;

    //*************************************************************************/
    // Overriden methods of IEventBusData interface.                          */
    //*************************************************************************/
    public void onGetDataCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        switch (Storage.getCurrentlyLoadedView()) {
            case Constants.CLIENT_PROJECTS:
                getClientProjectsCount(grid, searchDefinition);
                break;
            case Constants.CLIENT_PROJECT_DISCUSSIONS:
                getClientProjectConversationsCount(grid, searchDefinition);
                break;
            case Constants.CLIENT_OFFERED_PROJECTS:
                getClientOfferedProjectsCount(grid, searchDefinition);
                break;
            case Constants.CLIENT_OFFERED_PROJECT_CONTESTANTS:
                getClientProjectContestantsCount(grid, searchDefinition);
                break;
            case Constants.CLIENT_ASSIGNED_PROJECTS:
                getClientAssignedProjectsCount(grid, searchDefinition);
                break;
            default:
                break;
        }
    }

    public void onGetData(SearchDefinition searchDefinition) {
        switch (Storage.getCurrentlyLoadedView()) {
            case Constants.CLIENT_PROJECTS:
                getClientProjects(searchDefinition);
                break;
            case Constants.CLIENT_PROJECT_DISCUSSIONS:
                getClientProjectConversations(searchDefinition);
                break;
            case Constants.CLIENT_OFFERED_PROJECTS:
                getClientOfferedProjects(searchDefinition);
                break;
            case Constants.CLIENT_OFFERED_PROJECT_CONTESTANTS:
                getClientOfferedProjectContestants(searchDefinition);
                break;
            case Constants.CLIENT_ASSIGNED_PROJECTS:
                getClientAssignedProjects(searchDefinition);
                break;
            default:
                break;
        }
    }

    //*************************************************************************/
    // Retrieving methods - CLIENT PROJECTS                                   */
    //*************************************************************************/
    private void getClientProjectsCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        clientDemandsService.getClientProjectsCount(Storage.getUser().getUserId(), searchDefinition,
                new SecuredAsyncCallback<Long>(eventBus) {
                    @Override
                    public void onSuccess(Long result) {
                        grid.createAsyncDataProvider(result.intValue());
                    }
                });
    }

    private void getClientProjects(SearchDefinition searchDefinition) {
        clientDemandsService.getClientProjects(
                Storage.getUser().getUserId(), searchDefinition,
                new SecuredAsyncCallback<List<ClientProjectDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<ClientProjectDetail> result) {
                        eventBus.displayClientProjects(result);
                    }
                });
    }

    //*************************************************************************/
    // Retrieving methods - CLIENT PROJECT CONVERSATIONS                      */
    //*************************************************************************/
    private void getClientProjectConversationsCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        clientDemandsService.getClientProjectConversationsCount(
                Storage.getUser().getUserId(), Storage.getDemandId(), searchDefinition,
                new SecuredAsyncCallback<Long>(eventBus) {
                    @Override
                    public void onSuccess(Long result) {
                        grid.createAsyncDataProvider(result.intValue());
                    }
                });
    }

    private void getClientProjectConversations(SearchDefinition searchDefinition) {
        clientDemandsService.getClientProjectConversations(
                Storage.getUser().getUserId(), Storage.getDemandId(), searchDefinition,
                new SecuredAsyncCallback<List<ClientProjectConversationDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<ClientProjectConversationDetail> result) {
                        eventBus.displayClientProjectConversations(result);
                    }
                });
    }

    //*************************************************************************/
    // Retrieving methods - CLIENT OFFERED PROJECTS                           */
    //*************************************************************************/
    private void getClientOfferedProjectsCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        clientDemandsService.getClientOfferedProjectsCount(Storage.getUser().getUserId(), searchDefinition,
                new SecuredAsyncCallback<Long>(eventBus) {
                    @Override
                    public void onSuccess(Long result) {
                        grid.createAsyncDataProvider(result.intValue());
                    }
                });
    }

    private void getClientOfferedProjects(SearchDefinition searchDefinition) {
        clientDemandsService.getClientOfferedProjects(
                Storage.getUser().getUserId(), Storage.getDemandId(), searchDefinition,
                new SecuredAsyncCallback<List<ClientProjectDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<ClientProjectDetail> result) {
                        eventBus.displayClientOfferedProjects(result);
                    }
                });
    }

    //*************************************************************************/
    // Retrieving methods - CLIENT PROJECT CONTESTANTS                        */
    //*************************************************************************/
    private void getClientProjectContestantsCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        clientDemandsService.getClientProjectContestantsCount(
                Storage.getUser().getUserId(), Storage.getDemandId(), searchDefinition,
                new SecuredAsyncCallback<Long>(eventBus) {
                    @Override
                    public void onSuccess(Long result) {
                        grid.createAsyncDataProvider(result.intValue());
                    }
                });
    }

    private void getClientOfferedProjectContestants(SearchDefinition searchDefinition) {
        clientDemandsService.getClientProjectContestants(
                Storage.getUser().getUserId(), Storage.getDemandId(), searchDefinition,
                new SecuredAsyncCallback<List<FullOfferDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<FullOfferDetail> result) {
                        eventBus.displayClientProjectContestants(result);
                    }
                });
    }

    /**************************************************************************/
    /* Retrieving methods - CLIENT PROJECTS                                   */
    /**************************************************************************/
    private void getClientAssignedProjectsCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        clientDemandsService.getClientAssignedProjectsCount(
                Storage.getUser().getUserId(), searchDefinition,
                new SecuredAsyncCallback<Long>(eventBus) {
                    @Override
                    public void onSuccess(Long result) {
                        grid.createAsyncDataProvider(result.intValue());
                    }
                });
    }

    private void getClientAssignedProjects(SearchDefinition searchDefinition) {
        clientDemandsService.getClientAssignedProjects(
                Storage.getUser().getUserId(), searchDefinition,
                new SecuredAsyncCallback<List<FullOfferDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<FullOfferDetail> result) {
                        eventBus.displayClientAssignedProjects(result);
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

    public void onRequestCloseDemand(FullDemandDetail demandDetail) {
        clientDemandsService.closeDemand(demandDetail, new SecuredAsyncCallback<ArrayList<Void>>(eventBus) {
            @Override
            public void onSuccess(ArrayList<Void> result) {
                //Empty by default
            }
        });
    }

    public void onRequestAcceptOffer(FullOfferDetail fullOfferDetail) {
        clientDemandsService.acceptOffer(fullOfferDetail, new SecuredAsyncCallback<ArrayList<Void>>(eventBus) {
            @Override
            public void onSuccess(ArrayList<Void> result) {
                //Empty by default
            }
        });
    }

    public void onRequestDeclineOffer(OfferDetail offerDetail) {
        clientDemandsService.declineOffer(offerDetail, new SecuredAsyncCallback<ArrayList<Void>>(eventBus) {
            @Override
            public void onSuccess(ArrayList<Void> result) {
                //Empty by default
            }
        });
    }

    /**************************************************************************/
    /* Button actions - messaging.                                            */
    /**************************************************************************/
    public void onGetOfferStatusChange(OfferDetail offerDetail) {
        GWT.log("STATE: " + offerDetail.getState());
        clientDemandsService.changeOfferState(offerDetail, new SecuredAsyncCallback<OfferDetail>(eventBus) {
            @Override
            public void onSuccess(OfferDetail result) {
                //TODO zistit ci bude treba nejaky refresh aj ked mame asyynchDataProvider, asi hej
                //skusit najpr redreaw na gride
//                eventBus.setOfferDetailChange(result);
            }
        });
    }
}
