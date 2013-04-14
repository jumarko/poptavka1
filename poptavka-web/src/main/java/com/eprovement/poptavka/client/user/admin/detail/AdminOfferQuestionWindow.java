package com.eprovement.poptavka.client.user.admin.detail;

import com.eprovement.poptavka.client.common.ValidationMonitor;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.OfferMessageDetail;
import com.github.gwtbootstrap.client.ui.FluidRow;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import javax.validation.groups.Default;

/**
 * Mixed widget for sending offer as well for asking questions.
 *
 * @author Beho
 * @author Martin Slavkovsky
 */
public class AdminOfferQuestionWindow extends Composite implements ProvidesValidate {

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    private static AdminOfferQuestionUiBinder uiBinder = GWT.create(AdminOfferQuestionUiBinder.class);

    interface AdminOfferQuestionUiBinder extends UiBinder<Widget, AdminOfferQuestionWindow> {
    }
    /** Constants. **/
    public static final String EMPTY = "";
    /** UiBinder attributes. **/
    @UiField(provided = true) ValidationMonitor bodyMonitor;
    @UiField Button questionReplyBtn, submitBtn, cancelBtn;
    @UiField FluidRow header, messageBodyRow, messageButtonsRow;
    @UiField Label sender, sent, body;
    /** Class attributes. **/
    private int selectedResponse;
    private MessageDetail replyToMessage;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    public AdminOfferQuestionWindow() {
        initValidationMonitors();
        initWidget(uiBinder.createAndBindUi(this));

        Storage.RSCS.detailViews().ensureInjected();
    }

    private void initValidationMonitors() {
        bodyMonitor = new ValidationMonitor<OfferMessageDetail>(
                OfferMessageDetail.class, Default.class, OfferMessageDetail.MessageField.BODY.getValue());
    }

    /**************************************************************************/
    /* UiHandlers                                                             */
    /**************************************************************************/
    @UiHandler("questionReplyBtn")
    public void questionReplyBtnHandler(ClickEvent event) {
        resetValidationStyles();
        setSendingQuestionStyle();
    }

    @UiHandler("cancelBtn")
    public void cancelBtnHandler(ClickEvent event) {
        setDefaultStyle();
    }

    /**************************************************************************/
    /* SETTER                                                                 */
    /**************************************************************************/
    public void setSendingQuestionStyle() {
        header.setVisible(false);
        messageBodyRow.setVisible(true);
        messageButtonsRow.setVisible(true);
    }

    public void setDefaultStyle() {
        header.setVisible(true);
        messageBodyRow.setVisible(false);
        messageButtonsRow.setVisible(false);
        bodyMonitor.setValue(EMPTY);
    }

    private void resetValidationStyles() {
        bodyMonitor.reset();
    }

    /**************************************************************************/
    /* GETTER of created messages.                                            */
    /**************************************************************************/
    public MessageDetail getCreatedMessage() {
        MessageDetail message = new MessageDetail();
        message.setBody((String) bodyMonitor.getValue());
        return message;
    }

    /**************************************************************************/
    /* GETTER                                                                 */
    /**************************************************************************/
    public Button getSubmitBtn() {
        return submitBtn;
    }

    public int getSelectedResponse() {
        return selectedResponse;
    }

    @Override
    public boolean isValid() {
        return bodyMonitor.isValid();
    }

    public Widget getWidgetView() {
        return this;
    }

    /**************************************************************************/
    /* Conversation Methods                                                   */
    /**************************************************************************/
    public MessageDetail getMessage() {
        return replyToMessage;
    }

    public void setMessage(MessageDetail message) {
        String cssColor;
        if (Storage.getUser().getUserId() == message.getSenderId()) {
            cssColor = Storage.RSCS.detailViews().conversationDetailHeaderRed();
        } else {
            cssColor = Storage.RSCS.detailViews().conversationDetailHeaderGreen();
        }
        sender.addStyleName(cssColor);

        this.replyToMessage = message;
        sender.setText(message.getSenderName());
        if (message.getSent() != null) {
            sent.setText(Storage.DATE_FORMAT.format(message.getSent()));
        }
        body.setText(message.getBody());
    }

    /**
     * Received the message being sent and fills it with necessary attributes, from stored message.
     *
     * @param MessageDetail message being sent
     * @return updated message
     */
    public MessageDetail updateSendingMessage(MessageDetail messageDetail) {
        messageDetail.setThreadRootId(replyToMessage.getThreadRootId());
        messageDetail.setParentId(replyToMessage.getMessageId());
        return messageDetail;
    }

    public void clear() {
        messageBodyRow.setVisible(false);
        messageButtonsRow.setVisible(false);
    }
}
