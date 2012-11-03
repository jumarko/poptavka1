package com.eprovement.poptavka.client.home.createSupplier;

import java.util.logging.Logger;

import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.service.demand.SupplierCreationRPCServiceAsync;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;

/**
 * Handler for RPC calls for localities & categories.
 *
 * @author Beho
 *
 */
@EventHandler
public class SupplierCreationHandler extends BaseEventHandler<SupplierCreationEventBus> {

    private SupplierCreationRPCServiceAsync supplierCreationService;
    private static final Logger LOGGER = Logger.getLogger("MainHandler");

    @Inject
    void setSupplierCreationRPCService(SupplierCreationRPCServiceAsync service) {
        supplierCreationService = service;
    }

    public void onRegisterSupplier(BusinessUserDetail newSupplier) {
        supplierCreationService.createNewSupplier(newSupplier, new SecuredAsyncCallback<BusinessUserDetail>(eventBus) {

            @Override
            public void onSuccess(BusinessUserDetail supplier) {
                // TODO forward to user/atAccount
                eventBus.loadingHide();
            }
        });
    }

    public void onCheckFreeEmail(String email) {
        supplierCreationService.checkFreeEmail(email, new SecuredAsyncCallback<Boolean>(eventBus) {
            @Override
            public void onSuccess(Boolean result) {
                LOGGER.fine("result of compare " + result);
                eventBus.checkFreeEmailResponse(result);
            }
        });
    }
}
