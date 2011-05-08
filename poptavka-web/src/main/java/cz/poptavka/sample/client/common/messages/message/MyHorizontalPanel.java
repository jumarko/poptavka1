package cz.poptavka.sample.client.common.messages.message;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class MyHorizontalPanel extends HorizontalPanel {

    private HandlerRegistration handlerRegistration;

    public MyHorizontalPanel() {
        this.sinkEvents(Event.ONCLICK);
    }

    public HandlerRegistration addClickHandler(ClickHandler click) {
        handlerRegistration = addDomHandler(click, ClickEvent.getType());
        return handlerRegistration;
    }
}
