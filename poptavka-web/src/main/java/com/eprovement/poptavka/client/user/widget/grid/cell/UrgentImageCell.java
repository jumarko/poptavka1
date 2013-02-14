package com.eprovement.poptavka.client.user.widget.grid.cell;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.github.gwtbootstrap.client.ui.constants.Placement;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.ImageResourceRenderer;
import java.util.Date;

/**
 * Provides urgency images.
 *
 * @author Martin Slavkovsky
 */
public class UrgentImageCell extends AbstractCell<Date> {

    private static ImageResourceRenderer renderer;
    private static final int DAY_URGENT = 4;
    private static final int DAY_URGENT_LESS = 8;
//    TODO RELEASE Jaro - uncomment if implemented
//    private static final ImageResource HEADER = Storage.RSCS.images().header();
    private static final ImageResource URGENT = Storage.RSCS.images().urgent();
    private static final ImageResource URGENT_LESS = Storage.RSCS.images().lessUrgent();
    private static final ImageResource NORMAL = Storage.RSCS.images().normal();

    public UrgentImageCell() {
        if (renderer == null) {
            renderer = new ImageResourceRenderer();
        }
    }

    @Override
    public void render(Context context, Date value, SafeHtmlBuilder sb) {
        /*
         * Getting value null tells us to use header image and create tooltip.
         * The result will be used in header.
         */
        if (value == null) {
            renderUrgencyHeader(context, value, sb);
            /*
             * Otherwise provide appropriate urgency image according to given date.
             */
        } else {
            renderUrgencyColumnCell(context, value, sb);
        }
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    private void renderUrgencyHeader(Context context, Date value, SafeHtmlBuilder sb) {
        StringBuilder sbs = new StringBuilder();
        //TODO RELEASE Jaro - make it look goot like in design. Thanks.
        sbs.append("<a href=\"#\" ");
        sbs.append("placement=\"" + Placement.BOTTOM + "\" ");
        sbs.append("delay=0");
        sbs.append("animation=\"true\" ");
//            sbs.append("data-toggle=\"tooltip-arrow\" ");
        sbs.append("title=\"" + Storage.MSGS.tooltipUrgency() + "\" ");
        sbs.append(">");
        sb.appendHtmlConstant(sbs.toString());
        //TODO RELEASE Jaro - switch to HEADER if implemented
        sb.append(renderer.render(URGENT));
//            sb.append(renderer.render(HEADER));
        sb.appendHtmlConstant("</a>");
    }

    private void renderUrgencyColumnCell(Context context, Date value, SafeHtmlBuilder sb) {
        long diffMilliseconds = value.getTime() - (new Date()).getTime();
        long diffDays = diffMilliseconds / Constants.DAY_LENGTH;

        //(0-4 days) hight
        if ((int) diffDays <= DAY_URGENT) {
            sb.append(renderer.render(URGENT));
            //(5-8 days) medium
        } else if ((int) diffDays <= DAY_URGENT_LESS) {
            sb.append(renderer.render(URGENT_LESS));
            //(9-infinity days) low
        } else {
            sb.append(renderer.render(NORMAL));
        }
    }
}
