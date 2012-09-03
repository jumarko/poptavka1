package com.eprovement.poptavka.client.common.login;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.History;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.client.service.demand.MailRPCServiceAsync;
import com.eprovement.poptavka.client.service.demand.UserRPCServiceAsync;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.Timer;

@Presenter(view = LoginPopupView.class, multiple = true)
public class LoginPopupPresenter extends LazyPresenter<LoginPopupPresenter.LoginPopupInterface, RootEventBus> {

    private static final Logger LOGGER = Logger.getLogger(LoginPopupPresenter.class.getName());
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private static final String DEFAULT_SPRING_LOGIN_URL = "j_spring_security_check";
    private static final String DEFAULT_SPRING_LOGOUT_URL = "j_spring_security_logout";
    private static final int COOKIE_TIMEOUT = 1000 * 60 * 60 * 24;
    private final short timeout = 1500;
    private MailRPCServiceAsync mailService = null;
    private String springLoginUrl = null;
    private String logoutUrl = null;

    public interface LoginPopupInterface extends LazyView {

        boolean isValid();

        String getLogin();

        String getPassword();

        void hidePopup();

        void setLoadingStatus(String localizableMessage);

        void setUnknownError();

        void setLoginError();

        LoginPopupPresenter getPresenter();
    }
    @Inject
    private UserRPCServiceAsync userService;

    @Inject
    void setMailService(MailRPCServiceAsync service) {
        mailService = service;
    }

