/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.grid.cell.SafeClickableTextCell;
import com.eprovement.poptavka.client.user.widget.grid.columns.OfferReceivedDateColumn.TableDisplayOfferReceivedDate;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;
import java.util.Date;

/**
 * Use to create <b>offer received date column</b> in table.
 * Object must implemnets <b>TableDisplayOfferReceivedDate</b> to be displayable
 * in table with offer received date column.
 *
 * @author Martin Slavkovsky
 */
public class OfferReceivedDateColumn extends Column<TableDisplayOfferReceivedDate, String> {

    public interface TableDisplayOfferReceivedDate {

        Date getOfferReceivedDate();
    }

    /**
     * Creates OfferReceivedDateColumn with:
     * <ul>
     *   <li>sortable: true</li>
     *   <li>cellStyleNames: none</li>
     *   <li>fieldUpdater: provided</li>
     * </ul>
     * @param fieldUpdater
     */
    public OfferReceivedDateColumn(FieldUpdater fieldUpdater) {
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
    public String getValue(TableDisplayOfferReceivedDate object) {
        return Storage.get().getDateTimeFormat().format(object.getOfferReceivedDate());
    }
}
