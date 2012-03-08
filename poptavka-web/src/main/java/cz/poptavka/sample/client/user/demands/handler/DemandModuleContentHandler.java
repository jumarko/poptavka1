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
import cz.poptavka.sample.client.service.demand.DemandRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.MessageRPCServiceAsync;
import cz.poptavka.sample.client.user.demands.DemandModuleEventBus;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.demandsModule.ClientDemandDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import cz.poptavka.sample.shared.domain.demandsModule.ClientOfferDetail;
import cz.poptavka.sample.shared.domain.message.PotentialDemandMessage;
import cz.poptavka.sample.shared.domain.type.ViewType;
import java.util.Map;

@EventHandler
public class DemandModuleContentHandler extends BaseEventHandler<DemandModuleEventBus> {

    @Inject
    private MessageRPCServiceAsync messageService;
    @Inject
    private DemandRPCServiceAsync demandService;

    public void onRequestClientsDemands() {
        GWT.log("DemandModuleContentHandler > UserId: " + Storage.getUser().getUserId() + " ,ClientId: "
                + Storage.getUser().getClientId());
        messageService.getClientDemands(Storage.getUser().getUserId(), Storage.getUser().getClientId(),
                new AsyncCallback<ArrayList<ClientDemandDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("MessageHandler: onGetClientDemandCOnversations:\n\n" + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(ArrayList<ClientDemandDetail> result) {
                        eventBus.responseClientsDemands(result);
                    }
                });
    }

    public void onRequestClientsOffers() {
//        GWT.log("DemandModuleContentHandler (clientOffers) > UserId: " + Storage.getUser().getUserId());
//        messageService.getClientOffers(Storage.getUser().getUserId(),
//                new AsyncCallback<ArrayList<ClientOfferDetail>>() {
//
//                    @Override
//                    public void onFailure(Throwable caught) {
//                        Window.alert("MessageHandler: onGetClientOfferOnversations:\n\n" + caught.getMessage());
//                    }
//
//                    @Override
//                    public void onSuccess(ArrayList<ClientOfferDetail> result) {
//                        eventBus.responseClientsOffers(result);
//                    }
//                });
    }

    public void onFilterClientOffersCount(SearchModuleDataHolder detail) {
        messageService.filterClientOffersCount(detail, new AsyncCallback<Long>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("onFilterDemands (HomeDemandsHandler) - not supported yet.");
            }

            @Override
            public void onSuccess(Long result) {
                eventBus.createAsyncClientDemandDataProvider(result.intValue());
            }
        });
    }

    public void onFilterClientOffers(int start, int count, SearchModuleDataHolder detail,
            Map<String, OrderType> orderColumns) {
        messageService.filterClientOffers(start, count, detail, orderColumns,
                new AsyncCallback<ArrayList<ClientDemandDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException(
                                "onFilterDemands (HomeDemandsHandler) - not supported yet.");
                    }

                    @Override
                    public void onSuccess(ArrayList<ClientDemandDetail> result) {
                        eventBus.responseClientsOffers(result);
                    }
                });
    }

    public void onRequestClientsOfferMessages(Long clientDemandId) {
        GWT.log("DemandModuleContentHandler (clientOffers) > UserId: " + Storage.getUser().getUserId());
        messageService.getClientOfferMessages(clientDemandId,
                new AsyncCallback<ArrayList<ClientOfferDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("MessageHandler: onGetClientOfferOnversations:\n\n" + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(ArrayList<ClientOfferDetail> result) {
                        eventBus.responseClientsOfferMessages(result);
                    }
                });
    }

    /**
     * Get Supplier's potential demands list. No parameter is needed.
     * Business UserID is fetched from Storage
     */
    public void onRequestSupplierNewDemands(SearchModuleDataHolder searchModuleDataHolder) {
        messageService.getPotentialDemands(Storage.getUser().getUserId(),
                new AsyncCallback<ArrayList<PotentialDemandMessage>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        if (!(caught instanceof IllegalArgumentException)) {
                            Window.alert("Error in MessageHandler in method: onGetPotentialDemandsList\n\n"
                                    + caught.getMessage());
                        }
                    }

                    @Override
                    public void onSuccess(
                            ArrayList<PotentialDemandMessage> result) {
                        GWT.log(">> CALL *RequestSupplierNewDemands. Result size: " + result.size());
                        eventBus.responseSupplierNewDemands(result);
                    }
                });
//        messageService.getPotentialDemandsBySearch(Storage.getUser().getUserId(), searchModuleDataHolder,
//                new AsyncCallback<ArrayList<PotentialDemandMessage>>() {
//                    @Override
//                    public void onFailure(Throwable caught) {
//                        if (!(caught instanceof IllegalArgumentException)) {
//                            Window.alert("Error in MessageHandler in method: onGetPotentialDemandsList\n\n"
//                                + caught.getMessage());
//                        }
//                    }
//
//                    @Override
//                    public void onSuccess(
//                            ArrayList<PotentialDemandMessage> result) {
//                        GWT.log(">> CALL *RequestSupplierNewDemands. Result size: " + result.size());
//                        eventBus.responseSupplierNewDemands(result);
//                    }
//                });
    }

    /**
     * Changes demands Read status. Changes are displayed immediately on frontend. No onSuccess code is needed.
     *
     * @param list list of demands which read status should be changed
     * @param newStatus of demandList
     */
    public void onRequestReadStatusUpdate(List<Long> selectedIdList, boolean newStatus) {
        messageService.setMessageReadStatus(selectedIdList, newStatus, new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error in MessageHandler in method: onRequestReadStatusUpdate"
                        + caught.getMessage());
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
        messageService.setMessageStarStatus(userMessageIdList, newStatus, new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error in MessageHandler in method: onRequestStarStatusUpdate"
                        + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                //Empty by default
            }
        });
    }

    public void onRequestDemandDetail(Long demandId, final ViewType type) {
        demandService.getFullDemandDetail(demandId, new AsyncCallback<FullDemandDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error in DemandModuleHandler in method: onRequestDemandDetail"
                        + caught.getMessage());
            }

            @Override
            public void onSuccess(FullDemandDetail result) {
                eventBus.responseDemandDetail(result, type);
            }
        });
    }

    public void onRequestOfferDetail(Long offerId, final ViewType type) {
//        demandService.getOfferDetail(offerId, new AsyncCallback<OfferDetail>() {
//
//            @Override
//            public void onFailure(Throwable caught) {
//                Window.alert("Error in DemandModuleHandler in method: onRequestOfferDetail"
//                        + caught.getMessage());
//            }
//
//            @Override
//            public void onSuccess(OfferDetail result) {
//                eventBus.responseOfferDetail(result, type);
//            }
//        });
    }
}