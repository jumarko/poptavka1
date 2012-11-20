package com.eprovement.poptavka.client.common.login;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
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
        // retrieve UserDetail and subsequently BusinessUserDetail object and store them in Storage
        userService.getLoggedUser(new SecuredAsyncCallback<UserDetail>(eventBus) {
            @Override
            public void onSuccess(UserDetail userDetail) {
                Storage.setUserDetail(userDetail);
                userService.getLoggedBusinessUser(new SecuredAsyncCallback<BusinessUserDetail>(eventBus) {
                    @Override
                    public void onSuccess(BusinessUserDetail loggedUser) {
                        GWT.log("user id " + loggedUser.getUserId());
                        Storage.setBusinessUserDetail(loggedUser);
                        // TODO ivlcek - fix the session model on the base of SpringSecurity rememberMe
                        final String sessionId = "id=" + loggedUser.getUserId();
                        forwardUser();
                        hideView();
                    }
                });
            }
        });
    }

    /**************************************************************************/
    /* History helper methods                                                 */
    /**************************************************************************/
    /**
     * Set account layout and forward user to appropriate module according to his role.
     * Called from login method only during normal login - by user.
     */
    private void forwardUser() {
        //Set account layout
        eventBus.atAccount();

        //forward user to welcome view of appropriate module according to his roles
        if (Storage.getBusinessUserDetail().getBusinessRoles().contains(
                BusinessUserDetail.BusinessRole.SUPPLIER)) {
            eventBus.goToSupplierDemandsModule(null, Constants.NONE);
        } else if (Storage.getBusinessUserDetail().getBusinessRoles().contains(
                BusinessUserDetail.BusinessRole.CLIENT)) {
            eventBus.goToClientDemandsModule(null, Constants.NONE);
        }
    }
}
