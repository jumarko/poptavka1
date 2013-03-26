package com.eprovement.poptavka.client.user.widget.grid.cell;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.ImageResourceRenderer;

import com.eprovement.poptavka.client.common.session.Storage;

/**
 * Clickable cell displaying star status of message.
 *
 * @author beho
 *
 */
public class StarImageCell extends AbstractCell<Boolean> {

    private static ImageResourceRenderer renderer = new ImageResourceRenderer();

    public StarImageCell() {
        super("click", "keydown");
    }

    @Override
    public void render(com.google.gwt.cell.client.Cell.Context context,
            Boolean value, SafeHtmlBuilder sb) {
        if (value == null) {
            sb.append(renderer.render(Storage.RSCS.images().starHeader()));
        } else {
            if (value) {
                sb.append(renderer.render(Storage.RSCS.images().starGreen()));
            } else {
                sb.append(renderer.render(Storage.RSCS.images().starSilver()));
            }
        }
    }

    @Override
    public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context,
            Element parent, Boolean value, NativeEvent event,
            ValueUpdater<Boolean> valueUpdater) {
        if (("click".equals(event.getType())) || ("keydown".equals(event.getType()))) {
            onEnterKeyDown(context, parent, value, event, valueUpdater);
        }
    }

    @Override
    protected void onEnterKeyDown(
            com.google.gwt.cell.client.Cell.Context context, Element parent,
            Boolean value, NativeEvent event, ValueUpdater<Boolean> valueUpdater) {
        if (valueUpdater != null) {
            valueUpdater.update(value);
        }
    }
}
