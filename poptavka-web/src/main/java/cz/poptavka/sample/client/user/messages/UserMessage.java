package cz.poptavka.sample.client.user.messages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.shared.domain.MessageDetail;

public class UserMessage extends Composite {

    public enum MessageDisplayType {
        FIRST, LAST, BOTH, NORMAL;
    }

    private static final int DATE_POS = 3;

    private static UserMessageViewUiBinder uiBinder = GWT
            .create(UserMessageViewUiBinder.class);

    interface UserMessageViewUiBinder extends
            UiBinder<Widget, UserMessage> {
    }

    public interface MessageStyle extends CssResource {
        @ClassName("message")
        String message();

        @ClassName("message-first")
        String messageFirst();

        @ClassName("message-last")
        String messageLast();

        @ClassName("message-opened")
        String messageOpened();

        @ClassName("message-header")
        String messageHeader();

        @ClassName("message-body")
        String messageBody();

        @ClassName("action-button")
        String actionButton();

    }

    @UiField MessageStyle style;

    @UiField Element header;
    @UiField Element body;
    @UiField Element headerTable;
    @UiField Element messagePreview;

//    NOT USED
//    @UiField Anchor replyButton;

    private boolean collapsed = false;

    public UserMessage() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public UserMessage(MessageDetail message, boolean collapsed) {
        this();
        // GWT cut off css3 style, but this shouldn't be visible at all

//        replyButton.getElement().getStyle().setBackgroundImage("-moz-linear-gradient(center bottom,rgb(46,45,46) 4%,"
//                + "rgb(122,118,122) 54%");

        setMessage(message);

        // set collapsed state
        this.collapsed = collapsed;
        toggleCollapsed();
    }

    /**
     * Constructs object of user message with custom parameter. Used for FIRST or LAST message
     * in the conversation. If there is only one message, it's style should be BOTH
     *
     * @param message message to fill the view
     * @param collapsed define if this newly created message should be collapsed
     * @param style if true, message is set as first, if false message is set as last!
     */
    public UserMessage(MessageDetail message, boolean collapsed, MessageDisplayType style) {
        this(message, collapsed);
        setMessageStyle(style);
            // NOT USED keep commented

            // Get buttons list, and set Eventhandlers for them;
            // According to DOM specification and widget structure, it's the last child div of
            // parent div. It's NOT the last child, because last ist the textNode after that div.
            // That means childCount - 2
//            Element actionButtonBar = (Element) body.getChild(body.getChildCount() - 2);
//            actionButtonBar.getStyle().setDisplay(Display.BLOCK);
//            DOM.sinkEvents(elem, eventBits);

    }

    public void setMessage(MessageDetail message) {
        NodeList<Element> tableColumns = headerTable.getElementsByTagName("td");
        // author
        tableColumns.getItem(0).setInnerText(message.getSubject());

        // message
        messagePreview.setInnerText(message.getBody());

        // date
        tableColumns.getItem(DATE_POS).setInnerText(message.getSent().toString());

        // message body
        // the first child is our content place
        body.getElementsByTagName("div").getItem(0).setInnerHTML(message.getBody());
    }


    public void setMessageStyle(MessageDisplayType type) {
        switch (type) {
            case FIRST:
                header.getParentElement().addClassName(style.messageFirst());
                break;
            case BOTH:
                header.getParentElement().addClassName(style.messageFirst());
            case LAST:
                header.getParentElement().addClassName(style.messageLast());
                break;
            case NORMAL:
                header.getParentElement().removeClassName(style.messageLast());
                break;
            default:
                break;
        }
    }

    public void toggleCollapsed() {
        if (collapsed) {
            body.getStyle().setDisplay(Display.NONE);
        } else {
            body.getStyle().setDisplay(Display.BLOCK);
        }
        collapsed = !collapsed;
    }

    /**********************************************************************************/
    /**                       Widget internal behavior handling.                       */
    @Override
    protected void onLoad() {
        com.google.gwt.user.client.Element castedElement = castElement(header);
        DOM.sinkEvents(castedElement, Event.ONCLICK);
        DOM.setEventListener(castedElement, new MessageToggleHangler());
    }

    @Override
    protected void onUnload() {
        super.onUnload();
        DOM.setEventListener(castElement(header), null);
    }

    private com.google.gwt.user.client.Element castElement(Element elem) {
        return (com.google.gwt.user.client.Element) elem;
    }

    private class MessageToggleHangler implements EventListener {
        @Override
        public void onBrowserEvent(Event event) {
            event.preventDefault();
            event.stopPropagation();
            if (event.getTypeInt() == Event.ONCLICK) {
                toggleCollapsed();
            }
        }
    }

}
