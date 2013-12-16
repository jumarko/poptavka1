/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.client.user.widget.grid.cell.CreatedDateCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;
import java.util.Date;

/**
 * Use to create <b>createdDate column</b> in table.
 * Object must implemnets <b>TableDisplayCreatedDate</b> to be displayable in table with created date column.
 *
 * @author Martin Slavkovsky
 */
public class CreatedDateColumn extends Column<CreatedDateColumn.TableDisplayCreatedDate, Date> {

    public interface TableDisplayCreatedDate {

        Date getCreated();
    }

    /**
     * Creates CreatedDateColumn with:
     * <ul>
     *   <li>sortable: true</li>
     *   <li>cellStyleNames: none</li>
     *   <li>fieldUpdater: provided</li>
     * </ul>
     * @param fieldUpdater
     */
    public CreatedDateColumn(FieldUpdater fieldUpdater) {
        super(new CreatedDateCell());
        setSortable(true);
        if (fieldUpdater != null) {
            setFieldUpdater(fieldUpdater);
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Date getValue(TableDisplayCreatedDate object) {
        return object.getCreated();
    }
}
