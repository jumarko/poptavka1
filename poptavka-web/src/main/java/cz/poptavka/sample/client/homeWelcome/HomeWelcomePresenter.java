package cz.poptavka.sample.client.homeWelcome;

import com.google.gwt.core.client.GWT;
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

    public void onStart() {
        GWT.log("HomeWelcome module loaded");
        eventBus.setHomeBodyHolderWidget(view.getWidgetView());
    }

    public void onForward() {
        // Here we can handle actions like change CSS in menus etc.
        // TODO praso - check MVP4G blog to lear best practicies for forward event
    }

    public void onInitHomeWelcomeModule(SearchModuleDataHolder searchDataHolder) {
        Storage.setCurrentlyLoadedModule("welcome");
        this.searchDataHolder = searchDataHolder;
    }
}
