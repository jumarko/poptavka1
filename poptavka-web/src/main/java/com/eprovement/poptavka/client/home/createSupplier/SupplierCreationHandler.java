package com.eprovement.poptavka.client.home.createSupplier;

import java.util.logging.Logger;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import com.eprovement.poptavka.client.main.errorDialog.ErrorDialogPopupView;
import com.eprovement.poptavka.client.service.demand.SupplierCreationRPCServiceAsync;
import com.eprovement.poptavka.client.service.demand.UserRPCServiceAsync;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.exceptions.ExceptionUtils;
import com.eprovement.poptavka.shared.exceptions.RPCException;

/**
 * Handler for RPC calls for localities & categories.
 *
 * @author Beho
 *
 */
@EventHandler
public class SupplierCreationHandler extends BaseEventHandler<SupplierCreationEventBus> {

    private SupplierCreationRPCServiceAsync supplierCreationService;
    private UserRPCServiceAsync userRpcService;
    private static final Logger LOGGER = Logger.getLogger("MainHandler");

    private ErrorDialogPopupView errorDialog;

    @Inject
    void setSupplierCreationRPCService(SupplierCreationRPCServiceAsync service) {
        supplierCreationService = service;
    }

    @Inject
    void setUserRpcService(UserRPCServiceAsync userRpcService) {
        this.userRpcService = userRpcService;
    }

    public void onRegisterSupplier(BusinessUserDetail newSupplier) {
        supplierCreationService.createNewSupplier(newSupplier, new AsyncCallback<BusinessUserDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                eventBus.loadingHide();
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
            }

            @Override
            public void onSuccess(BusinessUserDetail supplier) {
                // TODO forward to user/atAccount
                eventBus.loadingHide();
            }
        });
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
            }
        });
    }
}
