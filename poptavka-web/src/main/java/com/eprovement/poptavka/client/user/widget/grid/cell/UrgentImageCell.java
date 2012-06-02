package com.eprovement.poptavka.client.user.widget.grid.cell;

import java.util.Date;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageResourceRenderer;
import com.google.gwt.user.client.ui.PopupPanel;

import com.eprovement.poptavka.client.main.Storage;

public class UrgentImageCell extends AbstractCell<Date> {

    private static ImageResourceRenderer renderer;

    private static final int DAY_URGENT = 4;
    private static final int DAY_URGENT_LESS = 8;
    private static final int DAY_NORMAL = 12;

    private static final ImageResource URGENT = Storage.RSCS.images().urgent();
    private static final ImageResource URGENT_LESS = Storage.RSCS.images().lessUrgent();
    private static final ImageResource NORMAL = Storage.RSCS.images().normal();
    private static final ImageResource NORMAL_LESS = Storage.RSCS.images().lessNormal();

    private PopupPanel popup = new PopupPanel(true);

    private boolean displayed;

    public UrgentImageCell() {
        super("click", "keydown", "mouseover", "mouseout");
        if (renderer == null) {
            renderer = new ImageResourceRenderer();
        }
    }

    @Override
    public void render(com.google.gwt.cell.client.Cell.Context context,
            Date value, SafeHtmlBuilder sb) {
        long diffSec = value.getTime() - (new Date()).getTime();
        long diffDays = diffSec / Storage.DAY_LENGTH;

        //(0-4) velmi specha
        if ((int) diffDays <= DAY_URGENT) {
            sb.append(renderer.render(URGENT));
          //(5-8) specha
        } else if ((int) diffDays <= DAY_URGENT_LESS) {
            sb.append(renderer.render(URGENT_LESS));
        } else if ((int) diffDays <= DAY_NORMAL) {
            //(9-12) nespecha
            sb.append(renderer.render(NORMAL));
        } else if (DAY_NORMAL < (int) diffDays) {
            //(13-oo) vobec nespecha
            sb.append(renderer.render(NORMAL_LESS));
        }
    }

    @Override
    public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context,
            Element parent, Date value, NativeEvent event,
            ValueUpdater<Date> valueUpdater) {
        if (("click".equals(event.getType())) || ("keydown".equals(event.getType()))) {
            onEnterKeyDown(context, parent, value, event, valueUpdater);
        }
        if ("mouseover".equals(event.getType())) {
            displayPopup(parent, event);
        }
        if ("mouseout".equals(event.getType())) {
            hidePopup();
        }
    }

    @Override
    protected void onEnterKeyDown(
            com.google.gwt.cell.client.Cell.Context context, Element parent,
            Date value, NativeEvent event, ValueUpdater<Date> valueUpdater) {
        if (valueUpdater != null) {
            valueUpdater.update(value);
        }
    }

    private void displayPopup(Element parent, NativeEvent event) {
        if (displayed) {
            return;
        }
        popup.setWidth("150px");
        popup.setHeight("150px");
        StringBuilder sb = new StringBuilder();
        sb.append("<ul style='list-style: none; margin-left: 5px;'>");
        sb.append("<li>" + AbstractImagePrototype.create(Storage.RSCS.images().urgent()).getHTML() + " - "
                + Storage.MSGS.urgency() + "</li>");
        sb.append("<li>" + AbstractImagePrototype.create(Storage.RSCS.images().lessUrgent()).getHTML() + " - "
                + Storage.MSGS.urgency() + "</li>");
        sb.append("<li>" + AbstractImagePrototype.create(Storage.RSCS.images().normal()).getHTML() + " - "
                + Storage.MSGS.urgency() + "</li>");
        sb.append("<li>" + AbstractImagePrototype.create(Storage.RSCS.images().lessNormal()).getHTML() + " - "
                + Storage.MSGS.urgency() + "</li></ul>");
        popup.getElement().setInnerHTML(sb.toString());
        popup.setPopupPosition(event.getClientX() + 50, event.getClientY() + 35);
        popup.show();
        displayed = true;
    }

    private void hidePopup() {
        displayed = false;
        popup.hide();
    }

}
