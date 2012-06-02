package com.eprovement.poptavka.client.user.widget.detail;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.client.main.Storage;

/**
 * Clickable div. With toggle display functionality and loading image.
 *
 * @author beho
 *
 */
public class ClickableDiv extends Composite implements HasClickHandlers, HasText {

    private static ClickableDivUiBinder uiBinder = GWT
            .create(ClickableDivUiBinder.class);

    interface ClickableDivUiBinder extends UiBinder<Widget, ClickableDiv> {
    }

    @UiField HTMLPanel master;
    @UiField Image toggleImg, loadImg;
    @UiField SpanElement title;

    private boolean open = true;

    public ClickableDiv() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }

    public void toggleOpen() {
        if (open) {
            toggleImg.setResource(Storage.RSCS.images().toggleClosed());
        } else {
            toggleImg.setResource(Storage.RSCS.images().toggleOpen());
        }
        open = !open;
    }

    public void toggleLoading() {
        loadImg.setVisible(!loadImg.isVisible());
    }

    @Override
    public String getText() {
        return title.getInnerText();
    }

    @Override
    public void setText(String text) {
        title.setInnerText(text);
    }

}
