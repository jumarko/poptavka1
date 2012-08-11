package com.eprovement.poptavka.client.user.demands.handler;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.common.errorDialog.ErrorDialogPopupView;
import com.eprovement.poptavka.client.service.demand.DemandsRPCServiceAsync;
import com.eprovement.poptavka.client.user.demands.DemandEventBus;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.ClientDemandMessageDetail;
import com.eprovement.poptavka.shared.domain.message.PotentialDemandMessage;
import com.eprovement.poptavka.shared.domain.type.ViewType;

@EventHandler
public class DemandContentHandler extends BaseEventHandler<DemandEventBus> {

    @Inject
    private DemandsRPCServiceAsync demandsService;
    private ErrorDialogPopupView errorDialog;

    public void onRequestClientsDemands(SearchModuleDataHolder searchModuleDataHolder) {
        GWT.log("DemandModuleContentHandler > UserId: " + ((BusinessUserDetail) Storage.getUser()).getUserId()
                + " ,ClientId: " + ((BusinessUserDetail) Storage.getUser()).getClientId());
        demandsService.getListOfClientDemandMessages(Storage.getUser().getUserId(),
                ((BusinessUserDetail) Storage.getUser()).getClientId(),
                new SecuredAsyncCallback<ArrayList<ClientDemandMessageDetail>>() {
                    @Override
                    public void onSuccess(ArrayList<ClientDemandMessageDetail> result) {
                        eventBus.responseClientsDemands(result);
                    }
                });
    }

    /**
     * Get Supplier's potential demands list. No parameter is needed.
     * Business UserID is fetched from Storage
     */
    public void onRequestSupplierNewDemands(SearchModuleDataHolder searchModuleDataHolder) {
        demandsService.getPotentialDemands(Storage.getUser().getUserId(),
                new SecuredAsyncCallback<ArrayList<PotentialDemandMessage>>() {
                    @Override
                    public void onSuccess(
                            ArrayList<PotentialDemandMessage> result) {
                        GWT.log(">> CALL *RequestSupplierNewDemands. Result size: " + result.size());
                        eventBus.responseSupplierNewDemands(result);
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
        demandsService.setMessageReadStatus(selectedIdList, newStatus, new SecuredAsyncCallback<Void>() {
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
        demandsService.setMessageStarStatus(userMessageIdList, newStatus, new SecuredAsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                //Empty by default
            }
        });
    }

    public void onRequestDemandDetail(Long demandId, final ViewType type) {
        demandsService.getFullDemandDetail(demandId, new SecuredAsyncCallback<FullDemandDetail>() {
            @Override
            public void onSuccess(FullDemandDetail result) {
//                eventBus.responseDemandDetail(result, type);
            }
        });
    }
}