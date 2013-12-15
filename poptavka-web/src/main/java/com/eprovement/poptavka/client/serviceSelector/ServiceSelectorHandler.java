/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.serviceSelector;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.service.demand.ServiceSelectorRPCServiceAsync;
import com.eprovement.poptavka.domain.enums.ServiceType;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import java.util.ArrayList;

/**
 * Handle RPS calls for ServiceSelector module.
 *
 * @author Martin Slavkovsky
 */
@EventHandler
public class ServiceSelectorHandler extends BaseEventHandler<ServiceSelectorEventBus> {

    /**************************************************************************/
    /* Inject RPS services                                                    */
    /**************************************************************************/
    @Inject
    private ServiceSelectorRPCServiceAsync serviceSelectorService = null;

    /**************************************************************************/
    /* Get Suppliers data                                                     */
    /**************************************************************************/
    public void onRequestServices(ServiceType serviceType) {
        serviceSelectorService.getSupplierServices(serviceType,
            new SecuredAsyncCallback<ArrayList<ServiceDetail>>(eventBus) {
                @Override
                public void onSuccess(ArrayList<ServiceDetail> data) {
                    eventBus.displayServices(data);
                }
            });
    }
}