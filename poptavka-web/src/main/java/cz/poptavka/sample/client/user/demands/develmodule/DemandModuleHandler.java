package cz.poptavka.sample.client.user.demands.develmodule;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.client.service.demand.MessageRPCServiceAsync;
import cz.poptavka.sample.shared.domain.message.PotentialDemandMessage;

@EventHandler
public class DemandModuleHandler extends BaseEventHandler<DemandModuleEventBus> {

    @Inject
    private MessageRPCServiceAsync messageService;

    /**
     * Get Supplier's potential demands list. No parameter is needed.
     * Business UserID is fetched from Storage
     */
    public void onRequestSupplierNewDemands() {
        messageService.getPotentialDemands(Storage.getUser().getUserId(),
                new AsyncCallback<ArrayList<PotentialDemandMessage>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("Error in MessageHandler in method: onGetPotentialDemandsList"
                                + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(
                            ArrayList<PotentialDemandMessage> result) {
                        GWT.log(">> CALL *RequestSupplierNewDemands. Result size: " + result.size());
                        eventBus.responseSupplierNewDemands(result);
                    }
                });
    }
}
