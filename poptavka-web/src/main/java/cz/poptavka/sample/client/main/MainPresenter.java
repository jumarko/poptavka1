package cz.poptavka.sample.client.main;


import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.main.common.LoadingPopup;
import cz.poptavka.sample.client.resources.StyleResource;

@Presenter(view = MainView.class)
public class MainPresenter extends BasePresenter<MainPresenter.MainViewInterface, MainEventBus> {

    private static final Logger LOGGER = Logger.getLogger("MainPresenter");

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private PopupPanel popup = null;

    public interface MainViewInterface {
        void setBodyWidget(Widget body);

        void toggleMainLayout(boolean switchToUserLayout);

        // TODO eventually change to hyperlink
        Anchor getLoginButton();

        Boolean getCompactDisplay();

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
        LOGGER.info("    > Home Module");
        eventBus.atHome();
        /** for user part development **/
//        LOGGER.info("    > User Module");
//        eventBus.atAccount();
        eventBus.compactModeResponse(view.getCompactDisplay());
    }

    /**
     * Sets widget to View's body section. Body section can hold one widget only.
     *
     * @param body widget to be inserted
     */
    public void onSetBodyHolderWidget(Widget body) {
        view.setBodyWidget(body);
    }

    public void onBeforeLoad() {
        eventBus.loadingShow("Loading");
    }

    public void onAfterLoad() {
        eventBus.loadingHide();
    }

    public void onSetPublicLayout() {
        LOGGER.fine("Toggle layout");
        view.toggleMainLayout(false);
    }

    public void onSetUserLayout() {
        LOGGER.fine("Toggle layout");
        view.toggleMainLayout(true);
    }

    public void onLoadingShow(String loadingMessage) {
        if (!(popup == null)) {
            LoadingPopup popupContent = (LoadingPopup) popup.getWidget();
            popupContent.setMessage(loadingMessage);
        } else {
            createLoadingPopup(loadingMessage);
        }
    }

    public void onLoadingHide() {
        popup.hide();
        popup = null;
    }

    private static final int OFFSET_X = 60;
    private static final int OFFSET_Y = 35;

    private void createLoadingPopup(String loadingMessage) {
        popup = new PopupPanel(false, false);
        popup.setStylePrimaryName(StyleResource.INSTANCE.common().loadingPopup());
        popup.setWidget(new LoadingPopup(loadingMessage));
        popup.setPopupPosition((Window.getClientWidth() / 2) - OFFSET_X, (Window.getClientHeight() / 2) - OFFSET_Y);
        popup.show();
    }

    public void onCompactModeCheck() {
        eventBus.compactModeResponse(view.getCompactDisplay());
    }
}
