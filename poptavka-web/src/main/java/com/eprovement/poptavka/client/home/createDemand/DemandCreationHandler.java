package com.eprovement.poptavka.client.home.createDemand;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.service.demand.DemandCreationRPCServiceAsync;
import com.eprovement.poptavka.client.service.demand.UserRPCServiceAsync;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import java.util.logging.Logger;

/**
 * Handler for RPC calls for DemandCreationModule.
 *
 * @author Beho, Martin Slavkovsky
 *
 */
@EventHandler
public class DemandCreationHandler extends BaseEventHandler<DemandCreationEventBus> {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private static final Logger LOGGER = Logger.getLogger("MainHandler");
    private DemandCreationRPCServiceAsync demandCreationService = null;
    private UserRPCServiceAsync userRpcService;

    /**************************************************************************/
    /* Inject RPCs                                                            */
    /**************************************************************************/
    @Inject
    void setDemandCreationModuleRPCServiceAsync(DemandCreationRPCServiceAsync service) {
        demandCreationService = service;
    }

    @Inject
    void setUserRpcService(UserRPCServiceAsync userRpcService) {
        this.userRpcService = userRpcService;
    }

    /**************************************************************************/
    /* Methods                                                                */
    /**************************************************************************/
    /**
     * Method registers new client and afterwards creates new demand.
     *
     * @param client newly created client
     */
    public void onRegisterNewClient(BusinessUserDetail client) {
//        demandCreationService.createNewClient(client, new SecuredAsyncCallback<BusinessUserDetail>(eventBus) {
//            @Override
//            public void onSuccess(BusinessUserDetail client) {
//                if (client.getClientId() != -1) {
//                    //popytaj ten overovaci kod
//                    eventBus.responseRegisterNewClient(client);
//                }
//            }
//        });
        //TODO remove - fake - just for devel
        BusinessUserDetail c = new BusinessUserDetail();
        c.setEmail("martin@user.cz");
        c.setPassword("kreslo");
        eventBus.loadingHide();
        eventBus.responseRegisterNewClient(c);
    }

    public void onActivateClient(String activationCode) {
        if (activationCode.equals("123")) {
            eventBus.responseActivateClient(true);
        } else {
            eventBus.responseActivateClient(false);
        }
//        demandCreationService.activateClient(activationCode, new SecuredAsyncCallback<Boolean>(eventBus) {
//            @Override
//            public void onSuccess(Boolean result) {
//                eventBus.responseActivateClient(result);
//            }
//        });
    }
    private boolean bool = true;

    public void onSentActivationCodeAgain(BusinessUserDetail client) {
        //TODO remove - fake - for devel
        bool = !bool;
        eventBus.responseSendActivationCodeAgain(bool);
//        demandCreationService.sentActivationCodeAgain(client, new SecuredAsyncCallback<Boolean>(eventBus) {
//            @Override
//            public void onSuccess(Boolean result) {
//                eventBus.responseSendActivationCodeAgain(result);
//            }
//        });
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
//                        eventBus.responseCreateDemand();
//                        eventBus.loadingHide();
                        eventBus.loadingShow(Storage.MSGS.demandCreatedAndForwarding());
                        eventBus.goToClientDemandsModule(null, Constants.CLIENT_DEMANDS);

                        // TODO forward to user/atAccount
//                        eventBus.addNewDemand(result);
                    }
                });
        LOGGER.info("submitting new demand");
    }

    public void onCheckFreeEmail(String email) {
        demandCreationService.checkFreeEmail(email, new SecuredAsyncCallback<Boolean>(eventBus) {
            @Override
            public void onSuccess(Boolean result) {
                LOGGER.fine("result of compare " + result);
                eventBus.checkFreeEmailResponse(result);
                // eventBus.checkFreeEmailResponse();
            }
        });
    }
}