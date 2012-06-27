package com.eprovement.poptavka.client.home.createDemand;

import java.util.logging.Logger;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import com.eprovement.poptavka.client.main.errorDialog.ErrorDialogPopupView;
import com.eprovement.poptavka.client.service.demand.DemandCreationRPCServiceAsync;
import com.eprovement.poptavka.client.service.demand.UserRPCServiceAsync;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.exceptions.ExceptionUtils;
import com.eprovement.poptavka.shared.exceptions.RPCException;

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

    private ErrorDialogPopupView errorDialog;

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
    public void onVerifyExistingClient(final BusinessUserDetail client) {
        LOGGER.fine("verify start");
        userRpcService.loginUser(client, new AsyncCallback<UserDetail>() {
            @Override
            public void onFailure(Throwable loginException) {
                LOGGER.info("login error:" + loginException.getMessage());
                eventBus.loadingHide();
                eventBus.loginError();
            }

            @Override
            public void onSuccess(final UserDetail loggedUser) {
                userRpcService.getUserById(loggedUser.getUserId(), new AsyncCallback<BusinessUserDetail>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        // TODO
                        if (caught instanceof RPCException) {
                            ExceptionUtils.showErrorDialog(errorDialog, caught);
                        }
                        throw new IllegalStateException("Cannot get business user for user id="
                                + loggedUser.getUserId());
                    }

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
        demandCreationService.createNewClient(client, new AsyncCallback<BusinessUserDetail>() {

            @Override
            public void onFailure(Throwable arg0) {
                if (arg0 instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, arg0);
                }
            }

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
                new AsyncCallback<FullDemandDetail>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        eventBus.loadingHide();
                        if (caught instanceof RPCException) {
                            ExceptionUtils.showErrorDialog(errorDialog, caught);
                        }
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
        userRpcService.checkFreeEmail(email, new AsyncCallback<Boolean>() {

            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
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