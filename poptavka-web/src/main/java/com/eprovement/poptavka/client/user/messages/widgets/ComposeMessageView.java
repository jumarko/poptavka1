package com.eprovement.poptavka.client.user.messages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;

/**
 *
 * @author martin slavkovsky
 *
 */
public class ComposeMessageView extends Composite implements ComposeMessagePresenter.IComposeMessage {

    private static ComposeMessageViewUiBinder uiBinder = GWT.create(ComposeMessageViewUiBinder.class);

    interface ComposeMessageViewUiBinder extends UiBinder<Widget, ComposeMessageView> {
    }
    //table handling buttons
    @UiField
    Button sendBtn, discardBtn;
    @UiField
    TextBox recipient, subject;
    @UiField
    TextArea body;
    @UiField
    HTMLPanel wrapper;

    @Override
    public void clearMessage() {
        recipient.setText("");
        subject.setText("");
        body.setText("");
    }

    @Override
    public MessageDetail getMessage(MessageDetail detail) {
        detail.setSenderId(Storage.getUser().getUserId());
        detail.setSubject(subject.getText());
        detail.setBody(body.getText());
        return detail;
    }

    @Override
    public TextBox getRecipientTextBox() {
        return recipient;
    }

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public Button getSendBtn() {
        return sendBtn;
    }

    @Override
    public Button getDiscardBtn() {
        return discardBtn;
    }

    @Override
    public HTMLPanel getWrapperPanel() {
        return wrapper;
    }
}
