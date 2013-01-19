package com.eprovement.poptavka.client.user.widget.messaging;

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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import java.math.BigDecimal;

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
    @UiField TextBox priceBox;
    @UiField DateBox dateBox;
    @UiField DivElement header, messageBody, offerBody;
    @UiField Label errorLabelText, errorLabelPrice, errorLabelDate;
    /** Class attributes. **/
    private int selectedResponse;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    public OfferQuestionWindow() {
        Storage.RSCS.message().ensureInjected();
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
        header.getStyle().setDisplay(Display.NONE);
        messageBody.getStyle().setDisplay(Display.BLOCK);
        offerBody.getStyle().setDisplay(Display.BLOCK);
    }

    public void setSendingQuestionStyle() {
        header.getStyle().setDisplay(Display.NONE);
        messageBody.getStyle().setDisplay(Display.BLOCK);
    }

    public void setDefaultStyle() {
        messageBody.getStyle().setDisplay(Display.NONE);
        offerBody.getStyle().setDisplay(Display.NONE);
        header.getStyle().setDisplay(Display.BLOCK);
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
        Long price = null;
        try {
            price = Long.parseLong(priceBox.getValue());
        } catch (Exception ex) {
            price = 0L;
        }
        offerMessageDetailImpl.setPrice(BigDecimal.valueOf(price));
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
                Long.valueOf(priceBox.getValue());
            } catch (Exception ex) {
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
}
