/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.client.user.widget.grid.columns.SenderColumn.TableDisplaySender;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;

/**
 * Use to create <b>sender column</b> in table.
 * Object must implemnets <b>TableDisplaySender</b> to be displayable in table with sender column.
 *
 * @author Martin Slavkovsky
 */
public class SenderColumn  extends Column<TableDisplaySender, String> {

    public interface TableDisplaySender {

        String getSender();
    }

    /**
     * Creates SenderColumn with:
     * <ul>
     *   <li>sortable: true</li>
     *   <li>cellStyleNames: none</li>
     *   <li>fieldUpdater: provided</li>
     * </ul>
     * @param fieldUpdater
     */
    public SenderColumn(FieldUpdater fieldUpdater) {
        super(new ClickableTextCell());
        setSortable(true);
        if (fieldUpdater != null) {
            setFieldUpdater(fieldUpdater);
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public String getValue(TableDisplaySender object) {
        return object.getSender();
    }
}
