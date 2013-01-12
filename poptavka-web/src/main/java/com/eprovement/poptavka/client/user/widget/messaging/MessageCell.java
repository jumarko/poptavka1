/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.widget.messaging;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiRenderer;
import com.google.gwt.user.client.Window;

/**
 *
 * @author mato
 */
public class MessageCell extends AbstractCell<MessageDetail> {

    private DateTimeFormat dateFormat = DateTimeFormat.getFormat(Storage.MSGS.dateFormat());
    private static final int HEADER_TEXT_LIMIT = 30;

    interface MyUiRenderer extends UiRenderer {

        void render(SafeHtmlBuilder sb, String sender, String date, String body);

        void onBrowserEvent(MessageCell o, NativeEvent e, Element p, MessageDetail n);
//        void onBrowserEvent(Context context, NativeEvent e, Element p, MessageDetail n);
    }
    private static MyUiRenderer renderer = GWT.create(MyUiRenderer.class);

    public MessageCell() {
        super(BrowserEvents.CLICK);
    }

    @Override
    public void render(Context context, MessageDetail value, SafeHtmlBuilder sb) {
        renderer.render(sb, value.getSenderName(), dateFormat.format(value.getSent()), getBodyText(value.getBody()));
//        renderer.render(sb, value.getSenderName(), dateFormat.format(value.getSent()), body);
    }

    @Override
    public void onBrowserEvent(Context context, Element parent, MessageDetail value,
            NativeEvent event, ValueUpdater<MessageDetail> valueUpdater) {
//        renderer.render(new SafeHtmlBuilder(), value.getSenderName(),
//        dateFormat.format(value.getSent()), value.getBody());
//        renderer.onBrowserEvent(this, event, parent, value);
//        renderer.onBrowserEvent(context, event, parent, value);
//        onBrowserEvent(context, parent, value, event, valueUpdater);
        Window.alert("Doriti");
    }

    private String getBodyText(String body) {
        if (body.length() < HEADER_TEXT_LIMIT) {
            return body;
        } else {
            return body.substring(0, HEADER_TEXT_LIMIT) + "... ";
        }
    }
    private String body = "body";

    public void setBody(String text) {
        body = text;
    }
//
//    private static final String HTML_UNREAD_START = "<strong>";
//    private static final String HTML_UNREAD_END = "</strong>";
//    private static final int HEADER_TEXT_LIMIT = 30;
//    private DateTimeFormat dateFormat = DateTimeFormat.getFormat(Storage.MSGS.dateFormat());
//    private boolean open = false;
//
//    public MessageCell() {
//        super("click");
//    }
//
//    @Override
//    public void render(Context context, MessageDetail value, SafeHtmlBuilder sb) {
//        sb.appendHtmlConstant("<table>");
//        sb.appendHtmlConstant("<tr><th>");
//        sb.append(getHeader(value));
//        sb.appendHtmlConstant("</th></tr>");
//
//        if (open) {
//            sb.appendHtmlConstant("<tr><td>");
//            sb.appendEscaped(value.getBody());
//            sb.appendHtmlConstant("</td></tr>");
//        }
//        sb.appendHtmlConstant("</table>");
//    }
//
//    /**
//     * Called when an event occurs in a rendered instance of this Cell. The
//     * parent element refers to the element that contains the rendered cell, NOT
//     * to the outermost element that the Cell rendered.
//     */
//    @Override
//    public void onBrowserEvent(Context context, Element parent, MessageDetail value,
//            NativeEvent event, ValueUpdater<MessageDetail> valueUpdater) {
//        super.onBrowserEvent(context, parent, value, event, valueUpdater);
//        if ("click".equals(event.getType())) {
//            open = !open;
//            render(context, value, new SafeHtmlBuilder());
//        }
//    }
//
//    private SafeHtml getHeader(MessageDetail message) {
//        SafeHtmlBuilder header = new SafeHtmlBuilder();
//        if (message.isRead()) {
//            header.appendHtmlConstant(HTML_UNREAD_START);
//            header.appendHtmlConstant("<div style=\"float:left\">");
//            header.appendEscaped(getHeaderText(message.getBody()));
//            header.appendHtmlConstant("</div>");
//            header.appendEscaped(dateFormat.format(message.getSent()));
//            header.appendHtmlConstant("<div style=\"float:right\">");
//            header.appendHtmlConstant(HTML_UNREAD_END);
//            header.appendHtmlConstant("</div>");
//        } else {
//            header.appendEscaped(getHeaderText(message.getBody()));
//        }
//        return header.toSafeHtml();
//    }
//
//    private String getHeaderText(String messageBody) {
//        if (messageBody.length() < HEADER_TEXT_LIMIT) {
//            return messageBody;
//        } else {
//            return messageBody.substring(0, HEADER_TEXT_LIMIT) + "... ";
//        }
//    }
}
