package cz.poptavka.sample.client.main;


import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = MainView.class)
public class MainPresenter extends BasePresenter<MainPresenter.MainViewInterface, MainEventBus> {

    private static final Logger LOGGER = Logger.getLogger(MainPresenter.class.getName());

    public interface MainViewInterface {
        void setBodyWidget(Widget body);

        void setLoginWidget(Widget login);
    }

    /**
     * Initial Event. Calls all default modules to load: LoginModule, HomeModule
     */
    public void onStart() {
        LOGGER.info("Starting main presenter...");
        eventBus.initLogin();
        eventBus.initHome();
    }

    /**
     * Sets widget to View's body section. Body section can hold one widget only.
     *
     * @param body widget to be inserted
     */
    public void onSetBodyHolderWidget(Widget body) {
        view.setBodyWidget(body);
    }

    /**
     * Sets widget to login-area.
     *
     * @param login login widget
     */
    public void onSetLoginWidget(Widget login) {
        view.setLoginWidget(login);
    }
}
