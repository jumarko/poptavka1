/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.grid.cell.SafeClickableTextCell;
import com.eprovement.poptavka.client.user.widget.grid.columns.PriceColumn.TableDisplayPrice;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;
import java.math.BigDecimal;

/**
 * Use to create <b>price column</b> in table.
 * Object must implemnets <b>TableDisplayPrice</b> to be displayable in table with price column.
 *
 * @author Martin Slavkovsky
 */
public class PriceColumn extends Column<TableDisplayPrice, String> {

    public interface TableDisplayPrice {

        BigDecimal getPrice();
    }

    /**
     * Creates PriceColumn with:
     * <ul>
     *   <li>sortable: true</li>
     *   <li>cellStyleNames: none</li>
     *   <li>fieldUpdater: provided</li>
     * </ul>
     * @param fieldUpdater
     */
    public PriceColumn(FieldUpdater fieldUpdater) {
        super(new SafeClickableTextCell());
        setSortable(true);
        if (fieldUpdater != null) {
            setFieldUpdater(fieldUpdater);
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public String getValue(TableDisplayPrice object) {
        return Storage.CURRENCY_FORMAT.format(object.getPrice());
    }
}
