package cz.poptavka.sample.client.user.demands.tab;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.user.UserEventBus;

@Presenter(view = MyDemandsOperatorView.class)
public class MyDemandsOperatorPresenter extends
    LazyPresenter<MyDemandsOperatorPresenter.MyDemandsOperatorViewInterface, UserEventBus> {

    public interface MyDemandsOperatorViewInterface extends LazyView {
        Widget getWidgetView();
    }

    public void onInvokeMyDemandsOperator() {
        eventBus.displayContent(view.getWidgetView());
    }
}
