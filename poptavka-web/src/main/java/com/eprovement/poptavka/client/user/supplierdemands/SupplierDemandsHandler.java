package com.eprovement.poptavka.client.user.supplierdemands;

import com.eprovement.poptavka.client.common.SecuredAsyncCallback;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.service.demand.SupplierDemandsRPCServiceAsync;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.offer.FullOfferDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import java.util.List;

@EventHandler
public class SupplierDemandsHandler extends BaseEventHandler<SupplierDemandsEventBus> {

    @Inject
    private SupplierDemandsRPCServiceAsync supplierDemandsService = null;

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

    public void onGetData(SearchDefinition searchDefinition) {
        switch (Storage.getCurrentlyLoadedView()) {
            case Constants.SUPPLIER_POTENTIAL_PROJECTS:
                getSupplierPotentialProjects(searchDefinition);
                break;
            case Constants.SUPPLIER_CONTESTS:
                getSupplierContests(searchDefinition);
                break;
            case Constants.SUPPLIER_ASSIGNED_PROJECTS:
                getSupplierAssignedProjects(searchDefinition);
                break;
            default:
                break;
        }
    }
    //*************************************************************************/
    // Retrieving methods                                                     */
    //*************************************************************************/

    private void getSupplierPotentialProjectsCount(final UniversalAsyncGrid grid, SearchModuleDataHolder detail) {
        supplierDemandsService.getSupplierPotentialProjectsCount(Storage.getUser().getUserId(), detail,
                new SecuredAsyncCallback<Long>() {
                    public void onSuccess(Long result) {
                        grid.createAsyncDataProvider(result.intValue());
                    }
                });
    }

    private void getSupplierPotentialProjects(SearchDefinition searchDefinition) {
        supplierDemandsService.getSupplierPotentialProjects(
                Storage.getUser().getUserId(), searchDefinition,
                new SecuredAsyncCallback<List<FullOfferDetail>>() {
                    @Override
                    public void onSuccess(List<FullOfferDetail> result) {
//                        eventBus.displaySupplierPotentialProjects(result);
                        eventBus.displaySupplierDemandsData(result);
                    }
                });
    }
    //

    private void getSupplierContestsCount(final UniversalAsyncGrid grid, SearchModuleDataHolder detail) {
        supplierDemandsService.getSupplierContestsCount(
                Storage.getUser().getUserId(), detail, new SecuredAsyncCallback<Long>() {
                    @Override
                    public void onSuccess(Long result) {
                        grid.createAsyncDataProvider(result.intValue());
                    }
                });
    }

    private void getSupplierContests(SearchDefinition searchDefinition) {
        supplierDemandsService.getSupplierContests(
                Storage.getUser().getUserId(), searchDefinition,
                new SecuredAsyncCallback<List<FullOfferDetail>>() {
                    @Override
                    public void onSuccess(List<FullOfferDetail> result) {
                        eventBus.displaySupplierDemandsData(result);
                    }
                });
    }
    //

    private void getSupplierAssignedProjectsCount(final UniversalAsyncGrid grid, SearchModuleDataHolder detail) {
        supplierDemandsService.getSupplierAssignedProjectsCount(
                Storage.getUser().getUserId(), detail,
                new SecuredAsyncCallback<Long>() {
                    @Override
                    public void onSuccess(Long result) {
                        grid.createAsyncDataProvider(result.intValue());
                    }
                });
    }

    private void getSupplierAssignedProjects(SearchDefinition searchDefinition) {
        supplierDemandsService.getSupplierAssignedProjects(
                Storage.getUser().getUserId(), searchDefinition,
                new SecuredAsyncCallback<List<FullOfferDetail>>() {
                    @Override
                    public void onSuccess(List<FullOfferDetail> result) {
                        eventBus.displaySupplierDemandsData(result);
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
        supplierDemandsService.setMessageReadStatus(selectedIdList, newStatus, new SecuredAsyncCallback<Void>() {
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
        supplierDemandsService.setMessageStarStatus(userMessageIdList, newStatus, new SecuredAsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                //Empty by default
            }
        });
    }
}
