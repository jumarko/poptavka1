package cz.poptavka.sample.client.user.widget.messaging;

import java.math.BigDecimal;

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
import com.google.web.bindery.event.shared.HandlerRegistration;

import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.message.OfferMessageDetail;

/**
 * Mixed widget for sending offer as well for asking questions.
 *
 * @author Beho
 */
public class DevelOfferQuestionWindow extends Composite implements DevelOfferQuestionPresenter.ReplyInterface {

    private static ReplyWindowUiBinder uiBinder = GWT.create(ReplyWindowUiBinder.class);
    interface ReplyWindowUiBinder extends UiBinder<Widget, DevelOfferQuestionWindow> {   }

    private static final StyleResource CSS = GWT.create(StyleResource.class);

    private static final String RESPONSE_OFFER = "offer";
    private static final String RESPONSE_QUESTION = "question";

    @UiField Element header;
    @UiField Anchor offerReplyBtn;
    @UiField Anchor questionReplyBtn;
    @UiField TextArea replyTextArea;
    @UiField TextBox priceBox;
    @UiField DateBox dateBox;
    @UiField Anchor submitBtn;
    @UiField Anchor cancelBtn;
    //main widget part is hidden
    private boolean hiddenReplyBody = true;

    private String selectedResponse = null;
    private HandlerRegistration submitHandlerRegistration = null;

    private long demandId = 0;

    @Override
    public void createView() {
        CSS.message().ensureInjected();
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    protected void onLoad() {
//        com.google.gwt.user.client.Element castedElement = castElement(header);
//        DOM.sinkEvents(castedElement, Event.ONCLICK);
//        DOM.setEventListener(castedElement, new MessageToggleHangler());
        offerReplyBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                selectedResponse = RESPONSE_OFFER;
                replyTextArea.getElement().getNextSiblingElement()
                    .getFirstChildElement().getStyle().setVisibility(Visibility.VISIBLE);
                toggleWidget();
            }
        });
        questionReplyBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                GWT.log(" CLICK casted");
                selectedResponse = RESPONSE_QUESTION;
                replyTextArea.getElement().getNextSiblingElement()
                    .getFirstChildElement().getStyle().setVisibility(Visibility.HIDDEN);
                toggleWidget();
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

    @Override
    protected void onUnload() {
        super.onUnload();
        DOM.setEventListener(castElement(header), null);
    }

    private com.google.gwt.user.client.Element castElement(Element elem) {
        return (com.google.gwt.user.client.Element) elem;
    }

    /**
     * Add ClickHandler to submitButton and demandId of demand user is replying to. Initially assigned to attribute,
     * to prevent multiple clickHandlers on one widget. Because this widget is not instancialized multiple times.
     *
     * @param submitButtonHandler
     * @param selectedDemandId
     */
    public void addClickHandler(ClickHandler submitButtonHandler) {
        if (submitHandlerRegistration == null) {
            submitHandlerRegistration = submitBtn.addClickHandler(submitButtonHandler);
        } else {
            submitHandlerRegistration.removeHandler();
            submitHandlerRegistration = submitBtn.addClickHandler(submitButtonHandler);
        }
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public MessageDetail getCreatedMessage() {
        MessageDetail message = null;
        message = new MessageDetail();
        message.setBody(replyTextArea.getText());
        return message;
    }

    @Override
    public boolean isValid() {
        int errorCount = 0;
        errorCount += (replyTextArea.getText().equals("") ? 1 : 0);
        if (selectedResponse.equals(RESPONSE_OFFER)) {
            errorCount += (priceBox.getText().equals("") ? 1 : 0);
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

    @Override
    public void setSendingStyle() {
        // display sending message window, some loader or whatever
        header.getStyle().setDisplay(Display.NONE);
        header.getNextSiblingElement().getStyle().setDisplay(Display.NONE);
    }

    @Override
    public void setNormalStyle() {
        // display sending message window, whatever
        header.getStyle().setDisplay(Display.BLOCK);
        header.getNextSiblingElement().getStyle().setDisplay(Display.NONE);
        hiddenReplyBody = true;
        replyTextArea.setText("");
    }

    // TODO maybe not necessary, check
    @Override
    public boolean isResponseQuestion() {
        return selectedResponse == RESPONSE_QUESTION;
    }

    // TODO maybe not necessary, check
    @Override
    public void setResponseToQuestion() {
        selectedResponse = RESPONSE_QUESTION;
    }

    @Override
    public OfferMessageDetail getCreatedOfferMessage() {
        OfferMessageDetail offerMessageDetailImpl = new OfferMessageDetail();
        Long price = null;
        try {
            price = Long.parseLong(priceBox.getValue());
        } catch (Exception ex) {
            price = 0L;
        }
        offerMessageDetailImpl.setPrice(BigDecimal.valueOf(price));
        offerMessageDetailImpl.setEndDate(dateBox.getValue());

        return offerMessageDetailImpl;
    }

}
