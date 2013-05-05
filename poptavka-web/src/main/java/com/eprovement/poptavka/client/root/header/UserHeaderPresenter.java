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
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
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

        Window.addWindowClosingHandler(new Window.ClosingHandler() {
            @Override
            public void onWindowClosing(ClosingEvent event) {
                //TODO LATER Martin - musi to tu byt, pouzivame vobec cookies?
//                Cookies.setCookie("login", "no");
            }
        });
        view.getPushButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (Storage.getBusinessUserDetail().getBusinessRoles().contains(
                        BusinessUserDetail.BusinessRole.ADMIN)) {
                    eventBus.goToAdminModule(null, Constants.NONE);
                } else if (Storage.getBusinessUserDetail().getBusinessRoles().contains(
                        BusinessUserDetail.BusinessRole.SUPPLIER)) {
                    eventBus.goToSupplierDemandsModule(null, Constants.NONE);
                } else {
                    eventBus.goToClientDemandsModule(null, Constants.NONE);
                }
            }
        });
        view.getPushSystemButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToMessagesModule(null, Constants.NONE);
            }
        });
    }

    public void onAtAccount() {
        GWT.log("UserHeader presenter loaded");
        eventBus.setHeader(view);
        view.getUsername().setText(Storage.getUser().getEmail());
    }

    /**
     * Loads notification icons with adequate numbers of unread messages for user header section.
     * @param numberOfMessages carrying number of unread demand and system messages
     */
    public void onSetUpdatedUnreadMessagesCount(UnreadMessagesDetail numberOfMessages) {
        if (numberOfMessages.getUnreadMessagesCount() == 0) {
            setButton(view.getPushButton(), new Image(Storage.RSCS.images().envelopeImageEmpty()), false, null);
        } else {
            view.getNewMessagesCount().setText(String.valueOf(numberOfMessages.getUnreadMessagesCount()));
            setButton(view.getPushButton(), new Image(Storage.RSCS.images().envelopeImage()), true,
                    new Image(Storage.RSCS.images().envelopeHoverImage()));
        }
        if (numberOfMessages.getUnreadSystemMessageCount() == 0) {
            setButton(view.getPushSystemButton(), new Image(Storage.RSCS.images().flagImageEmpty()), false, null);
        } else {
            view.getNewSystemMessagesCount().setText(String.valueOf(numberOfMessages.getUnreadSystemMessageCount()));
            setButton(view.getPushSystemButton(), new Image(Storage.RSCS.images().flagImage()), true,
                    new Image(Storage.RSCS.images().flagHoverImage()));
        }
    }

    /**
     * Define images for hovering and click actions for given button.
     * @param button to be adjusted
     * @param image default image
     * @param upHoveringFace is hovering actions enabled for this button
     * @param upHoveringImage to be displayed if hovering action is enabled
     */
    private void setButton(PushButton button, Image image, boolean upHoveringFace, Image upHoveringImage) {
        if (upHoveringFace) {
            button.getUpHoveringFace().setImage(upHoveringImage);
        } else {
            button.getUpHoveringFace().setImage(image);
        }
        button.getUpFace().setImage(image);
        button.getDownFace().setImage(image);
        button.getDownHoveringFace().setImage(image);
    }

    /**
     * Logs out and user is forwarded to HomeWelcomModule.
     */
    public void onLogout(final int widgetToLoad) {
//        startStopUserTimer(false);
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

    /**
     * Starts or stops timer for Client or Supplier based on the user's role which is logged in.
     * @param start true if Timer should be started, false if timer should be stopped
     */
    private void startStopUserTimer(boolean start) {
        if (Storage.getBusinessUserDetail().getBusinessRoles().contains(
                BusinessUserDetail.BusinessRole.SUPPLIER)) {
            // timer for supplier
            if (start) {
                eventBus.startSupplierNotificationTimer();
            } else {
                eventBus.stopSupplierNotificationTimer();
            }
        } else {
            if (Storage.getBusinessUserDetail().getBusinessRoles().contains(
                BusinessUserDetail.BusinessRole.CLIENT)) {
                // timer for client
                if (start) {
                    eventBus.startClientNotificationTimer();
                } else {
                    eventBus.stopClientNotificationTimer();
                }
            }
        }
    }

}