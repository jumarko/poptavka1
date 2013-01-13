package com.eprovement.poptavka.client.user.widget.messaging;

import com.eprovement.poptavka.client.common.session.Storage;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.client.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Dummy message with no actions, just to display data.
 *
 * @author Beho
 */
public class SimpleMessageWindow extends Composite {

    public enum MessageDisplayType {

        FIRST, LAST, BOTH, NORMAL;
    }
    private static final int DATE_POS = 3;
    private static SimpleMessageWindowUiBinder uiBinder = GWT
            .create(SimpleMessageWindowUiBinder.class);

    interface SimpleMessageWindowUiBinder extends
            UiBinder<Widget, SimpleMessageWindow> {
    }
    private static final StyleResource CSS = GWT.create(StyleResource.class);
    @UiField
    Element header;
    @UiField
    Element body;
    @UiField
    Element headerTable;
    @UiField
    Element messagePreview;
    @UiField
    TextBox updateRead;
    private MessageDetail messageDetail;
    private boolean collapsed = false;

    public SimpleMessageWindow() {
        CSS.message().ensureInjected();
        initWidget(uiBinder.createAndBindUi(this));
    }

    public SimpleMessageWindow(MessageDetail message, boolean isCollapsed) {
        this();
        setMessage(message);
        setCollapsed(isCollapsed);
    }

    /**
     * Constructs object of user message with custom parameter. Used for FIRST or LAST message
     * in the conversation. If there is only one message, it's style should be BOTH
     *
     * @param message message to fill the view
     * @param collapsed define if this newly created message should be collapsed
     * @param style if true, message is set as first, if false message is set as last!
     */
    public SimpleMessageWindow(MessageDetail message, boolean collapsed, MessageDisplayType style) {
        this(message, collapsed);
        setMessageStyle(style);
    }

    public void setMessage(MessageDetail message) {
        String cssBold = Storage.RSCS.message().messagesUnread();
        String cssColor;
        if (message.getSenderId() == Storage.getUser().getUserId()) {
            cssColor = Storage.RSCS.message().messagesMine();
        } else {
            cssColor = Storage.RSCS.message().messagesReceived();
        }
        this.messageDetail = message;
        NodeList<Element> tableColumns = headerTable.getElementsByTagName("td");
        // author
        tableColumns.getItem(0).setInnerText(message.getSubject());
        tableColumns.getItem(0).addClassName(cssColor);
        if (!message.isRead()) {
            tableColumns.getItem(0).addClassName(cssBold);
        }

        // message
        messagePreview.setInnerText(message.getBody());
        messagePreview.addClassName(cssColor);
        if (!message.isRead()) {
            messagePreview.addClassName(cssBold);
        }

        // date
        Date date = message.getSent() == null ? message.getCreated() : message.getSent();
        tableColumns.getItem(DATE_POS).setInnerText(DateTimeFormat.getFormat(
                DateTimeFormat.PredefinedFormat.DATE_SHORT).format(date));
        tableColumns.getItem(DATE_POS).addClassName(cssColor);
        if (!message.isRead()) {
            tableColumns.getItem(DATE_POS).addClassName(cssBold);
        }

        // message body
        // the first child is our content place
        body.getElementsByTagName("div").getItem(0).setInnerHTML(message.getBody());
    }

    public void setCollapsed(boolean isCollapsed) {
        this.collapsed = isCollapsed;
        toggleCollapsed();
    }

    public void setMessageStyle(MessageDisplayType type) {
        /* TODO RELEASE: Martin S. */
        switch (type) {
            case FIRST:
                // header.getParentElement().addClassName(CSS.message().messageFirst());
                break;
            case BOTH:
                // header.getParentElement().addClassName(CSS.message().messageFirst());
            case LAST:
                // header.getParentElement().addClassName(CSS.message().messageLast());
                break;
            case NORMAL:
                // header.getParentElement().removeClassName(CSS.message().messageLast());
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
            if (!messageDetail.isRead()) {
                NodeList<Element> tableColumns = headerTable.getElementsByTagName("td");
                tableColumns.getItem(0).removeClassName(Storage.RSCS.message().messagesUnread());
                tableColumns.getItem(DATE_POS).removeClassName(Storage.RSCS.message().messagesUnread());
                messagePreview.removeClassName(Storage.RSCS.message().messagesUnread());
                messagePreview.removeClassName(Storage.RSCS.message().messagesUnread());
                //set message read
                updateRead.setText(Long.toString(messageDetail.getUserMessageId()));
                DomEvent.fireNativeEvent(Document.get().createChangeEvent(), updateRead);
            }
        }
        collapsed = !collapsed;
    }

    public TextBox getUpdateRead() {
        return updateRead;
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
