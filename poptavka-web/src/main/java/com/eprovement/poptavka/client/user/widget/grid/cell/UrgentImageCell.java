package com.eprovement.poptavka.client.user.widget.grid.cell;

import com.eprovement.poptavka.client.common.DateUtils;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiRenderer;
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
    /* UiRenderer                                                             */
    /**************************************************************************/
    private static UrgentImageCell.MyUiRenderer uiRenderer = GWT.create(UrgentImageCell.MyUiRenderer.class);

    interface MyUiRenderer extends UiRenderer {

        void render(SafeHtmlBuilder sb, SafeHtml image, String tooltip);
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private static ImageResourceRenderer imageRenderer = new ImageResourceRenderer();
    private SafeHtml imageResource;
    private String tooltip;

    /**************************************************************************/
    /* Override methods                                                       */
    /**************************************************************************/
    @Override
    public void render(Context context, Date value, SafeHtmlBuilder sb) {
        setImageResoureAndTooltip(value);
        uiRenderer.render(sb, imageResource, tooltip);
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Set proper image resource and its tooltip according to give date.
     * Getting date value null returns pair header image/tooltip.
     * Otherwise return appropriate urgency image/tooltip according to given date.
     * Tooltip is also used as alt value for image.
     *
     * @param value - given date
     */
    private void setImageResoureAndTooltip(Date value) {
        if (value == null) {
            imageResource = imageRenderer.render(Storage.RSCS.images().urgencyHeader());
            tooltip = Storage.MSGS.urgencyTooltip();
            return;
        }
        int daysBetween = CalendarUtil.getDaysBetween(DateUtils.getNowDate(), value);
        if (daysBetween < 0) {
            imageResource = imageRenderer.render(Storage.RSCS.images().urgencyHeader());
            tooltip = Storage.MSGS.urgencyExpiredDesc();
            return;
        }
        if (daysBetween <= Constants.DAYS_URGENCY_HIGH) {
            imageResource = imageRenderer.render(Storage.RSCS.images().urgencyRed());
            tooltip = Storage.MSGS.urgencyHighDesc();
            return;
        }
        if (daysBetween <= Constants.DAYS_URGENCY_HIGHER) {
            imageResource = imageRenderer.render(Storage.RSCS.images().urgencyOrange());
            tooltip = Storage.MSGS.urgencyHigherDesc();
            return;
        }
        imageResource = imageRenderer.render(Storage.RSCS.images().urgencyGreen());
        tooltip = Storage.MSGS.urgencyNormalDesc();
    }
}
