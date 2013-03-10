package com.eprovement.poptavka.client.user.widget.grid.cell;

import com.eprovement.poptavka.client.common.DateUtils;
import com.eprovement.poptavka.client.common.session.Constants;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiRenderer;
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

    interface UrgencyImageCellStyle extends CssResource {

        String header();

        String high();

        String higher();

        String normal();
    }
    /**************************************************************************/
    /* UiRenderer                                                             */
    /**************************************************************************/
    private static UrgentImageCell.MyUiRenderer uiRenderer = GWT.create(UrgentImageCell.MyUiRenderer.class);

    interface MyUiRenderer extends UiRenderer {

        UrgencyImageCellStyle getCellStyle();

        void render(SafeHtmlBuilder sb, String imageClass);
    }

    /**************************************************************************/
    /* Override methods                                                       */
    /**************************************************************************/
    @Override
    public void render(Context context, Date value, SafeHtmlBuilder sb) {

        if (value == null) {
            uiRenderer.render(sb, uiRenderer.getCellStyle().header());

        } else {
            uiRenderer.render(sb, getImageClass(value));
        }
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Get proper image class from UrgencyImageCellStyle according to give date.
     * Getting date value null returns header image.
     * Otherwise return appropriate urgency image according to given date.
     *
     * @param value - given date
     * @return urgency image cell class
     */
    private String getImageClass(Date value) {
        if (value == null) {
            return uiRenderer.getCellStyle().header();
        }
        int daysBetween = CalendarUtil.getDaysBetween(value, DateUtils.getNowDate());
        if (daysBetween < Constants.DAYS_URGENCY_HIGH) {
            return uiRenderer.getCellStyle().high();
        }
        if (daysBetween <= Constants.DAYS_URGENCY_HIGHER) {
            return uiRenderer.getCellStyle().higher();
        }
        return uiRenderer.getCellStyle().normal();
    }
}
