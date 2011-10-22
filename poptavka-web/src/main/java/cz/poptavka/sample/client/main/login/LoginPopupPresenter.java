package cz.poptavka.sample.client.main.login;

import java.util.Date;
import java.util.logging.Logger;

import com.google.gwt.user.client.Cookies;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.main.MainEventBus;
import cz.poptavka.sample.client.service.demand.UserRPCServiceAsync;

@Presenter(view = LoginPopupView.class, multiple = true)
public class LoginPopupPresenter extends LazyPresenter<LoginPopupPresenter.LoginPopupInterface, MainEventBus> {

    private static final Logger LOGGER = Logger.getLogger(LoginPopupPresenter.class
        .getName());

    private static final int COOKIE_TIMEOUT = 1000 * 60 * 60 * 24;

    public interface LoginPopupInterface extends LazyView {

        boolean isValid();

        String getLogin();

        String getPassword();

        void hidePopup();

        void setLoadingStatus();

        void setUnknownError();

        void setLoginError();

        LoginPopupPresenter getPresenter();

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
            //DEVEL ONLY FOR FAST LOGIN
//            userService.loginUser(new UserDetail(username, password), new AsyncCallback<String>() {
//                @Override
//                public void onFailure(Throwable arg0) {
//                    view.setUnknownError();
//                }
//
//                @Override
//                public void onSuccess(String sessionId) {
//                    if (sessionId != null) {
//                        setSessionID(sessionId);
//                        eventBus.atAccount();
//                        hideView();
//                    } else {
//                        view.setLoginError();
//                    }
//                }
//            });
            //DEVEL ONLY FOR FAST SUPPLIER LOGIN
            setSessionID("id=149");
            eventBus.atAccount();
            hideView();
        }
    }

    public void hideView() {
        eventBus.removeHandler(view.getPresenter());
        view.hidePopup();
    }

    /** Sets session ID **/
    private void setSessionID(String sessionId) {
        LOGGER.fine("Setting SID cookie");
        int cookieTimeout = COOKIE_TIMEOUT;
        Date expires = new Date((new Date()).getTime() + cookieTimeout);
        Cookies.setCookie("sid", sessionId);
    }

}
