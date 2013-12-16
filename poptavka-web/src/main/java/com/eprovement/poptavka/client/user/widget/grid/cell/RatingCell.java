/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.grid.cell;

import com.eprovement.poptavka.client.common.session.Storage;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.ImageResourceRenderer;

/**
 * Cell for rendering rating information.
 * @author Martin Slavkovsky
 */
public class RatingCell extends AbstractCell<Integer> {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private ImageResourceRenderer renderer;
    private static final ImageResource RATING_STAR = Storage.RSCS.images().ratingStar();

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates Rating cell.
     */
    public RatingCell() {
        if (renderer == null) {
            renderer = new ImageResourceRenderer();
        }
    }

    /**************************************************************************/
    /* Overriden methods                                                      */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public void render(Cell.Context context, Integer value, SafeHtmlBuilder sb) {
        /*
         * Always do a null check on the value. Cell widgets can pass null to
         * cells if the underlying data contains a null, or if the data arrives
         * out of order.
         */
        if (value == null) {
            return;
        }

        sb.appendHtmlConstant("<div style=\"font-weight:normal;font-size:100%;color:#6F6B59;"
                + ";float:left\">");
        sb.append(renderer.render(RATING_STAR));
        sb.appendEscaped("   " + Integer.toString(value));
        sb.appendHtmlConstant("</div>");
    }
}
