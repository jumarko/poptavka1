/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.messages;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.CssInjector;
import com.eprovement.poptavka.client.user.messages.toolbar.MessagesToolbarView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * Messages view consists of texboxes/attributes that can be used as sorting filter.
 * @author Martin Slavkovsky
 */
public class MessagesView extends Composite
        implements MessagesPresenter.MessagesLayoutInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static MessagesModuleViewUiBinder uiBinder = GWT.create(MessagesModuleViewUiBinder.class);

    interface MessagesModuleViewUiBinder extends UiBinder<Widget, MessagesView> {
    }

    /**************************************************************************/
    /* CSS                                                                    */
    /**************************************************************************/
    static {
        CssInjector.INSTANCE.ensureCommonStylesInjected();
    }

    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField SimplePanel contentContainer;
    @UiField Button menuMessagesInboxBtn;
    /** UiBinder attributes. **/
    @Inject
    private MessagesToolbarView toolbar;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates Messages view's compontents.
     */
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    /**
     * Loads right styles to menu buttons.
     * @param loadedWidget - use module constants from class Contants.
     */
    @Override
    public void setMessagesMenuStyleChange(int loadedWidget) {
        switch (loadedWidget) {
            case Constants.MESSAGES_INBOX:
                menuMessagesInboxBtn.addStyleName(Constants.ACT);
                break;
            default:
                break;
        }
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /** Panels. **/
    /**
     * @return the content container
     */
    @Override
    public SimplePanel getContentContainer() {
        return contentContainer;
    }

    /** Buttons. **/
    /**
     * @return the messages inbox button
     */
    @Override
    public Button getMessagesInbox() {
        return menuMessagesInboxBtn;
    }

    /**
     * @return the custom toolbar content
     */
    @Override
    public Widget getToolbarContent() {
        return toolbar;
    }

    /** Other. **/
    /**
     * @return the widget view
     */
    @Override
    public Widget getWidgetView() {
        return this;
    }
}