/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.grid.cell.SafeClickableTextCell;
import com.eprovement.poptavka.client.user.widget.grid.columns.FinishDateColumn.TableDisplayFinishDate;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;
import java.util.Date;

/**
 * Use to create <b>Finnish date column</b> in table.
 * Object must implemnets <b>TableDisplayFinnishDate</b> to be displayable in table with finnish date column.
 *
 * @author Martin Slavkovsky
 */
public class FinishDateColumn extends Column<TableDisplayFinishDate, String> {

    public interface TableDisplayFinishDate {

        Date getFinishDate();
    }

    /**
     * Creates FinnishDateColumn with:
     * <ul>
     *   <li>sortable: true</li>
     *   <li>cellStyleNames: none</li>
     *   <li>fieldUpdater: provided</li>
     * </ul>
     * @param fieldUpdater
     */
    public FinishDateColumn(FieldUpdater fieldUpdater) {
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
    public String getValue(TableDisplayFinishDate object) {
        return Storage.get().getDateTimeFormat().format(object.getFinishDate());
    }
}
