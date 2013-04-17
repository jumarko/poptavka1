package com.eprovement.poptavka.client.user.widget.detail;

import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MessageDetailView extends Composite {

    private static MessageDetailViewUiBinder uiBinder = GWT.create(MessageDetailViewUiBinder.class);

    interface MessageDetailViewUiBinder extends UiBinder<Widget, MessageDetailView> {
    }
    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField
    Label subject, sender, body;
    /** Class attributes. **/
    private static final String EMPTY = "";

    /**************************************************************************/
    /* INITIALIZATON                                                          */
    /**************************************************************************/
    public MessageDetailView() {
        initWidget(uiBinder.createAndBindUi(this));

        StyleResource.INSTANCE.detailViews().ensureInjected();
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    public void setMessageDetail(MessageDetail messageDetail) {
        GWT.log("detail detail" + messageDetail.toString());
        subject.setText(messageDetail.getSubject());
        sender.setText(messageDetail.getSender());
        body.setText(messageDetail.getBody());
    }

    public void clear() {
        subject.setText(EMPTY);
        sender.setText(EMPTY);
        body.setText(EMPTY);
    }
}
