/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.root.header;

import com.eprovement.poptavka.client.common.CommonAccessRoles;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.client.root.interfaces.IHeaderView;
import com.eprovement.poptavka.client.root.interfaces.IHeaderView.IHeaderPresenter;
import com.eprovement.poptavka.client.user.admin.interfaces.IAdminModule;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PushButton;

/**
 * Header presenter.
 *
 * @author Ivan Vlcek, Martin Slavkovsky
 */
@Presenter(view = HeaderView.class)
public class HeaderPresenter extends BasePresenter<IHeaderView, RootEventBus>
    implements IHeaderPresenter {

    //See usage. In this way style are overriden correctly, not need to use !important
    private static final String USER = "user";
    private static final String ADMIN = "admin";
    private boolean isMenuVisible = false;

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        eventBus.setHeader(view);
    }

    /**************************************************************************/
    /* Layout events.                                                         */
    /**************************************************************************/
    /**
     * Sets header for unlogged user.
     */
    public void onAtHome() {
        view.getLogin().setVisible(true);
        view.getLogout().setVisible(false);
        view.getNotifications().setVisible(false);
        view.getMenu().removeStyleName(ADMIN);
        view.getSearch().getSearchPanel().removeStyleName(ADMIN);
        view.getSearch().getSearchButton().removeStyleName(ADMIN);
        view.getMenu().removeStyleName(USER);
        view.getSearch().getSearchPanel().removeStyleName(USER);
        view.getSearch().getSearchButton().removeStyleName(USER);
        view.getSettingsAnchor().setVisible(false);
    }

    /**
     * Sets header for logged user.
     */
    public void onAtAccount() {
        view.getLogin().setVisible(false);
        view.getLogout().setVisible(true);
        view.getNotifications().setVisible(true);
        if (Storage.getUser().getAccessRoles().contains(CommonAccessRoles.ADMIN)) {
            view.getMenu().addStyleName(ADMIN);
            view.getSearch().getSearchPanel().addStyleName(ADMIN);
            view.getSearch().getSearchButton().addStyleName(ADMIN);
        } else {
            eventBus.requestCreditCount(Storage.getUser().getUserId());
            view.getMenu().addStyleName(USER);
            view.getSearch().getSearchPanel().addStyleName(USER);
            view.getSearch().getSearchButton().addStyleName(USER);
        }
        view.getSettingsAnchor().setVisible(true);
        view.getLogout().getUsername().setText(Storage.getUser().getEmail());
    }

    /**************************************************************************/
    /* Business events.                                                       */
    /**************************************************************************/
    /**
     * Sets menu widget to header.
     * There are two types of menu - home menu for unlogged users and user menu for logged users.
     * @param menu widget
     */
    public void onSetMenu(IsWidget menu) {
        GWT.log("Menu widget set");
        view.getMenu().setWidget(menu);
    }

    /**
     * Sets search widget to header.
     * @param searchBar widget
     */
    public void onSetSearchBar(IsWidget searchBar) {
        GWT.log("Search bar widget set");
        view.getSearch().setSearchBar(searchBar);
    }

    /**************************************************************************/
    /* Bind                                                                   */
    /**************************************************************************/
    @Override
    public void bind() {
        view.getLogo().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (Storage.getUser() == null) {
                    eventBus.menuStyleChange(Constants.HOME_WELCOME_MODULE);
                } else {
                    eventBus.menuStyleChange(Constants.HOME_WELCOME_MODULE);
                }
                eventBus.goToHomeWelcomeModule();
            }
        });
        bindLoginHandlers();
        bindLogoutHandlers();
        bindSearchHandlers();
        bindSettingsHandlers();
        bindNotificationHandlers();
    }

    /**
     * Bind login handlers. Registers two handlers:
     * <ul>
     *    <li>one for <b>button</b> on middle-large screens</li>
     *    <li>one for <b>icon anchor</b> on tiny-small screens</li>
     * </ul>
     */
    private void bindLoginHandlers() {
        view.getLogin().getLoginButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.login(Constants.NONE);
            }
        });
        view.getLogin().getLoginButtonSmall().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.login(Constants.NONE);
            }
        });
    }

    /**
     * Bind logout handlers.
     * Register tree handlers:
     * <ul>
     *    <li>one for <b>button</b> on middle-large screens</li>
     *    <li>one for <b>menu - settings</b> on middle-large screens</li>
     * </ul>
     */
    private void bindLogoutHandlers() {
        view.getLogout().getMenuLogOut().setScheduledCommand(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                eventBus.menuStyleChange(Constants.HOME_WELCOME_MODULE);
                eventBus.logout(Constants.HOME_WELCOME_MODULE);
            }
        });
        view.getLogout().getMenuMyProfile().setScheduledCommand(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                eventBus.menuStyleChange(Constants.USER_SETTINGS_MODULE);
                eventBus.goToSettingsModule();
            }
        });
    }

    /**
     * Bind search handlers.
     */
    private void bindSearchHandlers() {
        view.getSearch().getSearchButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.showAdvancedSearchPopup();
            }
        });
    }

    /**
     * Bind settings handlers.
     * On tiny-small screens login button with dropdown menu is not visible, therefore
     * display settings icon anchor for accessing Settings/My Profile.
     */
    private void bindSettingsHandlers() {
        view.getSettingsAnchor().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.menuStyleChange(Constants.USER_SETTINGS_MODULE);
                eventBus.goToSettingsModule();
            }
        });
    }

    /**
     * Bind notification handlers.
     * <ul>
     *   <li>one for <b>system messages</b></li>
     *   <li>one for <b>messages</b></li>
     * </ul>
     */
    private void bindNotificationHandlers() {
        view.getNotifications().getPushButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                //TODO Martin - ADMIN is not a business role isn't it?
                if (Storage.getBusinessUserDetail().getBusinessRoles().contains(
                    BusinessUserDetail.BusinessRole.ADMIN)) {
                    eventBus.goToAdminModule(null, IAdminModule.AdminWidget.DASHBOARD);
                } else if (Storage.getBusinessUserDetail().getBusinessRoles().contains(
                    BusinessUserDetail.BusinessRole.SUPPLIER)) {
                    eventBus.goToSupplierDemandsModule(null, Constants.NONE);
                } else {
                    eventBus.goToClientDemandsModule(null, Constants.NONE);
                }
            }
        });
        view.getNotifications().getPushSystemButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToMessagesModule(null, Constants.NONE);
            }
        });
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * Loads notification icons with adequate numbers of unread messages for user header section.
     * @param numberOfMessages carrying number of unread demand and system messages
     */
    public void onSetUpdatedUnreadMessagesCount(UnreadMessagesDetail numberOfMessages) {
        if (numberOfMessages.getUnreadMessagesCount() == 0) {
            view.getNotifications().getNewMessagesCount().setText("");
            setButton(view.getNotifications().getPushButton(),
                new Image(Storage.RSCS.images().envelopeImageEmpty()), false, null);
        } else {
            view.getNotifications().getNewMessagesCount().setText(
                String.valueOf(numberOfMessages.getUnreadMessagesCount()));
            setButton(view.getNotifications().getPushButton(), new Image(Storage.RSCS.images().envelopeImage()), true,
                new Image(Storage.RSCS.images().envelopeHoverImage()));
        }
        if (numberOfMessages.getUnreadSystemMessageCount() == 0) {
            view.getNotifications().getNewSystemMessagesCount().setText("");
            setButton(view.getNotifications().getPushSystemButton(),
                new Image(Storage.RSCS.images().flagImageEmpty()), false, null);
        } else {
            view.getNotifications().getNewSystemMessagesCount().setText(
                String.valueOf(numberOfMessages.getUnreadSystemMessageCount()));
            setButton(view.getNotifications().getPushSystemButton(), new Image(Storage.RSCS.images().flagImage()), true,
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
     * Sets credit widget status
     * @param credit - user's current credits.
     */
    public void onResponseCreditCount(Integer credit) {
        if (credit == 0) {
            view.getNotifications().getCreditCount().setText("");
            setButton(view.getNotifications().getCreditButton(),
                new Image(Storage.RSCS.images().creditCardImageEmpty()), false, null);
        } else {
            view.getNotifications().getCreditCount().setText(String.valueOf(credit));
            setButton(view.getNotifications().getCreditButton(), new Image(Storage.RSCS.images().creditCardImage()), true,
                new Image(Storage.RSCS.images().creditCardHoverImage()));
        }
    }
}