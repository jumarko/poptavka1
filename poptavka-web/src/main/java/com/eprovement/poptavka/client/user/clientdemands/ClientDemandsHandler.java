package com.eprovement.poptavka.client.user.clientdemands;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.errorDialog.ErrorDialogPopupView;
import com.eprovement.poptavka.client.service.demand.ClientDemandsRPCServiceAsync;
import com.eprovement.poptavka.client.user.handler.MessageHandler;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectContestantDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectConversationDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectDetail;
import com.eprovement.poptavka.shared.exceptions.ExceptionUtils;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import java.util.List;
import java.util.Map;

@EventHandler
public class ClientDemandsHandler extends BaseEventHandler<ClientDemandsEventBus> {

    @Inject
    private ClientDemandsRPCServiceAsync clientDemandsService;
    private ErrorDialogPopupView errorDialog;

    //*************************************************************************/
    // Overriden methods of IEventBusData interface.                          */
    //*************************************************************************/
    public void onGetDataCount(final UniversalAsyncGrid grid, SearchModuleDataHolder detail) {
        switch (Storage.getCurrentlyLoadedView()) {
            case Constants.CLIENT_PROJECTS:
                getClientProjectsCount(grid, detail);
                break;
            case Constants.CLIENT_PROJECT_DISCUSSIONS:
                getClientProjectConversationsCount(grid, detail);
                break;
            case Constants.CLIENT_OFFERED_PROJECTS:
                getClientOfferedProjectsCount(grid, detail);
                break;
            case Constants.CLIENT_PROJECT_CONTESTANTS:
                getClientProjectContestantsCount(grid, detail);
                break;
            case Constants.CLIENT_ASSIGNED_PROJECTS:
                getClientAssignedProjectsCount(grid, detail);
                break;
            default:
                break;
        }
    }

    public void onGetData(int start, int maxResults,
            SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        getClientProjects(start, maxResults, detail, orderColumns);
        switch (Storage.getCurrentlyLoadedView()) {
            case Constants.CLIENT_PROJECTS:
                getClientProjects(start, maxResults, detail, orderColumns);
                break;
            case Constants.CLIENT_PROJECT_DISCUSSIONS:
                getClientProjectConversations(start, maxResults, detail, orderColumns);
                break;
            case Constants.CLIENT_OFFERED_PROJECTS:
                getClientOfferedProjects(start, maxResults, detail, orderColumns);
                break;
            case Constants.CLIENT_PROJECT_CONTESTANTS:
                getClientProjectContestants(start, maxResults, detail, orderColumns);
                break;
            case Constants.CLIENT_ASSIGNED_PROJECTS:
                getClientAssignedProjects(start, maxResults, detail, orderColumns);
                break;
            default:
                break;
        }
    }

    //*************************************************************************/
    // Retrieving methods - CLIENT PROJECTS                                   */
    //*************************************************************************/
    private void getClientProjectsCount(final UniversalAsyncGrid grid, SearchModuleDataHolder detail) {
        clientDemandsService.getClientProjectsCount(Storage.getUser().getUserId(), detail,
                new AsyncCallback<Long>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof RPCException) {
                            ExceptionUtils.showErrorDialog(errorDialog, caught);
                        }
                    }

