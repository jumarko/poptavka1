package com.eprovement.poptavka.client.home.createDemand;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import com.eprovement.poptavka.client.service.demand.DemandCreationRPCServiceAsync;
import com.eprovement.poptavka.client.service.demand.UserRPCServiceAsync;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;

/**
 * Handler for RPC calls for DemandCreationModule
 *
 * @author Beho
 *
 */
@EventHandler
public class DemandCreationHandler extends BaseEventHandler<DemandCreationEventBus> {

    private static final Logger LOGGER = Logger.getLogger("MainHandler");

    private DemandCreationRPCServiceAsync demandCreationService = null;
    private UserRPCServiceAsync userRpcService;

    @Inject
    void setDemandCreationModuleRPCServiceAsync(DemandCreationRPCServiceAsync service) {
        demandCreationService = service;
    }

    @Inject
    void setUserRpcService(UserRPCServiceAsync userRpcService) {
        this.userRpcService = userRpcService;
    }

    /**
     * Verify identity of user, if exists in the system.
     * If so, new demand is created.
     *
     * @param client existing user detail
     */
    public void onVerifyExistingClient(String email, String password) {
        LOGGER.fine("verify start");
        userRpcService.loginUser(email, password, new SecuredAsyncCallback<UserDetail>(eventBus) {

            @Override
            public void onSuccess(final UserDetail loggedUser) {
                userRpcService.getBusinessUserById(loggedUser.getUserId(),
                        new SecuredAsyncCallback<BusinessUserDetail>(eventBus) {

                        @Override
                        public void onSuccess(BusinessUserDetail businessUserDetail) {
                            eventBus.prepareNewDemandForNewClient(businessUserDetail);
                        }
                    });
            }
        });
    }

    /**
     * Method registers new client and afterwards creates new demand.
     *
     * @param client newly created client
     */
    public void onRegisterNewClient(BusinessUserDetail client) {
        demandCreationService.createNewClient(client, new SecuredAsyncCallback<BusinessUserDetail>(eventBus) {
            @Override
            public void onSuccess(BusinessUserDetail client) {
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
                new SecuredAsyncCallback<FullDemandDetail>(eventBus) {

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
        userRpcService.checkFreeEmail(email, new SecuredAsyncCallback<Boolean>(eventBus) {
            @Override
            public void onSuccess(Boolean result) {
                LOGGER.fine("result of compare " + result);
                eventBus.checkFreeEmailResponse(result);
                // eventBus.checkFreeEmailResponse();
            }
        });
    }
}