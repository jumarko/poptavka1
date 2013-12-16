package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.grid.cell.SafeClickableTextCell;
import com.eprovement.poptavka.client.user.widget.grid.columns.PriceColumn.TableDisplayPrice;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;
import java.math.BigDecimal;

/**
 *
 * @author Mato
 */
public class PriceColumn extends Column<TableDisplayPrice, String> {

    public interface TableDisplayPrice {

        BigDecimal getPrice();
    }

    public PriceColumn(FieldUpdater fieldUpdater) {
        super(new SafeClickableTextCell());
        setSortable(true);
        if (fieldUpdater != null) {
            setFieldUpdater(fieldUpdater);
        }
    }

    @Override
    public String getValue(TableDisplayPrice object) {
        return Storage.CURRENCY_FORMAT.format(object.getPrice());
    }
}
