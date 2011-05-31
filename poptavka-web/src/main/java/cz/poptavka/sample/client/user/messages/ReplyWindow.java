package cz.poptavka.sample.client.user.messages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.shared.domain.MessageDetail;

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
    @UiField Anchor submitBtn;
    @UiField Anchor cancelBtn;
    //main widget part is hidden
    private boolean hiddenReplyBody = true;

    private int selectedResponse = 0;

    @Override
    public void createView() {
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
                    .getFirstChildElement().getStyle().setDisplay(Display.BLOCK);
                toggleWidget();
            }
        });
        questionReplyBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                selectedResponse = RESPONSE_QUESTION;
                replyTextArea.getElement().getNextSiblingElement()
                    .getFirstChildElement().getStyle().setDisplay(Display.NONE);
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
     * Add ClickHandler to submitButton. ClickHandler to cancelButton is added automatically
     * @param submitButtonHandler
     */
    public void addClickHandler(ClickHandler submitButtonHandler) {
        submitBtn.addClickHandler(submitButtonHandler);
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public MessageDetail getCreatedMessage() {
        MessageDetail message = new MessageDetail();
        message.setBody(replyTextArea.getText());
        return message;
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
    }

//    public Long getMessageToReplyId() {
//        return messageToReplyId;
//    }
//
//    public void setMessageToReplyId(Long messageToReplyId) {
//        this.messageToReplyId = messageToReplyId;
//    }

}
