package cz.poptavka.sample.client.main;


import java.util.logging.Logger;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.home.HomePresenter.AnchorEnum;

@Presenter(view = MainView.class)
public class MainPresenter extends BasePresenter<MainPresenter.MainViewInterface, MainEventBus> {

    private static final Logger LOGGER = Logger.getLogger("MainPresenter");

    public interface MainViewInterface {
        void setBodyWidget(Widget body);

        void setLoginWidget(Widget login);
    }

    /**
     * Initial Event. Calls all default modules to load: LoginModule, HomeModule
     */
    public void onStart() {
        LOGGER.info("Initializing application ... ");
        LOGGER.info("    > Login Module");
        eventBus.initLogin();
        LOGGER.info("    > Home Module");
        eventBus.atHome();
//        LOGGER.info("    > User Module");
//        eventBus.initUser();
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
     * Crossroad method for placing widget from common package.
     *
     * @param homeSection boolean if target section si home section
     * @param anchor actual anchor where to place the content
     * @param content content to be placed
     * @param clearOthers boolean if widgets from others anchors should be removed
     */
    public void onSetAnchorWidget(boolean homeSection, AnchorEnum anchor, Widget content, boolean clearOthers) {
        if (homeSection) {
            eventBus.setHomeWidget(anchor, content, clearOthers);
        } else {
//            eventBus.setUserWidget(anchor, content, clearOthers);
        }
    }

    /**
     * Sets widget to login-area.
     *
     * @param login login widget
     */
    public void onSetLoginWidget(Widget login) {
        view.setLoginWidget(login);
    }

    public void onBeforeLoad() {
        Document.get().getElementById("gwt-modules-loading").getStyle().setDisplay(Display.BLOCK);
        Document.get().getElementById("loading").getStyle().setDisplay(Display.BLOCK);
    }

    public void onAfterLoad() {
        Document.get().getElementById("gwt-modules-loading").getStyle().setDisplay(Display.NONE);
        Document.get().getElementById("loading").getStyle().setDisplay(Display.NONE);
    }
}
