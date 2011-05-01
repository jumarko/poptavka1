package cz.poptavka.sample.client.user.demands;

import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.user.UserEventBus;

@Presenter(view = DemandsView.class)
public class DemandsPresenter
    extends BasePresenter<DemandsPresenter.DemandsViewInterface, UserEventBus> {

    private static final Logger LOGGER = Logger.getLogger("DemandsPressenter");

    public interface DemandsViewInterface {

        Widget getWidgetView();
    }

    public void onAtAccount() {
        //init
        eventBus.setTabWidget(view.getWidgetView());
    }
}