    /**
     * Real html login. SHOULD/WILL be used in prod
     */
    public void doLogin() {
        if (view.isValid()) { // both email and password fields are not empty
            view.setLoadingStatus(MSGS.verifyAccount());
            final RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, getSpringLoginUrl());
            rb.setHeader("Content-Type", "application/x-www-form-urlencoded");
            final StringBuilder sbParams = new StringBuilder(100);
            sbParams.append("j_username=");
            sbParams.append(URL.encode(view.getLogin()));
            sbParams.append("&j_password=");
            sbParams.append(URL.encode(view.getPassword()));

            try {
                rb.sendRequest(sbParams.toString(), new RequestCallback() {
                    @Override
                    public void onError(final Request request, final Throwable exception) {
                        // Couldn't connect to server (could be timeout, SOP violation, etc.)
                        LOGGER.severe("Server part (poptavka-core) doesn't respond during user logging, exception="
                                + exception.getMessage());
                        view.setUnknownError();
                        // TODO jumarko - Shall we send email notifications when this happens?
                    }

                    @Override
                    public void onResponseReceived(final Request request, final Response response) {
                        int status = response.getStatusCode();
                        LOGGER.fine("Response status code = " + status);
                        if (status == Response.SC_OK) { // 200: everything's ok
                            LOGGER.info("User=" + view.getLogin() + " has logged in!");
                            view.setLoadingStatus(MSGS.loggingIn());
                            // notify all interested components that uses has succesfully logged in
                            fireAfterLoginEvent();
                        } else if (status == Response.SC_UNAUTHORIZED) { // 401: wrong credentials...
                            LOGGER.fine("User entered wrong credentials !");
                            view.setLoginError();
                        } else { // something else ?
                            // other status codes can be processed here
                            LOGGER.severe("Unexptected response status code while logging in, code=" + status);
                            view.setUnknownError();
                        }
                    }
                });

            } catch (RequestException exception) {
                LOGGER.severe("RequestException thrown during user logging, exception=" + exception.getMessage());
                view.setUnknownError();
                // TODO jumarko - Shall we send email notifications when this happens?
            }
        }
    }

    /**
     * Method logs out the user via RequestBuilder request. Storage session should be invalidated here.
     */
    public void onLogout() {
        view.setLoadingStatus(MSGS.loggingOut());
        final RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, getLogoutUrl());
        // rb.setHeader("Accept", "application/json"); // json expected ?
        rb.setCallback(new RequestCallback() {
            @Override
            public void onResponseReceived(Request request, Response response) {
                if (response.getStatusCode() == Response.SC_OK) { // 200 everything is ok.
                    LOGGER.info("User=" + Storage.getUser().getEmail() + " has logged out!");
                    Timer t = new Timer() {
                        @Override
                        public void run() {
                            hideView();
                        }
                    };
                    t.schedule(timeout);
                    //remove user from session management to force user input login information
                    Storage.setUser(null);
                    // Forward user to HomeWelcomeModule
                    eventBus.atHome();
                    eventBus.goToHomeWelcomeModule(null);
                } else {
                    LOGGER.severe("Unexptected response status code while logging out, code="
                            + response.getStatusCode());
                    view.setUnknownError();
                    // TODO jumarko - Shall we send email notifications when this happens?
                }
            }

            @Override
            public void onError(Request request, Throwable exception) {
                // Couldn't connect to server (could be timeout, SOP violation, etc.)
                LOGGER.severe("Server part (poptavka-core) doesn't respond during user logging out, exception="
                        + exception.getMessage());
                view.setUnknownError();
                // TODO jumarko - Shall we send email notifications when this happens?
            }
        });
        try {
            rb.send();
        } catch (RequestException exception) {
            LOGGER.severe("RequestException thrown during user logging out, exception=" + exception.getMessage());
            view.setUnknownError();
            // TODO jumarko - Shall we send email notifications when this happens?
        }
    }

    /**
     * Method returns the logout URL that when called issues the logout process that is handled by SpringSecurity.
     *
     * @return gwt logout url with j_spring_security_logout postfix
     */
    public String getLogoutUrl() {
        if (logoutUrl == null) {
            logoutUrl = GWT.getHostPageBaseURL() + DEFAULT_SPRING_LOGOUT_URL;
        }
        return logoutUrl;
    }

    /**
     * Logout URL can be defined in the application. For now we don't use this method.
     *
     * @param logoutUrl
     */
    public void setLogoutUrl(final String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    /**
     * Method returns the login URL that when called issues the login process that is handled by SpringSecurity.
     *
     * @return gwt login url with j_spring_security_check postfix
     */
    public String getSpringLoginUrl() {
        if (this.springLoginUrl == null) {
            this.springLoginUrl = GWT.getHostPageBaseURL() + DEFAULT_SPRING_LOGIN_URL;
        }
        return springLoginUrl;
    }

    /**
     * Hides the LoginPopupView.
     */
    public void hideView() {
        eventBus.removeHandler(view.getPresenter());
        view.hidePopup();
    }

    /**
     * Sets session ID. TODO ivlcek - this will be removed after we fully integrate Spring Security
     */
    private void setSessionID(String sessionId) {
//        LOGGER.fine("Setting SID cookie");
//        int cookieTimeout = COOKIE_TIMEOUT;
//        Date expires = new Date((new Date()).getTime() + cookieTimeout);
//        Cookies.setCookie("sid", sessionId);
    }

    /**
     * This method is initiated after successfull login. All components must be notified of this event in order to
     * change the view etc. User is forwarded to ClientDemands module or SupplierDemands module based on his business
     * roles.
     */
    private void fireAfterLoginEvent() {
        userService.getLoggedUser(new SecuredAsyncCallback<UserDetail>(eventBus) {
            @Override
            public void onSuccess(UserDetail userDetail) {
                Storage.setUser(userDetail);
            }

            @Override
            protected void onServiceFailure(Throwable caught) {
                view.setUnknownError();
            }
        });
        // TODO ivlcek - Do we have to call RPC two times for userDetail and BusinessUserDetail objects?
        userService.getLoggedBusinessUser(new SecuredAsyncCallback<BusinessUserDetail>(eventBus) {
            @Override
            protected void onServiceFailure(Throwable caught) {
                // TODO: review this failure handling code
                view.setUnknownError();
            }

            @Override
            public void onSuccess(BusinessUserDetail loggedUser) {
                GWT.log("user id " + loggedUser.getUserId());
//                Storage.setUser(loggedUser);
                Storage.setBusinessUserDetail(loggedUser);
                final String sessionId = "id=" + loggedUser.getUserId();
//                                    if (sessionId != null) {
//                                        setSessionID(sessionId);

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

                    eventBus.goToMessagesModule(null, Constants.NONE);
                    if (Storage.getBusinessUserDetail().getBusinessRoles().contains(
                            BusinessUserDetail.BusinessRole.SUPPLIER)) {
                        eventBus.goToSupplierDemandsModule(null, Constants.NONE);
                    } else if (Storage.getBusinessUserDetail().getBusinessRoles().contains(
                            BusinessUserDetail.BusinessRole.CLIENT)) {
                        eventBus.goToClientDemandsModule(null, Constants.NONE);
                    }
                }
                hideView();
            }
        });

    }
}
