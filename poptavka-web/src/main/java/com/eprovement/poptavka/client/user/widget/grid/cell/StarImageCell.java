/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
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

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private static ImageResourceRenderer renderer = new ImageResourceRenderer();

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates StarImageCell.
     */
    public StarImageCell() {
        super("click");
    }

    /**************************************************************************/
    /* Overriden methods                                                      */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
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

    /**
     * @{inheritDoc}
     */
    @Override
    public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context,
            Element parent, Boolean value, NativeEvent event,
            ValueUpdater<Boolean> valueUpdater) {
        if ("click".equals(event.getType()) && valueUpdater != null) {
            valueUpdater.update(value);
        }
    }
}
