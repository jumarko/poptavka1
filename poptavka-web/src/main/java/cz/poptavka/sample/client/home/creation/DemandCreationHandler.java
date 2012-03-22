package cz.poptavka.sample.client.home.creation;

import com.google.gwt.user.client.Window;
import java.util.logging.Logger;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import cz.poptavka.sample.client.service.demand.DemandCreationRPCServiceAsync;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;

/**
 * Handler for RPC calls for DemandCreationModule
 *
 * @author Beho
 *
 */
@EventHandler
public class DemandCreationHandler extends BaseEventHandler<DemandCreationEventBus> {

    private DemandCreationRPCServiceAsync demandCreationService = null;
    private static final Logger LOGGER = Logger.getLogger("MainHandler");

    @Inject
    void setDemandCreationModuleRPCServiceAsync(DemandCreationRPCServiceAsync service) {
        demandCreationService = service;
    }

    /**
     * Verify identity of user, if exists in the system.
     * If so, new demand is created.
     *
     * @param client existing user detail
     */
    public void onVerifyExistingClient(UserDetail client) {
        LOGGER.fine("verify start");
        demandCreationService.verifyClient(client, new AsyncCallback<UserDetail>() {

            @Override
            public void onFailure(Throwable arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onSuccess(UserDetail client) {
                LOGGER.fine("verify result");
                if (client == null) {
                    eventBus.loadingHide();
                    eventBus.loginError();
                    return;
                }
                if (client.getClientId() != -1) {
                    eventBus.prepareNewDemandForNewClient(client);
                } else {
                    eventBus.loadingHide();
                    eventBus.loginError();
                }
            }
        });
    }

    /**
     * Method registers new client and afterwards creates new demand.
     *
     * @param client newly created client
     */
    public void onRegisterNewClient(UserDetail client) {
        demandCreationService.createNewClient(client, new AsyncCallback<UserDetail>() {

            @Override
            public void onFailure(Throwable arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onSuccess(UserDetail client) {
                if (client.getClientId() != -1) {
                    eventBus.prepareNewDemandForNewClient(client);
                }
            }
        });
    }

    /**
     * Creates new demand.
     *
     * @param detail
     *            front-end entity of demand
     * @param clientId
     *            client id
     */
    public void onCreateDemand(FullDemandDetail detail, Long clientId) {
        demandCreationService.createNewDemand(detail, clientId,
                new AsyncCallback<FullDemandDetail>() {

                    @Override
                    public void onFailure(Throwable arg0) {
                        eventBus.loadingHide();
                        Window.alert(arg0.getMessage());
                    }

                    @Override
                    public void onSuccess(FullDemandDetail result) {
                        // signal event
                        eventBus.loadingHide();
                        // TODO forward to user/atAccount
//                        eventBus.addNewDemand(result);
                    }
                });
        LOGGER.info("submitting new demand");
    }

    public void onCheckFreeEmail(String email) {
        demandCreationService.checkFreeEmail(email, new AsyncCallback<Boolean>() {

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