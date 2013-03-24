package com.eprovement.poptavka.client.user.admin.detail;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.OfferMessageDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

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
    @UiField Anchor questionReplyBtn, submitBtn, cancelBtn;
    @UiField TextArea replyTextArea;
    @UiField DivElement header, messageBody, messagePanel;
    @UiField Label errorLabelText;
    @UiField Label sender, sent, body;
    /** Class attributes. **/
    private int selectedResponse;
    private MessageDetail replyToMessage;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    public AdminOfferQuestionWindow() {
        Storage.RSCS.detailViews().ensureInjected();
        initWidget(uiBinder.createAndBindUi(this));
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
        header.getStyle().setDisplay(Display.NONE);
        messageBody.getStyle().setDisplay(Display.BLOCK);
    }

    public void setDefaultStyle() {
        messageBody.getStyle().setDisplay(Display.NONE);
        header.getStyle().setDisplay(Display.BLOCK);
        replyTextArea.setText(EMPTY);
    }

    private void resetValidationStyles() {
        replyTextArea.removeStyleName(Storage.RSCS.common().errorField());
        errorLabelText.setText(EMPTY);
    }

    /**************************************************************************/
    /* GETTER of created messages.                                            */
    /**************************************************************************/
    public MessageDetail getCreatedMessage() {
        MessageDetail message = new MessageDetail();
        message.setBody(replyTextArea.getText());
        return message;
    }

    /**************************************************************************/
    /* GETTER                                                                 */
    /**************************************************************************/
    public Anchor getSubmitBtn() {
        return submitBtn;
    }

    public int getSelectedResponse() {
        return selectedResponse;
    }

    @Override
    public boolean isValid() {
        resetValidationStyles();
        if (replyTextArea.getText().isEmpty()) {
            replyTextArea.addStyleName(Storage.RSCS.common().errorField());
            errorLabelText.setText(Storage.VMSGS.messageNotEmptyBody());
            return false;
        }
        return true;
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
        String cssBall;
        String cssColor;
        if (Storage.getUser().getUserId() == message.getSenderId()) {
            cssBall = Storage.RSCS.detailViews().conversationDetailRed();
            cssColor = Storage.RSCS.detailViews().conversationDetailHeaderRed();
        } else {
            cssBall = Storage.RSCS.detailViews().conversationDetailGreen();
            cssColor = Storage.RSCS.detailViews().conversationDetailHeaderGreen();
        }
        messagePanel.addClassName(cssBall);
        sender.addStyleName(cssColor);

        messagePanel.getStyle().setDisplay(Display.BLOCK);
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
    public OfferMessageDetail updateSendingOfferMessage(OfferMessageDetail messageDetail) {
        messageDetail.setThreadRootId(replyToMessage.getThreadRootId());
        messageDetail.setParentId(replyToMessage.getMessageId());
        messageDetail.setSupplierId(Storage.getBusinessUserDetail().getSupplierId());
        return messageDetail;
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
        messagePanel.getStyle().setDisplay(Display.NONE);
    }
}
