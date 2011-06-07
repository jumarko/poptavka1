package cz.poptavka.sample.client.user.messages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetailImpl;

public class ReplyWindow extends Composite implements ReplyWindowPresenter.ReplyInterface {

    private static ReplyWindowUiBinder uiBinder = GWT.create(ReplyWindowUiBinder.class);
    interface ReplyWindowUiBinder extends UiBinder<Widget, ReplyWindow> {   }

    public interface ReplyStyle extends CssResource {
        @ClassName("reply-message")
        String replyMessage();

        @ClassName("reply-message-arrow-border")
        String replyMessageArrowBorder();

        @ClassName("reply-message-arrow")
        String replyMessageArrow();

        @ClassName("text-area")
        String textArea();
    }

    private static final int RESPONSE_OFFER = 0;
    private static final int RESPONSE_QUESTION = 1;

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

    private int selectedResponse = 0;

    private long demandId = 0;

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));

        /** CSS3 specific styling **/
        submitBtn.getElement().getStyle().setBackgroundImage("-moz-linear-gradient(center bottom,rgb(46,45,46) 4%,"
                + "rgb(122,118,122) 54%");

        cancelBtn.getElement().getStyle().setBackgroundImage("-moz-linear-gradient(center bottom,rgb(46,45,46) 4%,"
                + " rgb(122,118,122) 54%;");
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
     * Add ClickHandler to submitButton and demandId of demand user is replying to.
     *
     * @param submitButtonHandler
     * @param selectedDemandId
     */
    public void addClickHandler(ClickHandler submitButtonHandler, long selectedDemandId) {
        submitBtn.addClickHandler(submitButtonHandler);
        demandId = selectedDemandId;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public MessageDetailImpl getCreatedMessage() {
        MessageDetailImpl message = new MessageDetailImpl();
        message.setBody(replyTextArea.getText());
        message.setDemandId(demandId);
        return message;
    }

    @Override
    public boolean isValid() {
        int errorCount = 0;
        errorCount += (replyTextArea.getText().equals("") ? 1 : 0);
        if (selectedResponse == RESPONSE_OFFER) {
            errorCount += (priceBox.getText().equals("") ? 1 : 0);
            try {
                Long.parseLong(priceBox.getValue());
            } catch (Exception ex) {
                errorCount++;
            }
            errorCount += (dateBox.getValue() == null ? 1 : 0);
        }
        // TODO error display
        return errorCount == 0;
    }

    @Override
    public OfferDetail getCreatedOffer() {
        OfferDetail offer = new OfferDetail();
        MessageDetailImpl message = getCreatedMessage();
        offer.setPrice(priceBox.getText());
        offer.setFinishDate(dateBox.getValue());
        offer.setMessageDetail(message);
        return offer;
    }

    @Override
    public void setSendingStyle() {
        // TODO Auto-generated method stub
        // display sending message window, whatever
        header.getStyle().setDisplay(Display.NONE);
        header.getNextSiblingElement().getStyle().setDisplay(Display.NONE);
    }

    @Override
    public void setNormalStyle() {
        // TODO Auto-generated method stub
        // display sending message window, whatever
        header.getStyle().setDisplay(Display.BLOCK);
        header.getNextSiblingElement().getStyle().setDisplay(Display.NONE);
        hiddenReplyBody = true;
    }

    @Override
    public boolean isResponseQuestion() {
        return selectedResponse == RESPONSE_QUESTION;
    }

    @Override
    public Anchor getCancelButton() {
        return cancelBtn;
    }

    @Override
    public void setResponseToQuestion() {
        selectedResponse = RESPONSE_QUESTION;
    }

}
