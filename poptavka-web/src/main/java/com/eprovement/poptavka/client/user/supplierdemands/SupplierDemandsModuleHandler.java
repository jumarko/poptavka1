package com.eprovement.poptavka.client.user.supplierdemands;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.service.demand.SupplierDemandsModuleRPCServiceAsync;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.eprovement.poptavka.shared.domain.offer.FullOfferDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import java.util.List;

@EventHandler
public class SupplierDemandsModuleHandler extends BaseEventHandler<SupplierDemandsModuleEventBus> {

    @Inject
    private SupplierDemandsModuleRPCServiceAsync supplierDemandsService = null;

    //*************************************************************************/
    // Overriden methods of IEventBusData interface.                          */
    //*************************************************************************/
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
            default:
                break;
        }
    }

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
            default:
                break;
        }
    }
    //*************************************************************************/
    // Retrieving methods                                                     */
    //*************************************************************************/

    private void getSupplierPotentialDemandsCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        supplierDemandsService.getSupplierPotentialDemandsCount(Storage.getUser().getUserId(), searchDefinition,
                new SecuredAsyncCallback<Long>(eventBus) {
                    public void onSuccess(Long result) {
                        grid.createAsyncDataProvider(result.intValue());
                    }
                });
    }

    private void getSupplierPotentialDemands(SearchDefinition searchDefinition) {
        supplierDemandsService.getSupplierPotentialDemands(
                Storage.getUser().getUserId(), searchDefinition,
                new SecuredAsyncCallback<List<FullOfferDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<FullOfferDetail> result) {
//                        eventBus.displaySupplierPotentialDemands(result);
                        eventBus.displaySupplierDemandsData(result);
                    }
                });
    }
    //

    private void getSupplierOffersCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        supplierDemandsService.getSupplierOffersCount(
                Storage.getUser().getUserId(), searchDefinition, new SecuredAsyncCallback<Long>(eventBus) {
                    @Override
                    public void onSuccess(Long result) {
                        grid.createAsyncDataProvider(result.intValue());
                    }
                });
    }

    private void getSupplierOffers(SearchDefinition searchDefinition) {
        supplierDemandsService.getSupplierOffers(
                Storage.getUser().getUserId(), searchDefinition,
                new SecuredAsyncCallback<List<FullOfferDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<FullOfferDetail> result) {
                        eventBus.displaySupplierDemandsData(result);
                    }
                });
    }
    //

    private void getSupplierAssignedDemandsCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        supplierDemandsService.getSupplierAssignedDemandsCount(
                Storage.getUser().getUserId(), searchDefinition,
                new SecuredAsyncCallback<Long>(eventBus) {
                    @Override
                    public void onSuccess(Long result) {
                        grid.createAsyncDataProvider(result.intValue());
                    }
                });
    }

    private void getSupplierAssignedDemands(SearchDefinition searchDefinition) {
        supplierDemandsService.getSupplierAssignedDemands(
                Storage.getUser().getUserId(), searchDefinition,
                new SecuredAsyncCallback<List<FullOfferDetail>>(eventBus) {
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
        supplierDemandsService.setMessageReadStatus(selectedIdList, newStatus,
                new SecuredAsyncCallback<Void>(eventBus) {
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
        supplierDemandsService.setMessageStarStatus(userMessageIdList, newStatus,
                new SecuredAsyncCallback<Void>(eventBus) {
                    @Override
                    public void onSuccess(Void result) {
                        //Empty by default
                    }
                });
    }

    public void onRequestFinishOffer(FullOfferDetail fullOfferDetail) {
        supplierDemandsService.finishOffer(fullOfferDetail, new SecuredAsyncCallback<Void>(eventBus) {

            @Override
            public void onSuccess(Void result) {
                //Empty by default
            }
        });
    }

    public void onUpdateUnreadMessagesCount() {
        supplierDemandsService.updateUnreadMessagesCount(new SecuredAsyncCallback<UnreadMessagesDetail>(eventBus) {

            @Override
            public void onSuccess(UnreadMessagesDetail result) {
                // empty i.e number of new messages could be retrieved
                GWT.log("UpdateUnreadMessagesCount retrieved, number=" + result.getUnreadMessagesCount());
            }
        });
    }
}
