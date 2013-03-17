package com.eprovement.poptavka.client.common.login;

import com.eprovement.poptavka.client.common.CommonAccessRoles;
import com.eprovement.poptavka.client.service.demand.RootRPCServiceAsync;
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
import com.eprovement.poptavka.client.service.demand.UserRPCServiceAsync;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.http.client.URL;

@Presenter(view = LoginPopupView.class, multiple = true)
public class LoginPopupPresenter extends LazyPresenter<LoginPopupPresenter.LoginPopupInterface, RootEventBus> {

    private static final Logger LOGGER = Logger.getLogger(LoginPopupPresenter.class.getName());
    private static final String DEFAULT_SPRING_LOGIN_URL = "j_spring_security_check";
    private static final String DEFAULT_SPRING_LOGOUT_URL = "j_spring_security_logout";
    private int widgetToLoad = Constants.NONE;
    private String springLoginUrl = null;

    public interface LoginPopupInterface extends LazyView {

        boolean isValid();

        TextBox getLogin();

        TextBox getPassword();

        void hidePopup();

        void setLoadingStatus(String localizableMessage);

        void setLoadingProgress(Integer newPercentage, String newMessage);

        void setErrorMessage(String message);

        LoginPopupPresenter getPresenter();
    }
    @Inject
    private UserRPCServiceAsync userService;
    @Inject
    private RootRPCServiceAsync rootService;

    /**
     * Real html login. SHOULD/WILL be used in prod
     */
    public void doLogin() {
        if (view.isValid()) { // both email and password fields are not empty
            view.setLoadingStatus(Storage.MSGS.loggingVerifyAccount());
            verifyUser();
        }
    }

    private void verifyUser() {
        view.setLoadingProgress(0, Storage.MSGS.loggingVerifyAccount());
        // TODO release: check if user is VERIFIED
        // if not then display activation popup
        rootService.getBusinessUserByEmail(getUserEmail(), new SecuredAsyncCallback<BusinessUserDetail>(eventBus) {
            @Override
            public void onSuccess(BusinessUserDetail user) {
                if (user == null) {
                    LOGGER.info("User entered invalid email=" + getUserEmail());
                    view.setErrorMessage(Storage.MSGS.wrongLoginMessage());
                    return;
                }

                if (user.isVerified()) {
                    view.setLoadingProgress(30, Storage.MSGS.loggingIn());
                    // user has already been verified - it is ready for login
                    loginUser();
                } else {
                    // we need to set plaintext password for activation code popup
                    // the current password from DB is encrypted and will not be useful
                    user.setPassword(getUserPassword());

                    hideView();

                    // user has not been verified yet - prompt user for activation code
                    eventBus.initActivationCodePopup(user, widgetToLoad);
                    // ActivationCodePopupPresenter performs autologin once user is activated properly
                }
            }

            @Override
            protected void onServiceFailure(Throwable caught, int errorResponse, String errorId) {
                super.onServiceFailure(caught, errorResponse, errorId);
                hideView();
            }
        });

    }

    private void loginUser() {

        final RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, getSpringLoginUrl());
        rb.setHeader("Content-Type", "application/x-www-form-urlencoded");
        final StringBuilder sbParams = new StringBuilder(100);
        sbParams.append("j_username=");
        sbParams.append(URL.encode(getUserEmail()));
        sbParams.append("&j_password=");
        sbParams.append(URL.encode(getUserPassword()));

        try {
            rb.sendRequest(sbParams.toString(), new RequestCallback() {
                @Override
                public void onError(final Request request, final Throwable exception) {
                    // Couldn't connect to server (could be timeout, SOP violation, etc.)
                    LOGGER.severe("Server part (poptavka-core) doesn't respond during user logging, exception="
                            + exception.getMessage());
                    view.setErrorMessage(Storage.MSGS.loginUnknownError());
                    // TODO jumarko - Shall we send email notifications when this happens?
                }

                @Override
                public void onResponseReceived(final Request request, final Response response) {
                    view.setLoadingProgress(50, Storage.MSGS.loggingLoadProfile());
                    int status = response.getStatusCode();
                    LOGGER.fine("Response status code = " + status);
                    if (status == Response.SC_OK) {
                        LOGGER.info("User=" + view.getLogin() + " has logged in!");
                        // notify all interested components that user has succesfully logged in
                        fireAfterLoginEvent();
                    } else if (status == Response.SC_UNAUTHORIZED) {
                        LOGGER.fine("User entered wrong credentials !");
                        view.setErrorMessage(Storage.MSGS.wrongLoginMessage());
                    } else {
                        // other status codes can be processed here
                        LOGGER.severe("Unexptected response status code while logging in, code=" + status);
                        view.setErrorMessage(Storage.MSGS.loginUnknownError());
                    }
                }
            });

        } catch (RequestException exception) {
            LOGGER.severe("RequestException thrown during user logging, exception=" + exception.getMessage());
            view.setErrorMessage(Storage.MSGS.loginUnknownError());
            // TODO jumarko - Shall we send email notifications when this happens?
        }
    }

    public void doAutoLogin(String email, String password) {
        view.getLogin().setText(email);
        view.getPassword().setText(password);
        doLogin();
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
                view.setLoadingProgress(70, Storage.MSGS.loggingForward());
                Storage.setUserDetail(userDetail);
                userService.getLoggedBusinessUser(new SecuredAsyncCallback<BusinessUserDetail>(eventBus) {
                    @Override
                    public void onSuccess(BusinessUserDetail loggedUser) {
                        view.setLoadingProgress(90, null);
                        GWT.log("user id " + loggedUser.getUserId());
                        Storage.setBusinessUserDetail(loggedUser);
                        Storage.loadClientAndSupplierIDs();
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

        GWT.log("BUSSINESS ROLES ++++ " + Storage.getBusinessUserDetail().getBusinessRoles().toString());
        GWT.log("ACCESS ROLES ++++ " + Storage.getUser().getAccessRoles().toString());
        //If exact module is known to be loaded, do it
        if (widgetToLoad == Constants.CREATE_DEMAND) {
            eventBus.goToCreateDemandModule();
            return;
        }
        //otherwise forward user to welcome view of appropriate module according to his roles
        view.setLoadingProgress(100, null);
        if (Storage.getUser().getAccessRoles().contains(CommonAccessRoles.ADMIN)) {
            eventBus.goToAdminModule(null, widgetToLoad);
        } else if (Storage.getBusinessUserDetail().getBusinessRoles().contains(
                BusinessUserDetail.BusinessRole.SUPPLIER)) {
            eventBus.goToSupplierDemandsModule(null, widgetToLoad);
        } else if (Storage.getBusinessUserDetail().getBusinessRoles().contains(
                BusinessUserDetail.BusinessRole.CLIENT)) {
            eventBus.goToClientDemandsModule(null, widgetToLoad);
        }
    }

    private String getUserEmail() {
        return view.getLogin().getText().trim();
    }

    private String getUserPassword() {
        return view.getPassword().getText();
    }

    public void loadWidget(int widgetId) {
        this.widgetToLoad = widgetId;
    }
}
