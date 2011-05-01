package cz.poptavka.sample.client.user;

import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = UserView.class)
public class UserPresenter extends BasePresenter<UserPresenter.UserViewInterface, UserEventBus> {

    private static final Logger LOGGER = Logger.getLogger("UserPresenter");

    public interface UserViewInterface {
        void setBody(Widget body);

        Widget getWidgetView();
    }

    public void onAtAccount() {
        //init
        LOGGER.info("init user widget ...");
        eventBus.setUserLayout();
        eventBus.setBodyHolderWidget(view.getWidgetView());
    }

    public void onSetTabWidget(Widget tabBody) {
        LOGGER.info("widget body here !!");
        view.setBody(tabBody);
    }
}
