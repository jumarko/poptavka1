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
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.main.Constants;
import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.client.main.errorDialog.ErrorDialogPopupView;
import cz.poptavka.sample.client.root.RootEventBus;
import cz.poptavka.sample.client.service.demand.MailRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.UserRPCServiceAsync;
import cz.poptavka.sample.shared.domain.LoggedUserDetail;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.exceptions.ExceptionUtils;
import cz.poptavka.sample.shared.exceptions.RPCException;

@Presenter(view = LoginPopupView.class, multiple = true)
public class LoginPopupPresenter extends LazyPresenter<LoginPopupPresenter.LoginPopupInterface, RootEventBus> {

    private static final Logger LOGGER = Logger.getLogger(LoginPopupPresenter.class.getName());
    private static final int COOKIE_TIMEOUT = 1000 * 60 * 60 * 24;
    private MailRPCServiceAsync mailService = null;
    private ErrorDialogPopupView errorDialog;

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
            // for testing purpose, if sending mails works
            mailService.sendMail("kolkar100@gmail.com", "TestMessage", "Test", "poptavka@poptavam.com",
                    new AsyncCallback<Boolean>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            GWT.log("message not sent succesfully");
                            if (caught instanceof RPCException) {
                                ExceptionUtils.showErrorDialog(errorDialog, caught);
                            }
                        }

                        @Override
                        public void onSuccess(Boolean result) {
                            GWT.log("message sent succesfully");
                        }
                    });
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
            userService.loginUser(new UserDetail(username, password), new AsyncCallback<LoggedUserDetail>() {

                @Override
                public void onFailure(Throwable caught) {
                    if (caught instanceof RPCException) {
                        ExceptionUtils.showErrorDialog(errorDialog, caught);
                    }
                    view.setUnknownError();
                }

                @Override
                public void onSuccess(LoggedUserDetail loggedUser) {
                    final String sessionId = "id=" + loggedUser.getUserId();
                    if (sessionId != null) {
                        // TODO Praso - workaround for developoment purposes
                        setSessionID(sessionId);
                        //Martin: Change id = 149 to id = 613248 for testing new user and his demands
//                        setSessionID("id=149");
//                        setSessionID("id=613248");

                        //Martin - musi byt kvoli histori.
                        //Kedze tato metoda obsarava prihlasovanie, musel som ju zahrnut.
                        //Pretoze ak sa prihlasenie podari, musi sa naloadovat iny widget
                        //ako pri neuspesnom prihlaseni. Nie je sposob ako to zistit
                        //z history convertara "externe"
                        if (History.getToken().equals("atAccount")) {
                            eventBus.setHistoryStoredForNextOne(false);
                            eventBus.atAccount();
                            History.forward();
                            Storage.setActionLoginAccountHistory("back");
                        }
                        if (History.getToken().equals("atHome")) {
                            eventBus.setHistoryStoredForNextOne(false);
                            eventBus.atAccount();
                            History.back();
                            Storage.setActionLoginHomeHistory("forward");
                        }
                        if (!History.getToken().equals("atAccount")
                                && !History.getToken().equals("atHome")) {
                            eventBus.atAccount();
                            eventBus.goToDemandModule(null, Constants.NONE);
                        }
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

    @Inject
    void setMailService(MailRPCServiceAsync service) {
        mailService = service;
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
