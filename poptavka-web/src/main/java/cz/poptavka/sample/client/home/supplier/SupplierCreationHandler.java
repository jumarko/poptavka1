package cz.poptavka.sample.client.home.supplier;

import java.util.logging.Logger;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import cz.poptavka.sample.client.main.errorDialog.ErrorDialogPopupView;
import cz.poptavka.sample.client.service.demand.SupplierCreationRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.UserRPCServiceAsync;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.exceptions.CommonException;

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

    public void onRegisterSupplier(UserDetail newSupplier) {
        supplierCreationService.createNewSupplier(newSupplier, new AsyncCallback<UserDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                eventBus.loadingHide();
                if (caught instanceof CommonException) {
                    CommonException commonException = (CommonException) caught;
                    errorDialog = new ErrorDialogPopupView();
                    errorDialog.show(commonException.getSymbol());
                }
            }

            @Override
            public void onSuccess(UserDetail supplier) {
                // TODO forward to user/atAccount
                eventBus.loadingHide();
            }
        });
    }

    public void onCheckFreeEmail(String email) {
        userRpcService.checkFreeEmail(email, new AsyncCallback<Boolean>() {

            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof CommonException) {
                    CommonException commonException = (CommonException) caught;
                    errorDialog = new ErrorDialogPopupView();
                    errorDialog.show(commonException.getSymbol());
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
