package cz.poptavka.sample.client.user.demands.handler;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.main.errorDialog.ErrorDialogPopupView;
import cz.poptavka.sample.client.service.demand.DemandsRPCServiceAsync;
import cz.poptavka.sample.client.user.demands.DemandEventBus;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import cz.poptavka.sample.shared.domain.message.ClientDemandMessageDetail;
import cz.poptavka.sample.shared.domain.message.PotentialDemandMessage;
import cz.poptavka.sample.shared.domain.type.ViewType;
import cz.poptavka.sample.shared.exceptions.ExceptionUtils;
import cz.poptavka.sample.shared.exceptions.RPCException;

@EventHandler
public class DemandContentHandler extends BaseEventHandler<DemandEventBus> {

//    @Inject
//    private MessageRPCServiceAsync messageService;
//    @Inject
//    private DemandRPCServiceAsync demandService;
    @Inject
    private DemandsRPCServiceAsync demandsService;
    private ErrorDialogPopupView errorDialog;

    public void onRequestClientsDemands(SearchModuleDataHolder searchModuleDataHolder) {
        GWT.log("DemandModuleContentHandler > UserId: " + Storage.getUser().getUserId() + " ,ClientId: "
                + Storage.getUser().getClientId());
        demandsService.getListOfClientDemandMessages(Storage.getUser().getUserId(), Storage.getUser().getClientId(),
                new AsyncCallback<ArrayList<ClientDemandMessageDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof RPCException) {
                            ExceptionUtils.showErrorDialog(errorDialog, caught);
                        }
                        Window.alert("MessageHandler: onGetClientDemandCOnversations:\n\n" + caught.getMessage());
                    }

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
                new AsyncCallback<ArrayList<PotentialDemandMessage>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        if (!(caught instanceof IllegalArgumentException)) {
                            Window.alert("Error in MessageHandler in method: onGetPotentialDemandsList\n\n"
                                    + caught.getMessage());
                        }
                        if (caught instanceof RPCException) {
                            ExceptionUtils.showErrorDialog(errorDialog, caught);
                        }
                    }

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
     * @param list list of demands which read status should be changed
     * @param newStatus of demandList
     */
    public void onRequestReadStatusUpdate(List<Long> selectedIdList, boolean newStatus) {
        demandsService.setMessageReadStatus(selectedIdList, newStatus, new AsyncCallback<Void>() {

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
     * @param list list od demands which star status should be changed
     * @param newStatus of demandList
     */
    public void onRequestStarStatusUpdate(List<Long> userMessageIdList, boolean newStatus) {
        demandsService.setMessageStarStatus(userMessageIdList, newStatus, new AsyncCallback<Void>() {

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
        demandsService.getFullDemandDetail(demandId, new AsyncCallback<FullDemandDetail>() {

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
}