                    @Override
                    public void onSuccess(Long result) {
                        grid.createAsyncDataProvider(result.intValue());
                    }
                });
    }

    private void getClientProjects(int start, int maxResults,
            SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        clientDemandsService.getClientProjects(
                Storage.getUser().getUserId(), start, maxResults, detail, orderColumns,
                new AsyncCallback<List<ClientProjectDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    @Override
                    public void onSuccess(List<ClientProjectDetail> result) {
                        eventBus.displayClientProjects(result);
                    }
                });
    }

    //*************************************************************************/
    // Retrieving methods - CLIENT PROJECT CONVERSATIONS                      */
    //*************************************************************************/
    private void getClientProjectConversationsCount(final UniversalAsyncGrid grid, SearchModuleDataHolder detail) {
        clientDemandsService.getClientProjectConversationsCount(
                Storage.getUser().getUserId(), Storage.getDemandId(), detail,
                new AsyncCallback<Long>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    @Override
                    public void onSuccess(Long result) {
                        grid.createAsyncDataProvider(result.intValue());
                    }
                });
    }

    private void getClientProjectConversations(int start, int maxResults,
            SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        clientDemandsService.getClientProjectConversations(start, start, start, maxResults, detail, orderColumns,
                new AsyncCallback<List<ClientProjectConversationDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    @Override
                    public void onSuccess(List<ClientProjectConversationDetail> result) {
                        eventBus.displayClientProjectConversations(result);
                    }
                });
    }

    //*************************************************************************/
    // Retrieving methods - CLIENT OFFERED PROJECTS                           */
    //*************************************************************************/
    private void getClientOfferedProjectsCount(final UniversalAsyncGrid grid, SearchModuleDataHolder detail) {
        clientDemandsService.getClientOfferedProjectsCount(Storage.getUser().getUserId(), detail,
                new AsyncCallback<Long>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof RPCException) {
                            ExceptionUtils.showErrorDialog(errorDialog, caught);
                        }
                    }

                    @Override
                    public void onSuccess(Long result) {
                        grid.createAsyncDataProvider(result.intValue());
                    }
                });
    }

    private void getClientOfferedProjects(int start, int maxResults,
            SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        clientDemandsService.getClientOfferedProjects(
                Storage.getUser().getUserId(), Storage.getDemandId(), start, maxResults, detail, orderColumns,
                new AsyncCallback<List<ClientProjectDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    @Override
                    public void onSuccess(List<ClientProjectDetail> result) {
                        eventBus.displayClientOfferedProjects(result);
                    }
                });
    }

    //*************************************************************************/
    // Retrieving methods - CLIENT PROJECT CONTESTANTS                        */
    //*************************************************************************/
    private void getClientProjectContestantsCount(final UniversalAsyncGrid grid, SearchModuleDataHolder detail) {
        clientDemandsService.getClientProjectContestantsCount(
                Storage.getUser().getUserId(), Storage.getDemandId(), detail,
                new AsyncCallback<Long>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    @Override
                    public void onSuccess(Long result) {
                        grid.createAsyncDataProvider(result.intValue());
                    }
                });
    }

    private void getClientProjectContestants(int start, int maxResults,
            SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        clientDemandsService.getClientProjectContestants(
                Storage.getUser().getUserId(), Storage.getDemandId(), start, maxResults, detail, orderColumns,
                new AsyncCallback<List<ClientProjectContestantDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    @Override
                    public void onSuccess(List<ClientProjectContestantDetail> result) {
                        eventBus.displayClientProjectContestants(result);
                    }
                });
    }

    /**************************************************************************/
    /* Retrieving methods - CLIENT PROJECTS                                   */
    /**************************************************************************/
    private void getClientAssignedProjectsCount(final UniversalAsyncGrid grid, SearchModuleDataHolder detail) {
        clientDemandsService.getClientAssignedProjectsCount(
                Storage.getUser().getUserId(), detail,
                new AsyncCallback<Long>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof RPCException) {
                            ExceptionUtils.showErrorDialog(errorDialog, caught);
                        }
                    }

                    @Override
                    public void onSuccess(Long result) {
                        grid.createAsyncDataProvider(result.intValue());
                    }
                });
    }

    private void getClientAssignedProjects(int start, int maxResults,
            SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        clientDemandsService.getClientAssignedProjects(
                Storage.getUser().getUserId(), start, maxResults, detail, orderColumns,
                new AsyncCallback<List<ClientProjectContestantDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    @Override
                    public void onSuccess(List<ClientProjectContestantDetail> result) {
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
        clientDemandsService.setMessageReadStatus(selectedIdList, newStatus, new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error in MessageHandler in method: onRequestReadStatusUpdate"
                        + caught.getMessage());
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
            }

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
        clientDemandsService.setMessageStarStatus(userMessageIdList, newStatus, new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error in MessageHandler in method: onRequestStarStatusUpdate"
                        + caught.getMessage());
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
            }

            @Override
            public void onSuccess(Void result) {
                //Empty by default
            }
        });
    }

    /**************************************************************************/
    /* Button actions - messaging.                                            */
    /**************************************************************************/
    public void onGetOfferStatusChange(OfferDetail offerDetail) {
        GWT.log("STATE: " + offerDetail.getState());
        clientDemandsService.changeOfferState(offerDetail, new AsyncCallback<OfferDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
                Window.alert(MessageHandler.class.getName() + " at onGetOfferStatusChange\n\n" + caught.getMessage());
            }

            @Override
            public void onSuccess(OfferDetail result) {
                //TODO zistit ci bude treba nejaky refresh aj ked mame asyynchDataProvider, asi hej
                //skusit najpr redreaw na gride
//                eventBus.setOfferDetailChange(result);
            }
        });
    }
}
