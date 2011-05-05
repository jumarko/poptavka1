package cz.poptavka.sample.client.user.demands.tab;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.user.UserEventBus;

@Presenter(view = OffersView.class)
public class OffersPresenter extends LazyPresenter<OffersPresenter.OffersInterface, UserEventBus> {

    public interface OffersInterface extends LazyView {
        Widget getWidgetView();
    }

    public void onInvokeOffers() {
        eventBus.displayContent(view.getWidgetView());
    }
}
