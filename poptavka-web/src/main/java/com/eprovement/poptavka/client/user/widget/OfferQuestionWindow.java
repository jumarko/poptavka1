package com.eprovement.poptavka.client.user.widget;

import com.eprovement.poptavka.client.common.BigDecimalBox;
import com.eprovement.poptavka.client.common.ValidationMonitor;
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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
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
    @UiField(provided = true) ValidationMonitor bodyMonitor, priceMonitor, finishDateMonitor;
    @UiField Button offerReplyBtn, questionReplyBtn;
    @UiField Button submitBtn, cancelBtn;
    @UiField HTMLPanel header, messagePanel;
    @UiField FluidContainer messageBody;
    @UiField FluidRow priceRow, finishDateRow;
    @UiField Label sender, sent, body;
    /** Class attributes. **/
    private int selectedResponse;
    private MessageDetail replyToMessage;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    public OfferQuestionWindow() {
        initValidationMonitors();
        initWidget(uiBinder.createAndBindUi(this));
        offerReplyBtn.setVisible(false);

        Storage.RSCS.detailViews().ensureInjected();
    }

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
        finishDateRow.setVisible(true);
    }

    public void setSendingQuestionStyle() {
        header.setVisible(false);
        messageBody.setVisible(true);
        priceRow.setVisible(false);
        finishDateRow.setVisible(false);
    }

    public void setDefaultStyle() {
        header.setVisible(true);
        messageBody.setVisible(false);
        priceRow.setVisible(false);
        finishDateRow.setVisible(false);
        ((BigDecimalBox) priceMonitor.getWidget()).setText(EMPTY);
        ((DateBox) finishDateMonitor.getWidget()).getTextBox().setText(EMPTY);
        bodyMonitor.setValue(EMPTY);
    }

    private void resetValidationStyles() {
        bodyMonitor.reset();
        priceMonitor.reset();
        finishDateMonitor.reset();
    }

    public void allowSendingOffer() {
        offerReplyBtn.setVisible(true);
    }

    /**************************************************************************/
    /* GETTER of created messages.                                            */
    /**************************************************************************/
    public MessageDetail getCreatedMessage() {
        MessageDetail message = new MessageDetail();
        message.setBody((String) bodyMonitor.getValue());
        return message;
    }

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
    public Button getOfferReplyBtn() {
        return offerReplyBtn;
    }

    public Button getSubmitBtn() {
        return submitBtn;
    }

    public int getSelectedResponse() {
        return selectedResponse;
    }

    @Override
    public boolean isValid() {
        boolean valid = bodyMonitor.isValid();
        if (selectedResponse == RESPONSE_OFFER) {
            valid = priceMonitor.isValid() && valid;
            valid = finishDateMonitor.isValid() && valid;
        }
        return valid;
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

        messagePanel.setVisible(true);
        this.replyToMessage = message;
        sender.setText(message.getSenderName());
        sent.setText(Storage.DATE_FORMAT.format(message.getSent()));
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

    public void clear() {
        messagePanel.setVisible(false);
    }
}
