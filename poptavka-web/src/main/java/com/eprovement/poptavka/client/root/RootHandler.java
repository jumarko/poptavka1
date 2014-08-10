/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.root;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.root.interfaces.IRootModule;
import com.eprovement.poptavka.client.service.root.RootRPCServiceAsync;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

/**
 * @author Martin Slavkovsky
 * @since 9.4.2014
 */
@EventHandler
public class RootHandler extends BaseEventHandler<RootEventBus> implements IRootModule.Handler {

    @Inject
    private RootRPCServiceAsync rootService;

    @Override
    public void onUnsubscribeUser(String password) {
        rootService.unsubscribe(password, new SecuredAsyncCallback<Boolean>(eventBus) {

            @Override
            public void onSuccess(Boolean result) {
                eventBus.responseUnsubscribe(result);
            }
        });
    }

    @Override
    public void onRequestCreditCount(long userId) {
        rootService.getCreditCount(userId, new SecuredAsyncCallback<Integer>(eventBus) {

            @Override
            public void onSuccess(Integer count) {
                eventBus.responseCreditCount(count);
            }
        });
    }
}