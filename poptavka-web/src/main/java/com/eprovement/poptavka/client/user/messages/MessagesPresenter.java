/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.messages;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.root.toolbar.ProvidesToolbar;
import com.eprovement.poptavka.client.user.messages.MessagesPresenter.MessagesLayoutInterface;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.mvp4g.client.history.NavigationConfirmationInterface;
import com.mvp4g.client.history.NavigationEventCommand;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

/**
 * Messages presenter
 * @author Martin Slavkovsky
 */
@Presenter(view = MessagesView.class)
public class MessagesPresenter
        extends LazyPresenter<MessagesLayoutInterface, MessagesEventBus>
        implements NavigationConfirmationInterface {

    /**************************************************************************/
    /* View interface                                                         */
    /**************************************************************************/
    public interface MessagesLayoutInterface extends LazyView, IsWidget, ProvidesToolbar {

        void setMessagesMenuStyleChange(int loadedWidget);

        SimplePanel getContentContainer();

        Button getMessagesInbox();

        Widget getWidgetView();
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private SearchModuleDataHolder filter = null;

    /**************************************************************************/
    /* Bind                                                                   */
    /**************************************************************************/
    /**
     * Binds menu buttons handlers
     */
    @Override
    public void bindView() {
        view.getMessagesInbox().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.closeSubMenu();
                eventBus.goToMessagesModule(filter, Constants.MESSAGES_INBOX);
            }
        });
    }

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        // nothing by default
    }

    /**
     * Every call of onForward method invokes updateUnreadMessagesCount event that is secured thus user without
     * particular access role can't access it and loginPopupView will be displayed.
     * Sets body, toolbar.
     */
    public void onForward() {
        //Must be set before any widget start initialize because of autoDisplay feature
        if (!(Storage.getUser() == null && Storage.isAppCalledByURL() != null && Storage.isAppCalledByURL())) {
            eventBus.updateUnreadMessagesCount();
        }
        eventBus.setBody(view.getWidgetView());
        eventBus.setToolbarContent("Inbox", view.getToolbarContent(), true);
        eventBus.menuStyleChange(Constants.USER_MESSAGES_MODULE);
    }

    @Override
    public void confirm(NavigationEventCommand event) {
        // nothing by default
    }

    /**************************************************************************/
    /* Business events handled by presenter                                   */
    /**************************************************************************/
    /**
     * Initialize view for message module.
     * @param filter
     */
    public void onGoToMessagesModule(SearchModuleDataHolder filter, int loadWidget) {
        this.filter = filter;

        eventBus.initInbox(filter);
    }

    /**
     * Sets Messages widget to MessagesModule content.
     * @param content widget
     */
    public void onDisplayView(IsWidget content) {
        view.getContentContainer().setWidget(content);
    }

    /**
     * Set active style for menu styles.
     * @param loadedWidget
     */
    public void onMessagesMenuStyleChange(int loadedWidget) {
        view.setMessagesMenuStyleChange(loadedWidget);
    }
}