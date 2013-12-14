/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.homeWelcome;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.service.demand.HomeWelcomeRPCServiceAsync;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import java.util.ArrayList;

/**
 * Manages RPC services calls for HomeWelcome module.
 *
 * @author Martin Slavkovsky
 */
@EventHandler
public class HomeWelcomeHandler extends BaseEventHandler<HomeWelcomeEventBus> {

    /**************************************************************************/
    /* Inject RPC services                                                    */
    /**************************************************************************/
    private HomeWelcomeRPCServiceAsync welcomeService = null;

    @Inject
    public void setHomeWelcomeService(HomeWelcomeRPCServiceAsync service) {
        welcomeService = service;
    }

    /**************************************************************************/
    /* Categories                                                             */
    /**************************************************************************/
    /**
     * Get all root categories.
     *
     * @return list of root categories
     */
    public void onGetRootCategories() {
        welcomeService.getRootCategories(new SecuredAsyncCallback<ArrayList<ICatLocDetail>>(eventBus) {
            @Override
            public void onSuccess(ArrayList<ICatLocDetail> result) {
                eventBus.displayCategories(result);
            }
        });
    }
}