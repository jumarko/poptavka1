/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.common.userRegistration;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.service.demand.UserRegistrationRPCServiceAsync;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

/**
 * Handles RPC calls for UserRegistration module.
 * @author Martin Slavkovsky
 */
@EventHandler
public class UserRegistrationHandler extends BaseEventHandler<UserRegistrationEventBus> {

    /**************************************************************************/
    /* Inject RPC services                                                    */
    /**************************************************************************/
    @Inject
    private UserRegistrationRPCServiceAsync userService;

    /**************************************************************************/
    /* Account Info                                                           */
    /**************************************************************************/
    /**
     * Checks if given email is free.
     * @param email to be checked
     */
    public void onCheckFreeEmail(String email) {
        userService.checkFreeEmail(email, new SecuredAsyncCallback<Boolean>(eventBus) {
            @Override
            public void onSuccess(Boolean result) {
                eventBus.checkFreeEmailResponse(result);
            }
        });
    }
}
