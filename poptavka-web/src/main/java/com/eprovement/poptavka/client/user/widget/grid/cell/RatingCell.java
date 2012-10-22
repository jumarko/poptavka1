/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.widget.grid.cell;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.ImageResourceRenderer;

/**
 *
 * @author Martin
 */
public class RatingCell extends AbstractCell<FullSupplierDetail> {

    private static ImageResourceRenderer renderer;
    //constants
    private static final ImageResource RATE_6 = Storage.RSCS.images().rate6();

    public RatingCell() {
        if (renderer == null) {
            renderer = new ImageResourceRenderer();
        }
    }

    @Override
    public void render(Cell.Context context, FullSupplierDetail value, SafeHtmlBuilder sb) {
        /*
         * Always do a null check on the value. Cell widgets can pass null to
         * cells if the underlying data contains a null, or if the data arrives
         * out of order.
         */
        if (value == null) {
            return;
        }

        sb.appendHtmlConstant("<div style=\"font-weight:bold; color:#F7BD00;\">");
        //TODO Jaro - musi byt obrazok rating.png rozekany na jednotlive rate,
        //alebo sa to da zrobit "posuvanim" cez CSS?
        int rate = value.getOverallRating();
        if (rate == 10) {                   //*****
//            sb.append(renderer.render(RATE_10));
        } else if (9 <= rate && rate < 10) {
//            sb.append(renderer.render(RATE_9);
        } else if (8 <= rate && rate < 9) { //****.
//            sb.append(renderer.render(RATE_8));
        } else if (7 <= rate && rate < 8) {
//            sb.append(renderer.render(RATE_7));
        } else if (6 <= rate && rate < 7) { //***..
//            sb.append(renderer.render(RATE_6));
        } else if (5 <= rate && rate < 6) {
//            sb.append(renderer.render(RATE_5));
        } else if (4 <= rate && rate < 5) { //**...
//            sb.append(renderer.render(RATE_4));
        } else if (3 <= rate && rate < 4) {
//            sb.append(renderer.render(RATE_3));
        } else if (2 <= rate && rate < 3) { //*....
//            sb.append(renderer.render(RATE_2));
        } else if (1 <= rate && rate < 2) {
//            sb.append(renderer.render(RATE_1));
        } else { //if (rate < 1)            //.....
//            sb.append(renderer.render(RATE_0));
            sb.append(renderer.render(RATE_6)); //len pre ukazku
        }
        //TODO Jaro - CSS tak aby tam nemuselo byt to "   "
        sb.appendEscaped("   " + Integer.toString(rate));
        sb.appendHtmlConstant("</div>");
    }
}
