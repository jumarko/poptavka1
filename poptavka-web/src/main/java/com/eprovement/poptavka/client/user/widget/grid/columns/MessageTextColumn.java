/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.client.user.widget.grid.cell.SafeClickableTextCell;
import com.eprovement.poptavka.client.user.widget.grid.columns.MessageTextColumn.TableDisplayMessageText;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;

/**
 * Use to create <b>message text column</b> in table.
 * Object must implemnets <b>TableDisplayMessageText</b> to be displayable in table with message text column.
 *
 * @author Martin Slavkovsky
 */
public class MessageTextColumn extends Column<TableDisplayMessageText, String> {

    public interface TableDisplayMessageText {

        String getMessageText();
    }

    /**
     * Creates MessageTextColumn with:
     * <ul>
     *   <li>sortable: false</li>
     *   <li>cellStyleNames: ellipsis</li>
     *   <li>fieldUpdater: provided</li>
     * </ul>
     * @param fieldUpdater
     */
    public MessageTextColumn(FieldUpdater fieldUpdater) {
        super(new SafeClickableTextCell());
        setSortable(false);
        setCellStyleNames("ellipsis");
        if (fieldUpdater != null) {
            setFieldUpdater(fieldUpdater);
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public String getValue(TableDisplayMessageText object) {
        return object.getMessageText();
    }
}
