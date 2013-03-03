package com.eprovement.poptavka.client.user.widget.grid.cell;

import com.eprovement.poptavka.client.common.DateUtils;
import com.eprovement.poptavka.client.common.session.Storage;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import java.util.Date;

/**
 * Provides SafeHtml string column representing demand's created date.
 * Lets demonstrate options:
 * Time (i.e. "12:30") is displayed if demand was created the same day it is viewing,
 * "Yesterday" is displayed if demand was created day before the day it is viewing.
 * Date (i.e. "12 feb 2012") is displayed in all other cases.
 *
 * @author Martin Slavkovsky
 */
public class CreatedDateCell extends DateCell {

    public static final DateTimeFormat TIME_FORMATTER = DateTimeFormat.getFormat("hh:mm");

    private SafeHtmlRenderer<Date> renderer = new AbstractSafeHtmlRenderer<Date>() {
        @Override
        public SafeHtml render(Date demandCreation) {
            if (demandCreation == null) {
                return SafeHtmlUtils.fromTrustedString(Storage.MSGS.commonNotDefined());
            } else {
                if (CalendarUtil.isSameDate(DateUtils.getNowDate(), demandCreation)) {
                    return SafeHtmlUtils.fromTrustedString(TIME_FORMATTER.format(demandCreation));
                }
                if (CalendarUtil.isSameDate(DateUtils.getYesterdayDate(), demandCreation)) {
                    return SafeHtmlUtils.fromTrustedString(Storage.MSGS.creationDateYesterday());
                }
                return SafeHtmlUtils.fromTrustedString(Storage.FORMATTER.format(demandCreation));
            }
        }
    };

    public CreatedDateCell() {
    }

    @Override
    public void render(Context context, Date value, SafeHtmlBuilder sb) {
        sb.append(renderer.render(value));
    }
}
