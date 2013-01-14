package com.eprovement.poptavka.client.user.widget.messaging;

import com.eprovement.poptavka.client.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.OfferMessageDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
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
public class OfferQuestionWindow extends Composite {

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    private static ReplyWindowUiBinder uiBinder = GWT.create(ReplyWindowUiBinder.class);

    interface ReplyWindowUiBinder extends UiBinder<Widget, OfferQuestionWindow> {
    }
    private static final StyleResource CSS = GWT.create(StyleResource.class);
    //Constants
    public static final int RESPONSE_OFFER = 0;
    public static final int RESPONSE_QUESTION = 1;
    //UiBinder attributes
    @UiField
    Element header;
    @UiField
    Anchor offerReplyBtn, questionReplyBtn;
    @UiField
    Anchor submitBtn, cancelBtn;
    @UiField
    TextArea replyTextArea;
    @UiField
    TextBox priceBox;
    @UiField
    DateBox dateBox;
    //main widget part is hidden
    private boolean hiddenReplyBody = true;
    private int selectedResponse;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    public OfferQuestionWindow() {
        CSS.message().ensureInjected();
        initWidget(uiBinder.createAndBindUi(this));
        offerReplyBtn.setVisible(false);
    }

    //rozdiel medzi uiBinderHandlerom a onLoad???
    @Override
    protected void onLoad() {
//        com.google.gwt.user.client.Element castedElement = castElement(header);
//        DOM.sinkEvents(castedElement, Event.ONCLICK);
//        DOM.setEventListener(castedElement, new MessageToggleHangler());
        offerReplyBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                addOfferReply();
            }
        });
        questionReplyBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                addQuestionReply();
            }
        });
        cancelBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                toggleWidget();
                replyTextArea.setText("");
            }
        });
    }

    @Override
    protected void onUnload() {
        super.onUnload();
        DOM.setEventListener(castElement(header), null);
    }

    public void addQuestionReply() {
        selectedResponse = RESPONSE_QUESTION;
        replyTextArea.getElement().getNextSiblingElement()
                .getFirstChildElement().getStyle().setVisibility(Visibility.HIDDEN);
        toggleWidget();
    }

    public void addOfferReply() {
        selectedResponse = RESPONSE_OFFER;
        replyTextArea.getElement().getNextSiblingElement()
                .getFirstChildElement().getStyle().setVisibility(Visibility.VISIBLE);
        toggleWidget();
    }

    //Martin - neviem co to znamena, pouziva pri DOM.setEventListener
    //vyhnem sa tomu ak pouzijem uiBinderHandler??
    private com.google.gwt.user.client.Element castElement(Element elem) {
        return (com.google.gwt.user.client.Element) elem;
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    public void setSendingStyle() {
        // display sending message window, some loader or whatever
        header.getStyle().setDisplay(Display.NONE);
        header.getNextSiblingElement().getStyle().setDisplay(Display.NONE);
    }

    public void setNormalStyle() {
        // display sending message window, whatever
        header.getStyle().setDisplay(Display.BLOCK);
        header.getNextSiblingElement().getStyle().setDisplay(Display.NONE);
        hiddenReplyBody = true;
        replyTextArea.setText("");
    }

    //Martin - tiez neviem co presne to robi, pouziva pri akciach, vid onLoad
    public void toggleWidget() {
        if (hiddenReplyBody) {
            header.getStyle().setDisplay(Display.NONE);
            header.getNextSiblingElement().getStyle().setDisplay(Display.BLOCK);
            // not needed, commented in uiBinder
//            header.getNextSiblingElement().getFirstChildElement().getStyle().setDisplay(Display.BLOCK);
        } else {
            header.getStyle().setDisplay(Display.BLOCK);
            header.getNextSiblingElement().getStyle().setDisplay(Display.NONE);
            // not needed, commented in uiBinder
//            header.getNextSiblingElement().getFirstChildElement().getStyle().setDisplay(Display.NONE);
            replyTextArea.setValue("");
        }
        hiddenReplyBody = !hiddenReplyBody;
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

    //Zrobit bean validaciu?? .. asi netreba
    public boolean isValid() {
        int errorCount = 0;
        errorCount += (replyTextArea.getText().isEmpty() ? 1 : 0);
        if (selectedResponse == RESPONSE_OFFER) {
            errorCount += (priceBox.getText().isEmpty() ? 1 : 0);
            try {
                Long.valueOf(priceBox.getValue());
            } catch (Exception ex) {
                errorCount++;
            }
            errorCount += (dateBox.getValue() == null ? 1 : 0);
        }
        // TODO error display
        return errorCount == 0;
    }

    public Widget getWidgetView() {
        return this;
    }
}
