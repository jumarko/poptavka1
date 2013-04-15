/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.widget.grid.cell;

import com.eprovement.poptavka.client.common.session.Storage;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.ImageResourceRenderer;

/**
 *
 * @author Martin
 */
public class RatingCell extends AbstractCell<Integer> {

    private ImageResourceRenderer renderer;
    private static final ImageResource RATING_STAR = Storage.RSCS.images().starGold();

    public RatingCell() {
        if (renderer == null) {
            renderer = new ImageResourceRenderer();
        }
    }

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

        sb.appendHtmlConstant("<div style=\"font-weight:bold;font-size:100%;color:#F7BD00;"
                + "text-shadow:0.5px 0 #FFCC00;float:left\">");
        sb.append(renderer.render(RATING_STAR));
        sb.appendEscaped("   " + Integer.toString(value));
        sb.appendHtmlConstant("</div>");
    }
}
