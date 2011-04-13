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

    public void onInitUser() {
        //init
        LOGGER.info("init user widget ...");
        eventBus.setBodyHolderWidget(view.getWidgetView());

        eventBus.setListOfDemandsWidget();
    }

    public void onSetTabWidget(Widget tabBody) {
        view.setBody(tabBody);
    }
}
