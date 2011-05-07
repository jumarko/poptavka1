package cz.poptavka.sample.client.user.demands.tab;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.user.UserEventBus;

@Presenter(view = NewDemandView.class)
public class NewDemandPresenter extends LazyPresenter<NewDemandPresenter.NewDemandInterface, UserEventBus> {

    public interface NewDemandInterface extends LazyView {
        Widget getWidgetView();
    }

    public void onInvokeNewDemand() {
        eventBus.displayContent(view.getWidgetView());
    }
}
