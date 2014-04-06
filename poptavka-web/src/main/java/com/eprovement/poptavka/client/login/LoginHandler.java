/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.login;

import com.eprovement.poptavka.client.common.CommonAccessRoles;
import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.service.demand.MailRPCServiceAsync;
import com.eprovement.poptavka.client.service.demand.LoginRPCServiceAsync;
import com.eprovement.poptavka.client.service.demand.LoginUnsecRPCServiceAsync;
import com.eprovement.poptavka.client.user.admin.interfaces.IAdminModule;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.eprovement.poptavka.shared.domain.message.ContactUsDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import java.util.logging.Logger;

/**
 * Login handler manages module's RPC calls.
 *
 * @author Martin Slavkovsky
 */
@EventHandler
public class LoginHandler extends BaseEventHandler<LoginEventBus> {

    @Inject
    private LoginRPCServiceAsync loginService;
    @Inject
    private LoginUnsecRPCServiceAsync loginUnsecService;
    @Inject
    private MailRPCServiceAsync mailService;
    private static final Logger LOGGER = Logger.getLogger(LoginHandler.class.getName());
    private static final String DEFAULT_SPRING_LOGIN_URL = "j_spring_security_check";
    private static final String DEFAULT_SPRING_LOGOUT_URL = "j_spring_security_logout";
    private String springLoginUrl = null;
    private String springLogoutUrl = null;
    private boolean displayThankYouPopup;

    /**
     * This method populates Storage i.e. our custom GWT session object with UserDetail.
     * A secured RPC service is invoked so this method is succesfylly called only if user is logged in and he opened our
     * website in new browser tab, which obviously starts the whole app from the begining.
     * If user is not logged in the RPC service will cause the initiation of loginPopupView via SecuredAsyncCallback.
     * @param widgetToLoad is a constant of view that will be loaded at the end
     */
    public void onLoginFromSession(final int widgetToLoad) {
        loginService.getLoggedUser(new SecuredAsyncCallback<UserDetail>(eventBus) {
            @Override
            public void onSuccess(UserDetail userDetail) {
                Storage.setUserDetail(userDetail);
                loginService.getLoggedBusinessUser(new SecuredAsyncCallback<BusinessUserDetail>(eventBus) {
                    @Override
                    public void onSuccess(BusinessUserDetail businessUserDetail) {
                        Storage.setBusinessUserDetail(businessUserDetail);
                        Storage.loadClientAndSupplierIDs();
                        GWT.log("login from session,  user id " + businessUserDetail.getUserId());
                        forwardUserLogin(widgetToLoad);
                    }
                });

            }
        });
    }

    /**
     * Verifes if user provided correct email and password.
     * @param userEmail
     * @param userPassword
     * @param widgetToLoad is a constant of view that will be loaded at the end
     */
    public void onVerifyUser(final String userEmail, final String userPassword, final int widgetToLoad) {
        eventBus.setLoadingProgress(0, Storage.MSGS.loggingVerifyAccount());
        loginUnsecService.getBusinessUserByEmail(
                userEmail.trim(), new SecuredAsyncCallback<BusinessUserDetail>(eventBus) {
                    @Override
                    public void onSuccess(BusinessUserDetail user) {
                        if (user == null) {
                            LOGGER.info("User entered invalid email=" + user);
                            eventBus.setErrorMessage(Storage.MSGS.wrongLoginMessage());
                            return;
                        }

                        if (user.isVerified()) {
                            eventBus.setLoadingProgress(30, Storage.MSGS.loggingIn());
                            // user has already been verified - it is ready for login
                            loginUser(userEmail, userPassword, widgetToLoad);
                        } else {
                            // we need to set plaintext password for activation code popup
                            // the current password from DB is encrypted and will not be useful
                            user.setPassword(userPassword);

                            eventBus.hideView();

                            // user has not been verified yet - prompt user for activation code
                            eventBus.initActivationCodePopup(user, widgetToLoad);
                            // ActivationCodePopupPresenter performs autologin once user is activated properly
                        }
                    }

                    @Override
                    protected void onServiceFailure(Throwable caught, int errorResponse, String errorId) {
                        super.onServiceFailure(caught, errorResponse, errorId);
                        eventBus.hideView();
                    }
                });
    }

