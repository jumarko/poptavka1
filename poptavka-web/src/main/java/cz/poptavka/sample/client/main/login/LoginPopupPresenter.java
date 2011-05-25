package cz.poptavka.sample.client.main.login;

import java.util.Date;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.main.MainEventBus;
import cz.poptavka.sample.client.service.demand.UserRPCServiceAsync;
import cz.poptavka.sample.shared.domain.UserDetail;

@Presenter(view = LoginPopupView.class, multiple = true)
public class LoginPopupPresenter extends LazyPresenter<LoginPopupPresenter.LoginPopupInterface, MainEventBus> {

    private static final Logger LOGGER = Logger.getLogger(LoginPopupPresenter.class
        .getName());

    public interface LoginPopupInterface extends LazyView {

        Button getLoginButton();

        void show();

        boolean isValid();

        String getLogin();

        String getPassword();

        void hide();

        void setLoadingStatus();

        void setUnknownError();

        FocusWidget getCancelButton();

        void setLoginError();

        TextBox getPassBox();

        TextBox getEmailBox();

        boolean getRememberMe();

    }

    @Inject
    private UserRPCServiceAsync userService;

    public void bindView() {
        view.getLoginButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                startLoginIn();
            }
        });
        KeyDownHandler enterKeyHandler = getHandler();
        view.getLoginButton().addKeyDownHandler(enterKeyHandler);
        view.getEmailBox().addKeyDownHandler(enterKeyHandler);
        view.getPassBox().addKeyDownHandler(enterKeyHandler);
        view.getCancelButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                view.hide();
            }
        });
    }

    public void onLogin() {
        view.show();
    }

    private void registerUser(String login, String password) {
        view.setLoadingStatus();
        //if there was previous rememberMe or new remember settings
        if (!view.getRememberMe()) {
            Cookies.removeCookie("pop-user");
            Cookies.removeCookie("pop-password");
        } else {
            setLoginCookies();
        }
        userService.loginUser(new UserDetail(login, password), new AsyncCallback<UserDetail>() {

            @Override
            public void onFailure(Throwable arg0) {
                view.setUnknownError();
            }

            @Override
            public void onSuccess(UserDetail user) {
                if (user != null) {
                    view.hide();
                    eventBus.atAccount(user);
                } else {
                    view.setLoginError();
                }
            }
        });
    }

    private void startLoginIn() {
        boolean isValid = view.isValid();
        if (isValid) {
            registerUser(view.getLogin(), view.getPassword());
        }
    }

    private KeyDownHandler getHandler() {
        KeyDownHandler handler = new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    if (view.getRememberMe()) {
                        setLoginCookies();
                    }
                    startLoginIn();
                }
            }
        };
        return handler;
    }

    private void setLoginCookies() {
        LOGGER.fine("Setting cookie");
        int cookieTimeout = 1000 * 60 * 60 * 24;
        Date expires = new Date((new Date()).getTime() + cookieTimeout);
        Cookies.setCookie("pop-user", view.getLogin(), expires);
        Cookies.setCookie("pop-password", view.getPassword(), expires);
    }

}
