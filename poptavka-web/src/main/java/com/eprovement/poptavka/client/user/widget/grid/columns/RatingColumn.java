/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.client.user.widget.grid.cell.RatingCell;
import com.eprovement.poptavka.client.user.widget.grid.columns.RatingColumn.TableDisplayRating;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;

/**
 * Use to create <b>rating column</b> in table.
 * Object must implemnets <b>TableDisplayRating</b> to be displayable in table with rating column.
 *
 * @author Martin Slavkovsky
 */
public class RatingColumn extends Column<TableDisplayRating, Integer> {

    public interface TableDisplayRating {

        Integer getOveralRating();
    }

    /**
     * Creates RatingColumn with:
     * <ul>
     *   <li>sortable: true</li>
     *   <li>cellStyleNames: none</li>
     *   <li>fieldUpdater: provided</li>
     * </ul>
     * @param fieldUpdater
     */
    public RatingColumn(FieldUpdater fieldUpdater) {
        super(new RatingCell());
        setSortable(true);
        if (fieldUpdater != null) {
            setFieldUpdater(fieldUpdater);
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Integer getValue(TableDisplayRating object) {
        return object.getOveralRating();
    }
}
