/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.grid.cell.SafeClickableTextCell;
import com.eprovement.poptavka.client.user.widget.grid.columns.EndDateColumn.TableDisplayEndDate;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;
import java.util.Date;

/**
 * Use to create <b>end date column</b> in table.
 * Object must implemnets <b>TableDisplayEndDate</b> to be displayable in table with end date column.
 *
 * @author Martin Slavkovsky
 */
public class EndDateColumn extends Column<TableDisplayEndDate, String> {

    public interface TableDisplayEndDate {

        Date getEndDate();
    }

    /**
     * Creates EndDateColumn with:
     * <ul>
     *   <li>sortable: true</li>
     *   <li>cellStyleNames: none</li>
     *   <li>fieldUpdater: provided</li>
     * </ul>
     * @param fieldUpdater
     */
    public EndDateColumn(FieldUpdater fieldUpdater) {
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
    public String getValue(TableDisplayEndDate object) {
        return Storage.get().getDateTimeFormat().format(object.getEndDate());
    }
}
