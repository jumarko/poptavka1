package cz.poptavka.sample.client.user.demands.widgets;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;

import cz.poptavka.sample.client.resources.StyleResource;

public class OffersFlexTable extends FlexTable {

    private static final Logger LOGGER = Logger.getLogger(OffersFlexTable.class.getName());

    private static final int COLUMN_SIGN = 0;
    private static final int COLUMN_DEMAND = 1;
    private static final int COLUMN_PRICE = 2;
    private static final int COLUMN_RATING = 3;
    private static final int COLUMN_DATE = 4;

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    public OffersFlexTable() {
        this.setHTML(0, COLUMN_SIGN, "&nbsp;");
        this.setHTML(0, COLUMN_DEMAND, "<strong>Detail</strong>");
        this.setHTML(0, COLUMN_PRICE, "<strong>Cena</strong>");
        this.setWidget(0, COLUMN_RATING, new Image(StyleResource.INSTANCE.images().star()));
        this.setHTML(0, COLUMN_DATE, "<strong>" + MSGS.endDate() + "</strong>");
        LOGGER.fine("Constructor");
    }

}
