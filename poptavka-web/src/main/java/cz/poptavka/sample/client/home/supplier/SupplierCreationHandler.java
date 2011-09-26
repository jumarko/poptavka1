package cz.poptavka.sample.client.home.supplier;

import java.util.logging.Logger;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

//import cz.poptavka.sample.client.service.demand.CategoryRPCServiceAsync;
//import cz.poptavka.sample.client.service.demand.ClientRPCServiceAsync;
//import cz.poptavka.sample.client.service.demand.DemandRPCServiceAsync;
//import cz.poptavka.sample.client.service.demand.LocalityRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.ClientRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.SupplierRPCServiceAsync;
import cz.poptavka.sample.shared.domain.UserDetail;

/**
 * Handler for RPC calls for localities & categories.
 *
 * @author Beho
 *
 */
@EventHandler
public class SupplierCreationHandler extends BaseEventHandler<SupplierCreationEventBus> {

//    private LocalityRPCServiceAsync localityService = null;
//    private CategoryRPCServiceAsync categoryService = null;
//    private DemandRPCServiceAsync demandService = null;
//    private ClientRPCServiceAsync clientService = null;
    private SupplierRPCServiceAsync supplierService = null;
    @Inject
    private ClientRPCServiceAsync clientService = null;
    private static final Logger LOGGER = Logger.getLogger("MainHandler");

//    @Inject
//    public void setLocalityService(LocalityRPCServiceAsync service) {
//        localityService = service;
//    }
//
//    @Inject
//    public void setCategoryService(CategoryRPCServiceAsync service) {
//        categoryService = service;
//    }
//
//    @Inject
//    void setDemandService(DemandRPCServiceAsync service) {
//        demandService = service;
//    }
//
//    @Inject
//    void setClientRPCServiceAsync(ClientRPCServiceAsync service) {
//        clientService = service;
//    }
    @Inject
    void setSupplierRPCService(SupplierRPCServiceAsync service) {
        supplierService = service;
    }

//    /**
//     * Verify identity of user, if exists in the system.
//     * If so, new demand is created.
//     *
//     * @param client existing user detail
//     */
//    public void onVerifyExistingClient(UserDetail client) {
//        LOGGER.fine("verify start");
//        clientService.verifyClient(client, new AsyncCallback<UserDetail>() {
//            @Override
//            public void onFailure(Throwable arg0) {
//                // TODO Auto-generated method stub
//
//            }
//            @Override
//            public void onSuccess(UserDetail client) {
//                LOGGER.fine("verify result");
//                if (client == null) {
//                    eventBus.loadingHide();
//                    eventBus.loginError();
//                    return;
//                }
//                if (client.getClientId() != -1) {
//                    eventBus.prepareNewDemandForNewClient(client);
//                } else {
//                    eventBus.loadingHide();
//                    eventBus.loginError();
//                }
//            }
//        });
//    }
//    /**
//     * Method registers new client and afterwards creates new demand.
//     *
//     * @param client newly created client
//     */
//    public void onRegisterNewClient(UserDetail client) {
//        clientService.createNewClient(client, new AsyncCallback<UserDetail>() {
//            @Override
//            public void onFailure(Throwable arg0) {
//                // TODO Auto-generated method stub
//            }
//            @Override
//            public void onSuccess(UserDetail client) {
//                if (client.getClientId() != -1) {
//                    eventBus.prepareNewDemandForNewClient(client);
//                }
//            }
//        });
//    }
    public void onRegisterSupplier(UserDetail newSupplier) {
        supplierService.createNewSupplier(newSupplier, new AsyncCallback<UserDetail>() {

            @Override
            public void onFailure(Throwable arg0) {
                eventBus.loadingHide();
                Window.alert("Unexpected error occured! \n" + arg0.getMessage());
            }

            @Override
            public void onSuccess(UserDetail supplier) {
                // TODO forward to user/atAccount
                eventBus.loadingHide();
            }
        });
    }

    public void onCheckFreeEmail(String email) {
        clientService.checkFreeEmail(email, new AsyncCallback<Boolean>() {

            @Override
            public void onFailure(Throwable arg0) {
            }

            @Override
            public void onSuccess(Boolean result) {
                LOGGER.fine("result of compare " + result);
                eventBus.checkFreeEmailResponse(result);
                // eventBus.checkFreeEmailResponse();
            }
        });
    }
}
