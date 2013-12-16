package com.eprovement.poptavka.client.homeWelcome;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.service.demand.HomeWelcomeRPCServiceAsync;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import java.util.ArrayList;

@EventHandler
public class HomeWelcomeHandler extends BaseEventHandler<HomeWelcomeEventBus> {

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
