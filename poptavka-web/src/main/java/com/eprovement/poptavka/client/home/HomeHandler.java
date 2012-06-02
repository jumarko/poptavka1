package com.eprovement.poptavka.client.home;

import java.util.logging.Logger;

import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import com.eprovement.poptavka.client.service.demand.CategoryRPCServiceAsync;
import com.eprovement.poptavka.client.service.demand.ClientRPCServiceAsync;
import com.eprovement.poptavka.client.service.demand.DemandRPCServiceAsync;
import com.eprovement.poptavka.client.service.demand.LocalityRPCServiceAsync;

/**
 * Handler for RPC calls for localities & categories.
 *
 * TODO can be removed
 *
 * @author Beho
 *
 */
@EventHandler
public class HomeHandler extends BaseEventHandler<HomeEventBus> {

    private LocalityRPCServiceAsync localityService = null;
    private CategoryRPCServiceAsync categoryService = null;
    private DemandRPCServiceAsync demandService = null;
    private ClientRPCServiceAsync clientService = null;
//    private SupplierRPCServiceAsync supplierService = null;
    private static final Logger LOGGER = Logger.getLogger("MainHandler");

    @Inject
    public void setLocalityService(LocalityRPCServiceAsync service) {
        localityService = service;
    }

    @Inject
    public void setCategoryService(CategoryRPCServiceAsync service) {
        categoryService = service;
    }

    @Inject
    void setDemandService(DemandRPCServiceAsync service) {
        demandService = service;
    }

    @Inject
    void setClientRPCServiceAsync(ClientRPCServiceAsync service) {
        clientService = service;
    }

//    @Inject
//    void setSupplierRPCService(SupplierRPCServiceAsync service) {
//        supplierService = service;
//    }


    // TODO praso moved to DemandCreationHandler
//    /**
//     * Verify identity of user, if exists in the system.
//     * If so, new demand is created.
//     *
//     * @param client existing user detail
//     */
//    public void onVerifyExistingClient(UserDetail client) {
//        LOGGER.fine("verify start");
//        clientService.verifyClient(client, new AsyncCallback<UserDetail>() {
//
//            @Override
//            public void onFailure(Throwable arg0) {
//                // TODO Auto-generated method stub
//            }
//
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


    // TODO praso - moved to demandcreationhandler
//    /**
//     * Method registers new client and afterwards creates new demand.
//     *
//     * @param client newly created client
//     */
//    public void onRegisterNewClient(UserDetail client) {
//        clientService.createNewClient(client, new AsyncCallback<UserDetail>() {
//
//            @Override
//            public void onFailure(Throwable arg0) {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void onSuccess(UserDetail client) {
//                if (client.getClientId() != -1) {
//                    eventBus.prepareNewDemandForNewClient(client);
//                }
//            }
//        });
//    }
//    public void onRegisterSupplier(UserDetail newSupplier) {
//        supplierService.createNewSupplier(newSupplier, new AsyncCallback<UserDetail>() {
//            @Override
//            public void onFailure(Throwable arg0) {
//                eventBus.loadingHide();
//                Window.alert("Unexpected error occured! \n" + arg0.getMessage());
//            }
//
//            @Override
//            public void onSuccess(UserDetail supplier) {
//                // TODO forward to user/atAccount
//                eventBus.loadingHide();
//            }
//        });
//    }
}
