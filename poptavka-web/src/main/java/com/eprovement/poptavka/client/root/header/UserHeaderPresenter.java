package com.eprovement.poptavka.client.root.header;

import com.eprovement.poptavka.client.common.session.Constants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.client.root.interfaces.IUserHeaderView;
import com.eprovement.poptavka.client.root.interfaces.IUserHeaderView.IUserHeaderPresenter;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.RequestException;
import java.util.logging.Logger;

@Presenter(view = UserHeaderView.class)
public class UserHeaderPresenter extends BasePresenter<IUserHeaderView, RootEventBus>
        implements IUserHeaderPresenter {

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private static final Logger LOGGER = Logger.getLogger(UserHeaderPresenter.class.getName());
    private static final String DEFAULT_SPRING_LOGOUT_URL = "j_spring_security_logout";
    private String logoutUrl = null;

    @Override
    public void bind() {
        view.getMenuLogOut().setScheduledCommand(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                onLogout(Constants.HOME_WELCOME_MODULE);
            }
        });
        view.getMenuMyProfile().setScheduledCommand(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                eventBus.userMenuStyleChange(Constants.USER_SETTINGS_MODULE);
                eventBus.goToSettingsModule();
            }
        });

        view.getMenuHelp().setScheduledCommand(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                Window.alert("Implement Help Command in UserHeaderMenuCommands class");
            }
        });

        view.getMenuCustomerService().setScheduledCommand(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                Window.alert("Implement Cutom Service Command in UserHeaderMenuCommands class.");
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

    public void onAtAccount() {
        GWT.log("UserHeader presenter loaded");
        eventBus.setHeader(view);
        view.getUsername().setText(Storage.getUser().getEmail());

    }

    public void onSetUpdatedUnreadMessagesCount(int numberOfMessages) {
        view.getNewMessagesCount().setText(String.valueOf(numberOfMessages));
    }

    /**
     * Logs out and user is forwarded to HomeWelcomModule.
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
                    forwardUser(widgetToLoad);
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
    private void forwardUser(int widgetToLoad) {
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
}