package cz.poptavka.sample.client.main.login;

import java.util.Date;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.root.RootEventBus;
import cz.poptavka.sample.client.service.demand.UserRPCServiceAsync;
import cz.poptavka.sample.shared.domain.UserDetail;

@Presenter(view = LoginPopupView.class, multiple = true)
public class LoginPopupPresenter extends LazyPresenter<LoginPopupPresenter.LoginPopupInterface, RootEventBus> {

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
            RequestBuilder builder = new RequestBuilder(RequestBuilder.POST,
                    URL.encode("/poptavka/j_spring_security_check"));
            builder.setHeader("Content-type", "application/x-www-form-urlencoded");
            try {
                Request request = builder.sendRequest("j_username=" + view.getLogin()
                        + "&j_password=" + view.getPassword(),
                        new RequestCallback() {
                            public void onError(Request request,
                                    Throwable exception) {
                                // Couldn't connect to server (could be timeout,
                                // SOP violation, etc.)
                                GWT.log("error during login");
                            }

                            public void onResponseReceived(Request request,
                                    Response response) {
                                if (200 == response.getStatusCode()) {
                                    // Process the response in
                                    GWT.log(response.getText());
                                } else {
                                    // Handle the error. Can get the status text
                                    // from response.getStatusText()
                                    view.setLoginError();
                                    return;
                                }
                            }
                        });
            } catch (RequestException e) {
                // Couldn't connect to server
                GWT.log("exception during login");
            }
            //DEVEL ONLY FOR FAST LOGIN
            userService.loginUser(new UserDetail(username, password), new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable arg0) {
                    view.setUnknownError();
                }

                @Override
                public void onSuccess(String sessionId) {
                    if (sessionId != null) {
                        // TODO Praso - workaround for developoment purposes
                        setSessionID(sessionId);
                        //Martin: Change id = 149 to id = 613248 for testing new user and his demands
//                        setSessionID("id=149");
//                        setSessionID("id=613248");
                        eventBus.atAccount();
                        hideView();
                    } else {
                        view.setLoginError();
                    }
                }
            });
            //DEVEL ONLY FOR FAST SUPPLIER LOGIN
//            This is the reason why the atAccountMethod is called twice
//            setSessionID("id=149");
//            eventBus.atAccount();
//            hideView();
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
