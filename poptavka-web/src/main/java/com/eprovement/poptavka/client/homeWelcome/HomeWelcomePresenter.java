package com.eprovement.poptavka.client.homeWelcome;

import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import com.eprovement.poptavka.client.homeWelcome.interfaces.IHomeWelcomeView;
import com.eprovement.poptavka.client.homeWelcome.interfaces.IHomeWelcomeView.IHomeWelcomePresenter;
import com.eprovement.poptavka.client.main.common.search.SearchModuleDataHolder;

@Presenter(view = HomeWelcomeView.class)
public class HomeWelcomePresenter extends BasePresenter<IHomeWelcomeView, HomeWelcomeEventBus> implements
        IHomeWelcomePresenter {

    private SearchModuleDataHolder searchDataHolder = null;

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        // nothing
    }

    public void onForward() {
        eventBus.setUpSearchBar(null, false, false, false);
    }

    /**************************************************************************/
    /* Navigation events                                                      */
    /**************************************************************************/
    public void onGoToHomeWelcomeModule(SearchModuleDataHolder searchDataHolder) {
        this.searchDataHolder = searchDataHolder;
    }

    /**************************************************************************/
    /* Business events handled by presenter                                   */
    /**************************************************************************/
    /**************************************************************************/
    /* Business events handled by eventbus or RPC                             */
    /**************************************************************************/
}
