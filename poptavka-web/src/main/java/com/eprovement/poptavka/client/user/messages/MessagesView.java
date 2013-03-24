package com.eprovement.poptavka.client.user.messages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.resources.StyleResource;

public class MessagesView extends Composite
        implements MessagesPresenter.MessagesLayoutInterface {

    private static MessagesModuleViewUiBinder uiBinder = GWT.create(MessagesModuleViewUiBinder.class);

    interface MessagesModuleViewUiBinder extends UiBinder<Widget, MessagesView> {
    }
    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    @UiField SimplePanel contentPanel, footerHolder;
    @UiField Button menuMessagesInboxBtn;

    /**************************************************************************/
    /* Initialization                                                            */
    /**************************************************************************/
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        StyleResource.INSTANCE.common().ensureInjected();
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /** Panels. **/
    @Override
    public SimplePanel getContentPanel() {
        return contentPanel;
    }

    @Override
    public SimplePanel getFooterHolder() {
        return footerHolder;
    }

    /** Buttons. **/
    @Override
    public Button getMessagesInbox() {
        return menuMessagesInboxBtn;
    }

    /** Other. **/
    @Override
    public Widget getWidgetView() {
        return this;
    }
}
