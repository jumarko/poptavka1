package com.eprovement.poptavka.client.home.createSupplier;

import com.eprovement.poptavka.client.common.session.Constants;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.service.demand.SupplierCreationRPCServiceAsync;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;

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

    public void onRegisterSupplier(final FullSupplierDetail newSupplier) {
        supplierCreationService.createNewSupplier(newSupplier, new SecuredAsyncCallback<FullSupplierDetail>(eventBus) {
            @Override
            public void onSuccess(FullSupplierDetail supplier) {
                eventBus.loadingHide();
                eventBus.autoLogin(
                        newSupplier.getUserData().getEmail(),
                        newSupplier.getUserData().getPassword(),
                        Constants.SUPPLIER_DEMANDS_WELCOME);
            }
        });
    }
}
