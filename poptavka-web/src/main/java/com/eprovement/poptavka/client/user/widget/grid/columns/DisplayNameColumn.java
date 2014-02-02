/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.grid.columns;

import com.eprovement.poptavka.client.user.widget.grid.cell.SafeClickableTextCell;
import com.eprovement.poptavka.client.user.widget.grid.columns.DisplayNameColumn.TableDisplayDisplayName;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;

/**
 * Use to create <b>display name column</b> in table.
 * Object must implemnets <b>TableDisplayDisplayName</b> to be displayable in table with display name column.
 *
 * @author Martin Slavkovsky
 */
public class DisplayNameColumn extends Column<TableDisplayDisplayName, String> {

    public interface TableDisplayDisplayName {

        String getDisplayName();

        /**
         * Provide 0 if count unavailable or if only display name alone needed.
         */
        int getMessagesCount();
    }

    /**
     * Creates DisplayNameColumn with:
     * <ul>
     *   <li>sortable: false</li>
     *   <li>cellStyleNames: ellipsis</li>
     *   <li>fieldUpdater: provided</li>
     * </ul>
     * @param fieldUpdater
     */
    public DisplayNameColumn(FieldUpdater fieldUpdater) {
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
    public String getValue(TableDisplayDisplayName object) {
        return getCellTextAccordingToMessagesCount(object.getMessagesCount(), object.getDisplayName());
    }

    /**
     * Formats display name text by adding messages count abter it
     * if unread messages count is not 0.
     *
     * @param messagesCount value
     * @param cellText - display name text
     * @return formated cell value
     */
    private String getCellTextAccordingToMessagesCount(int messagesCount, String cellText) {
        if (messagesCount > 0) {
            StringBuilder title = new StringBuilder();
            title.append(cellText);
            title.append(" (");
            title.append(messagesCount);
            title.append(")");
            return title.toString();
        } else {
            return cellText;
        }
    }
}
