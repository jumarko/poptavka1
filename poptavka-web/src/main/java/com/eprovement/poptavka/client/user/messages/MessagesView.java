package com.eprovement.poptavka.client.user.messages;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.user.messages.toolbar.MessagesToolbarView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.resources.StyleResource;
import com.google.inject.Inject;

public class MessagesView extends Composite
        implements MessagesPresenter.MessagesLayoutInterface {

    private static MessagesModuleViewUiBinder uiBinder = GWT.create(MessagesModuleViewUiBinder.class);

    interface MessagesModuleViewUiBinder extends UiBinder<Widget, MessagesView> {
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
    /* Initialization                                                            */
    /**************************************************************************/
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));

        StyleResource.INSTANCE.common().ensureInjected();
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
    @Override
    public SimplePanel getContentContainer() {
        return contentContainer;
    }

    /** Buttons. **/
    @Override
    public Button getMessagesInbox() {
        return menuMessagesInboxBtn;
    }

    @Override
    public Widget getToolbarContent() {
        return toolbar;
    }

    /** Other. **/
    @Override
    public Widget getWidgetView() {
        return this;
    }
}