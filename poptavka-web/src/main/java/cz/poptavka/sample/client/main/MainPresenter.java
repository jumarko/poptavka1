package cz.poptavka.sample.client.main;


import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.home.HomePresenter.AnchorEnum;

@Presenter(view = MainView.class)
public class MainPresenter extends BasePresenter<MainPresenter.MainViewInterface, MainEventBus> {

    private static final Logger LOGGER = Logger.getLogger("MainPresenter");

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    public interface MainViewInterface {
        void setBodyWidget(Widget body);

        void toggleMainLayout(boolean switchToUserLayout);

        // TODO eventually change to hyperlink
        Anchor getLoginButton();
    }

    private boolean loggedIn = false;

    @Override
    public void bind() {
        view.getLoginButton().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent arg0) {
                if (loggedIn) {
                    eventBus.atHome();
                    view.getLoginButton().setText(MSGS.logIn());
                } else {
                    eventBus.atAccount();
                    view.getLoginButton().setText(MSGS.logOut());
                }
                loggedIn = !loggedIn;
            }
        });
    }

    /**
     * Initial Event. Calls all default modules to load: HomeModule
     *
     * For development purposes User Module can be loaded instantly
     */
    public void onStart() {
        LOGGER.info("Initializing application ... ");
        /** for public part development **/
//        LOGGER.info("    > Home Module");
//        eventBus.atHome();
        /** for user part development **/
        LOGGER.info("    > User Module");
        eventBus.atAccount();
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
            eventBus.setTabWidget(content);
        }
    }

    public void onBeforeLoad() {
        Document.get().getElementById("gwt-modules-loading").getStyle().setDisplay(Display.BLOCK);
        Document.get().getElementById("loading").getStyle().setDisplay(Display.BLOCK);
    }

    public void onAfterLoad() {
        Document.get().getElementById("gwt-modules-loading").getStyle().setDisplay(Display.NONE);
        Document.get().getElementById("loading").getStyle().setDisplay(Display.NONE);
    }

    public void onSetPublicLayout() {
        LOGGER.fine("Toggle layout");
        view.toggleMainLayout(false);
    }

    public void onSetUserLayout() {
        LOGGER.fine("Toggle layout");
        view.toggleMainLayout(true);
    }
}
