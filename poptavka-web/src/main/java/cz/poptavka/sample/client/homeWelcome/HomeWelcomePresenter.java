package cz.poptavka.sample.client.homeWelcome;

import com.google.gwt.core.client.GWT;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.homeWelcome.interfaces.IHomeWelcomeView;
import cz.poptavka.sample.client.homeWelcome.interfaces.IHomeWelcomeView.IHomeWelcomePresenter;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;

@Presenter(view = HomeWelcomeView.class)
public class HomeWelcomePresenter extends
        BasePresenter<IHomeWelcomeView, HomeWelcomeEventBus> implements
        IHomeWelcomePresenter {

    public void onStart() {
        GWT.log("Home welcome module loaded");
        eventBus.setBodyHolderWidget(view.getWidgetView());
    }

    public void onInitHomeWelcomeModule(SearchModuleDataHolder searchDataHolder) {

    }

}