    /**
     * Login user after successfull verification.
     * @param user
     * @param password
     * @param widgetToLoad is a constant of view that will be loaded at the end
     */
    public void loginUser(final String user, final String password, final int widgetToLoad) {

        final RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, getSpringLoginUrl());
        rb.setHeader("Content-Type", "application/x-www-form-urlencoded");
        final StringBuilder sbParams = new StringBuilder(100);
        sbParams.append("j_username=");
        sbParams.append(URL.encode(user));
        sbParams.append("&j_password=");
        sbParams.append(URL.encode(password));

        try {
            rb.sendRequest(sbParams.toString(), new RequestCallback() {
                @Override
                public void onError(final Request request, final Throwable exception) {
                    // Couldn't connect to server (could be timeout, SOP violation, etc.)
                    LOGGER.severe("Server part (poptavka-core) doesn't respond during user logging, exception="
                            + exception.getMessage());
                    eventBus.setErrorMessage(Storage.MSGS.loginUnknownError());
                }

                @Override
                public void onResponseReceived(final Request request, final Response response) {
                    eventBus.setLoadingProgress(50, Storage.MSGS.loggingLoadProfile());
                    int status = response.getStatusCode();
                    LOGGER.fine("Response status code = " + status);
                    if (status == Response.SC_OK) {
                        LOGGER.info("User=" + user + " has logged in!");
                        // notify all interested components that user has succesfully logged in
                        fireAfterLoginEvent(widgetToLoad);
                    } else if (status == Response.SC_UNAUTHORIZED) {
                        LOGGER.fine("User entered wrong credentials !");
                        eventBus.setErrorMessage(Storage.MSGS.wrongLoginMessage());
                    } else {
                        // other status codes can be processed here
                        LOGGER.severe("Unexptected response status code while logging in, code=" + status);
                        eventBus.setErrorMessage(Storage.MSGS.loginUnknownError());
                    }
                }
            });

        } catch (RequestException exception) {
            LOGGER.severe("RequestException thrown during user logging, exception=" + exception.getMessage());
            eventBus.setErrorMessage(Storage.MSGS.loginUnknownError());
        }
    }

    /**
     * This method is initiated after successfull login. All components must be notified of this event in order to
     * change the view etc. User is forwarded to ClientDemands module or SupplierDemands module based on his business
     * roles.
     * @param widgetToLoad is a constant of view that will be loaded at the end
     */
    private void fireAfterLoginEvent(final int widgetToLoad) {
        // retrieve UserDetail and subsequently BusinessUserDetail object and store them in Storage
        loginService.getLoggedUser(new SecuredAsyncCallback<UserDetail>(eventBus) {
            @Override
            public void onSuccess(UserDetail userDetail) {
                eventBus.setLoadingProgress(70, Storage.MSGS.loggingForward());
                Storage.setUserDetail(userDetail);
                loginService.getLoggedBusinessUser(new SecuredAsyncCallback<BusinessUserDetail>(eventBus) {
                    @Override
                    public void onSuccess(BusinessUserDetail loggedUser) {
                        eventBus.setLoadingProgress(90, null);
                        GWT.log("user id " + loggedUser.getUserId());
                        Storage.setBusinessUserDetail(loggedUser);
                        Storage.loadClientAndSupplierIDs();
                        // TODO LATER ivlcek - fix the session model on the base of SpringSecurity rememberMe
                        final String sessionId = "id=" + loggedUser.getUserId();
                        forwardUserLogin(widgetToLoad);
                        eventBus.hideView();
                    }
                });
            }
        });
    }

    /**
     * Logs out and user is forwarded to HomeWelcomModule.
     * @param widgetToLoad is a constant of view that will be loaded at the end
     */
    public void onLogout(final int widgetToLoad) {
        final RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, getLogoutUrl());
        rb.setCallback(new RequestCallback() {
            @Override
            public void onResponseReceived(Request request, Response response) {
                if (response.getStatusCode() == Response.SC_OK) { // 200 everything is ok.
                    LOGGER.info("User=" + Storage.getUser().getEmail() + " has logged out!");
                    //remove user from session management to force user input login information
                    Storage.invalidateStorage();
                    forwardUserLogout(widgetToLoad);
                } else {
                    LOGGER.severe("Unexptected response status code while logging out, code="
                            + response.getStatusCode());
                    eventBus.displayError(response.getStatusCode(), null);
                }
            }

            @Override
            public void onError(Request request, Throwable exception) {
                // Couldn't connect to server (could be timeout, SOP violation, etc.)
                LOGGER.severe("Server part (poptavka-core) doesn't respond during user logging out, exception="
                        + exception.getMessage());
                eventBus.displayError(0, null);
            }
        });
        try {
            rb.send();
        } catch (RequestException exception) {
            LOGGER.severe("RequestException thrown during user logging out, exception=" + exception.getMessage());
            eventBus.displayError(0, null);
        }
    }

    /**
     * Sets whether thank you popup will be displayed at the end.
     * @param displayThankYouPopup true enables thank you popup, false disables
     */
    public void onShowThankYouPopupAfterLogin(boolean displayThankYouPopup) {
        this.displayThankYouPopup = displayThankYouPopup;
    }

    /**************************************************************************/
    /* Reset password                                                         */
    /**************************************************************************/
    /**
     * Reset password for account with passed email.
     * @param email whose password will be reset
     */
    public void onResetPassword(final String email) {
        eventBus.setLoadingProgress(0, Storage.MSGS.loggingVerifyAccount());
        loginUnsecService.getBusinessUserByEmail(email.trim(), new SecuredAsyncCallback<BusinessUserDetail>(eventBus) {
            @Override
            public void onSuccess(final BusinessUserDetail user) {
                if (user == null) {
                    eventBus.setErrorMessage(Storage.MSGS.wrongEmailWhenReseting());
                    return;
                }
                if (user.isVerified()) {
                    eventBus.setLoadingProgress(30, null);
                    loginUnsecService.resetPassword(user.getUserId(), new SecuredAsyncCallback<String>(eventBus) {
                        @Override
                        public void onSuccess(String newPassword) {
                            sendEmailWithNewPassword(user, newPassword);
                        }
                    });
                }
            }

            @Override
            protected void onServiceFailure(Throwable caught, int errorResponse, String errorId) {
                super.onServiceFailure(caught, errorResponse, errorId);
                eventBus.hideView();
            }
        });
    }

    /**
     * Send user an email with new password.
     * @param user whose password was reset
     * @param newPassword new user's password
     */
    private void sendEmailWithNewPassword(final BusinessUserDetail user, final String newPassword) {
        eventBus.setLoadingProgress(60, null);
        ContactUsDetail dialogDetail = new ContactUsDetail();
        dialogDetail.setRecipient(user.getEmail());
        dialogDetail.setMessage(Storage.MSGS.resetPasswordEmail(user.getPersonFirstName(), newPassword));
        dialogDetail.setSubject(Storage.MSGS.resetPasswordEmailSubject());
        mailService.sendMail(dialogDetail, new SecuredAsyncCallback<Boolean>(eventBus) {
            @Override
            public void onSuccess(Boolean result) {
                eventBus.setLoadingProgress(100, Storage.MSGS.resetPasswordInfoStatus());
            }
        });
    }

    /**************************************************************************/
    /* History helper methods                                                 */
    /**************************************************************************/
    /**
     * Set account layout and forward user to appropriate module according to his role.
     * Called by loginFromSession from HistoryConverter.
     * @param widgetToLoad is a constant of view that will be loaded at the end
     */
    private void forwardUserLogin(int widgetToLoad) {
        //Set account layout
        eventBus.atAccount();

        GWT.log("BUSSINESS ROLES ++++ " + Storage.getBusinessUserDetail().getBusinessRoles().toString());
        GWT.log("ACCESS ROLES ++++ " + Storage.getUser().getAccessRoles().toString());
        //If exact module is known to be loaded, do it
        switch (widgetToLoad) {
            case Constants.SKIP:
                return;
            case Constants.USER_SETTINGS_MODULE:
                eventBus.goToSettingsModule();
                break;
            case Constants.HOME_DEMANDS_MODULE:
                eventBus.goToHomeDemandsModule(null);
                break;
            case Constants.HOME_SUPPLIERS_MODULE:
                eventBus.goToHomeSuppliersModule(null);
                break;
            case Constants.CREATE_DEMAND:
                forwardToCreateDemands();
                break;
            case Constants.CREATE_SUPPLIER:
                eventBus.goToCreateSupplierModule();
                break;
            case Constants.MESSAGES_INBOX:
                eventBus.goToMessagesModule(null, Constants.MESSAGES_INBOX);
                break;
            case Constants.USER_ADMININSTRATION_MODULE:
                eventBus.goToAdminModule(null, IAdminModule.AdminWidget.DASHBOARD);
                break;
            default:
                //otherwise forward user to welcome view of appropriate module according to his roles
                if (Storage.getUser().getAccessRoles().contains(CommonAccessRoles.ADMIN)) {
                    eventBus.goToAdminModule(null, IAdminModule.AdminWidget.DASHBOARD);
                } else if (Storage.getBusinessUserDetail().getBusinessRoles().contains(
                        BusinessUserDetail.BusinessRole.SUPPLIER)) {
                    forwardToSupplierDemands(widgetToLoad);
                } else if (Storage.getBusinessUserDetail().getBusinessRoles().contains(
                        BusinessUserDetail.BusinessRole.CLIENT)) {
                    forwardToClientDemands(widgetToLoad);
                }
                break;
        }
    }

    /**
     * Forwards user after logout to a widget.
     * Each widget has assigned an constant that must be provided.
     * @param widgetToLoad is a constant of view that will be loaded after logout
     */
    private void forwardUserLogout(int widgetToLoad) {
        //Set home layout
        eventBus.atHome();
        //If logout was invoked by user, forward to default module
        switch (widgetToLoad) {
            case Constants.CREATE_DEMAND:
                eventBus.goToCreateDemandModule();
                break;
            default:
                eventBus.goToHomeWelcomeModule();
                break;
        }
    }

    /**
     * Forwards to CreateDemands module.
     * Display thank you popup if enabled.
     */
    private void forwardToCreateDemands() {
        Timer additionalAction = new Timer() {
            @Override
            public void run() {
                eventBus.goToCreateDemandModule();
            }
        };
        if (displayThankYouPopup) {
            eventBus.showThankYouPopup(
                    Storage.MSGS.thankYouCodeActivatedToDemandCreation(), additionalAction);
        } else {
            eventBus.goToCreateDemandModule();
        }
    }

    /**
     * Forwards to ClientDemands module and to a its particular widget.
     * Display thank you popup if enabled.
     * @param widgetToLoad is a constant of view that will be loaded
     */
    private void forwardToClientDemands(final int widgetToLoad) {
        Timer additionalAction = new Timer() {
            @Override
            public void run() {
                eventBus.goToClientDemandsModule(null, widgetToLoad);
            }
        };
        if (displayThankYouPopup) {
            eventBus.showThankYouPopup(
                    Storage.MSGS.thankYouCodeActivatedToClientDashboard(), additionalAction);
        } else {
            eventBus.goToClientDemandsModule(null, widgetToLoad);
        }
    }

    /**
     * Forwards to SupplierDemands module and to a its particular widget.
     * Display thank you popup if enabled.
     * @param widgetToLoad is a constant of view that will be loaded
     */
    private void forwardToSupplierDemands(final int widgetToLoad) {
        Timer additionalAction = new Timer() {
            @Override
            public void run() {
                eventBus.goToSupplierDemandsModule(null, widgetToLoad);
            }
        };
        if (displayThankYouPopup) {
            eventBus.showThankYouPopup(
                    Storage.MSGS.thankYouCodeActivatedToSupplierDashboard(), additionalAction);
        } else {
            eventBus.goToSupplierDemandsModule(null, widgetToLoad);
        }
    }

    /**
     * Method returns the login URL that when called issues the login process that is handled by SpringSecurity.
     * @return gwt login url with j_spring_security_check postfix
     */
    private String getSpringLoginUrl() {
        if (this.springLoginUrl == null) {
            this.springLoginUrl = GWT.getHostPageBaseURL() + DEFAULT_SPRING_LOGIN_URL;
        }
        return springLoginUrl;
    }

    /**
     * Method returns the logout URL that when called issues the logout process that is handled by SpringSecurity.
     * @return gwt logout url with j_spring_security_logout postfix
     */
    public String getLogoutUrl() {
        if (springLogoutUrl == null) {
            springLogoutUrl = GWT.getHostPageBaseURL() + DEFAULT_SPRING_LOGOUT_URL;
        }
        return springLogoutUrl;
    }
}
