/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.widget.grid.cell;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiRenderer;
import java.util.Date;

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

        void render(SafeHtmlBuilder sb, String cssColor, String sender, String date, String body);

        void onBrowserEvent(MessageCell o, NativeEvent e, Element p, MessageDetail n);

        SpanElement getBodySpan(Element parent);
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private static final int HEADER_TEXT_LIMIT = 30;
    private boolean open;

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
        String cssColor;
        if (Storage.getUser().getUserId() == value.getSenderId()) {
            cssColor = Storage.RSCS.detailViews().conversationDetailHeaderRed();
        } else {
            cssColor = Storage.RSCS.detailViews().conversationDetailHeaderGreen();
        }
        renderer.render(sb, cssColor, value.getSender(),
                getSentText(value.getSent()), getBodyText(value.getBody()));
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
    private String getSentText(Date sent) {
        if (sent == null) {
            return "";
        } else {
            return Storage.DATE_FORMAT_LONG.format(sent);
        }
    }

    private String getBodyText(String body) {
        if (body.length() < HEADER_TEXT_LIMIT) {
            return body;
        } else {
            return body.substring(0, HEADER_TEXT_LIMIT) + "... ";
        }
    }
}
