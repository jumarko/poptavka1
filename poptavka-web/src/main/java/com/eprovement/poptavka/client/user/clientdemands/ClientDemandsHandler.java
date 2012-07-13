package com.eprovement.poptavka.client.user.clientdemands;

import com.eprovement.poptavka.client.main.Constants;
import com.eprovement.poptavka.client.main.Storage;
import com.eprovement.poptavka.client.main.errorDialog.ErrorDialogPopupView;
import com.eprovement.poptavka.client.service.demand.ClientDemandsRPCServiceAsync;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectContestantDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectConversationDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.domain.type.ViewType;
import com.eprovement.poptavka.shared.exceptions.ExceptionUtils;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import java.util.ArrayList;
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
        clientDemandsService.getClientProjects(start, maxResults, detail, orderColumns,
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

    public void onRequestDemandDetail(Long demandId, final ViewType type) {
        clientDemandsService.getFullDemandDetail(demandId, new AsyncCallback<FullDemandDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error in DemandModuleHandler in method: onRequestDemandDetail"
                        + caught.getMessage());
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
            }

            @Override
            public void onSuccess(FullDemandDetail result) {
                eventBus.responseDemandDetail(result, type);
            }
        });
    }

    public void onRequestSupplierDetail(Long supplierId, final ViewType type) {
        clientDemandsService.getFullSupplierDetail(supplierId, new AsyncCallback<FullSupplierDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error in DemandModuleHandler in method: onRequestDemandDetail"
                        + caught.getMessage());
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
            }

            @Override
            public void onSuccess(FullSupplierDetail result) {
                eventBus.responseSupplierDetail(result, type);
            }
        });
    }

    /**
     * Load demand/related conversation from DB.
     *
     * @param messageId
     * @param userMessageId
     * @param userId
     */
    public void onRequestConversation(long messageId, Long userMessageId, Long userId) {
        clientDemandsService.getSuppliersPotentialDemandConversation(messageId, userId, userMessageId,
                new AsyncCallback<ArrayList<MessageDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof RPCException) {
                            ExceptionUtils.showErrorDialog(errorDialog, caught);
                        }
                        Window.alert("DemandModuleMessageHandler: onRequestConversationForSupplierList:\n\n"
                                + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(ArrayList<MessageDetail> result) {
                        eventBus.responseConversation(result, ViewType.POTENTIAL);
                    }
                });
    }

    /**
     * Send message.
     * IMPORTANT: further implementation of other parts will show, if we need more than this method
     * for chat related stuff
     * @param messageToSend
     * @param type
     */
    public void onSendMessage(MessageDetail messageToSend, final ViewType type) {
        clientDemandsService.sendQueryToPotentialDemand(messageToSend, new AsyncCallback<MessageDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
                Window.alert("DemandModuleMessageHandler: onSendMessage:\n\n"
                        + caught.getMessage());
            }

            @Override
            public void onSuccess(MessageDetail sentMessage) {
                eventBus.sendMessageResponse(sentMessage, type);
            }
        });
    }
}
