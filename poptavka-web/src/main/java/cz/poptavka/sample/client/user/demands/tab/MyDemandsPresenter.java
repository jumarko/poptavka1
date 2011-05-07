package cz.poptavka.sample.client.user.demands.tab;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.user.UserEventBus;

@Presenter(view = MyDemandsView.class)
public class MyDemandsPresenter extends LazyPresenter<MyDemandsPresenter.MyDemandsInterface, UserEventBus> {

    public interface MyDemandsInterface extends LazyView {
        Widget getWidgetView();
    }

    public void onInvokeMyDemands() {
        eventBus.displayContent(view.getWidgetView());
    }
}
