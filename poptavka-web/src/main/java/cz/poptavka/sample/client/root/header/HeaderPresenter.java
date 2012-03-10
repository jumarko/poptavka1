package cz.poptavka.sample.client.root.header;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.main.login.LoginPopupPresenter;
import cz.poptavka.sample.client.root.RootEventBus;
import cz.poptavka.sample.client.root.interfaces.IHeaderView;
import cz.poptavka.sample.client.root.interfaces.IHeaderView.IHeaderPresenter;

@Presenter(view = HeaderView.class)
public class HeaderPresenter extends BasePresenter<IHeaderView, RootEventBus>
        implements IHeaderPresenter {

    // TODO praso - Nemali by sme mat LocalizablaMessages ako static instanciu v Storage?
    // Preco ju vytvarame znovu na tomto mieste?
    private static final LocalizableMessages MSGS = GWT
            .create(LocalizableMessages.class);

    private boolean loggedIn = false;

    private LoginPopupPresenter login;

    public void onStart() {
        GWT.log("Header module loaded");
        eventBus.setHeader(view);
    }

    @Override
    public void bind() {
        view.getLoginLink().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                GWT.log("Logged in? " + loggedIn);
                if (loggedIn) {
                    eventBus.atHome();
                    Cookies.setCookie("user-presenter", "unloaded");
                } else {
                    onInitLoginWindow();
                }
            }
        });
        Window.addWindowClosingHandler(new Window.ClosingHandler() {
            @Override
            public void onWindowClosing(ClosingEvent event) {
                Cookies.setCookie("user-presenter", "unloaded");
            }
        });
    }

    public void onInitLoginWindow() {
        login = eventBus.addHandler(LoginPopupPresenter.class);
        login.onLogin();
    }

    public void onAtHome() {
        this.loggedIn = false;
        view.getLoginLink().setText(MSGS.logIn());
        eventBus.displayMenu();
        eventBus.initHomeWelcomeModule(null);
        eventBus.setHomeMenu();
    }

    public void onAtAccount() {
        this.loggedIn = true;
        view.getLoginLink().setText(MSGS.logOut());
    }

    public void onSetUserLayout() {
        view.toggleMainLayout(true);
        view.getLoginLink().setText(MSGS.logOut());
        this.loggedIn = true;
    }

    public void onSetPublicLayout() {
        //eventBus.clearUserOnUnload();
        view.toggleMainLayout(false);
        view.getLoginLink().setText(MSGS.logIn());
        this.loggedIn = false;

    }
}
