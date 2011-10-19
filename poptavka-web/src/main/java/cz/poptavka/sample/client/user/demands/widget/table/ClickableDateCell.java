package cz.poptavka.sample.client.user.demands.widget.table;

import java.util.Date;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.text.shared.SafeHtmlRenderer;

import cz.poptavka.sample.client.main.Storage;

public class ClickableDateCell extends AbstractCell<Date> {

    private SafeHtmlRenderer<Date> renderer = new AbstractSafeHtmlRenderer<Date>() {

        @Override
        public SafeHtml render(Date demandCreation) {
            long creationDate = demandCreation.getTime();
            long actualDate = new Date().getTime();
            //conditions
            if ((actualDate - creationDate) < Storage.DAY_LENGTH) {
                return SafeHtmlUtils.fromTrustedString(Storage.MSGS.today());
            }
            if ((actualDate - creationDate) < (Storage.DAY_LENGTH * 2)) {
                return SafeHtmlUtils.fromTrustedString(Storage.MSGS.yesterday());
            }
            return SafeHtmlUtils.fromTrustedString(DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT)
                    .format(demandCreation));

        }

    };

    public ClickableDateCell() {
        super("click", "keydown");
    }


    @Override
    public void render(Context context, Date value, SafeHtmlBuilder sb) {
        sb.appendHtmlConstant(DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT).format(value));
    }

    @Override
    public void onBrowserEvent(Context context, Element parent, Date value,
        NativeEvent event, ValueUpdater<Date> valueUpdater) {
        super.onBrowserEvent(context, parent, value, event, valueUpdater);
        if ("click".equals(event.getType())) {
            onEnterKeyDown(context, parent, value, event, valueUpdater);
        }
    }

    @Override
    protected void onEnterKeyDown(Context context, Element parent, Date value,
        NativeEvent event, ValueUpdater<Date> valueUpdater) {
        if (valueUpdater != null) {
            valueUpdater.update(value);
        }
    }

}
