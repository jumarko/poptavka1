package com.eprovement.poptavka.client.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class SimpleIconLabel extends Composite implements HasMouseOverHandlers, HasMouseOutHandlers {

    private static SimpleIconLabelUiBinder uiBinder = GWT.create(SimpleIconLabelUiBinder.class);
    interface SimpleIconLabelUiBinder extends UiBinder<Widget, SimpleIconLabel> {    }

    @UiField HTMLPanel wrapper;
    @UiField DivElement inliner;
    @UiField SpanElement message;
    @UiField Image image;

    private PopupPanel panel = null;

    private boolean popupEnabled = true;

    private String description = "";

    public SimpleIconLabel(String initMessage, String initDescription) {
        initWidget(uiBinder.createAndBindUi(this));

        MyMouseEventHandler handler = new MyMouseEventHandler();
        addMouseOutHandler(handler);
        addMouseOverHandler(handler);

        message.setInnerText(initMessage);
        description = initDescription;

        image.getElement().getStyle().setVerticalAlign(VerticalAlign.MIDDLE);
    }

    public SimpleIconLabel(String message, boolean popupDisabled) {
        this(message, "");
    }

    public SimpleIconLabel(String description) {
        this("", description);
    }

    public SimpleIconLabel() {
        this("", "");
    }

    @Override
    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
        return addDomHandler(handler, MouseOutEvent.getType());
    }

    @Override
    public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
        return addDomHandler(handler, MouseOverEvent.getType());
    }

    public class MyMouseEventHandler implements MouseOverHandler, MouseOutHandler {
        public void onMouseOver(MouseOverEvent event) {
            int x = event.getClientX() + 32;
            int y = event.getClientY() - 5;
            if (!description.isEmpty()) {
                showInfo(x, y);
            }
        }

        public void onMouseOut(MouseOutEvent event) {
            if (panel != null) {
                panel.hide(true);
            }
        }
    }

    public void setMessage(String message) {
        this.message.setInnerText(message);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageResource(ImageResource resource) {
        this.image.setResource(resource);
    }

    public void setPopupEnabled(boolean enabled) {
        popupEnabled = enabled;
    }

    protected void showInfo(int x, int y) {
        if (popupEnabled) {
            panel = new PopupPanel();
            panel.setAutoHideEnabled(true);
            panel.add(new HTML(description));
            panel.setPopupPosition(x, y);
            panel.show();
        }
    }

    public void setInline(boolean inline) {
        wrapper.getElement().getStyle().setDisplay(Display.INLINE);
        inliner.getStyle().setDisplay(Display.INLINE);
//        image.getElement().getStyle().setVerticalAlign(VerticalAlign.TEXT_BOTTOM);
    }

}
