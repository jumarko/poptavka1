package com.eprovement.poptavka.client.user.widget;

import com.eprovement.poptavka.client.common.BigDecimalBox;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.OfferMessageDetail;
import com.github.gwtbootstrap.client.ui.FluidContainer;
import com.github.gwtbootstrap.client.ui.FluidRow;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * Mixed widget for sending offer as well for asking questions.
 *
 * @author Beho
 * @author Martin Slavkovsky
 */
public class OfferQuestionWindow extends Composite implements ProvidesValidate {

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    private static ReplyWindowUiBinder uiBinder = GWT.create(ReplyWindowUiBinder.class);

    interface ReplyWindowUiBinder extends UiBinder<Widget, OfferQuestionWindow> {
    }
    /** Constants. **/
    public static final int RESPONSE_OFFER = 0;
    public static final int RESPONSE_QUESTION = 1;
    public static final String EMPTY = "";
    /** UiBinder attributes. **/
    @UiField Anchor offerReplyBtn, questionReplyBtn;
    @UiField Anchor submitBtn, cancelBtn;
    @UiField TextArea replyTextArea;
    @UiField BigDecimalBox priceBox;
    @UiField DateBox dateBox;
    @UiField HTMLPanel header, messagePanel;
    @UiField FluidContainer messageBody;
    @UiField FluidRow priceRow, finnishDateRow;
    @UiField Label errorLabelText, errorLabelPrice, errorLabelDate;
    @UiField Label sender, sent, body;
    /** Class attributes. **/
    private int selectedResponse;
    private MessageDetail replyToMessage;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    public OfferQuestionWindow() {
        Storage.RSCS.detailViews().ensureInjected();
        initWidget(uiBinder.createAndBindUi(this));
        offerReplyBtn.setVisible(false);
    }

    /**************************************************************************/
    /* UiHandlers                                                             */
    /**************************************************************************/
    @UiHandler("offerReplyBtn")
    public void offerReplyBtnHandler(ClickEvent event) {
        resetValidationStyles();
        setSendingOfferStyle();
        selectedResponse = RESPONSE_OFFER;
    }

    @UiHandler("questionReplyBtn")
    public void questionReplyBtnHandler(ClickEvent event) {
        resetValidationStyles();
        setSendingQuestionStyle();
        selectedResponse = RESPONSE_QUESTION;
    }

    @UiHandler("cancelBtn")
    public void cancelBtnHandler(ClickEvent event) {
        setDefaultStyle();
    }

    /**************************************************************************/
    /* SETTER                                                                 */
    /**************************************************************************/
    public void setSendingOfferStyle() {
        header.setVisible(false);
        messageBody.setVisible(true);
        priceRow.setVisible(true);
        finnishDateRow.setVisible(true);
    }

    public void setSendingQuestionStyle() {
        header.setVisible(false);
        messageBody.setVisible(true);
    }

    public void setDefaultStyle() {
        header.setVisible(true);
        messageBody.setVisible(false);
        priceRow.setVisible(false);
        finnishDateRow.setVisible(false);
        replyTextArea.setText(EMPTY);
    }

    private void resetValidationStyles() {
        replyTextArea.removeStyleName(Storage.RSCS.common().errorField());
        errorLabelText.setText(EMPTY);
        priceBox.removeStyleName(Storage.RSCS.common().errorField());
        errorLabelPrice.setText(EMPTY);
        dateBox.removeStyleName(Storage.RSCS.common().errorField());
        errorLabelDate.setText(EMPTY);
    }

    public void allowSendingOffer() {
        offerReplyBtn.setVisible(true);
    }

    /**************************************************************************/
    /* GETTER of created messages.                                            */
    /**************************************************************************/
    public MessageDetail getCreatedMessage() {
        MessageDetail message = new MessageDetail();
        message.setBody(replyTextArea.getText());
        return message;
    }

    public OfferMessageDetail getCreatedOfferMessage() {
        OfferMessageDetail offerMessageDetailImpl = new OfferMessageDetail();
        offerMessageDetailImpl.setPrice(priceBox.getValue());
        offerMessageDetailImpl.setOfferFinishDate(dateBox.getValue());
        offerMessageDetailImpl.setBody(replyTextArea.getText());
        return offerMessageDetailImpl;
    }

    /**************************************************************************/
    /* GETTER                                                                 */
    /**************************************************************************/
    public Anchor getOfferReplyBtn() {
        return offerReplyBtn;
    }

    public Anchor getSubmitBtn() {
        return submitBtn;
    }

    public int getSelectedResponse() {
        return selectedResponse;
    }

    @Override
    public boolean isValid() {
        resetValidationStyles();
        int errorCount = 0;
        if (replyTextArea.getText().isEmpty()) {
            replyTextArea.addStyleName(Storage.RSCS.common().errorField());
            errorLabelText.setText(Storage.VMSGS.messageNotEmptyBody());
            errorCount++;
        }
        if (selectedResponse == RESPONSE_OFFER) {
            if (priceBox.getText().isEmpty()) {
                priceBox.addStyleName(Storage.RSCS.common().errorField());
                errorLabelPrice.setText(Storage.VMSGS.messageNotEmptyPrice());
                errorCount++;
            }
            try {
                priceBox.getValue();
            } catch (NumberFormatException ex) {
                priceBox.addStyleName(Storage.RSCS.common().errorField());
                errorLabelPrice.setText(Storage.VMSGS.messageInvalidPrice());
                errorCount++;
            }
            if (dateBox.getValue() == null) {
                priceBox.addStyleName(Storage.RSCS.common().errorField());
                errorLabelDate.setText(Storage.VMSGS.messageNotEmptyDate());
                errorCount++;
            }
        }
        return errorCount == 0;
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
        messagePanel.setStyleName(cssBall);
        sender.addStyleName(cssColor);

        messagePanel.setVisible(true);
        this.replyToMessage = message;
        sender.setText(message.getSenderName());
        sent.setText(Storage.FORMATTER.format(message.getSent()));
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
        messagePanel.setVisible(false);
    }
}
