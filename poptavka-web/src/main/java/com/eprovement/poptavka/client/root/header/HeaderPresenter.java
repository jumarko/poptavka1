package com.eprovement.poptavka.client.root.header;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import com.eprovement.poptavka.client.common.login.LoginPopupPresenter;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.client.root.interfaces.IHeaderView;
import com.eprovement.poptavka.client.root.interfaces.IHeaderView.IHeaderPresenter;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.History;
import java.util.logging.Logger;

@Presenter(view = HeaderView.class)
public class HeaderPresenter extends BasePresenter<IHeaderView, RootEventBus>
        implements IHeaderPresenter {

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private static final Logger LOGGER = Logger.getLogger(HeaderPresenter.class.getName());
    private static final String DEFAULT_SPRING_LOGOUT_URL = "j_spring_security_logout";
    private boolean loggedIn = false;
    private String logoutUrl = null;
    private LoginPopupPresenter login;

    public void onStart() {
        GWT.log("Header presenter loaded");
        eventBus.setHeader(view);
    }

    @Override
    public void bind() {
        view.getLoginLink().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                GWT.log("LoginPopupInitialized isUserLoggedIn=" + loggedIn);
                //neda nahradid tym cookies? ... co lepsie pouzivat tie cookies ci premennu?
                if (loggedIn) {
                    //If user invoked logout process, set pointer to false
                    Storage.setLogoutDueToHistory(false);
                    onLogout();
                } else {
                    //If user invoked login process, set pointer to false
                    Storage.setLoginDueToHistory(false);
                    onLogin();
                }
            }
        });
        Window.addWindowClosingHandler(new Window.ClosingHandler() {
            @Override
            public void onWindowClosing(ClosingEvent event) {
                //TODO Martin - musi to tu byt, pouzivame vobec cookies?
//                Cookies.setCookie("login", "no");
            }
        });
    }

    public void onAtHome() {
        this.loggedIn = false;
        view.getLoginLink().setText(MSGS.logIn());
    }

    public void onAtAccount() {
        this.loggedIn = true;
        view.getLoginLink().setText(MSGS.logOut());
    }

    /**
     * Method displays the LoginPoupView so that user can enter credentials and log in.
     */
    public void onLogin() {
        eventBus.addHandler(LoginPopupPresenter.class);
    }

    /**
     * Method displays the LoginPopupView while user is being logged out.
     */
    public void onLogout() {
        final RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, getLogoutUrl());
        // rb.setHeader("Accept", "application/json"); // json expected ?
        rb.setCallback(new RequestCallback() {
            @Override
            public void onResponseReceived(Request request, Response response) {
                if (!Storage.isLogoutDueToHistory()) {
                    eventBus.registerLogEventForHistory();
                }
                if (response.getStatusCode() == Response.SC_OK) { // 200 everything is ok.
                    LOGGER.info("User=" + Storage.getUser().getEmail() + " has logged out!");
                    // TODO ivlcek - when usrs logs out display a message. We need some message bar in layout
                    //remove user from session management to force user input login information
                    Storage.invalidateStorage();
                    forwardUser();
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

    /**************************************************************************/
    /* History helper methods                                                 */
    /**************************************************************************/
    private void forwardUser() {
        //Set home layout
        eventBus.atHome();
        //If logout process was invoked because of history
        //forward history to next stored token, not default one
        if (Storage.isLogoutDueToHistory()) {
            if (Storage.getForwardHistory().equals(Storage.BACK)) {
                History.back();
            } else {
                History.forward();
            }
        } else {
            //If logout was invoked by user, forward to default module
            eventBus.goToHomeWelcomeModule(null);
        }
    }
}