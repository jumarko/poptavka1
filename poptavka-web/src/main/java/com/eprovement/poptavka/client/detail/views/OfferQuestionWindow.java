/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.detail.views;

import com.eprovement.poptavka.client.common.ui.WSBigDecimalBox;
import com.eprovement.poptavka.client.common.ui.WSDateBox;
import com.eprovement.poptavka.client.common.monitors.ValidationMonitor;
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
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Mixed widget for sending offer as well for asking questions.
 *
 * @author Beho
 * @author Martin Slavkovsky
 */
public class OfferQuestionWindow extends Composite implements ProvidesValidate {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static ReplyWindowUiBinder uiBinder = GWT.create(ReplyWindowUiBinder.class);

    interface ReplyWindowUiBinder extends UiBinder<Widget, OfferQuestionWindow> {
    }

    /**************************************************************************/
    /* CSS                                                                    */
    /**************************************************************************/
    static {
        Storage.RSCS.details().ensureInjected();
    }

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) ValidationMonitor bodyMonitor, priceMonitor, finishDateMonitor;
    @UiField Button offerReplyBtn, questionReplyBtn;
    @UiField Button submitBtn, cancelBtn;
    @UiField HTMLPanel header, messageBody;
    @UiField FluidRow priceRow, finishDateRow;
    @UiField Label sender, sent, body;
    /** Class attributes. **/
    private int selectedResponse;
    private MessageDetail replyToMessage;
    /** Constants. **/
    public static final int RESPONSE_OFFER = 0;
    public static final int RESPONSE_QUESTION = 1;
    public static final String EMPTY = "";

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    /**
     * Creates offer question view's components.
     */
    public OfferQuestionWindow() {
        initValidationMonitors();
        initWidget(uiBinder.createAndBindUi(this));
        offerReplyBtn.setVisible(false);
    }

    /**
     * Initialze validation monitors.
     */
    private void initValidationMonitors() {
        bodyMonitor = new ValidationMonitor<OfferMessageDetail>(
                OfferMessageDetail.class, OfferMessageDetail.MessageField.BODY.getValue());
        priceMonitor = new ValidationMonitor<OfferMessageDetail>(
                OfferMessageDetail.class, OfferMessageDetail.MessageField.PRICE.getValue());
        finishDateMonitor = new ValidationMonitor<OfferMessageDetail>(
                OfferMessageDetail.class, OfferMessageDetail.MessageField.FINISH_DATE.getValue());
    }


    /**************************************************************************/
    /* UiHandlers                                                             */
    /**************************************************************************/
    /**
     * Binds handler for <b>offer</b> reply button.
     */
    @UiHandler("offerReplyBtn")
    public void offerReplyBtnHandler(ClickEvent event) {
        resetValidationStyles();
        setSendingOfferStyle();
        selectedResponse = RESPONSE_OFFER;
    }

    /**
     * Binds handler for <b>question</b> reply button.
     */
    @UiHandler("questionReplyBtn")
    public void questionReplyBtnHandler(ClickEvent event) {
        resetValidationStyles();
        setSendingQuestionStyle();
        selectedResponse = RESPONSE_QUESTION;
    }

    /**
     * Binds handler for <b>cancel</b> button.
     */
    @UiHandler("cancelBtn")
    public void cancelBtnHandler(ClickEvent event) {
        setDefaultStyle();
    }

    /**************************************************************************/
    /* SETTER                                                                 */
    /**************************************************************************/
    /**
     * Sets sending <b>offer</b> styles and widget layout.
     */
    public void setSendingOfferStyle() {
        header.setVisible(false);
        messageBody.setVisible(true);
        priceRow.setVisible(true);
        finishDateRow.setVisible(true);
        submitBtn.setEnabled(true);
        ((TextArea) bodyMonitor.getWidget()).setFocus(true);
    }

    /**
     * Sets sending <b>question</b> styles and widget layout.
     */
    public void setSendingQuestionStyle() {
        header.setVisible(false);
        messageBody.setVisible(true);
        priceRow.setVisible(false);
        finishDateRow.setVisible(false);
        submitBtn.setEnabled(true);
        ((TextArea) bodyMonitor.getWidget()).setFocus(true);
    }

    /**
     * Sets default styles.
     */
    public void setDefaultStyle() {
        header.setVisible(true);
        messageBody.setVisible(false);
        priceRow.setVisible(false);
        finishDateRow.setVisible(false);
        ((WSBigDecimalBox) priceMonitor.getWidget()).setText(EMPTY);
        ((WSDateBox) finishDateMonitor.getWidget()).getTextBox().setText(EMPTY);
        bodyMonitor.setValue(EMPTY);
    }

    /**
     * Reset validation styles.
     */
    private void resetValidationStyles() {
        bodyMonitor.resetValidation();
        priceMonitor.resetValidation();
        finishDateMonitor.resetValidation();
    }

    /**
     * Enables sending offer.
     * @param enable true if sending offer should be enabled, false otherwise
     */
    public void setSendingOfferEnabled(boolean enable) {
        offerReplyBtn.setVisible(enable);
    }

    /**************************************************************************/
    /* GETTER of created messages.                                            */
    /**************************************************************************/
    /**
     * Get question message.
     * @return created question message
     */
    public MessageDetail getCreatedMessage() {
        MessageDetail message = new MessageDetail();
        message.setBody((String) bodyMonitor.getValue());
        return message;
    }

    /**
     * Get offer message
     * @return created offer message
     */
    public OfferMessageDetail getCreatedOfferMessage() {
        OfferMessageDetail offerMessageDetailImpl = new OfferMessageDetail();
        offerMessageDetailImpl.setPrice((BigDecimal) priceMonitor.getValue());
        offerMessageDetailImpl.setFinishDate((Date) finishDateMonitor.getValue());
        offerMessageDetailImpl.setBody((String) bodyMonitor.getValue());
        return offerMessageDetailImpl;
    }

    /**************************************************************************/
    /* GETTER                                                                 */
    /**************************************************************************/
    /**
     * @return the offer reply button
     */
    public Button getOfferReplyBtn() {
        return offerReplyBtn;
    }

    /**
     * @return the submit button
     */
    public Button getSubmitBtn() {
        return submitBtn;
    }

    /**
     * @return 0 for RESPONSE_OFFER, 1 for RESPONSE_QUESTION
     */
    public int getSelectedResponse() {
        return selectedResponse;
    }

    /**
     * Validate view's components.
     * @return true if components are valid, false otherwise
     */
    @Override
    public boolean isValid() {
        boolean valid = bodyMonitor.isValid();
        if (selectedResponse == RESPONSE_OFFER) {
            valid = priceMonitor.isValid() && valid;
            valid = finishDateMonitor.isValid() && valid;
        }
        return valid;
    }

    /**
     * @return the widget view
     */
    public Widget getWidgetView() {
        return this;
    }

    /**************************************************************************/
    /* Conversation Methods                                                   */
    /**************************************************************************/
    /**
     * @return the message detail
     */
    public MessageDetail getMessage() {
        return replyToMessage;
    }

    /**
     * Sets message detail.
     * @param message to be set
     */
    public void setMessage(MessageDetail message) {
        if (Storage.getUser().getUserId() == message.getSenderId()) {
            sender.setStyleName(Storage.RSCS.details().conversationDetailHeaderRed());
        } else {
            sender.setStyleName(Storage.RSCS.details().conversationDetailHeaderGreen());
        }

        this.replyToMessage = message;
        sender.setText(message.getSender());
        if (message.getSent() != null) {
            sent.setText(Storage.get().getDateTimeFormat().format(message.getSent()));
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
        messageDetail.setStarred(replyToMessage.isStarred());
        messageDetail.setSubject(replyToMessage.getSubject());
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
        messageDetail.setStarred(replyToMessage.isStarred());
        messageDetail.setSubject(replyToMessage.getSubject());
        return messageDetail;
    }

    /**
     * Clear compontents.
     */
    public void clear() {
        messageBody.setVisible(false);
        replyToMessage = null;
        setDefaultStyle();
    }
}
