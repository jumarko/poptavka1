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
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiRenderer;

/**
 * Displays messageDetail as message cell in conversation panel widget's list .
 *
 * @author Martin Slavkovsky
 */
public class MessageCell extends AbstractCell<MessageDetail> {

    /**************************************************************************/
    /* UiRenderer                                                             */
    /**************************************************************************/
    private static MyUiRenderer renderer = GWT.create(MyUiRenderer.class);

    interface MyUiRenderer extends UiRenderer {

        void render(SafeHtmlBuilder sb, String cssBall, String cssColor, String sender, String date, String body);

        void onBrowserEvent(MessageCell o, NativeEvent e, Element p, MessageDetail n);

        SpanElement getBodySpan(Element parent);
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private DateTimeFormat dateFormat = DateTimeFormat.getFormat(Storage.MSGS.dateFormat());
    private static final int HEADER_TEXT_LIMIT = 30;
    private boolean open = false;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    public MessageCell() {
        super(BrowserEvents.CLICK);
    }

    /**************************************************************************/
    /* Overriden methods                                                      */
    /**************************************************************************/
    @Override
    public void render(Context context, MessageDetail value, SafeHtmlBuilder sb) {
        String cssBall;
        String cssColor;
        if (Storage.getUser().getUserId() == value.getSenderId()) {
            cssBall = Storage.RSCS.detailViews().conversationDetailRed();
            cssColor = Storage.RSCS.detailViews().conversationDetailHeaderRed();
        } else {
            cssBall = Storage.RSCS.detailViews().conversationDetailGreen();
            cssColor = Storage.RSCS.detailViews().conversationDetailHeaderGreen();
        }
        renderer.render(sb, cssBall, cssColor, value.getSenderName(),
                dateFormat.format(value.getSent()), getBodyText(value.getBody()));
    }

    @Override
    public void onBrowserEvent(Context context, Element parent, MessageDetail value,
            NativeEvent event, ValueUpdater<MessageDetail> valueUpdater) {
        if (open) {
            renderer.getBodySpan(parent).setInnerText(getBodyText(value.getBody()));
        } else {
            renderer.getBodySpan(parent).setInnerText(value.getBody());
        }
        open = !open;
        renderer.onBrowserEvent(this, event, parent, value);
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    private String getBodyText(String body) {
        if (body.length() < HEADER_TEXT_LIMIT) {
            return body;
        } else {
            return body.substring(0, HEADER_TEXT_LIMIT) + "... ";
        }
    }
}
