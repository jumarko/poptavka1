package cz.poptavka.sample.client.main.login;

import java.util.Date;
import java.util.logging.Logger;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
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

        boolean isValid();

        String getLogin();

        String getPassword();

        void hidePopup();

        void setLoadingStatus();

        void setUnknownError();

        void setLoginError();

    }

    @Inject
    private UserRPCServiceAsync userService;

    public void onLogin() {
        LOGGER.info("++ Login Popup Widget initialized ++");
    }

    /** Real html login. SHOULD/WILL be used in prod */
    public void doLogin() {
        if (view.isValid()) {
            view.setLoadingStatus();
            String username = view.getLogin();
            String password = view.getPassword();
            userService.loginUser(new UserDetail(username, password), new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable arg0) {
                    view.setUnknownError();
                }

                @Override
                public void onSuccess(String sessionId) {
                    if (sessionId != null) {
                        setSessionID(sessionId);
                        view.hidePopup();
                        eventBus.atAccount();
                    } else {
                        view.setLoginError();
                    }
                }
            });
        }
    }

    /** Sets session ID **/
    private void setSessionID(String sessionId) {
        LOGGER.fine("Setting SID cookie");
        int cookieTimeout = 1000 * 60 * 60 * 24;
        Date expires = new Date((new Date()).getTime() + cookieTimeout);
        Cookies.setCookie("sid", sessionId);
    }

}
