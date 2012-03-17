package cz.poptavka.sample.client.homeWelcome;

import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.homeWelcome.interfaces.IHomeWelcomeView;
import cz.poptavka.sample.client.homeWelcome.interfaces.IHomeWelcomeView.IHomeWelcomePresenter;
import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;

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
        // nothing
    }

    /**************************************************************************/
    /* Navigation events                                                      */
    /**************************************************************************/
    public void onGoToHomeWelcomeModule(SearchModuleDataHolder searchDataHolder) {
        // TODO martin - dopln si loadedView v ostatnych presenteroch to mavas vzdy vyplnene
        Storage.setCurrentlyLoadedModule("welcome");
        this.searchDataHolder = searchDataHolder;
    }

    /**************************************************************************/
    /* Business events handled by presenter                                   */
    /**************************************************************************/
    /**************************************************************************/
    /* Business events handled by eventbus or RPC                             */
    /**************************************************************************/
}
