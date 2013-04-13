package com.eprovement.poptavka.client.user.messages;

import com.eprovement.poptavka.client.root.footer.FooterView;
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
    @UiField(provided = true) Widget footer;
    @UiField SimplePanel contentPanel;
    @UiField Button menuMessagesInboxBtn;
    /** Class attributes. **/
    private @Inject FooterView footerView;

    /**************************************************************************/
    /* Initialization                                                            */
    /**************************************************************************/
    @Override
    public void createView() {
        footer = footerView;
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
