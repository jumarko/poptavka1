package com.eprovement.poptavka.client.user.supplierdemands;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.main.errorDialog.ErrorDialogPopupView;
import com.eprovement.poptavka.client.service.demand.SupplierDemandsRPCServiceAsync;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectContestantDetail;
import com.eprovement.poptavka.shared.domain.supplierdemands.SupplierPotentialProjectDetail;
import com.eprovement.poptavka.shared.exceptions.ExceptionUtils;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import java.util.List;
import java.util.Map;

@EventHandler
public class SupplierDemandsHandler extends BaseEventHandler<SupplierDemandsEventBus> {

    @Inject
    private SupplierDemandsRPCServiceAsync supplierDemandsService = null;
    private ErrorDialogPopupView errorDialog;

    //*************************************************************************/
    // Overriden methods of IEventBusData interface.                          */
    //*************************************************************************/
    public void onGetDataCount(final UniversalAsyncGrid grid, SearchModuleDataHolder detail) {
        switch (Storage.getCurrentlyLoadedView()) {
            case Constants.SUPPLIER_POTENTIAL_PROJECTS:
                getSupplierPotentialProjectsCount(grid, detail);
                break;
            case Constants.SUPPLIER_CONTESTS:
                getSupplierContestsCount(grid, detail);
                break;
            case Constants.SUPPLIER_ASSIGNED_PROJECTS:
                getSupplierAssignedProjectsCount(grid, detail);
                break;
            default:
                break;
        }
    }

    public void onGetData(int start, int maxResults,
            SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        switch (Storage.getCurrentlyLoadedView()) {
            case Constants.SUPPLIER_POTENTIAL_PROJECTS:
                getSupplierPotentialProjects(start, maxResults, detail, orderColumns);
                break;
            case Constants.SUPPLIER_CONTESTS:
                getSupplierContests(start, maxResults, detail, orderColumns);
                break;
            case Constants.SUPPLIER_ASSIGNED_PROJECTS:
                getSupplierAssignedProjects(start, maxResults, detail, orderColumns);
                break;
            default:
                break;
        }
    }
    //*************************************************************************/
    // Retrieving methods                                                     */
    //*************************************************************************/

    private void getSupplierPotentialProjectsCount(final UniversalAsyncGrid grid, SearchModuleDataHolder detail) {
        supplierDemandsService.getSupplierPotentialProjectsCount(
                Storage.getUser().getUserId(), detail,
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

    private void getSupplierPotentialProjects(int start, int maxResults,
            SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        supplierDemandsService.getSupplierPotentialProjects(
                Storage.getUser().getUserId(), start, maxResults, detail, orderColumns,
                new AsyncCallback<List<SupplierPotentialProjectDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    @Override
                    public void onSuccess(List<SupplierPotentialProjectDetail> result) {
                        eventBus.displaySupplierPotentialProjects(result);
                    }
                });
    }
    //

    private void getSupplierContestsCount(final UniversalAsyncGrid grid, SearchModuleDataHolder detail) {
        supplierDemandsService.getSupplierContestsCount(
                Storage.getUser().getUserId(), detail,
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

    private void getSupplierContests(int start, int maxResults,
            SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        supplierDemandsService.getSupplierContests(
                Storage.getUser().getUserId(), start, maxResults, detail, orderColumns,
                new AsyncCallback<List<ClientProjectContestantDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    @Override
                    public void onSuccess(List<ClientProjectContestantDetail> result) {
//                        eventBus.displaySupplierProjectContests(result);
                    }
                });
    }
    //

    private void getSupplierAssignedProjectsCount(final UniversalAsyncGrid grid, SearchModuleDataHolder detail) {
        supplierDemandsService.getSupplierAssignedProjectsCount(
                Storage.getUser().getUserId(), detail,
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

    private void getSupplierAssignedProjects(int start, int maxResults,
            SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        supplierDemandsService.getSupplierAssignedProjects(
                Storage.getUser().getUserId(), start, maxResults, detail, orderColumns,
                new AsyncCallback<List<ClientProjectContestantDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    @Override
                    public void onSuccess(List<ClientProjectContestantDetail> result) {
//                        eventBus.displaySupplierAssignedProjects(result);
                    }
                });
    }

    //*************************************************************************/
    // Other                                                                    */
    //*************************************************************************/
    /**
     * Changes demands Read status. Changes are displayed immediately on frontend. No onSuccess code is needed.
     *
     * @param selectedIdList list of demands which read status should be changed
     * @param newStatus of demandList
     */
    public void onRequestReadStatusUpdate(List<Long> selectedIdList, boolean newStatus) {
        supplierDemandsService.setMessageReadStatus(selectedIdList, newStatus, new AsyncCallback<Void>() {

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
        supplierDemandsService.setMessageStarStatus(userMessageIdList, newStatus, new AsyncCallback<Void>() {

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
}
