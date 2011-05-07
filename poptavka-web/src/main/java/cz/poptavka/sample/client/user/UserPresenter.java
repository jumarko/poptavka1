package cz.poptavka.sample.client.user;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.shared.domain.DemandDetail;

/**
 * Main presenter for User account. COntains list of all demands for faster working with demands.
 *
 * @author Beho
 *
 */
@Presenter(view = UserView.class)
public class UserPresenter extends LazyPresenter<UserPresenter.UserViewInterface, UserEventBus> {

    private static final Logger LOGGER = Logger.getLogger("UserPresenter");

    private ArrayList<DemandDetail> myDemands = null;
//    private Settings userSettings = null;

    public interface UserViewInterface extends LazyView {
        void setBody(Widget body);

        Widget getWidgetView();
    }

    public void onAtAccount() {
        eventBus.setUserLayout();
        eventBus.setBodyHolderWidget(view.getWidgetView());
        eventBus.invokeMyDemands();
    }

    public void onSetTabWidget(Widget tabBody) {
        LOGGER.info("widget body here !!");
        view.setBody(tabBody);
    }
}
