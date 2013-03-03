package com.eprovement.poptavka.client.user.widget.grid.cell;

import com.eprovement.poptavka.client.common.DateUtils;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.github.gwtbootstrap.client.ui.constants.Placement;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.user.client.ui.ImageResourceRenderer;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import java.util.Date;

/**
 * Provides urgency images for header and column.
 * If Header image is required, just pass null value to date, otherwise urgency
 * images are returned.
 *
 * @author Martin Slavkovsky
 */
public class UrgentImageCell extends AbstractCell<Date> {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** Constants. **/
    private static final ImageResource HEADER = Storage.RSCS.images().urgencyHeader();
    private static final ImageResource HIGH = Storage.RSCS.images().urgencyRed();
    private static final ImageResource HIGHER = Storage.RSCS.images().urgencyOrange();
    private static final ImageResource NORMAL = Storage.RSCS.images().urgencyGreen();
    /** Renderers. **/
    private static ImageResourceRenderer imageRenderer;
    private SafeHtmlRenderer<Date> dateRenderer = new AbstractSafeHtmlRenderer<Date>() {
        @Override
        public SafeHtml render(Date value) {
            int daysBetween = CalendarUtil.getDaysBetween(value, DateUtils.getNowDate());
            if (daysBetween < Constants.DAYS_URGENCY_HIGHT) {
                return imageRenderer.render(HIGH);
            }
            if (daysBetween <= Constants.DAYS_URGENCY_HIGHTER) {
                return imageRenderer.render(HIGHER);
            }
            return imageRenderer.render(NORMAL);
        }
    };

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    public UrgentImageCell() {
        if (imageRenderer == null) {
            imageRenderer = new ImageResourceRenderer();
        }
    }

    /**************************************************************************/
    /* Override methods                                                       */
    /**************************************************************************/
    @Override
    public void render(Context context, Date value, SafeHtmlBuilder sb) {
        /*
         * Getting value null tells us to use header image and create tooltip.
         * The result will be used in header.
         */
        if (value == null) {
            renderUrgencyHeader(sb);
            /*
             * Otherwise provide appropriate urgency image according to given date.
             */
        } else {
            renderUrgencyColumnCell(value, sb);
        }
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    private void renderUrgencyHeader(SafeHtmlBuilder sb) {
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
        sb.append(imageRenderer.render(HEADER));
        sb.appendHtmlConstant("</a>");
    }

    private void renderUrgencyColumnCell(Date value, SafeHtmlBuilder sb) {
        sb.append(dateRenderer.render(value));
    }
}
