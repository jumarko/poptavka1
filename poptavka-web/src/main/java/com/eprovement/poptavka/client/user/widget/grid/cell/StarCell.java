package com.eprovement.poptavka.client.user.widget.grid.cell;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.ImageResourceRenderer;

import com.eprovement.poptavka.client.common.session.Storage;

/**
 * Clickable cell displaying star status of message.
 *
 * @author beho
 * @param <C>
 *
 */
public class StarCell extends AbstractCell<Boolean>  {

    private static ImageResourceRenderer renderer;
    //constants
    private static final ImageResource STARRED = Storage.RSCS.images().starGold();
    private static final ImageResource STARRED_NOT = Storage.RSCS.images().starSilver();

    public StarCell() {
        super("click", "keydown");
        if (renderer == null) {
            renderer = new ImageResourceRenderer();
        }
    }

    @Override
    public void render(com.google.gwt.cell.client.Cell.Context context,
            Boolean value, SafeHtmlBuilder sb) {
        if (value != null) {
            if (value) {
                sb.append(renderer.render(STARRED));
            } else {
                sb.append(renderer.render(STARRED_NOT));
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
