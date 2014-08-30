/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.homeWelcome;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.service.demand.HomeWelcomeRPCServiceAsync;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ILesserCatLocDetail;
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
        welcomeService.getRootCategories(new SecuredAsyncCallback<ArrayList<ILesserCatLocDetail>>(eventBus) {
            @Override
            public void onSuccess(ArrayList<ILesserCatLocDetail> result) {
                eventBus.displayCategories(result);
            }
        });
    }

    /**
     * Get full ICatLocDetail object for given root category and forward to HomeDemands.
     *
     * @param categoryId
     * @param index of selected displayed category
     * @return corresponding iCatLocDetail
     */
    public void onGetICatLocDetail(long categoryId, final int index) {
        welcomeService.getICatLocDetail(categoryId, new SecuredAsyncCallback<ICatLocDetail>(eventBus) {
            @Override
            public void onSuccess(ICatLocDetail result) {
                eventBus.goToHomeDemandsModuleFromWelcome(index, result);
            }
        });
    }
}