/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.home.createSupplier;

import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.home.createSupplier.interfaces.ISupplierCreationModule;
import com.eprovement.poptavka.client.service.demand.SupplierCreationRPCServiceAsync;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;

/**
 * Defines RPC calls for SupplierCreation module.
 *
 * @author Beho
 *
 */
@EventHandler
public class SupplierCreationHandler extends BaseEventHandler<SupplierCreationEventBus>
    implements ISupplierCreationModule.Handler {

    /**************************************************************************/
    /* Inject services                                                        */
    /**************************************************************************/
    private SupplierCreationRPCServiceAsync supplierCreationService;

    @Inject
    void setSupplierCreationRPCService(SupplierCreationRPCServiceAsync service) {
        supplierCreationService = service;
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * {@inheritDoc}
     */
    @Override
    public void onRequestRegisterSupplier(final FullSupplierDetail newSupplier) {
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
