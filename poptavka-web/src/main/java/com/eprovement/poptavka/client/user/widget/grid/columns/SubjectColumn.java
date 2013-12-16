/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.client.user.widget.grid.columns.SubjectColumn.TableDisplaySubject;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;

/**
 * Use to create <b>subject column</b> in table.
 * Object must implemnets <b>TableDisplaySubject</b> to be displayable in table with subject column.
 *
 * @author Martin Slavkovsky
 */
public class SubjectColumn extends Column<TableDisplaySubject, String> {

    public interface TableDisplaySubject {

        String getSubject();
    }

    /**
     * Creates SubjectColumn with:
     * <ul>
     *   <li>sortable: true</li>
     *   <li>cellStyleNames: ellipsis</li>
     *   <li>fieldUpdater: provided</li>
     * </ul>
     * @param fieldUpdater
     */
    public SubjectColumn(FieldUpdater fieldUpdater) {
        super(new ClickableTextCell());
        setSortable(true);
        setCellStyleNames("ellipsis");
        if (fieldUpdater != null) {
            setFieldUpdater(fieldUpdater);
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public String getValue(TableDisplaySubject object) {
        return object.getSubject();
    }
}
